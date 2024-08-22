package com.mesi_simulator;

import com.mesi_simulator.domain.Seat;
import com.mesi_simulator.domain.ReserveStatus;

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
