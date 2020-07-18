package com.deloitte.vending;

import com.deloitte.bucket.Bucket;
import com.deloitte.coin.Coin;
import com.deloitte.product.Item;

import java.util.List;

/**
 * Interface containing all the methods which needs to me implemented by vending machine
 */
public interface VendingMachine {
    public long selectItemAndGetPrice(Item item);

    public void insertCoin(Coin coin);

    public List<Coin> refund();

    public Bucket<Item, List<Coin>> collectItemAndChange();

    public void reset();
}
