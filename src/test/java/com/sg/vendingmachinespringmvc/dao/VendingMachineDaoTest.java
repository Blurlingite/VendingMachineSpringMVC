/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.dao;

import com.sg.vendingmachinespringmvc.model.Item;
import java.math.BigDecimal;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author vishnukdawah
 */
public class VendingMachineDaoTest  {
    
     private VendingMachineDao dao;
       
    public VendingMachineDaoTest() {
       this.dao = new VendingMachineDaoStubImpl(); 
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    // runs before each test method in this class
    @Before
    public void setUp() throws Exception {  // put "throws Exception" to temporarily stop errors caused by exceptions while testing
  
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Adds an item and gets that item
     * @throws java.lang.Exception
     */
    @Test
    public void testGetItem() throws Exception {
        // make a new item
        Item item = new Item(4, "Lava Cookie", new BigDecimal("1.00"), 4);

        
        // use the Dao's getItem() method to get the item you just added
        Item fromDao = dao.getItem(item.getItemId());
        
         //assert that the expected (item) is what you got back from the daoStub (fromDaoStub)
         //assertEquals(expected, actual result)
        assertEquals(item, fromDao);
        
        
    }

    /**
     * Test of getAllItems method, of class VendingMachineDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetAllItems() throws Exception {
        
        List<Item> itemListed = dao.getAllItems();
        
        // assert that the list contains 2 items
        assertEquals(2, itemListed.size());
        

    }
    
    @Test
    public void removeItem() {
        
        List<Item> itemListed = dao.getAllItems();
        
        assertEquals(2, itemListed.size());
        
        Item itemGot = dao.getItem(4);

        Item removedItem = dao.removeItem(itemGot);
        
        assertEquals(3,removedItem.getItemInventory());
        
    }


   
}
