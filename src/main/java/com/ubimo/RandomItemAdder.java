package com.ubimo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

class RandomItemAdder {
  private final Logger logger = LoggerFactory.getLogger(RandomItemAdder.class);

  private final Random random = new Random();
  private final ItemCollection itemCollection;
  private final String[] itemPool;

  RandomItemAdder(final ItemCollection itemCollection, final String... itemPool) {
    this.itemCollection = itemCollection;
    this.itemPool = itemPool;
  }

  /**
   * Adds a random element from the item pool into the ItemCollection
   */
  void add() {
    final String item = selectRandomItem();
    logger.info("Adding " + item);
    itemCollection.add(item);
  }

  private String selectRandomItem() {
    return itemPool[random.nextInt(itemPool.length)];
  }
}
