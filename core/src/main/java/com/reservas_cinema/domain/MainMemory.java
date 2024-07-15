package com.reservas_cinema.domain;

import java.util.*;

public class MainMemory {
  private final List<Long> data;
  private final int addressesPerBlock;

  public MainMemory(int numberOfAddresses, int addressesPerBlock) {
    this.data = new ArrayList<>(numberOfAddresses);
    this.addressesPerBlock = addressesPerBlock;
  }

  public Long readItem(int address) { return data.get(address); }
  public Long writeItem(int address, Long value) { return data.set(address, value); }

  public List<Long> readBlock(int blockNumber) {
    List<Long> block = new LinkedList<>();

    for (int i = firstAddress(blockNumber); i <= lastAddress(blockNumber); i++)
      block.add(data.get(i));

    return block;
  }

  public void writeBlock(int blockNumber, List<Long> block) {
    for (int i = firstAddress(blockNumber), j = 0; i <= lastAddress(blockNumber); i++, j++)
      data.set(i, block.get(j));
  }

  private int firstAddress(int blockNumber) { return blockNumber * addressesPerBlock; }
  private int lastAddress(int blockNumber) { return firstAddress(blockNumber) + addressesPerBlock - 1; }
}
