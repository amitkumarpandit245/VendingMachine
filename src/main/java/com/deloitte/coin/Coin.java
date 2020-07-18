package com.deloitte.coin;

/**
 * Enum to initialize the type of coins supported by vending machine
 */
public enum Coin {
    CENT(1), NICKLE(5), DIME(10), QUARTER(25);
    private int denomination;

    Coin(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }
}