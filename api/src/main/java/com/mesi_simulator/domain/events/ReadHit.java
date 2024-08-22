package com.mesi_simulator.domain.events;

public record ReadHit(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.READ;
  }
}
