package com.cinema_reservations.domain.dispatchers;

import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;

public class Dispatcher<Event> {
  private final List<Consumer<Event>> observers = new LinkedList<>();

  public void addObserver(Consumer<Event> observer) {
    observers.add(observer);
  }

  public void dispatch(Event event) {
    observers.forEach((observer) -> observer.accept(event));
  }
}
