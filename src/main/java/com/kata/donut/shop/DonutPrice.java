package com.kata.donut.shop;
import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class DonutPrice {
    private DonutPrice(){}
    private static final int SINGLE = 1;
    private static final int DOUBLE = 2;
    private static final int HALF_DOZEN = 6;
    private static final int DOZEN = 12;
    private static final int BAKERS_DOZEN = 13;

    private static final Predicate<Integer> SINGLE_PREDICATE = integer -> Range.closed(0, SINGLE)
            .contains(integer);
    private static final Predicate<Integer> HALF_DOZEN_PREDICATE = integer -> Range.closed(DOUBLE, HALF_DOZEN)
            .contains(integer);
    private static final Predicate<Integer> TILL_DOZEN_PREDICATE = integer -> Range.open(HALF_DOZEN, DOZEN)
            .contains(integer);
    private static final Predicate<Integer> DOZEN_PREDICATE = integer -> Range.closed(DOZEN, DOZEN)
            .contains(integer);
    private static final Predicate<Integer> BAKER_DOZEN_PREDICATE = integer -> Range.closed(BAKERS_DOZEN, DOZEN * 100)
            .contains(integer);
    private static final Map<Predicate,Double> PRICES = new HashMap<>();

    static {
        PRICES.put(SINGLE_PREDICATE,1.50d);
        PRICES.put(HALF_DOZEN_PREDICATE,1.35d);
        PRICES.put(TILL_DOZEN_PREDICATE,1.25d);
        PRICES.put(DOZEN_PREDICATE,1.00d);
        PRICES.put(BAKER_DOZEN_PREDICATE,0.95d);
    }

    static Double findPrice(int quantity){
        return PRICES
                .entrySet()
                .stream()
                .filter(intStreamDoubleEntry -> intStreamDoubleEntry.getKey().test(quantity))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0.0);
    }
}
