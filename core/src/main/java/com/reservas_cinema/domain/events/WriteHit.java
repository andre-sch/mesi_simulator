package com.reservas_cinema.domain.events;

public record WriteHit(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.WRITE;
  }
}
