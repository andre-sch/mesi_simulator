package com.cinema_reservations.domain.events;

public interface CacheEvent {
  public OperationType operation();
  public int blockNumber();
}
