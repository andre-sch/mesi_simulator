package com.mesi_simulator.application;

import java.util.*;
import com.mesi_simulator.domain.*;
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

  public Processor getProcessor(int id) {
    while (id >= processors.size())
      processors.add(new Processor(context, bus));
    return processors.get(id);
  }
}
