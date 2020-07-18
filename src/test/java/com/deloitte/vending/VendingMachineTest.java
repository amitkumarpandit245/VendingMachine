package com.deloitte.vending;

import com.deloitte.bucket.Bucket;
import com.deloitte.coin.Coin;
import com.deloitte.exceptions.ItemNotAvailableException;
import com.deloitte.exceptions.NotFullPaidException;
import com.deloitte.factory.VendingMachineFactory;
import com.deloitte.product.Item;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class to check Vending Machine Functionality
 */

public class VendingMachineTest {

    private static VendingMachine vm;

    @AfterClass
    public static void tearDown() {
        vm = null;
    }

    @Before
    public void before() {
        vm = VendingMachineFactory.createVendingMachine();
    }

    @Test
    public void when_BuyItemWithExactPrice_thenDispenseItemWithoutChange() {
        long price = vm.selectItemAndGetPrice(Item.CANDY);
        assertEquals(Item.CANDY.getPrice(), price);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        assertEquals(Item.CANDY, item);
        assertTrue(change.isEmpty());
    }

    @Test
    public void when_BuyItemWithLessPrice_thenDispenseItemWithChange() {
        long price = vm.selectItemAndGetPrice(Item.CHOCOLATES);
        assertEquals(Item.CHOCOLATES.getPrice(), price);
        vm.insertCoin(Coin.QUARTER);
        vm.insertCoin(Coin.QUARTER);
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        assertEquals(Item.CHOCOLATES, item);
        assertTrue(!change.isEmpty());
        assertEquals(50 - Item.CHOCOLATES.getPrice(), getTotal(change));
    }

    @Test
    public void when_EnteredMoneyIsLessThanTheItemPrice_thanRefundAmount() {
        long price = vm.selectItemAndGetPrice(Item.COLD_DRINK);
        assertEquals(Item.COLD_DRINK.getPrice(), price);
        vm.insertCoin(Coin.DIME);
        vm.insertCoin(Coin.NICKLE);
        vm.insertCoin(Coin.CENT);
        vm.insertCoin(Coin.QUARTER);
        assertEquals(41, getTotal(vm.refund()));
    }

    @Test(expected = ItemNotAvailableException.class)
    public void when_allTheItemsAreSoldOut_thanThrowItemNotAvailableException() {
        for (int i = 0; i <= 5; i++) {
            vm.selectItemAndGetPrice(Item.COLD_DRINK);
            vm.insertCoin(Coin.QUARTER);
            vm.insertCoin(Coin.DIME);
            vm.insertCoin(Coin.DIME);
            vm.collectItemAndChange();
        }
    }

    @Test(expected = NotFullPaidException.class)
    public void when_ItemPriceNotFullyPaid_thanThrowNotFullPaidException() {
        vm.selectItemAndGetPrice(Item.CANDY);
        vm.insertCoin(Coin.QUARTER);
        vm.collectItemAndChange();
        assertEquals(24, getTotal(vm.refund()));
    }

    @Test(expected = ItemNotAvailableException.class)
    public void when_ResetVendingMachineIsCalled_thanThrowItemNotAvailableException() {
        vm.reset();
        vm.selectItemAndGetPrice(Item.COLD_DRINK);
    }

    @Ignore
    private long getTotal(List<Coin> change) {
        long total = 0;
        for (Coin c : change) {
            total = total + c.getDenomination();
        }
        return total;
    }
}