package com.mesi_simulator.domain.events;

public record WriteMiss(
  int blockNumber
) implements CacheEvent {
  public OperationType operation() {
    return OperationType.WRITE;
  }
}
