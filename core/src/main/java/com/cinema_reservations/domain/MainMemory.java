package com.cinema_reservations.domain;

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
    return readListOfItems(firstAddress(blockNumber), addressesPerBlock);
  }

  public void writeBlock(int blockNumber, List<Long> block) {
    writeListOfItems(firstAddress(blockNumber), block);
  }

  public List<Long> readListOfItems(int address, int amount) {
    List<Long> items = new LinkedList<>();
    
    for (int i = 0; i < amount; i++)
      items.add(readItem(address + i));
    
    return items;
  }

  public void writeListOfItems(int address, List<Long> items) {
    for (int i = 0; i < items.size(); i++)
      writeItem(address + i, items.get(i));
  }

  private int firstAddress(int blockNumber) { return blockNumber * addressesPerBlock; }
}
