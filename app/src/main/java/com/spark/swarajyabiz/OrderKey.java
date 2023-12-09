package com.spark.swarajyabiz;

public class OrderKey {
    private String key;
    private Order order;

    public OrderKey(String key, Order order) {
        this.key = key;
        this.order=order;
    }

    public String getKey() {
        return key;
    }

    public Order getOrder() {
        return order;
    }
}

