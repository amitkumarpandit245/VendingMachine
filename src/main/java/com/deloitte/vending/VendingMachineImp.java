package com.deloitte.vending;

import com.deloitte.bucket.Bucket;
import com.deloitte.coin.Coin;
import com.deloitte.exceptions.ItemNotAvailableException;
import com.deloitte.exceptions.NotFullPaidException;
import com.deloitte.exceptions.NotSufficientChangeException;
import com.deloitte.inventory.Inventory;
import com.deloitte.product.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation class for Vending machine
 */
public class VendingMachineImp implements VendingMachine {
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private Inventory<Coin> cashInventory = new Inventory<Coin>();
    private Item currentItem;
    private long currentBalance;
    private long totalSales;

    public VendingMachineImp() {
        initializeVendingMachine();
    }

    private void initializeVendingMachine() {
        Arrays.stream(Coin.values()).forEach(c -> cashInventory.put(c, 5));
        Arrays.stream(Item.values()).forEach(i -> itemInventory.put(i, 5));
    }

    public long selectItemAndGetPrice(Item item) {
        if (itemInventory.hasItem(item)) {
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new ItemNotAvailableException("Item is not available, Please buy another item");
    }

    public void insertCoin(Coin coin) {
        currentBalance = currentBalance + coin.getDenomination();
        cashInventory.add(coin);
    }

    public List<Coin> refund() {
        List<Coin> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }

    private List<Coin> getChange(double amount) throws NotSufficientChangeException {

        List<Coin> changes = Collections.EMPTY_LIST;

        if (amount > 0) {
            changes = new ArrayList<Coin>();
            double balance = amount;
            while (balance > 0) {
                if (balance >= Coin.QUARTER.getDenomination()
                        && cashInventory.hasItem(Coin.QUARTER)) {
                    changes.add(Coin.QUARTER);
                    balance = balance - Coin.QUARTER.getDenomination();
                    continue;

                } else if (balance >= Coin.DIME.getDenomination()
                        && cashInventory.hasItem(Coin.DIME)) {
                    changes.add(Coin.DIME);
                    balance = balance - Coin.DIME.getDenomination();
                    continue;

                } else if (balance >= Coin.NICKLE.getDenomination()
                        && cashInventory.hasItem(Coin.NICKLE)) {
                    changes.add(Coin.NICKLE);
                    balance = balance - Coin.NICKLE.getDenomination();
                    continue;

                } else if (balance >= Coin.CENT.getDenomination()
                        && cashInventory.hasItem(Coin.CENT)) {
                    changes.add(Coin.CENT);
                    balance = balance - Coin.CENT.getDenomination();
                    continue;

                } else {
                    throw new NotSufficientChangeException("Not Sufficient Change in the inventory, Please try another product ");
                }
            }
        }
        return changes;
    }

    private void updateCashInventory(List<Coin> change) {
        for (Coin c : change) {
            cashInventory.deduct(c);
        }
    }

    public Bucket<Item, List<Coin>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();

        List<Coin> change = collectChange();

        return new Bucket<Item, List<Coin>>(item, change);
    }

    private Item collectItem() throws NotSufficientChangeException,
            NotFullPaidException {
        if (isFullPaid()) {
            if (hasSufficientChange()) {
                itemInventory.deduct(currentItem);
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentBalance - currentItem.getPrice();
        throw new NotFullPaidException("Price not full paid, remaining : ", remainingBalance);
    }

    private List<Coin> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coin> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    private boolean isFullPaid() {
        if (currentBalance >= currentItem.getPrice()) {
            return true;
        }
        return false;
    }

    private boolean hasSufficientChange() {
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private boolean hasSufficientChangeForAmount(long amount) {
        boolean hasChange;
        try {
            getChange(amount);
            hasChange = true;
        } catch (NotSufficientChangeException nsce) {
            hasChange = false;
        }
        return hasChange;
    }

    public void reset() {
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

}
