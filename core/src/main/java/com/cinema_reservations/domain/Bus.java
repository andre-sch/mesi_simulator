package com.cinema_reservations.domain;

import java.util.*;
import com.cinema_reservations.domain.events.*;
import com.cinema_reservations.domain.dispatchers.*;

public class Bus {
  private final List<Cache> caches = new LinkedList<>();
  private final Dispatcher<CacheEvent> eventDispatcher = new Dispatcher<>();
  private final BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher = new ReadMissDispatcher();

  public Bus(
    MainMemory memory,
    int numberOfCaches,
    int numberOfCacheLines
  ) {
    for (int i = 0; i < numberOfCaches; i++) {
      var cache = new Cache(
        memory,
        numberOfCacheLines,
        eventDispatcher,
        bidirectionalEventDispatcher
      );

      eventDispatcher.addObserver(cache.getHandler());
      bidirectionalEventDispatcher.addObserver(cache.getBidirectionalHandler());
      this.caches.add(cache);
    }
  }
}
