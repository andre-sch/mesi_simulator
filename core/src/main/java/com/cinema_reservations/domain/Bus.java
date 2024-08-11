package com.cinema_reservations.domain;

import com.cinema_reservations.domain.events.*;
import com.cinema_reservations.domain.dispatchers.*;

public class Bus {
  private final int numberOfBlocks = 10;
  private final int addressesPerBlock = 5;
  private final int numberOfLines = 5;
  
  private final MainMemory mainMemory = new MainMemory(numberOfBlocks, addressesPerBlock);

  private final Dispatcher<CacheEvent> eventDispatcher = new Dispatcher<>();
  private final BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher = new ReadMissDispatcher();

  public int numberOfBlocks() { return numberOfBlocks; }
  public int addressesPerBlock() { return addressesPerBlock; }
  public int numberOfLines() { return numberOfLines; }

  public Cache appendCache() {
    return new Cache(
      mainMemory,
      numberOfLines,
      eventDispatcher,
      bidirectionalEventDispatcher
    );
  }
}
