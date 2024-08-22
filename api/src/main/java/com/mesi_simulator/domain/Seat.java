package com.mesi_simulator.domain;

public record Seat(
  ReserveStatus status
) {
  public static Seat deserialize(Long serialization) {
    int code = serialization.intValue();
    var status = ReserveStatus.of(code);
    return new Seat(status);
  }

  public Long serialize() {
    return (long) status.get();
  }

  public static Seat random() {
    var randomStatus = ReserveStatus.random();
    return new Seat(randomStatus);
  }
}
