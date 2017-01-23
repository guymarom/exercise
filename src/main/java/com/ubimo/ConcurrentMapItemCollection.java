package com.ubimo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.collect.Lists.newArrayList;

/**
 * An implementation of Itms that uses a concurrent map for thread-safety.
 */
public class ConcurrentMapItemCollection implements ItemCollection {

  private final ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();

  /*
  A comparator that compares two integers in a reversed order. Meaning - if the first number is smaller than the
  second the result will be larger than 0, if the first number is smaller than the second the result will be smaller than 0.
   */
  private static final Comparator<Entry<String, Integer>> reverseIntegerComparator = new Comparator<Entry<String, Integer>>() {
    @Override
    public int compare(final Entry<String, Integer> e1, final Entry<String, Integer> e2) {
      return e1.getValue().compareTo(e2.getValue());
    }
  }.reversed();

  /**
   * Returns the value associated with the given item, or the value 0 if the item has no associated value
   */
  @Override
  public int getValue(final String item) {
    return map.getOrDefault(item, 0);
  }

  /**
   * Adds the given item to this collection.
   * <p>
   * If the given item does not yet exist in the collection it will be added with the value '1'
   * Otherwise, its value will be incremented by '1'
   * </p>
   * <p>
   * Time complexity is dependent on the chosen ConcurrentMap implementation. Generally speaking this
   * method may involve retries in case of locking and even rehashing.
   * </p>
   */
  @Override
  public void add(final String item) {
    map.compute(item, (s, currentValue) -> {
      if (currentValue == null) {
        return 1;
      } else {
        return currentValue + 1;
      }
    });
  }

  /**
   * Removes the given item from this collection
   * <p>
   * Time complexity is dependent on the chosen ConcurrentMap implementation.
   * </p>
   *
   * @return 'true' if and only if the item existed in the collection before removal. 'false' otherwise
   */
  @Override
  public boolean remove(final String item) {
    final Integer valueBeforeRemoval = map.remove(item);
    return valueBeforeRemoval != null && valueBeforeRemoval > 0;
  }

  /**
   * Returns one or more items that are associated with the maximal value in this collection.
   * If no values exist in the collection, the empty set will be returned.
   * <p>
   * Time complexity for this method is O(N).
   * </p>
   * <p>
   * This method does not lock the collection for addition of new items which means the returned result may be stale
   * in case additions are made during the calculation.
   * </p>
   */
  @Override
  public Set<String> getMaxValues() {
    if (map.size() == 0) return Collections.emptySet();

    final List<Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
    entries.sort(reverseIntegerComparator);
    final int maxValue = entries.get(0).getValue();
    final List<String> result = newArrayList();
    for (final Entry<String, Integer> entry : entries) {
      if (entry.getValue() == maxValue) result.add(entry.getKey());
    }
    return new HashSet<>(result);
  }

}
