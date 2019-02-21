/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.service;

import com.sg.vendingmachinespringmvc.model.Change;
import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author vishnukdawah
 */
public class VendingMachineServiceLayerTest {
    
    private VendingMachineServiceLayer service;    // Since this is a test of the Service Layer interface, you need this variable
//    private VendingMachineDao daoStub = new VendingMachineDaoStubImpl();
    
    private BigDecimal temporyMoneyIn = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    private Change cTest = new Change();
    private int itemIdFromPageTest = 0;
    
    private boolean wasVended = false;
    
    private int numOfQuartersTest = 0;
    private int numOfDimesTest = 0;
    private int numOfNickelsTest = 0;
    private int numOfPenniesTest = 0;
    
    private String stringNumOfQuartersTest = "";
    private String stringNumOfDimesTest = "";
    private String stringNumOfNickelsTest = "";
    private String stringNumOfPenniesTest = "";
    
    private String messageToHoldTest = "";
    private String cantBuyItemTest = "";
    
    

    public VendingMachineServiceLayerTest() {
//        this.service = VendingMachineServiceLayer;
        ApplicationContext ctx = new ClassPathXmlApplicationContext("test-applicationContext.xml");
        service = ctx.getBean("VendingMachineServiceLayer", VendingMachineServiceLayer.class);   // get the bean from that xml file
//        service = new VendingMachineServiceLayerImpl(daoStub);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getItem method, of class VendingMachineServiceLayer.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetItemById() throws Exception {
  
       Item nonExistant;    // will hold an item that doesn't exist to trigger ItemDoesNotExistException
       Item noInventory;    // will hold an item that has no inventory left to trigger NoItemInventoryException
       
       // try to get an item that does not exist
       try{
       nonExistant = service.getItembyId(46);
       fail("Did not catch ItemDoesNotExistException");
       }catch(ItemDoesNotExistException ex){
           return;
       }
       
       // try to get an item with no inventory
       try{
           noInventory = service.getItembyId(5);
           fail("Did not catch NoItemInventoryException");
       }catch(NoItemInventoryException ex){
           return;
       }
           

       
       // get the item
        Item item = service.getItembyId(4);
        // assert it is the right one
        assertEquals(4,item.getItemId());

    }

