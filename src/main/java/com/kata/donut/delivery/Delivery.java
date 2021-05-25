package com.kata.donut.delivery;

import com.kata.donut.customer.Customer;
import java.time.LocalDate;



public record Delivery(Customer customer, Order order) {

    public double getTotalPrice() {
        return order.donuts().stream()
                .mapToDouble(donut1 -> donut1.price()*donut1.quantity())
                .sum();
    }

    public boolean deliveredOn(LocalDate date){
        return order.date().isEqual(date);
    }
}
