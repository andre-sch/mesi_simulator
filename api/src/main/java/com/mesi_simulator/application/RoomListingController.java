package com.mesi_simulator.application;

import com.mesi_simulator.domain.Room;
import com.mesi_simulator.domain.ApplicationContext;
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
