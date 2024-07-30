package com.cinema_reservations.domain;

import java.util.List;
import java.util.LinkedList;

public record Room(
  long id,
  long numberOfRows,
  long seatsPerRow,
  List<Seat> seats
) {
  public static Room deserialize(List<Long> serialization) {
    int firstItem = 0;

    long id = serialization.remove(firstItem);
    long numberOfRows = serialization.remove(firstItem);
    long seatsPerRow = serialization.remove(firstItem);
    List<Seat> seats = new LinkedList<>();

    for (long serializedSeat : serialization)
      seats.add(Seat.deserialize(serializedSeat));

    return new Room(id, numberOfRows, seatsPerRow, seats);
  }

  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();

    serialization.add(id);
    serialization.add(numberOfRows);
    serialization.add(seatsPerRow);

    seats.forEach((seat) -> serialization.add(seat.serialize()));
    
    return serialization;
  }
}
