package com.ubimo

import spock.lang.Specification
import spock.lang.Unroll

class ConcurrentMapItemCollectionTest extends Specification {

    def "An item that was not added should have the value '0'"() {
        final String item = "myItem"

        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        expect:
            items.getValue(item) == 0
    }

    @Unroll
    def "An item that was added #timesAdded times should have the value '#timesAdded'"() {
        final String item = "myItem"

        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        when:
            (1..timesAdded).each { items.add(item) }

        then:
            items.getValue(item) == timesAdded

        where:
            timesAdded << [1, 2, 3, 10, 100]
    }

    def "Removing a non-existent item should return false"() {
        final String item = "myItem"

        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        expect:
            items.remove(item) == false
    }

    def "Removing an existing item should return true, and remove the item"() {
        final String item = "myItem"

        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        when:
            items.add(item)

        then:
            items.remove(item) == true
            items.getValue(item) == 0
    }

    def "Calling getMaxValues() on an empty Items object should return an empty set"() {
        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        expect:
            items.getMaxValues() == [].toSet()
    }

    def "Calling getMaxValues() after a single item was added should return that same item"() {
        final String item = "myItem"

        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        when:
            items.add(item)
            items.add(item)
            items.add(item)

        then:
            items.getMaxValues() == [item].toSet()
    }

    def "Calling getMaxValues() after two items were added a different number of times should return that one that was added more times"() {
        final String item1 = "myItem"
        final String item2 = "myItem2"

        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        when:
            items.add(item1)
            items.add(item2)
            items.add(item2)

        then:
            items.getMaxValues() == [item2].toSet()
    }

    @Unroll
    def "Calling getMaxValues() after #numOfItems items were added #timesToAdd times should return all those items"() {
        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        when:
            //Add the "max" itemCollection like they should be
            final List<String> itemsToAdd = (1..numOfItems as List<String>).collect { "item$it" }
            itemsToAdd.each { final String item -> (1..timesToAdd).each {items.add(item)} }

            //Add more itemCollection a random number of times that is less than the max
            final Random random = new Random()
            (numOfItems + 1..numOfItems + 10 as List<String>).each { final int itemNumber ->
                final String item = "item$itemNumber"
                (1..random.nextInt(timesToAdd - 2) + 1).each { items.add(item) }
            }

        then:
            items.getMaxValues() == itemsToAdd as Set<String>

        where:
            numOfItems | timesToAdd
            2          | 10
            3          | 5
            10         | 50
    }

    def "Calling getMaxValues() with items as requested should return [a, b]"() {
        given:
            final ConcurrentMapItemCollection items = new ConcurrentMapItemCollection()

        when:
            items.add("a")
            items.add("b")
            items.add("b")
            items.add("c")
            items.add("a")

        then:
            items.getMaxValues() == ["a", "b"] as Set<String>
    }
}
