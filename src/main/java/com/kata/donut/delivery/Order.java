package com.kata.donut.delivery;

import com.kata.donut.shop.Donut;
import com.kata.donut.constants.DonutType;
import com.kata.donut.customer.Customer;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public record Order(Customer customer, LocalDate date, List<Donut> donuts) {
    public Map<DonutType,Customer> getCustomerByDonutType(){
        return donuts.stream()
                .collect(Collectors.toMap(donut -> donut.type(),donut -> customer));
    }
}
