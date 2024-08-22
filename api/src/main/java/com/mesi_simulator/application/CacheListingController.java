package com.mesi_simulator.application;

import java.util.List;
import com.mesi_simulator.domain.Line;
import org.springframework.web.bind.annotation.*;

@RestController
public class CacheListingController {
  private ProcessingProvider processingProvider;

  public CacheListingController(ProcessingProvider processingProvider) {
    this.processingProvider = processingProvider;
  }

  @GetMapping("/caches/{id}")
  public List<Line> listContent(@PathVariable int id) {
    var processor = processingProvider.getProcessor(id);
    return processor.getCacheContent();
  }
}
