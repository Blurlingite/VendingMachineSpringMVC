/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.dao;

import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author vishnukdawah
 */
public class VendingMachineDaoInMemImpl implements VendingMachineDao {
    
    
    private Map<Integer, Item> allItems = new HashMap<>(); // (ItemId, Item)

    // add mock data
    public VendingMachineDaoInMemImpl() {
        
        Item itemA = new Item(1,"Snickers", new BigDecimal(1.85).setScale(2, RoundingMode.HALF_UP),9);
        Item itemB = new Item(2,"M&M's", new BigDecimal(1.50).setScale(2, RoundingMode.HALF_UP),2);
        Item itemC = new Item(3,"Pringles", new BigDecimal(2.10).setScale(2, RoundingMode.HALF_UP),5);
        Item itemD = new Item(4,"Reese's", new BigDecimal(1.85).setScale(2, RoundingMode.HALF_UP),4);
        Item itemE = new Item(5,"Pretzels", new BigDecimal(1.25).setScale(2, RoundingMode.HALF_UP),9);
        Item itemF = new Item(6,"Twinkies", new BigDecimal(1.95).setScale(2, RoundingMode.HALF_UP),3);
        Item itemG = new Item(7,"Doritos", new BigDecimal(1.75).setScale(2, RoundingMode.HALF_UP),11);
        Item itemH = new Item(8,"Almond Joy", new BigDecimal(1.85).setScale(2, RoundingMode.HALF_UP),0);
        Item itemI = new Item(9,"Trident", new BigDecimal(1.95).setScale(2, RoundingMode.HALF_UP),6);


        allItems.put(1,itemA);
        allItems.put(2,itemB);
        allItems.put(3,itemC);
        allItems.put(4,itemD);
        allItems.put(5,itemE);
        allItems.put(6,itemF);
        allItems.put(7,itemG);
        allItems.put(8,itemH);
        allItems.put(9,itemI);

        
    }

    
     // get a single item out of arraylist returned by getAllItems()
    @Override
    public Item getItem(int itemId) {
       
        return allItems.get(itemId);
        
    }
    
    // loads items from file into an arraylist
    @Override
    public List<Item> getAllItems() {
        
//        return new ArrayList<Item>(allItems.values()); // ArrayList is the actual implementation of List
        return allItems.values()
                .stream()   // get the stream of values
                .collect(Collectors.toList());  // collect those values into a list
    }

    @Override
    public Item removeItem(Item item) {
        
        int oldInventory = item.getItemInventory();
        item.setItemInventory(oldInventory - 1);
        allItems.put(item.getItemId(), item);
        
        return item;
    }
     
   
}
