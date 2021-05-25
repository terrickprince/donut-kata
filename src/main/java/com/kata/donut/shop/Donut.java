package com.kata.donut.shop;

import com.kata.donut.constants.DonutType;

public record Donut (DonutType type, double price,Integer quantity) { }
