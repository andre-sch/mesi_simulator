package com.mesi_simulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import com.mesi_simulator.domain.dispatchers.ReadMissDispatcher;
import com.mesi_simulator.domain.events.DataLookup;
import com.mesi_simulator.domain.events.ReadMiss;

public class ReadMissDispatcherTests {
  private final int blockNumber = 0;

  @Test
  public void bidirectionalDispatching() {
    var bidirectionalEventDispatcher = new ReadMissDispatcher();
    bidirectionalEventDispatcher.addObserver(this::dataAbsent);
    bidirectionalEventDispatcher.addObserver(this::dataPresent);

    var eventSent = new ReadMiss(blockNumber);
    var eventReceived = bidirectionalEventDispatcher.dispatch(eventSent);

    assertEquals(new DataLookup(blockNumber, true), eventReceived.get());
  }

  private DataLookup dataAbsent(ReadMiss event) {
    return new DataLookup(event.blockNumber(), false);
  }

  private DataLookup dataPresent(ReadMiss event) {
    return new DataLookup(event.blockNumber(), true);
  }
}
