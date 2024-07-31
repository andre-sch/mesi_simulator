package com.cinema_reservations.domain;

import java.util.List;
import java.util.LinkedList;

public record Room(
  long id,
  long numberOfRows,
  long seatsPerRow,
  List<Seat> seats
) {
  public Room(
    long id,
    long numberOfRows,
    long seatsPerRow,
    List<Seat> seats
  ) {
    this.id = id;
    this.numberOfRows = numberOfRows;
    this.seatsPerRow = seatsPerRow;
    this.seats = seats;

    if (seats.size() != numberOfRows * seatsPerRow)
      throw new RuntimeException("seat quantity mismatch");
  }

  public static Room deserialize(List<Long> serialization) {
    var serializationCopy = new LinkedList<>(serialization);
    int firstItem = 0;

    long id = serializationCopy.remove(firstItem);
    long numberOfRows = serializationCopy.remove(firstItem);
    long seatsPerRow = serializationCopy.remove(firstItem);
    List<Seat> seats = new LinkedList<>();

    for (long serializedSeat : serializationCopy)
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
