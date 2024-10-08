package com.mesi_simulator.domain;

import java.util.*;

public class Line {
  private Tag tag;
  private List<Long> content;

  public Line(int blockNumber, int numberOfAddresses) {
    this(blockNumber, Collections.nCopies(numberOfAddresses, 0L));
  }

  public Line(int blockNumber, List<Long> content) {
    this.tag = new Tag(blockNumber);
    this.content = content;
  }

  public int getBlockNumber() { return tag.blockNumber(); }
  public ProtocolStatus getStatus() { return tag.status(); }
  public List<Long> getContent() { return content; }

  public void setStatus(ProtocolStatus newStatus) {
    tag = new Tag(tag.blockNumber(), newStatus);
  }

  public void setContent(List<Long> newContent) {
    content = newContent;
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

