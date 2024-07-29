package com.cinema_reservations;

import com.cinema_reservations.domain.*;
import com.cinema_reservations.domain.dispatchers.*;
import com.cinema_reservations.domain.events.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CacheTests {
  private final int numberOfMemoryAddresses = 10;
  private final int addressesPerBlock = 2;
  private final int cacheLineLimit = 3;
  private final int blockNumber = 0;

  @Mock private Dispatcher<CacheEvent> eventDispatcher;
  @Mock private BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher;

  @BeforeEach
  public void initialize() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void writeMiss() {
    var memory = new MainMemory(numberOfMemoryAddresses, addressesPerBlock);
    var cache = new Cache(memory, cacheLineLimit, eventDispatcher, bidirectionalEventDispatcher);

    List<Long> newContent = List.of(2L, 3L);
    cache.writeBlock(blockNumber, newContent);

    verify(eventDispatcher).dispatch(new WriteMiss(blockNumber));

    assertEquals(newContent, cache.readBlock(blockNumber));
    verify(eventDispatcher).dispatch(new ReadHit(blockNumber));
  }
}
