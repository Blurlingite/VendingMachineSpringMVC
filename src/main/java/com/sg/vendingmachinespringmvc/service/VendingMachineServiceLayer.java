/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.service;

import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author vishnukdawah
 */
public interface VendingMachineServiceLayer {

    
    Item getItembyId(int itemId) throws NoItemInventoryException,ItemDoesNotExistException; 

    List<Item> getAllItems();
    
    BigDecimal compareCost(BigDecimal moneyInMachine, Item chosenItem) throws InsufficientFundsException;
    
    public void addMoneyOfThisValue(String moneyValue);

    public void acceptItemIdFromPage(String iDFromIndexPage);

    public void makePurchase(BigDecimal moneyAmount, int itemId) throws NoItemInventoryException, InsufficientFundsException, ItemDoesNotExistException;

    public BigDecimal getChange();

    public void changeReturn();

    public boolean theVendBoolean();

    public String getQuarterStringMessage();

    public String getDimeStringMessage();

    public String getNickelStringMessage();

    public String getPennyStringMessage();

    public String retrieveStringItemId();

    public String getMyMoneyString();
    
    public void setErrorMessageFromService(String e);

    public String assignMsg();
    
    public String displayMessage();

    public String displayErrorImage();
}
