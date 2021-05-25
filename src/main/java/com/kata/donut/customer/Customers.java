package com.kata.donut.customer;

import java.util.List;

public class Customers {

    List<Customer> customers;

    public Customers(List<Customer> customers) {
        this.customers = customers;
    }

    public boolean named(String name){
        return customers
                .stream()
                .anyMatch(customer -> customer.name().equalsIgnoreCase(name));
    }
}
