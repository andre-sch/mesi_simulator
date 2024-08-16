package com.cinema_reservations.domain;

import java.util.List;
import java.util.LinkedList;

public class Processor {
  private RoomDeserializer roomDeserializer = new RoomDeserializer();
  private ApplicationContext context;
  private Cache cache;
  private Bus bus;

  public Processor(
    ApplicationContext context,
    Bus bus
  ) {
    this.context = context;
    this.cache = bus.appendCache();
    this.bus = bus;
  }
  
  public List<Line> getCacheContent() {
    return cache.getContent();
  }

  public Room selectRoom(int roomId) {
    int firstBlockNumber = context.blockNumberOf(roomId);
    int lastBlockNumber = firstBlockNumber + context.blocksPerRoom() - 1;

    List<List<Long>> serialization = new LinkedList<>();
    for (int i = firstBlockNumber; i <= lastBlockNumber; i++) {
      List<Long> block = cache.readBlock(i);
      serialization.add(block);
    }

    return roomDeserializer.deserialize(serialization);
  }

  public void reserveSeat(int roomId, int seatNumber) {
    int blockNumber = context.blockNumberOf(roomId);
    blockNumber += context.blocksPerHeader();

    blockNumber += seatNumber / bus.addressesPerBlock();
    int blockSeatIndex = seatNumber % bus.addressesPerBlock();

    List<Long> block = cache.readBlock(blockNumber);
    long serializedSeat = block.get(blockSeatIndex);
    var seat = Seat.deserialize(serializedSeat);

    if (seat.status() == ReserveStatus.RESERVED)
      throw new RuntimeException("the seat is already reserved");

    var reservedSeat = new Seat(ReserveStatus.RESERVED);
    block.set(blockSeatIndex, reservedSeat.serialize());

    cache.writeBlock(blockNumber, block);
  }
}
