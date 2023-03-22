package com.antelif.library.domain.service.scheduler;

import com.antelif.library.domain.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/** Scheduler to calculate customer fee. */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerFeeScheduler {

  private final CustomerService customerService;

  /** Recalculates customer fee every day at 07:30. */
  @Scheduled(cron = "${app.properties.fee-calculation-rate}")
  @Async
  public void customerFeeCalculationScheduler() {
    StopWatch watch = new StopWatch();
    watch.start();

    log.info("Calculating fees of customers.");

    customerService.recalculateCustomerFee();

    watch.stop();

    log.info("Fee calculation took {} seconds.", watch.getTotalTimeSeconds());
  }
}
