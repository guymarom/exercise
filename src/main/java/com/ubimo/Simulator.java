package com.ubimo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulator {

  private final Logger logger = LoggerFactory.getLogger(Simulator.class);

  private final ItemCollection itemCollection;
  private final String[] itemPool;
  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

  public static void main(final String[] args) throws InterruptedException {
    final Simulator simulator = new Simulator(new ConcurrentMapItemCollection(), "a", "b", "c", "d");
    simulator.start();
    Thread.sleep(5000);
    simulator.start();
    Thread.sleep(5000);
    simulator.stop();
  }

  private Simulator(final ItemCollection itemCollection, final String... itemPool) {
    this.itemCollection = itemCollection;
    this.itemPool = itemPool;
  }

  private void start() {
    logger.info("Starting timer...");
    executorService.scheduleWithFixedDelay(new Runnable() {
      private final RandomItemAdder randomItemAdder = new RandomItemAdder(itemCollection, itemPool);

      @Override
      public void run() {
        randomItemAdder.add();
      }
    }, 0, 100, TimeUnit.MILLISECONDS);
  }

  private void stop() {
    logger.info("Shutting down...");
    executorService.shutdown();
    try {
      executorService.awaitTermination(1, TimeUnit.SECONDS);
      logger.info("Item with max value: " + itemCollection.getMaxValues());
    } catch (final InterruptedException e) {
      logger.error("Error shutting down");
    }
  }

}
