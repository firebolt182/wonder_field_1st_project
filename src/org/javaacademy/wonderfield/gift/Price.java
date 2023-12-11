package org.javaacademy.wonderfield.gift;

public enum Price {
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    ONE_THOUSAND_AND_HALF(1500),
    TWO_THOUSAND(2000),
    TWO_THOUSAND_AND_HALF(2500),
    THREE_THOUSAND(3000);

    private int price;

    Price(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
