package com.kata.donut.shop;

import com.kata.donut.constants.DonutType;
import com.kata.donut.customer.Customer;
import com.kata.donut.delivery.Delivery;
import com.kata.donut.delivery.Order;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.util.*;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DonutShop {

    private Map<DonutType,Integer> donutStock = new HashMap<>();

    private Map<Customer,List<Delivery>> custormerDeliveryDetails = new HashMap<>();

    public void makeDonuts(DonutType type, int quantity) {
        donutStock.put(type,quantity);
    }

    public Delivery deliverOrder(String name, LocalDate today, String product) {
        Customer customer = getOrCreateCustomer(name);
        Order order = new Order(customer,today,orderToDonut(product));
        Delivery delivery = new Delivery(customer,order);
        addCustomerDelivery(customer,delivery);
        return delivery;
    }

    public Double getTotalDeliveryValueFor(LocalDate today) {
        return custormerDeliveryDetails.entrySet().stream()
                .flatMap(customerListEntry -> customerListEntry.getValue().stream())
                .filter(delivery -> delivery.deliveredOn(today))
                .mapToDouble(Delivery::getTotalPrice)
                .sum();
    }

    public Customer getTopCustomer(){
        Comparator<Pair<Customer, Double>> comparing = Comparator.comparing(Pair::getValue1);
        return custormerDeliveryDetails.entrySet().stream()
                .map(entry -> new Pair<Customer,Double>(entry.getKey(),totalCost.apply(entry.getValue().stream())))
                .sorted(comparing.reversed())
                .findFirst()
                .get()
                .getValue0();
    }


    public DoubleSummaryStatistics getDonutPriceStatistics(LocalDate today, LocalDate today1) {
        DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();

        Predicate<Delivery> orderedBetweenDate =  delivery ->
                (delivery.order().date().equals(today) || delivery.order().date().equals(today1))
                        || (delivery.order().date().isAfter(today) && delivery.order().date().isBefore(today1));

        Map<Double,Integer> doublePriceQtyMap = custormerDeliveryDetails.entrySet().stream()
                .flatMap(customerListEntry -> customerListEntry.getValue().stream())
                .filter(orderedBetweenDate)
                .flatMap(delivery -> delivery.order().donuts().stream())
                .collect(Collectors.groupingBy(Donut::price,Collectors.summingInt(Donut::quantity)));

        doublePriceQtyMap.entrySet().stream().forEach(
                entry -> IntStream.rangeClosed(1,entry.getValue()).forEach(value -> statistics.accept(entry.getKey()))
        );
        return statistics;
    }

    public List<Pair<DonutType, Integer>> getTopDonuts(int i) {
        Comparator<Pair<DonutType, Integer>> comparing = Comparator.comparing(Pair::getValue1);
        return mapDonutTypeWithCount()
                .entrySet()
                .stream()
                .map(entry -> Pair.with(entry.getKey(),entry.getValue()))
                .sorted(comparing.reversed())
                .limit(i)
                .collect(Collectors.toList());
    }

    public Map<DonutType,List<Customer>> getCustomersByDonutTypesOrdered() {
        return   custormerDeliveryDetails
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().stream())
                .flatMap(deliveryStream -> deliveryStream.order().getCustomerByDonutType().entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue,Collectors.toList())));
    }

    private Customer getOrCreateCustomer(String name){
        return custormerDeliveryDetails
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().name().equalsIgnoreCase(name))
                .map(entry-> entry.getKey())
                .findFirst()
                .orElse(new Customer(name));
    }

    private void addCustomerDelivery(Customer customer,Delivery delivery){
        if(custormerDeliveryDetails.containsKey(customer)){
            custormerDeliveryDetails.get(customer).add(delivery);
        }
        else{
            custormerDeliveryDetails.put(customer,Collections.singletonList(delivery));
        }
    }

    private List<Donut> orderToDonut(String product) {
        List<Pair<DonutType,Integer>> bagOfDonut  = Arrays.stream(product.split(","))
                .map(item -> new Pair<DonutType,Integer>(DonutType.get(item.split(":")[0]),
                        Integer.valueOf(item.split(":")[1])))
                .collect(Collectors.toList());
        Integer quantity = bagOfDonut.stream()
                .mapToInt(item -> item.getValue1())
                .sum();
        return bagOfDonut.stream()
                .map( item -> new Donut(item.getValue0(), DonutPrice.findPrice(quantity),item.getValue1()))
                .collect(Collectors.toList());
    }

    private Function<Stream<Delivery>,Double> totalCost = deliveryStream -> deliveryStream
            .mapToDouble(delivery -> delivery.getTotalPrice())
            .sum();

    private Map<DonutType, Integer> mapDonutTypeWithCount() {
        return custormerDeliveryDetails.entrySet().stream()
                .flatMap(customerListEntry -> customerListEntry.getValue().stream())
                .flatMap(object -> object.order().donuts().stream())
                .collect(Collectors.groupingBy(Donut::type,Collectors.summingInt(Donut::quantity)));
    }
}
