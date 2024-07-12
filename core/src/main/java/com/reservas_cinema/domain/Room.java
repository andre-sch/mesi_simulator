package com.reservas_cinema.domain;

import java.util.LinkedList;
import java.util.List;

public record Room(
  int id,
  int numberOfRows,
  int seatsPerRow,
  List<Seat> seats
) implements Serializable {
  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();

    serialization.add((long) id);
    serialization.add((long) numberOfRows);
    serialization.add((long) seatsPerRow);

    seats.forEach((seat) -> serialization.addAll(seat.serialize()));
    
    return serialization;
  }
}
