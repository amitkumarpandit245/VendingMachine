package com.deloitte.product;

/**
 * Enum item to initialize vendor items
 */
public enum Item {
    CHOCOLATES("Chocolate", 25, 1), CANDY("Candy", 50, 2), COLD_DRINK("Cold Drink", 45, 3);
    private String name;
    private long price;
    private int code;

    Item(String name, long price, int code) {
        this.name = name;
        this.price = price;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public int getCode() {
        return code;
    }
}
