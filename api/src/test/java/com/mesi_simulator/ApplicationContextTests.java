package com.mesi_simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.mesi_simulator.domain.ApplicationContext;
import com.mesi_simulator.domain.MainMemory;

public class ApplicationContextTests {
  private final int numberOfBlocks = 12;
  private final int addressesPerBlock = 6;

  @Test
  public void blockCalculation() {
    var memory = new MainMemory(numberOfBlocks, addressesPerBlock);
    var context = new ApplicationContext(memory);

    assertEquals(1, context.blocksPerHeader());
    assertEquals(6, context.blocksPerRoom());
  }
}
