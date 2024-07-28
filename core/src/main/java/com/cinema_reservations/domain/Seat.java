package com.cinema_reservations.domain;

import java.util.LinkedList;
import java.util.List;

public record Seat(
  ReserveStatus status
) implements Serializable {
  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();
    serialization.add((long) status.get());
    return serialization;
  }
}
