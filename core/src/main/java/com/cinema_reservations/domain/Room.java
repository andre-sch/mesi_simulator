package com.cinema_reservations.domain;

import java.util.List;
import java.util.LinkedList;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Room {
  private long id;
  private long numberOfRows;
  private long seatsPerRow;
  private List<Seat> seats;

  public Room(long id, long numberOfRows, long seatsPerRow) {
    this.id = id;
    this.numberOfRows = numberOfRows;
    this.seatsPerRow = seatsPerRow;

    this.seats = new LinkedList<>();
    for (int i = 0; i < numberOfRows * seatsPerRow; i++)
      this.seats.add(new Seat(ReserveStatus.AVAILABLE));
  }

  public Room(long id, long numberOfRows, long seatsPerRow, List<Seat> seats) {
    this(id, numberOfRows, seatsPerRow);
    this.seats = seats;
  }

  public long getId() { return id; }
  public long getNumberOfRows() { return numberOfRows; }
  public long getSeatsPerRow() { return seatsPerRow; }
  public List<Seat> getSeats() { return seats; }

  public void randomize() {
    this.seats = seats.stream()
      .map((seat) -> Seat.random())
      .toList();
  }
}
