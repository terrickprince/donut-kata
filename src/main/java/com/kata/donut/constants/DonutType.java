package com.kata.donut.constants;

import java.util.Arrays;

public enum DonutType {
    BOSTON_CREAM("BC"),
    BAVARIAN_CREAM("BA"),
    BLUEBERRY("B"),
    GLAZED("G"),
    OLD_FASHIONED("OF"),
    PUMPKIN("P"),
    JELLY("J"),
    VANILLA_FROSTED("VF");

    private final String code;

    DonutType(String code) {
        this.code = code;
    }

    public static DonutType get(String code) {
        return Arrays.stream(DonutType.values())
                .filter(donutType -> donutType.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public String getCode() {
        return code;
    }
}
