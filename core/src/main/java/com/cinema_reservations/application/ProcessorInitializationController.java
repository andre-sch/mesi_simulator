package com.cinema_reservations.application;

import org.springframework.web.bind.annotation.*;

@RestController
public class ProcessorInitializationController {
  private ProcessingProvider processingProvider;

  public ProcessorInitializationController(
    ProcessingProvider processingProvider
  ) {
    this.processingProvider = processingProvider;
  }

  @PostMapping("/processors")
  public Response initialize() {
    int id = processingProvider.appendProcessor();
    return new Response(id);
  }

  private static record Response(int id) {}
}
