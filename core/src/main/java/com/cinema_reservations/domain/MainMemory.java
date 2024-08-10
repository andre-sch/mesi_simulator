package com.cinema_reservations.domain;

import java.util.*;

public class MainMemory {
  private final List<Long> data;
  private final int addressesPerBlock;

  public MainMemory(int numberOfAddresses, int addressesPerBlock) {
    this.addressesPerBlock = addressesPerBlock;
    this.data = new ArrayList<>();

    for (int i = 0; i < numberOfAddresses; i++)
      this.data.add(0L);
  }

  public Long readItem(int address) { return data.get(address); }
  public Long writeItem(int address, Long value) { return data.set(address, value); }

  public List<Long> readBlock(int blockNumber) {
    List<Long> block = new LinkedList<>();

    for (int i = 0; i < addressesPerBlock; i++)
      block.add(readItem(firstAddress(blockNumber) + i));

    return block;
  }

  public void writeBlock(int blockNumber, List<Long> block) {
    if (block.size() > addressesPerBlock)
      throw new RuntimeException("invalid block length");

    for (int i = 0; i < block.size(); i++)
      writeItem(firstAddress(blockNumber) + i, block.get(i));
  }

  private int firstAddress(int blockNumber) { return blockNumber * addressesPerBlock; }
}
