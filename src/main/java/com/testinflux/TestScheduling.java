package com.testinflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
//@EnableScheduling
public class TestScheduling {

  @Bean
  public void calling() {
    testScheduler();
  }

  @Scheduled(cron = "0 49 11 * * *", zone = "Asia/Saigon")
  public void testScheduler() {
    log.info("starting scheduler ..." + System.currentTimeMillis() / 1000);
  }

  @Scheduled(fixedRate = 2000L, initialDelay = 2000L)
  public void testSchedulers() {
    log.info("starting scheduler ..." + System.currentTimeMillis() / 1000);
  }
}
