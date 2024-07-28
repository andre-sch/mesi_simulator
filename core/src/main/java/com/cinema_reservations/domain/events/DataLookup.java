package com.cinema_reservations.domain.events;

public record DataLookup(
  int blockNumber,
  boolean isSuccessful
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.READ;
  }
}
