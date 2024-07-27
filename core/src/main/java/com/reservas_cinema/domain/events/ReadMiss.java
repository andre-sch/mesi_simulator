package com.reservas_cinema.domain.events;

public record ReadMiss(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.READ;
  }
}
