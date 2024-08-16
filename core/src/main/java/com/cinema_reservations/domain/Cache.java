package com.cinema_reservations.domain;

import java.util.*;
import java.util.function.*;
import com.cinema_reservations.domain.events.*;
import com.cinema_reservations.domain.dispatchers.*;

public class Cache {
  private final Queue<Line> data = new LinkedList<>();
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
    List<Line> dataCopy = new LinkedList<>(data);
    int blockNumber = 0;

    while (dataCopy.size() < maxLines)
      dataCopy.add(new Line(blockNumber, memory.addressesPerBlock()));

    return dataCopy;
  }

  public void writeBlock(int blockNumber, List<Long> newContent) {
    Line line;
    Optional<Line> internalCacheLookup = readFromCache(blockNumber);

    if (internalCacheLookup.isPresent()) {
      line = internalCacheLookup.get();
      eventDispatcher.dispatch(new WriteHit(blockNumber));
    } else {
      line = loadBlockInCache(blockNumber);
      eventDispatcher.dispatch(new WriteMiss(blockNumber));
    }

    line.setStatus(ProtocolStatus.MODIFIED);
    line.setContent(newContent);
  }

  public List<Long> readBlock(int blockNumber) {
    var internalCacheLookup = readFromCache(blockNumber);
    if (internalCacheLookup.isPresent()) {
      eventDispatcher.dispatch(new ReadHit(blockNumber));
      var line = internalCacheLookup.get();
      return line.getContent();
    }

    var event = new ReadMiss(blockNumber);

    eventDispatcher.dispatch(event);
    var externalCacheLookup = bidirectionalEventDispatcher.dispatch(event);
    var status = externalCacheLookup.isPresent() && externalCacheLookup.get().isSuccessful()
      ? ProtocolStatus.SHARED : ProtocolStatus.EXCLUSIVE;

    var line = loadBlockInCache(blockNumber);
    line.setStatus(status);

    return line.getContent();
  }

  private Optional<Line> readFromCache(int blockNumber) {
    for (var line : data) {
      boolean isPresent = line.getBlockNumber() == blockNumber;
      if (isPresent) return Optional.of(line);
    }

    return Optional.empty();
  }

  private Line loadBlockInCache(int blockNumber) {
    if (data.size() == maxLines) releaseLine();
    
    var content = memory.readBlock(blockNumber);
    var line = new Line(blockNumber, content);
    
    data.offer(line);
    return line;
  }
  
  private void releaseLine() {
    var line = data.poll();

    var block = line.getContent();
    var blockNumber = line.getBlockNumber();

    memory.writeBlock(blockNumber, block); // escreve mesmo se estiver invalido?
  }

  public Consumer<CacheEvent> getHandler() {
    return (CacheEvent event) -> {
      var internalCacheLookup = readFromCache(event.blockNumber());
      if (internalCacheLookup.isEmpty()) return;
      
      var line = internalCacheLookup.get();

      if (event.operation() == OperationType.WRITE)
        line.setStatus(ProtocolStatus.INVALID);
      
      if (event.operation() == OperationType.READ) {
        if (line.getStatus() == ProtocolStatus.MODIFIED)
          memory.writeBlock(line.getBlockNumber(), line.getContent());
        line.setStatus(ProtocolStatus.SHARED);
      }
    };
  }
  
  public Function<ReadMiss, DataLookup> getBidirectionalHandler() {
    return (ReadMiss event) -> {
      var internalCacheLookup = readFromCache(event.blockNumber());
      var isLookupSuccessful = internalCacheLookup.isPresent();

      return new DataLookup(event.blockNumber(), isLookupSuccessful);
    };
  }
}
