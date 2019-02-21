/*
NOTE: the only reason the file can be loaded is b/c I put the inventory.txt file in my tomcat folder's "bin" folder, otherwise,
it can't find the inventory.txt file even if it is in the project
This is b/c Tomcat is running the server, any manually created file you want to find must be in it's folder unless:
Try getting the file path and use that to find the inventory.txt file within this project
 */
package com.sg.vendingmachinespringmvc.controller;

import com.sg.vendingmachinespringmvc.model.Item;
import com.sg.vendingmachinespringmvc.service.InsufficientFundsException;
import com.sg.vendingmachinespringmvc.service.ItemDoesNotExistException;
import com.sg.vendingmachinespringmvc.service.NoItemInventoryException;
import com.sg.vendingmachinespringmvc.service.VendingMachineServiceLayer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author vishnukdawah
 */
@Controller
public class VendingMachineController {
    
    private VendingMachineServiceLayer service;
    

    // b/c of the bean declaration of the Dao in spring-persistence.xml, we can now inject the D
    @Inject
    public VendingMachineController(VendingMachineServiceLayer service) {
        this.service = service;
    }
    
    // will send data back to "/homePage" endpoint
    // we are getting all the items to be displayed in the form using a c:forEach tag
    
    // might need a request parameter so you can intake data from another method here and then pass it to the index page
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(HttpServletRequest fromOtherMethod, Model model){
        
        // get all the items from mock in DAO
        List<Item> everyItem = service.getAllItems();
        
        // add the list of all the items to the model so you can pass it to index.jsp and display it on the web page
        model.addAttribute("everyItem",everyItem);

        // get the value of the totalMoneyIn variable in the service layer and add it to the model
        String getStringTotalMoneyInFromServiceLayer = service.getMyMoneyString();
        model.addAttribute("totalMoneyInPlease",getStringTotalMoneyInFromServiceLayer);
        
        // take the itemId the service layer got and add it to the model
        String gimmieBackThatId = service.retrieveStringItemId();
        model.addAttribute("gimmieBackThatId",gimmieBackThatId);
        
        // get the change and assign number of each coin type in service layer
        BigDecimal change = service.getChange();
     
        // if the item was vended, clear the Total$ In box(by setting it to an empty string in the makePurchase method of service layer)
        // and then add it to the model
        if(service.theVendBoolean()){
            model.addAttribute("totalMoneyInPlease", getStringTotalMoneyInFromServiceLayer);
        }
        
        model.addAttribute("change", change);
        
        // get amount of each coin in change
        String numOfQuarters = service.getQuarterStringMessage();
        String numOfDimes = service.getDimeStringMessage();
        String numOfNickels = service.getNickelStringMessage();
        String numOfPennies = service.getPennyStringMessage();



        // add amount of coins to model so you can display them in index.jsp
        model.addAttribute("quarters", numOfQuarters);
        model.addAttribute("dimes", numOfDimes);
        model.addAttribute("nickels", numOfNickels);
        model.addAttribute("pennies", numOfPennies);

        // get whatever message depending on if item vend was successful or not
        String errorMsg = service.displayMessage();
        model.addAttribute("error", errorMsg);
        
        // allows image to be shown when item is sold out
        String errorImage = service.displayErrorImage();
        model.addAttribute("errorImage", errorImage);

        // deliver the results to index.jsp
        return "index";
        
    }
    
    @RequestMapping(value = "/useItemButton", method = RequestMethod.GET)
    public String useItemButton(HttpServletRequest request){
        
        // grab item ID from URL in jsp given by "itemId" in jsp
        String iDFromIndexPage = request.getParameter("itemId");
        
        // service takes in the item ID
        service.acceptItemIdFromPage(iDFromIndexPage);

        return "redirect:/";
    }
    
    
    
        // need a request parameter b/c we are pressing a button on the index.jsp page. We want to assign data to the request, then pass it to another Contoller method via redirect
    // b/c the user clicks on a link (that has a button inside it, so when they press the button) the RequestMethod is GET
    @RequestMapping(value = "/useAnyMoneyButton", method = RequestMethod.GET)
    public String useAnyMoneyButton(HttpServletRequest request){
        
        // take in the parameter you passed into the url from index.jsp (as part of the money button)
        String moneyValue = request.getParameter("moneyVal");
        
        // add that value from the parameter to the totalMoneyIn variable located in the service layer (which you will get later in
        // the index method (the one that returns "index")
        service.addMoneyOfThisValue(moneyValue);
        
        
        return "redirect:/";

        
    }
    
    // take the money inputted and item chosen to make a purchase possibly
    @RequestMapping(value = "/makePurchase", method = RequestMethod.GET)
    public String makePurchase(HttpServletRequest request, Model model){

        // get data from URL in jsp (total$ in and item ID)
        String stringMoneyAmount = request.getParameter("theAmountOfMoney");
        String stringItemNum = request.getParameter("theItemNumber");
        
        // if they click make purchase without selecting anything set to a zero string to avoid web page errors from parsing empty strings
        if(stringMoneyAmount.equalsIgnoreCase("") || stringItemNum.equalsIgnoreCase("")){
            stringMoneyAmount = "0";
            stringItemNum = "0";
        }
        
        // convert strings to data types needed in service layer method makePurchase()
        BigDecimal moneyAmount = new BigDecimal(stringMoneyAmount).setScale(2, RoundingMode.HALF_UP);
        int itemNum = Integer.parseInt(stringItemNum);
        
        
        // if the inventory is zero send message to the handleExceptions endpoint
        try {
            service.makePurchase(moneyAmount, itemNum); // if you can vend the item...
            service.assignMsg();    // add the Thank You!!! message to the message variable in service layer
            return "redirect:/";
        } catch (InsufficientFundsException | NoItemInventoryException | ItemDoesNotExistException ex) {    // if an exception is encountered...
            
            model.addAttribute("errorMessage", ex.getMessage());    // add it's message to the model
            
            // go to this endpoint to set error message
            return "redirect:/handleExceptions";
        }
        
        
    }
    
    // set all variables in service layer to 0
    @RequestMapping(value = "/changeReturn", method = RequestMethod.GET)
    public String changeReturn(HttpServletRequest request){
        
        service.changeReturn();
        
        return "redirect:/";
        
    }
    
    
    // makePurchase leads you here when it encounters an exception
    @RequestMapping(value = "/handleExceptions", method = RequestMethod.GET)
    public String handleExceptions(HttpServletRequest request){

        // get the message from makePurchase() endpoint and use it to set message in service layer
        String e = request.getParameter("errorMessage");
        service.setErrorMessageFromService(e);
        
        return "redirect:/";

        
    }
    
    
    
  
}
