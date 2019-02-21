/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.service;

import com.sg.vendingmachinespringmvc.dao.VendingMachineDao;
import com.sg.vendingmachinespringmvc.model.Change;
import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author vishnukdawah
 */
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {
    
    VendingMachineDao dao;
    
    Change c = new Change();
    
    // coin variables as ints
    public int numOfQuarters = 0;
    public int numOfDimes = 0;
    public int numOfNickels = 0;
    public int numOfPennies = 0;

    // coin varaiables as strings
    public String quarterMessage = "";
    public String dimeMessage = "";
    public String nickelMessage = "";
    public String pennyMessage = "";
    
    // keeps track of Total$ In (an BigDecimal version for calculations and a String version for displaying)
    public BigDecimal totalMoneyIn = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    public String stringTotalMoneyIn = "";
    
// keeps track of change
    public BigDecimal change = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    
    // keeps track of what item the user selected (an int version for calculations and a String version for displaying)
    public int itemIdFromPage = 0;
    public String stringItemIdFromPage = "";

    
//    keeps track of whether the item was vende dor not
    public boolean wasVended = false;
    
    // holds the message to be displayed in the message box
    public String messageToHold = "";
    
    // image url if Sold Out
    public String cantBuyItem = "";
    
    
    @Inject
    public VendingMachineServiceLayerImpl(VendingMachineDao dao) {
    
        this.dao = dao;
    
    }

   
    @Override
    public Item getItembyId(int itemId) throws ItemDoesNotExistException, NoItemInventoryException {
        
        Item retrievedItem;
        
        // turn NullPointerException into a NoItemInventoryException
        try{
         retrievedItem = dao.getItem(itemId);
        }catch(NullPointerException ex){
            throw new ItemDoesNotExistException(
            "Item is null");
        }
        
        // make sure the item has inventory after you get it
        validateItemInventory(retrievedItem);
        return dao.getItem(itemId); // return the item you got

    }
 
    
    
    @Override
    public List<Item> getAllItems() {
        
        return dao.getAllItems();
        
    }
    

   // used in vendItem() in controller to stop the user from buying an item that costs more than what they put in
    @Override
    public BigDecimal compareCost(BigDecimal moneyInMachine, Item chosenItem) throws InsufficientFundsException{
        
        BigDecimal zero = new BigDecimal("0.00");   // used for comparison
        BigDecimal changeToDispense = moneyInMachine.subtract(chosenItem.getItemCost());    // get change
        
        // find out how much money the user needs to buy the item
          BigDecimal moneyTheUserNeedsToBuy = ((c.calculateChange(chosenItem.getItemCost(), moneyInMachine)).multiply(new BigDecimal("-1.00")).setScale(2, RoundingMode.HALF_UP));
          
          // empty the coins here so they don't get counted before you use calculateChange() in makePurchase()  Ex. 2 quarters turn into 4 quarters if calculateChange() is called twice
          c.emptyIncrementors();
          
        if(changeToDispense.compareTo(zero) == -1){ // if the change is less than 0
            throw new InsufficientFundsException(   // throw this exception with an error message
            "Please deposit: " + moneyTheUserNeedsToBuy);
        }else{  // otherwise, return the change
            return changeToDispense;
        }
        
    }

    // use the money value submitted by the money button to add that value to the total money in the machine (stored in the service layer's variable)
    @Override
    public void addMoneyOfThisValue(String moneyValue) {
        // Ex. passing in 1.00 to make a BigDecimal
        BigDecimal moneyBD = new BigDecimal(moneyValue);
        
        // add money value to total$ in variable
        totalMoneyIn = totalMoneyIn.add(moneyBD);
        
//        convert BigDecimal to String and assign to the string you will return to Controller later
        stringTotalMoneyIn = String.valueOf(totalMoneyIn);
        
    }


    // take in the itemId submitted by the web page's URL and store it here in the service layer
    @Override
    public void acceptItemIdFromPage(String iDFromIndexPage) {
        
        // pass in itemID and give the int varaible that value so it can be used in calculations elsewhere in service layer
        int theChosenId = Integer.parseInt(iDFromIndexPage);
        itemIdFromPage = theChosenId;
        
        // convert int to String to be used in jsp so you can later assign it an empty string when Change Return is clicked
        stringItemIdFromPage = String.valueOf(itemIdFromPage);
        
    }


    // makes a purchase by taking the money in the machine and the itemId
    @Override
    public void makePurchase(BigDecimal moneyAmount, int itemId) throws NoItemInventoryException, InsufficientFundsException, ItemDoesNotExistException {

        // if BigDecimal moneyAmount is not 0...
        if(moneyAmount.signum() != 0){
         

        // get the item by the ID passed in by the URL
        Item itemGot = dao.getItem(itemId);
        
        // validate itemId (ItemDoesNotExistException)
        validateItemId(itemGot); 
        
        // validate that the user put in enough money before checking inventory, otherwise the inventory will be reduced even when the user didn't put in enough money
        // InsufficientFundsException
        compareCost(totalMoneyIn,itemGot);

        // validate the inventory before removing item (NoItemInventoryException)
        validateItemInventory(itemGot);
        
        // try to vend one but if the inventory is zero, throw NoItemInventoryException
        Item itemValid = dao.removeItem(itemGot);
        
        // if the item was vended (was not null), set boolean to true
        if(itemValid != null){
            
            wasVended = true;
            
            // when the make purchase button is clicked, clear the total$ in box
            stringTotalMoneyIn = "";
            // if the item was vended (wasVended is true) remove the image
            cantBuyItem = "";
            
        }

        // find the amount of change using Change DTO
        BigDecimal changeCalculated = c.calculateChange(itemGot.getItemCost(), moneyAmount);
        // get amount of each coin type in change
        BigDecimal a = getChange();
       
        quarterMessage = numOfQuarters + " Quarters";
        dimeMessage = numOfDimes + " Dimes";
        nickelMessage = numOfNickels + " Nickels";
        pennyMessage = numOfPennies + " Pennies";
        change = changeCalculated;
        
        
        
        }

    }

    // get the change (and assign in coins) stored in service layer
    @Override
    public BigDecimal getChange() {
       
        // get the number of each coin by using the Change object's getter methods and assign to variables
        numOfQuarters = c.getQuarterIncrementor();
        numOfDimes = c.getDimeIncrementor();
        numOfNickels = c.getNickelIncrementor();
        numOfPennies = c.getPennyIncrementor();
        
        return change;
        
    }
    

    
    // set all variables to 0 when the user clicks the Change Return button
    @Override
    public void changeReturn() {
        
//        // if no item was selected, the change is equal to the amount of money in the machine(and then will be displayed in the change box
//        if(itemIdFromPage == 0){
//            change = totalMoneyIn;
//        }

         
// set the total money in machine and the selected item Id to zero when the change return button is clicked
        totalMoneyIn = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        
        // clear Total$ In box
        stringTotalMoneyIn = "";
        
        // set ID variables to 0 and empty string
        itemIdFromPage = 0;
        stringItemIdFromPage = "";
        
        messageToHold = ""; // clear message in message box
        cantBuyItem = "";   // clear the out of stock image
        wasVended = false;  // switch boolean back to false, when it's time to attempt to vend another item
        
        // when the change return is clicked after change is there set change to zero
        // Since the total money and machine and item ID gets set to zero after change return is clicked, they will already be zero
        if(itemIdFromPage == 0 && totalMoneyIn.signum() == 0 ){
            
            change =  new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

        }
        
        // set amount of each coin to zero (setting global coin variables here to zero won't work b/c there will still be coins in the Change object
        c.setQuarterIncrementor(0);
        c.setDimeIncrementor(0);
        c.setNickelIncrementor(0);
        c.setPennyIncrementor(0);
        
        
        // clear the change return box by clearing all coin messages 
        quarterMessage = "";
        dimeMessage = "";
        nickelMessage = "";
        pennyMessage = "";
    }

    // was the item vended?
    @Override
    public boolean theVendBoolean() {
        
        return wasVended;
        
    }

    @Override
    public String getQuarterStringMessage() {

        return quarterMessage;
    }

    @Override
    public String getDimeStringMessage() {
        
        return dimeMessage;
        
    }

    @Override
    public String getNickelStringMessage() {
        
        return nickelMessage;
        
    }

    @Override
    public String getPennyStringMessage() {
        
        return pennyMessage;
    }

    @Override
    public String retrieveStringItemId() {
        
        return stringItemIdFromPage;
        
    }

    @Override
    public String getMyMoneyString() {
        
        return stringTotalMoneyIn;
        
    }
    
        // set a message to display
    @Override
    public void setErrorMessageFromService(String e) {
        
        messageToHold = e;
        
    }
    
    @Override
    public String assignMsg() {
        
        // if the item was vended
        if(wasVended == true && itemIdFromPage != 0){
             messageToHold = "Thank You!!!";
        }
        else if(itemIdFromPage == 0){   // if no item was selected
             messageToHold = "No item selected!";

        }
        else{   // if no money and/or no item was selected
             messageToHold = "Insert money and Item Choice!!!";
        }
        
        return messageToHold;
    }

    // display the message set by setErrorMessageFromService()
    @Override
    public String displayMessage() {
        
        return messageToHold;
        
    }

    @Override
    public String displayErrorImage() {
        // returns last part of URL needed to display image when item is sold out
        return cantBuyItem;
    }

    
    // used when user tries to make a purchase without selecting an item
    private void validateItemId(Item item) throws ItemDoesNotExistException{
        
        if(item == null){
            throw new ItemDoesNotExistException(
            "There is no such Item!!!");
        }
        
    }
    
// used when user tries to make a purchase of an item with no inventory left
    private void validateItemInventory(Item item) throws NoItemInventoryException{
        if(item.getItemInventory() == 0){
            cantBuyItem = "pichuSad.png";
            throw new NoItemInventoryException(
            "SOLD OUT!!!");
        }
    }

    
}
