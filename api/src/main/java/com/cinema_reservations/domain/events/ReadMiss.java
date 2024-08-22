package com.cinema_reservations.domain.events;

public record ReadMiss(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.READ;
  }
}
