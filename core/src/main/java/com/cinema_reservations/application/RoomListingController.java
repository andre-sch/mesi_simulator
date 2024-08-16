package com.cinema_reservations.application;

import com.cinema_reservations.domain.Room;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomListingController {
  private ProcessingProvider processingProvider;

  public RoomListingController(ProcessingProvider processingProvider) {
    this.processingProvider = processingProvider;
  }

  @PutMapping("/rooms/{roomId}")
  public Room list(@RequestBody Request request, @PathVariable int roomId) {
    var processor = processingProvider.getProcessor(request.processorId);
    return processor.selectRoom(roomId);
  }

  private static record Request(int processorId) {}
}
