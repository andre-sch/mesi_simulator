package com.reservas_cinema.domain;

import java.util.LinkedList;
import java.util.List;

public record Room(
  long id,
  long numberOfRows,
  long seatsPerRow,
  List<Seat> seats
) implements Serializable {
  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();

    serialization.add(id);
    serialization.add(numberOfRows);
    serialization.add(seatsPerRow);

    seats.forEach((seat) -> serialization.addAll(seat.serialize()));
    
    return serialization;
  }
}
