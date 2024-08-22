package com.cinema_reservations.domain.dispatchers;

import com.cinema_reservations.domain.events.*;

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
