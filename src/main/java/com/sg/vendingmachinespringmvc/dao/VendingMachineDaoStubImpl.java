/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.dao;

import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vishnukdawah
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao {
    
   Item onlyItem;
   Item zeroInventory;  // to help test for 0 inventory

  private Map<Integer, Item> allItemsTest = new HashMap<>(); // (ItemId, Item)

   
   public VendingMachineDaoStubImpl() {
       onlyItem = new Item(4, "Lava Cookie", new BigDecimal("1.00"), 4);
       zeroInventory = new Item(5, "Lemonade", new BigDecimal("1.00"), 0);
       allItemsTest.put(4, onlyItem);
       allItemsTest.put(5, zeroInventory);

   }


    @Override
    public Item getItem(int itemId) {
        Item item1 = allItemsTest.get(itemId);
        if(item1.getItemId() == onlyItem.getItemId()){
            return onlyItem;
        }else{
            return null;
        }
        
    }

    @Override
    public List<Item> getAllItems() {
        
        return new ArrayList<Item>(allItemsTest.values());
    }

    @Override
    public Item removeItem(Item item) {
        
        int oldInventory = item.getItemInventory();

        // remove one inventory
        item.setItemInventory(oldInventory - 1);
        
        Item removedOne = allItemsTest.put(4, item);


        // if you removed one inventory return it
        if(removedOne.getItemInventory() == (oldInventory - 1)){
            return removedOne;
        }else{
            return null;
        }
    }



    
}
