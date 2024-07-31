package com.cinema_reservations;

import java.util.List;
import java.util.LinkedList;
import com.cinema_reservations.domain.Room;
import com.cinema_reservations.domain.Seat;
import com.cinema_reservations.domain.ReserveStatus;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomTests {
  private final long roomId = 3;
  private final long numberOfRows = 2;
  private final long seatsPerRow = 1;

  private final Seat availableSeat = new Seat(ReserveStatus.AVAILABLE);
  private final long availableSeatSerialized = availableSeat.serialize();

  @Test
  public void serialization() {
    var room = buildRoom(numberOfRows, seatsPerRow);
    assertThat(room.serialize()).containsExactly(
      roomId,
      numberOfRows,
      seatsPerRow,
      availableSeatSerialized,
      availableSeatSerialized
    );
  }

  @Test
  public void deserialization() {
    var serialization = List.of(
      roomId,
      numberOfRows,
      seatsPerRow,
      availableSeatSerialized,
      availableSeatSerialized
    );

    var room = buildRoom(numberOfRows, seatsPerRow);
    assertEquals(room, Room.deserialize(serialization));
  }

  @Test  
  public void roomInitializationFailsWithMismatchedSeats() {
    assertThrows(RuntimeException.class, () -> {
      var noSeats = new LinkedList<Seat>();
      new Room(roomId, numberOfRows, seatsPerRow, noSeats);
    });
  }

  private Room buildRoom(long numberOfRows, long seatsPerRow) {
    List<Seat> availableSeats = new LinkedList<>();
    for (int i = 0; i < numberOfRows * seatsPerRow; i++)
      availableSeats.add(availableSeat);

    return new Room(
      roomId,
      numberOfRows,
      seatsPerRow,
      availableSeats
    );
  }
}
