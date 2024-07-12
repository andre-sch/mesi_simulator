package com.reservas_cinema.domain;

import java.util.LinkedList;
import java.util.List;

public record Session(
  Room room,
  int showTime,
  int dayOfWeek
) implements Serializable {
  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();
    
    serialization.addAll(room.serialize());
    serialization.add((long) showTime);
    serialization.add((long) dayOfWeek);

    return serialization;
  }
}