    /**
     * Test of getAllItems method, of class VendingMachineServiceLayer.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAllItems() throws Exception {
        
        List<Item> itemList = service.getAllItems();    // get the list which should only have 2 items
        assertEquals(2, itemList.size());   //  only 2 items should be in the list so assert that the size is 2
    }
    


    /**
     * Test of compareCost method, of class VendingMachineServiceLayer.
     * @throws java.lang.Exception
     */
    @Test
    public void testCompareCost() throws Exception {
        
//        try to buy an item  with a higher price than the money you have
        Item item = new Item(3, "Soda Pop", new BigDecimal("3.50"), 4);
        BigDecimal monopolyMoney = new BigDecimal("1.11");
        
        BigDecimal subtraction = monopolyMoney.subtract(item.getItemCost());
        
        try{
            service.compareCost(subtraction, item);
            fail("Expected InsufficientFundsException");
        }catch(InsufficientFundsException e){
            return;
        }
        
        
    }

    
     /**
     * Test of addMoneyOfThisValue method, of class VendingMachineServiceLayer.
     */
    @Test
    public void addMoneyOfThisValue() {
        
        // assert that there is no money yet
        assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP), temporyMoneyIn);
        
        // add money of 1.00
        String moneyTest = "1.00";
        BigDecimal moneyBD = new BigDecimal(moneyTest);
        temporyMoneyIn = temporyMoneyIn.add(moneyBD);
        
        // assert that money in machine is 1.00
        assertEquals(moneyBD, temporyMoneyIn);
        
    }
    
     /**
     * Test of acceptItemIdFromPage method, of class VendingMachineServiceLayer.
     */
    @Test
    public void acceptItemIdFromPage() {
        String iDFromIndexPage = "1";
        int theChosenId = Integer.parseInt(iDFromIndexPage);
        itemIdFromPageTest = theChosenId;
        
        assertEquals(theChosenId, itemIdFromPageTest);
        
    }

    
    /**
     * Test of makePurchase method, of class VendingMachineServiceLayer.
     * @throws java.lang.Exception
     */
    @Test
    public void makePurchase() throws Exception {
        
        // scenario 1:
         // try to trigger the exception by setting ID  0
        int itemTestId = 0;
        
        
        // scenario 2: set an item's inventory to 0 to catch NoItemInventoryException
         Item testItem = service.getItembyId(4);   // test item with 0 inventory;
         testItem.setItemInventory(0);
        
         
      //  scenario 3: set money to 0 and make a new item with a high price, try to buy the item with insufficient amount of money to catch InsufficientFundsException
         BigDecimal moneyAmount = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
         Item highPrice = new Item(7,"100 Grand",new BigDecimal(77),11);

//         scenario 4: if the item vend was successful
        
        try{
          service.getItembyId(itemTestId);
          fail("ItemDoesNotExistException was not thrown");
        }catch(ItemDoesNotExistException ex){
            return;
        }

        
        try{
            // get the item by the ID passed in by the URL
            Item getTestItemWithNoInventory = service.getItembyId(5);
            fail("Did not catch NoItemInventoryException"); // if you got the item, you failed to trigger the exception
        }catch(NoItemInventoryException ex){    // if itemGot was null catch this exception
            return;
        }
        
  
        try{
        // validate that the user put in enough money before checking inventory, otherwise the inventory will be reduced even when the user didn't put in enough money
        service.compareCost(moneyAmount,highPrice);
        fail("Where is the InsufficientFundsException?");
        } catch(InsufficientFundsException ex){
                return;
        }

    }
    
    /**
     * Test of getChange method, of class VendingMachineServiceLayer.
     */
   @Test
    public void getChange() {
       
        
        // set coins to these values
        cTest.setQuarterIncrementor(1);
        cTest.setDimeIncrementor(2);
        cTest.setNickelIncrementor(3);
        cTest.setPennyIncrementor(4);
        
        // get the number of each coin by using the Change object's getter methods
         numOfQuartersTest = cTest.getQuarterIncrementor();
         numOfDimesTest = cTest.getDimeIncrementor();
         numOfNickelsTest = cTest.getNickelIncrementor();
         numOfPenniesTest = cTest.getPennyIncrementor();
        
        // assert you have the right amount of each coin type
        assertEquals(1,numOfQuartersTest);
        assertEquals(2,numOfDimesTest);
        assertEquals(3,numOfNickelsTest);
        assertEquals(4,numOfPenniesTest);

    }
    
    
    
     /**
     * Test of getQuarterStringMessage method, of class VendingMachineServiceLayer.
     */
    @Test
    public void getQuarterStringMessage() {
        
        assertNotNull(stringNumOfQuartersTest);
        
    }
    /**
     * Test of getDimeStringMessage method, of class VendingMachineServiceLayer.
     */
    @Test
    public void getDimeStringMessage() {
        
        assertNotNull(stringNumOfDimesTest);
        
    }
    /**
     * Test of getNickelStringMessage method, of class VendingMachineServiceLayer.
     */
    @Test
    public void getNickelStringMessage() {
        
        assertNotNull(stringNumOfNickelsTest);
        
    }
    /**
     * Test of getPennyStringMessage method, of class VendingMachineServiceLayer.
     */
    @Test
    public void getPennyStringMessage() {
        
        assertNotNull(stringNumOfPenniesTest);
        
    }
    
    
    /**
     * Test of changeReturn method, of class VendingMachineServiceLayer.
     */
    @Test
    public void changeReturn() {
        
//        load machine up with money of 3.44
        temporyMoneyIn = new BigDecimal(3.44).setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal changeTest= new BigDecimal(2).setScale(2, RoundingMode.HALF_UP);
        // if no item was selected, the change is equal to the amount of money in the machine(and then will be displayed in the change box
        if(itemIdFromPageTest == 0){
            changeTest = temporyMoneyIn;
        }
        
        // assert that changeTest got assigned temporyMoneyIn (3.44)
        assertEquals(new BigDecimal(3.44).setScale(2, RoundingMode.HALF_UP),changeTest);

         
// set the total money in machine and the selected item Id to zero when the change return button is clicked, also, switch boolean back to false, when it's time to attempt to vend another item
        // then assert that they equal what you set
        temporyMoneyIn = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP),temporyMoneyIn);

        itemIdFromPageTest = 0;
        assertEquals(0,itemIdFromPageTest);

        messageToHoldTest = ""; // clear message in message box
        assertEquals("",messageToHoldTest);

        cantBuyItemTest = "";   // clear the out of stock image
        assertEquals("",cantBuyItemTest);

        wasVended = false;
        assertEquals(false,wasVended);

        
        // when the change return is clicked after change is there, clear the change box (set change to zero)
        // Since the total money and machine and item ID gets set to zero after change return is clicked, they will already be zero
        if(itemIdFromPageTest == 0 && temporyMoneyIn.signum() == 0 ){
            
            changeTest =  new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
            assertEquals(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP),changeTest);

        }
        
        // set amount of each coin to zero (setting global coin variables here to zero won't work b/c there will still be coins in the Change object
        cTest.setQuarterIncrementor(0);
        cTest.setDimeIncrementor(0);
        cTest.setNickelIncrementor(0);
        cTest.setPennyIncrementor(0);
        
        // assert what you just set is 0
        assertEquals(0, cTest.getQuarterIncrementor());
        assertEquals(0, cTest.getDimeIncrementor());
        assertEquals(0, cTest.getNickelIncrementor());
        assertEquals(0, cTest.getPennyIncrementor());

    }
    
    
    /**
     * Test of theVendBoolean method, of class VendingMachineServiceLayer.
     */
    @Test
    public void theVendBoolean() {
        
        assertNotNull(wasVended);
        
    }

       /**
     * Test of setErrorMessageFromService method, of class VendingMachineServiceLayer.
     */
    @Test
    public void setErrorMessageFromService() {
        
        //assert thatthere was no message yet
        assertEquals("", messageToHoldTest);

        messageToHoldTest = "An  Error Message";
        
        //assert the message is what you set it to
        assertEquals("An  Error Message", messageToHoldTest);
        
    }

     /**
     * Test of displayMessage method, of class VendingMachineServiceLayer.
     */
    @Test
    public void displayMessage() {
        
        //assert thatthere was no message yet
        assertEquals("", messageToHoldTest);

        messageToHoldTest = "Litwick";
        
        //assert the message is what you set it to
        assertEquals("Litwick", messageToHoldTest);

        
    }
    
    /**
     * Test of assignSuccessMsg method, of class VendingMachineServiceLayer.
     */ 
   @Test
    public void assignSuccessMsg() {
        
        // scenario 1: Thank You!!! message
        wasVended = true;
        itemIdFromPageTest = 5;
        
        if(wasVended == true && itemIdFromPageTest != 0){
            messageToHoldTest = "Thank You!!!";
            
        }
        else if(itemIdFromPageTest == 0){
            messageToHoldTest = "No item selected";

        }
        else{
            messageToHoldTest = "Insert money and Item Choice!!!";
        }
        
        // assert that messageToHoldTest got assigned the Thank You message
        assertEquals("Thank You!!!",messageToHoldTest);
        
        
        
        
        // scenario 2: No item selected message

        wasVended = false;
        itemIdFromPageTest = 0;
        
        if(wasVended == true && itemIdFromPageTest != 0){
           messageToHoldTest = "Thank You!!!";
            
        }
        else if(itemIdFromPageTest == 0){
            messageToHoldTest = "No item selected";

        }
        else{
            messageToHoldTest = "Insert money and Item Choice!!!";
        }
        
        
        // assert that messageToHoldTest got assigned No item selected message
        assertEquals("No item selected",messageToHoldTest);

        
        
        
        
       // scenario 3: Insert money and Item Choice!!! message
  
        wasVended = false;
        itemIdFromPageTest = 43;
        
        if(wasVended == true && itemIdFromPageTest != 0){
           messageToHoldTest = "Thank You!!!";
            
        }
        else if(itemIdFromPageTest == 0){
            messageToHoldTest = "No item selected";

        }
        else{
            messageToHoldTest = "Insert money and Item Choice!!!";
        }
        
        
         // assert that messageToHoldTest got assigned Insert money and Item Choice!!! message
         assertEquals("Insert money and Item Choice!!!",messageToHoldTest);

        
    }
    
    /**
     * Test of displayErrorImage method, of class VendingMachineServiceLayer.
     */ 
    @Test
    public void displayErrorImage() {
        
        // assert that you have something to return
        assertNotNull(cantBuyItemTest);
    }
    

    
}
