package com.ubimo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Simluator {

  private final Logger logger = LoggerFactory.getLogger(Simluator.class);

  private final ItemCollection itemCollection;
  private final String[] itemPool;
  private final Random random = new Random();
  private final Timer timer = new Timer("itemAddingTimer");

  public Simluator(final ItemCollection itemCollection, final String... itemPool) {
    this.itemCollection = itemCollection;
    this.itemPool = itemPool;
  }

  public void start() {
    logger.info("Starting timer");
    timer.schedule(new TimerTask() {
      private final RandomItemAdder randomItemAdder = new RandomItemAdder(itemCollection, itemPool);

      @Override
      public void run() {
        randomItemAdder.add();
      }
    }, 0, 100);
  }

  public void stop() {
timer.
  }

}
