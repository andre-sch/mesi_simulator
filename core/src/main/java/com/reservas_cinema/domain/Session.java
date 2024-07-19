package com.reservas_cinema.domain;

import java.util.LinkedList;
import java.util.List;

public record Session(
  Room room,
  long showTime,
  long dayOfWeek
) implements Serializable {
  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();
    
    serialization.addAll(room.serialize());
    serialization.add(showTime);
    serialization.add(dayOfWeek);

    return serialization;
  }
}
