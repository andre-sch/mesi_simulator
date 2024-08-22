package com.mesi_simulator.domain.events;

public interface CacheEvent {
  public OperationType operation();
  public int blockNumber();
}
