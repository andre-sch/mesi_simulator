package com.cinema_reservations;

import com.cinema_reservations.domain.Seat;
import com.cinema_reservations.domain.ReserveStatus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeatTests {
  private final long availableStatusCode = 0;
  private final long reservedStatusCode = 1;

  @Test
  public void serialization() {
    var seat = new Seat(ReserveStatus.RESERVED);
    assertEquals(reservedStatusCode, seat.serialize());
  }

  @Test
  public void deserialization() {
    var seat = Seat.deserialize(availableStatusCode);
    assertEquals(new Seat(ReserveStatus.AVAILABLE), seat);
  }
}
