package com.cinema_reservations.application;

import java.util.*;
import com.cinema_reservations.domain.*;
import org.springframework.stereotype.Component;

@Component
public class ProcessingProvider {
  private List<Processor> processors = new LinkedList<>();
  private ApplicationContext context;
  private Bus bus;

  public ProcessingProvider(
    ApplicationContext context,
    Bus bus
  ) {
    this.context = context;
    this.bus = bus;
  }

  public int appendProcessor() {
    int id = processors.size();
    processors.add(new Processor(context, bus));
    return id;
  }

  public Processor getProcessor(int id) {
    return processors.get(id);
  }
}
