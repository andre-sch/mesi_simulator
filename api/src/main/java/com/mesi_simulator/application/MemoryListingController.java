package com.mesi_simulator.application;

import java.util.List;
import com.mesi_simulator.domain.Bus;
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
