package com.cinema_reservations.infra;

import com.cinema_reservations.domain.Bus;
import com.cinema_reservations.domain.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class ApplicationConfig {
  @Bean
  public ApplicationContext applicationContext() {
    var mainMemory = bus().getSharedMemory();

    var context = new ApplicationContext(mainMemory);
    context.setupMemory();

    return context;
  }

  @Bean
  public Bus bus() { return new Bus(); }
}
