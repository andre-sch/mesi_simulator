package com.cinema_reservations.application;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
public class RoomReservationController {
  private ProcessingProvider processingProvider;

  public RoomReservationController(ProcessingProvider processingProvider) {
    this.processingProvider = processingProvider;
  }

  @PutMapping("/rooms/{roomId}/seats/{seatId}/reserve")
  @ResponseStatus(code = HttpStatus.NO_CONTENT)
  public void putMethodName(
    @RequestBody Request request,
    @PathVariable int roomId,
    @PathVariable int seatId
  ) {
    var processor = processingProvider.getProcessor(request.processorId);
    processor.reserveSeat(roomId, seatId);
  }

  private static record Request(int processorId) {}
}
