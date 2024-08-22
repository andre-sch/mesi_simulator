package com.mesi_simulator.domain.dispatchers;

import com.mesi_simulator.domain.events.*;

public class ReadMissDispatcher extends BidirectionalDispatcher<ReadMiss, DataLookup> {
  public ReadMissDispatcher() {
    super((DataLookup accumulatedLookup, DataLookup currentLookup) -> {
      return new DataLookup(
        accumulatedLookup.blockNumber(),
        accumulatedLookup.isSuccessful() || currentLookup.isSuccessful()
      );
    });
  }
}
