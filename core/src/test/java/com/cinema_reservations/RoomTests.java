package com.cinema_reservations;

import java.util.List;
import com.cinema_reservations.domain.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomTests {
  private final long roomId = 3;
  private final long numberOfRows = 2;
  private final long seatsPerRow = 1;

  private final int addressesPerBlock = 3;
  private final RoomSerializer roomSerializer = new RoomSerializer(addressesPerBlock);
  private final RoomDeserializer roomDeserializer = new RoomDeserializer();

  @Test
  public void serialization() {
    var room = new Room(roomId, numberOfRows, seatsPerRow);

    assertThat(roomSerializer.serialize(room))
      .containsExactlyElementsOf(serializedRoom());
  }

  @Test
  public void deserialization() {
    var room = new Room(roomId, numberOfRows, seatsPerRow);
    assertEquals(room, roomDeserializer.deserialize(serializedRoom()));
  }

  private List<List<Long>> serializedRoom() {
    Seat availableSeat = new Seat(ReserveStatus.AVAILABLE);
    long availableSeatSerialized = availableSeat.serialize();

    return List.of(
      List.of(roomId, numberOfRows, seatsPerRow),
      List.of(availableSeatSerialized, availableSeatSerialized)
    );
  }
}
