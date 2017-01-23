package com.ubimo

import spock.lang.Specification

class RandomItemAdderTest extends Specification {

    def "ItemCollection.add should be called #itemsToAdd times"() {
        given:
            final ItemCollection itemCollection = Mock(ItemCollection)
            final RandomItemAdder randomItemAdder = new RandomItemAdder(itemCollection, "a")

        when:
            (1..itemsToAdd).each {randomItemAdder.add()}

        then:
            itemsToAdd * itemCollection.add("a")

        where:
            itemsToAdd << [1, 2, 5, 10, 100]
    }

}
