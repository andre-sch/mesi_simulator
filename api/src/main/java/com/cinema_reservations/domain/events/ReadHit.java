package com.cinema_reservations.domain.events;

public record ReadHit(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.READ;
  }
}
