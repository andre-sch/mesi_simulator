package com.mesi_simulator.application;

import com.mesi_simulator.domain.Room;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomReservationController {
  private ProcessingProvider processingProvider;

  public RoomReservationController(ProcessingProvider processingProvider) {
    this.processingProvider = processingProvider;
  }

  @PutMapping("/rooms/{roomId}/seats/{seatId}/reserve")
  public Room putMethodName(
    @RequestBody Request request,
    @PathVariable int roomId,
    @PathVariable int seatId
  ) {
    var processor = processingProvider.getProcessor(request.processorId);
    var modifiedRoom = processor.reserveSeat(roomId, seatId);
    return modifiedRoom;
  }

  private static record Request(int processorId) {}
}
