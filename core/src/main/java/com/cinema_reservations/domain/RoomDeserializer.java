package com.cinema_reservations.domain;

import java.util.List;
import java.util.LinkedList;

public class RoomDeserializer {
  public Room deserialize(List<List<Long>> blockList) {
    var blockListCopy = new LinkedList<>(blockList);

    List<Long> header = blockListCopy.remove(0);

    long id = header.get(0);
    long numberOfRows = header.get(1);
    long seatsPerRow = header.get(2);

    long numberOfSeats = numberOfRows * seatsPerRow;

    List<Long> blockListContent = new LinkedList<>();
    blockListCopy.forEach(blockListContent::addAll);

    List<Seat> seats = new LinkedList<>();
    var contentIterator = blockListContent.listIterator();

    while (
      contentIterator.hasNext() &&
      contentIterator.nextIndex() < numberOfSeats
    ) {
      long serializedSeat = contentIterator.next();
      seats.add(Seat.deserialize(serializedSeat));
    }

    return new Room(id, numberOfRows, seatsPerRow, seats);
  }
}
