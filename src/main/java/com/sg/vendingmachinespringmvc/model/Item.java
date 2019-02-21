/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author vishnukdawah
 */
public class Item {
    private int itemId;
    private String itemName;
    private BigDecimal itemCost;
    private int itemInventory;

    public Item(int itemId, String itemName, BigDecimal itemCost, int itemInventory) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCost = itemCost; 
        this.itemInventory = itemInventory; 
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }

    public void setItemInventory(int itemInventory) {
        this.itemInventory = itemInventory;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public int getItemInventory() {
        return itemInventory;
    }

    
    // override equals() and hashCode() tohelp test functionality
    // will allow us to compare one Item object to another without writing extra code
    // whenever you do unit testing, override the equals() and hashCode() in the applicable dto
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.itemId;
        hash = 97 * hash + Objects.hashCode(this.itemName);
        hash = 97 * hash + Objects.hashCode(this.itemCost);
        hash = 97 * hash + this.itemInventory;
        return hash;
    }

    // use this to check if Dao altered the data in any way in unit testing
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (this.itemId != other.itemId) {
            return false;
        }
        if (this.itemInventory != other.itemInventory) {
            return false;
        }
        if (!Objects.equals(this.itemName, other.itemName)) {
            return false;
        }
        if (!Objects.equals(this.itemCost, other.itemCost)) {
            return false;
        }
        return true;
    }
    
    // for logging aspect, format how the entry looks by overridding the built in toString() methpd
    @Override
    public String toString(){
        return " ID: " + itemId + " |Name: " + itemName + " |Cost: " + itemCost + 
                " |Inventory: " + (itemInventory);
    }
    
}