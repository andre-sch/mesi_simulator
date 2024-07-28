package com.cinema_reservations.domain.events;

public record WriteMiss(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.WRITE;
  }
}
