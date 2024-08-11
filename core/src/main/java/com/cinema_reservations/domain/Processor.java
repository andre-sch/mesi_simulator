package com.cinema_reservations.domain;

import java.util.List;
import java.util.LinkedList;

public class Processor {
  private RoomDeserializer roomDeserializer = new RoomDeserializer();
  private ApplicationContext context;
  private Cache cache;
  private Bus bus;

  // todo: manipulate IO client interface (GUI) in Processor

  public Processor(
    ApplicationContext context,
    Bus bus
  ) {
    this.context = context;
    this.cache = bus.appendCache();
    this.bus = bus;
  }
  
  public Room selectRoom(int roomId) {
    int firstBlockNumber = context.blockNumberOf(roomId);
    int lastBlockNumber = firstBlockNumber + context.blocksPerRoom() - 1;

    List<List<Long>> serialization = new LinkedList<>();
    for (int i = firstBlockNumber; i <= lastBlockNumber; i++) {
      List<Long> block = cache.readBlock(i);
      serialization.add(block);
    }

    // todo: instead of returning, render the selected room
    return roomDeserializer.deserialize(serialization);
  }

  public void cancelRoomSelection() {
    // todo: just clear room rendering, do not clear cache
  }

  public void reserveSeat(int roomId, int row, int seatNumber) {
    int blockNumber = context.blockNumberOf(roomId);
    blockNumber += context.blocksPerHeader();
  
    int seatAddress = row * context.seatsPerRow() + seatNumber;
    blockNumber += seatAddress / bus.addressesPerBlock();

    List<Long> block = cache.readBlock(blockNumber);
    long serializedSeat = block.get(seatNumber);
    var seat = Seat.deserialize(serializedSeat);

    if (seat.status() == ReserveStatus.RESERVED)
      throw new RuntimeException("the seat is already reserved");

    var reservedSeat = new Seat(ReserveStatus.RESERVED);
    block.set(seatNumber, reservedSeat.serialize());

    cache.writeBlock(blockNumber, block);
  }
}
