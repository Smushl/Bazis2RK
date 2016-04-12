package ru.vesna_mf.logic;

/**
 * Created by Некрасов on 13.11.2015.
 */
public class OrderItem {


    Product product;
    Integer amount;

    public OrderItem(Product product, Integer amount) {
        this.product = product;
        this.amount = amount;
    }
}
