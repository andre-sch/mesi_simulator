package com.cinema_reservations.domain;

import java.util.Map;
import java.util.HashMap;

public class ApplicationContext {
  private final int numberOfRooms = 2;
  private final int numberOfRows = 9;
  private final int seatsPerRow = 3;

  private final MainMemory memory;
  private final RoomSerializer roomSerializer;
  private final Map<Integer, Integer> blockNumberByRoom = new HashMap<>();

  public ApplicationContext(MainMemory memory) {
    this.memory = memory;
    this.roomSerializer = new RoomSerializer(memory.addressesPerBlock());
  }

  public int numberOfRooms() { return numberOfRooms; }
  public int numberOfRows() { return numberOfRows; }
  public int seatsPerRow() { return seatsPerRow; }
  public int blockNumberOf(int roomId) { return blockNumberByRoom.get(roomId); }

  public int blocksPerRoom() {
    var sampleRoom = new Room(0, numberOfRows, seatsPerRow);
    int numberOfRoomAddresses = roomSerializer.serialize(sampleRoom).size();
    return numberOfRoomAddresses / memory.addressesPerBlock();
  }

  public int blocksPerHeader() {
    final int numberOfHeaderAddresses = 3; // roomId, numberOfRows, seatsPerRow
    return numberOfHeaderAddresses / memory.addressesPerBlock();
  }

  public void setupMemory() {
    int blockNumber = 0;
    for (int roomId = 0; roomId < numberOfRooms; roomId++) {
      var room = new Room(roomId, numberOfRows, seatsPerRow);
      room.randomize();

      var serializedBlocks = roomSerializer.serialize(room);
      blockNumberByRoom.put(roomId, blockNumber);
  
      for (var block : serializedBlocks) {
        memory.writeBlock(blockNumber, block);
        blockNumber++;
      }
    }
  }
}
