package com.mesi_simulator.domain.dispatchers;

import java.util.*;
import java.util.function.*;

public class BidirectionalDispatcher<EventSent, EventReceived> {
  private final List<Function<EventSent, EventReceived>> observers = new LinkedList<>();
  private final BinaryOperator<EventReceived> eventReducer;

  public BidirectionalDispatcher(BinaryOperator<EventReceived> eventReducer) {
    this.eventReducer = eventReducer;
  }
  
  public void addObserver(Function<EventSent, EventReceived> observer) {
    observers.add(observer);
  }

  public Optional<EventReceived> dispatch(EventSent event) {
    return observers.stream()
      .map((observer) -> observer.apply(event))
      .reduce(eventReducer);
  }
}
