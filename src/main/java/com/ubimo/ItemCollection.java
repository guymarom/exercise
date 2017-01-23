package com.ubimo;

import java.util.Set;

public interface ItemCollection {
  int getValue(String item);
  void add(String item);
  boolean remove(String item);
  Set<String> getMaxValues();
}
