package com.cinema_reservations.domain;

import java.text.MessageFormat;
import java.util.function.Consumer;
import com.cinema_reservations.domain.events.CacheEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheEventLogger implements Consumer<CacheEvent> {
  private int cacheIndex;

  public CacheEventLogger(int cacheIndex) {
    this.cacheIndex = cacheIndex;
  }

  public void accept(CacheEvent event) {
    String eventName = event.getClass().getSimpleName();
    int blockNumber = event.blockNumber();

    log.info(
      MessageFormat.format(
        "{0} - cache {1}, bloco {2}",
        eventName,
        cacheIndex,
        blockNumber
      )
    );
  }
}
