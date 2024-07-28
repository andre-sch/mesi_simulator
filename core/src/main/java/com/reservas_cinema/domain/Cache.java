package com.reservas_cinema.domain;

import java.util.*;
import com.reservas_cinema.domain.events.*;
import com.reservas_cinema.domain.dispatchers.*;

public class Cache {
  private final Queue<Line> data = new LinkedList<>();
  private final MainMemory memory;
  private final int maxLines;

  private final ReadHitDispatcher readHitDispatcher = new ReadHitDispatcher();
  private final ReadMissDispatcher readMissDispatcher = new ReadMissDispatcher();

  public Cache(MainMemory memory, int maxLines) {
    this.memory = memory;
    this.maxLines = maxLines;
  }

  public void writeBlock(int blockNumber, List<Long> block) {
    var storedBlock = readBlock(blockNumber);

    storedBlock.clear();
    storedBlock.addAll(block);
  }

  public List<Long> readBlock(int blockNumber) {
    var internalCacheLookup = readFromCache(blockNumber);
    if (internalCacheLookup.isPresent()) {
      readHitDispatcher.dispatch(new ReadHit(blockNumber));
      return internalCacheLookup.get().content();
    }

    var externalCacheLookup = readMissDispatcher.dispatch(new ReadMiss(blockNumber));
    var status = externalCacheLookup.isPresent() && externalCacheLookup.get().isSuccessful()
      ? ProtocolStatus.SHARED : ProtocolStatus.EXCLUSIVE;

    var cacheLine = loadBlockInCache(blockNumber);
    cacheLine.setStatus(status);

    return cacheLine.content();
  }

  private Optional<Line> readFromCache(int blockNumber) {
    for (var line : data) {
      boolean isPresent = line.tag().blockNumber() == blockNumber;
      if (isPresent) return Optional.of(line);
    }

    return Optional.empty();
  }

  public Line loadBlockInCache(int blockNumber) {
    if (data.size() == maxLines) releaseLine();
    
    var tag = new Tag(blockNumber);
    var content = memory.readBlock(blockNumber);
    var line = new Line(tag, content);
    
    data.offer(line);
    return line;
  }
  
  private void releaseLine() {
    var line = data.poll();

    var block = line.content();
    var blockNumber = line.tag().blockNumber();

    memory.writeBlock(blockNumber, block);
  }
}

class Line {
  private Tag tag;
  private List<Long> content;

  public Line(Tag tag, List<Long> content) {
    this.tag = tag;
    this.content = content;
  }

  public Tag tag() { return tag; }
  public List<Long> content() { return content; }

  public void setStatus(ProtocolStatus newStatus) {
    tag = new Tag(tag.blockNumber(), newStatus);
  }

  public void setContent(List<Long> newContent) {
    content.clear();
    content.addAll(newContent);
  }
}

record Tag(
  int blockNumber,
  ProtocolStatus status
) {
  public Tag(int blockNumber) {
    this(blockNumber, ProtocolStatus.INVALID);
  }
}

enum ProtocolStatus {
  MODIFIED,
  EXCLUSIVE,
  SHARED,
  INVALID
}
