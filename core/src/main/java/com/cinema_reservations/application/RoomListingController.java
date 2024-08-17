package com.cinema_reservations.application;

import com.cinema_reservations.domain.Room;
import com.cinema_reservations.domain.ApplicationContext;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomListingController {
  private ProcessingProvider processingProvider;
  private ApplicationContext context;

  public RoomListingController(
    ProcessingProvider processingProvider,
    ApplicationContext context
  ) {
    this.processingProvider = processingProvider;
    this.context = context;
  }

  @PutMapping("/rooms/{roomId}")
  public Room list(@RequestBody Request request, @PathVariable int roomId) {
    var processor = processingProvider.getProcessor(request.processorId);
    return processor.selectRoom(roomId);
  }

  @GetMapping("/rooms/count")
  public RoomCount count() {
    return new RoomCount(context.numberOfRooms());
  }

  private static record Request(int processorId) {}
  private static record RoomCount(int count) {}
}
