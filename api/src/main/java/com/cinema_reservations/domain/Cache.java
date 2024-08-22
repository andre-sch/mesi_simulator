package com.cinema_reservations.domain;

import java.util.*;
import java.util.function.*;
import com.cinema_reservations.domain.events.*;
import com.cinema_reservations.domain.dispatchers.*;

public class Cache {
  private final Map<Integer, Line> data = new LinkedHashMap<>();
  private final Queue<Integer> insertions = new LinkedList<>();
  private final MainMemory memory;
  private final int maxLines;

  private final Dispatcher<CacheEvent> eventDispatcher;
  private final BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher;

  public Cache(
    MainMemory memory,
    int maxLines,
    Dispatcher<CacheEvent> eventDispatcher,
    BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher
  ) {
    this.memory = memory;
    this.maxLines = maxLines;
    this.eventDispatcher = eventDispatcher;
    this.bidirectionalEventDispatcher = bidirectionalEventDispatcher;
  }

  public List<Line> getContent() {
    List<Line> dataCopy = new LinkedList<>(data.values());
    int blockNumber = 0;

    while (dataCopy.size() < maxLines)
      dataCopy.add(new Line(blockNumber, memory.addressesPerBlock()));

    return dataCopy;
  }

  public List<List<Long>> getRetainedBlocks() {
    var lines = getContent();
    return lines.stream()
      .map(Line::getContent)
      .toList();
  }

  public void writeBlock(int blockNumber, List<Long> newContent) {
    var line = fetch(blockNumber);
    CacheEvent event;
    
    if (line.getStatus() != ProtocolStatus.INVALID)
      event = new WriteHit(blockNumber);
    else event = new WriteMiss(blockNumber);
    
    eventDispatcher.dispatch(event);
    line.setStatus(ProtocolStatus.MODIFIED);
    line.setContent(newContent);
  }

  public List<Long> readBlock(int blockNumber) {
    var prefetch = fetchRetained(blockNumber);
    Line line;

    if (prefetch.isPresent()) {
      line = prefetch.get();
      eventDispatcher.dispatch(new ReadHit(blockNumber));
    } else {
      var event = new ReadMiss(blockNumber);

      eventDispatcher.dispatch(event);
      var externalCacheLookup = bidirectionalEventDispatcher.dispatch(event);
      var statusUpdate = externalCacheLookup.isPresent() && externalCacheLookup.get().isSuccessful()
        ? ProtocolStatus.SHARED : ProtocolStatus.EXCLUSIVE;
      
      line = fetch(blockNumber);
      line.setStatus(statusUpdate);
    }

    return line.getContent();
  }

  private Line fetch(int blockNumber) {
    for (var entry : data.entrySet()) {
      int cacheIndex = entry.getKey();
      var cacheLine = entry.getValue();

      if (cacheLine.getBlockNumber() == blockNumber) {
        if (cacheLine.getStatus() == ProtocolStatus.INVALID)
          cacheLine = loadFromMemory(blockNumber, cacheIndex);
  
        return cacheLine;
      }
    }
    return loadFromMemory(blockNumber);
  }

  private Line loadFromMemory(int blockNumber) {
    int nextInsertedIndex;
    boolean isCacheFull = data.size() == maxLines;
  
    if (isCacheFull) {
      int firstInsertedIndex = insertions.poll();
      Line firstInserted = data.get(firstInsertedIndex);

      if (firstInserted.getStatus() == ProtocolStatus.MODIFIED)
        memory.writeBlock(firstInserted.getBlockNumber(), firstInserted.getContent());

      nextInsertedIndex = firstInsertedIndex;
    } else nextInsertedIndex = insertions.size();

    return loadFromMemory(blockNumber, nextInsertedIndex);
  }

  private Line loadFromMemory(int blockNumber, int cacheIndex) {
    var content = memory.readBlock(blockNumber);
    var line = new Line(blockNumber, content);
    data.put(cacheIndex, line);

    if (!insertions.contains(cacheIndex))
      insertions.add(cacheIndex);
    
    return line;
  }

  public Consumer<CacheEvent> getHandler() {
    return (CacheEvent event) -> {
      var internalCacheLookup = fetchRetained(event.blockNumber());
      if (internalCacheLookup.isEmpty()) return;
      
      var line = internalCacheLookup.get();

      if (line.getStatus() == ProtocolStatus.MODIFIED)
        memory.writeBlock(line.getBlockNumber(), line.getContent());

      if (event.operation() == OperationType.READ)
        line.setStatus(ProtocolStatus.SHARED);
      else line.setStatus(ProtocolStatus.INVALID);
    };
  }
  
  public Function<ReadMiss, DataLookup> getBidirectionalHandler() {
    return (ReadMiss event) -> {
      var internalCacheLookup = fetchRetained(event.blockNumber());
      var isLookupSuccessful = internalCacheLookup.isPresent();

      return new DataLookup(event.blockNumber(), isLookupSuccessful);
    };
  }

  private Optional<Line> fetchRetained(int blockNumber) {
    for (var line : data.values()) {
      boolean isPresent = (
        line.getBlockNumber() == blockNumber &&
        line.getStatus() != ProtocolStatus.INVALID
      );

      if (isPresent) return Optional.of(line);
    }

    return Optional.empty();
  }
}
