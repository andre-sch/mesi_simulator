package com.cinema_reservations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.cinema_reservations.domain.ApplicationContext;
import com.cinema_reservations.domain.MainMemory;

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
