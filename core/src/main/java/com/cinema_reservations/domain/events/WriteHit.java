package com.cinema_reservations.domain.events;

public record WriteHit(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.WRITE;
  }
}
