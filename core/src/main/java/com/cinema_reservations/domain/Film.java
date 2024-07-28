package com.cinema_reservations.domain;

import java.util.LinkedList;
import java.util.List;

public record Film(
  long id,
  String title,
  String description,
  long duration,
  List<Session> sessions
) implements Serializable {
  public List<Long> serialize() {
    List<Long> serialization = new LinkedList<>();

    serialization.add(id);
    serialization.addAll(serializeSequence(title));
    serialization.addAll(serializeSequence(description));
    serialization.add(duration);

    sessions.forEach((session) -> serialization.addAll(session.serialize()));

    return serialization;
  }

  private List<Long> serializeSequence(String sequence) {
    List<Long> serialization = new LinkedList<>();
    
    final int bytesPerChunk = 8;
    final int bitsPerByte = 8;
    long chunk = 0;

    for (int i = 0; i < sequence.length(); i++) {
      int bytePosition = i % bytesPerChunk;
      chunk += sequence.charAt(i) << (bytePosition * bitsPerByte);

      boolean isInLastPosition = bytePosition == bytesPerChunk - 1;
      if (isInLastPosition) {
        serialization.add(chunk);
        chunk = 0;
      }
    }

    return serialization;
  }
}
