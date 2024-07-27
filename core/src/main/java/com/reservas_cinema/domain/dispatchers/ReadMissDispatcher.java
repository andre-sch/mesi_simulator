package com.reservas_cinema.domain.dispatchers;

import com.reservas_cinema.domain.events.*;

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
