package com.mesi_simulator;

import com.mesi_simulator.domain.*;
import com.mesi_simulator.domain.dispatchers.*;
import com.mesi_simulator.domain.events.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CacheTests {
  private final int numberOfMemoryAddresses = 10;
  private final int addressesPerBlock = 2;
  private final int cacheLineLimit = 1;
  private final int blockNumber = 0;

  private final List<Long> blockA = List.of(2L, 3L);
  private final List<Long> blockB = List.of(3L, 2L);
  private final List<Long> emptyBlock = List.of(0L, 0L);

  private final MainMemory memory = new MainMemory(numberOfMemoryAddresses, addressesPerBlock);

  @Mock private Dispatcher<CacheEvent> eventDispatcher;
  @Mock private BidirectionalDispatcher<ReadMiss, DataLookup> bidirectionalEventDispatcher;

  @BeforeEach
  public void initialize() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void writeMissAndReadHit() {
    // arrange
    var cache = buildCache();

    // act
    cache.writeBlock(blockNumber, blockA);

    // assert
    var line = getFirstLineOf(cache);
    assertEquals(ProtocolStatus.MODIFIED, line.getStatus());
    assertEquals(blockA, line.getContent());

    verify(eventDispatcher).dispatch(new WriteMiss(blockNumber));

    assertEquals(blockA, cache.readBlock(blockNumber));
    verify(eventDispatcher).dispatch(new ReadHit(blockNumber));
  }

  @Test
  public void writeHit() {
    // arrange
    var cache = buildCache();

    // act
    cache.writeBlock(blockNumber, blockA);
    cache.writeBlock(blockNumber, blockB);

    // assert
    var line = getFirstLineOf(cache);
    assertEquals(ProtocolStatus.MODIFIED, line.getStatus());
    assertEquals(blockB, line.getContent());
    
    verify(eventDispatcher).dispatch(new WriteMiss(blockNumber));
    verify(eventDispatcher).dispatch(new WriteHit(blockNumber));
  }

  @Test
  public void readMiss() {
    // arrange
    var cache = buildCache();
    var event = new ReadMiss(blockNumber);

    var dataExistsInExternalCaches = new DataLookup(blockNumber, true);
    when(bidirectionalEventDispatcher.dispatch(event))
      .thenReturn(Optional.of(dataExistsInExternalCaches));

    // act
    var block = cache.readBlock(blockNumber);

    // assert
    assertEquals(emptyBlock, block);

    var line = getFirstLineOf(cache);
    assertEquals(ProtocolStatus.SHARED, line.getStatus());

    verify(eventDispatcher).dispatch(event);
    verify(bidirectionalEventDispatcher).dispatch(event);
  }

  @Test
  public void writeBackPolicy() {
    // arrange
    var cache = buildCache();

    // act
    cache.writeBlock(blockNumber, blockA);
    cache.writeBlock(blockNumber + 1, blockB);
    
    // assert
    var line = getFirstLineOf(cache);
    assertEquals(blockB, line.getContent());
    assertEquals(blockA, memory.readBlock(blockNumber));
  }

  @Test
  public void externalCacheInvalidation() {
    var externalCache = buildCache();
    var snoopingHandler = externalCache.getHandler();
    externalCache.writeBlock(blockNumber, blockA);
    
    snoopingHandler.accept(new WriteHit(blockNumber));

    var line = getFirstLineOf(externalCache);
    assertEquals(ProtocolStatus.INVALID, line.getStatus());
  }

  @Test
  public void externalCacheSharing() {
    var externalCache = buildCache();
    var snoopingHandler = externalCache.getHandler();
    externalCache.writeBlock(blockNumber, blockA);

    snoopingHandler.accept(new ReadHit(blockNumber));

    var line = getFirstLineOf(externalCache);
    assertEquals(ProtocolStatus.SHARED, line.getStatus());
    assertEquals(blockA, memory.readBlock(blockNumber));
  }

  private Cache buildCache() {
    return new Cache(memory, cacheLineLimit, eventDispatcher, bidirectionalEventDispatcher);
  }

  private Line getFirstLineOf(Cache cache) {
    var content = cache.getContent();
    return content.get(0);
  }
}
