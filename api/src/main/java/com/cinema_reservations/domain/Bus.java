package com.cinema_reservations.domain;

import java.util.*;
import com.cinema_reservations.domain.events.*;
import com.cinema_reservations.domain.dispatchers.*;

public class Bus {
  private final int numberOfBlocks = 12;
  private final int addressesPerBlock = 6;
  private final int numberOfLines = 6;
  
  private final MainMemory mainMemory = new MainMemory(numberOfBlocks, addressesPerBlock);

  private final List<Cache> caches = new LinkedList<>();
  private final List<Dispatcher<CacheEvent>> eventDispatchers = new LinkedList<>();
  private final List<BidirectionalDispatcher<ReadMiss, DataLookup>> bidirectionalEventDispatchers = new LinkedList<>();

  public int numberOfBlocks() { return numberOfBlocks; }
  public int addressesPerBlock() { return addressesPerBlock; }
  public int numberOfLines() { return numberOfLines; }

  public MainMemory getSharedMemory() {
    return mainMemory;
  }

  public Cache appendCache() {
    var eventDispatcher = new Dispatcher<CacheEvent>();
    var bidirectionalEventDispatcher = new ReadMissDispatcher();

    var cache = new Cache(
      mainMemory,
      numberOfLines,
      eventDispatcher,
      bidirectionalEventDispatcher
    );

    int cacheIndex = caches.size();
    eventDispatcher.addObserver(new CacheEventLogger(cacheIndex));
    addCacheToRegisteredDispatchers(cache);
    addRegisteredCachesToEventDispatchers(
      eventDispatcher,
      bidirectionalEventDispatcher
    );

    eventDispatchers.add(eventDispatcher);
    bidirectionalEventDispatchers.add(bidirectionalEventDispatcher);
    caches.add(cache);

    return cache;
  }

  private void addCacheToRegisteredDispatchers(Cache cache) {
    for (var registeredEventDispatcher : eventDispatchers)
      registeredEventDispatcher.addObserver(cache.getHandler());

    for (var registeredBidirectionalEventDispatcher : bidirectionalEventDispatchers)
      registeredBidirectionalEventDispatcher.addObserver(cache.getBidirectionalHandler());
  }

  private void addRegisteredCachesToEventDispatchers(
    Dispatcher<CacheEvent> eventDispatcher,
    BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher
  ) {
    for (var cache : caches) {
      eventDispatcher.addObserver(cache.getHandler());
      bidirectionalEventDispatcher.addObserver(cache.getBidirectionalHandler());
    }
  }
}
