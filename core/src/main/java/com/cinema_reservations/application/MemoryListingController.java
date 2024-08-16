package com.cinema_reservations.application;

import java.util.List;
import com.cinema_reservations.domain.Bus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class MemoryListingController {
  private Bus bus;

  public MemoryListingController(Bus bus) {
    this.bus = bus;
  }

  @GetMapping("/shared-memory")
  public List<Long> listContent() {
    var memory = bus.getSharedMemory();
    return memory.getContent();
  }
}
