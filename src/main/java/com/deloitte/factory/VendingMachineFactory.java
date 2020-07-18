package com.deloitte.factory;

import com.deloitte.vending.VendingMachine;
import com.deloitte.vending.VendingMachineImp;

/**
 * Factory to produce Vending Machine Object
 */
public class VendingMachineFactory {
    public static VendingMachine createVendingMachine() {
        return new VendingMachineImp();
    }
}
