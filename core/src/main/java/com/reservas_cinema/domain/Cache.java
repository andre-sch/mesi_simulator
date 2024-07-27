package com.reservas_cinema.domain;

import java.util.*;
import com.reservas_cinema.domain.events.*;
import com.reservas_cinema.domain.dispatchers.*;

public class Cache {
  private final Queue<Line> data = new LinkedList<>();
  private final MainMemory memory;
  private final int maxLines;

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
    for (var line : data) {
      var blockIsAvailable = line.tag().blockNumber() == blockNumber;
      if (blockIsAvailable) return line.content();
    }

    return fetchBlock(blockNumber);
  }

  public List<Long> fetchBlock(int blockNumber) {
    if (data.size() == maxLines) releaseLine();
    
    var dataLookup = readMissDispatcher.dispatch(new ReadMiss(blockNumber));
    var status = dataLookup.isPresent() && dataLookup.get().isSuccessful()
      ? ProtocolStatus.SHARED : ProtocolStatus.EXCLUSIVE;

    var tag = new Tag(blockNumber, status);
    var content = memory.readBlock(blockNumber);
    
    data.offer(new Line(tag, content));
    return content;
  }
  
  private void releaseLine() {
    var line = data.poll();

    var block = line.content();
    var blockNumber = line.tag().blockNumber();

    memory.writeBlock(blockNumber, block);
  }
}

record Line(
  Tag tag,
  List<Long> content
) {}

record Tag(
  int blockNumber,
  ProtocolStatus status
) {}

enum ProtocolStatus {
  MODIFIED,
  EXCLUSIVE,
  SHARED,
  INVALID
}
