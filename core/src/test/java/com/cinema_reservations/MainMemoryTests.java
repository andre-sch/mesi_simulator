package com.cinema_reservations;

import java.util.List;
import java.util.LinkedList;
import com.cinema_reservations.domain.MainMemory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainMemoryTests {
  final List<Long> block = new LinkedList<>();
  final List<Long> dataset = new LinkedList<>();
  final int numberOfItems = 15;

  final int numberOfBlocks = 5;
  final int addressesPerBlock = 10;

  public MainMemoryTests() {
    initializeList(block, addressesPerBlock);
    initializeList(dataset, numberOfItems);
  }

  @Test
  public void writingBlock() {
    var memory = new MainMemory(numberOfBlocks, addressesPerBlock);

    int blockNumber = 2;
    memory.writeBlock(blockNumber, block);

    assertEquals(block, memory.readBlock(blockNumber));
  }

  @Test
  public void writingBlockWithInvalidLength() {
    var memory = new MainMemory(numberOfBlocks, addressesPerBlock);

    assertThrows(RuntimeException.class, () -> {
      int blockNumber = 2;
      memory.writeBlock(blockNumber, dataset);
    });
  }

  private void initializeList(List<Long> list, int length) {
    for (long i = 0; i < length; i++)
      list.add(i);
  }
}
