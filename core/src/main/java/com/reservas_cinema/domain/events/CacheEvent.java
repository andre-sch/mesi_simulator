package com.reservas_cinema.domain.events;

public interface CacheEvent {
  public OperationType operation();
  public int blockNumber();
}
