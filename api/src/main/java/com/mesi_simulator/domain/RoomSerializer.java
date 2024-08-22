package com.mesi_simulator.domain;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class RoomSerializer {
  private int addressesPerBlock;

  public RoomSerializer(int addressesPerBlock) {
    this.addressesPerBlock = addressesPerBlock;
  }

  public List<List<Long>> serialize(Room room) {
    List<List<Long>> blockList = new LinkedList<>();

    List<Long> header = List.of(
      room.getId(),
      room.getNumberOfRows(),
      room.getSeatsPerRow()
    );

    blockList.add(header);

    List<Long> blockBuffer = new LinkedList<>();
    var seatsIterator = room.getSeats().iterator();

    while (seatsIterator.hasNext()) {
      var seat = seatsIterator.next();
      blockBuffer.add(seat.serialize());

      if (!seatsIterator.hasNext() || blockBuffer.size() == addressesPerBlock) {
        var block = new ArrayList<>(blockBuffer);
        blockList.add(block);
        blockBuffer.clear();
      }
    }

    return blockList;
  }
}
