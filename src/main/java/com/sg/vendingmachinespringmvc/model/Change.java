/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachinespringmvc.model;

import static com.sg.vendingmachinespringmvc.model.Change.CoinEnums.DIME;
import static com.sg.vendingmachinespringmvc.model.Change.CoinEnums.NICKEL;
import static com.sg.vendingmachinespringmvc.model.Change.CoinEnums.PENNY;
import static com.sg.vendingmachinespringmvc.model.Change.CoinEnums.QUARTER;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author vishnukdawah
 */
public class Change {


   
    public enum CoinEnums { // declare coins in the form of enums
        
        QUARTER, DIME, NICKEL, PENNY
    
    }
    
    private BigDecimal quarter; // need this to store the value of a quarter (to be used with enums)
    private BigDecimal dime;    // need this to store the value of a dime (to be used with enums)
    private BigDecimal nickel;  // need this to store the value of a nickel (to be used with enums)
    private BigDecimal penny;   // need this to store the value of a penny (to be used with enums)
    
    private int quarterIncrementor; // goes up by 1 when a quarter is dispensed
    private int dimeIncrementor; // goes up by 1 when a dime is dispensed
    private int nickelIncrementor; // goes up by 1 when a nickel is dispensed
    private int pennyIncrementor; // goes up by 1 when a penny is dispensed

    public void setQuarterIncrementor(int i) {
        quarterIncrementor = i;
    }
    
    public void setDimeIncrementor(int i) {
        dimeIncrementor = i;
    }
    
    public void setNickelIncrementor(int i) {
        nickelIncrementor = i;
    }
        
     public void setPennyIncrementor(int i) {
        pennyIncrementor = i;
    }
        
        
    public int getQuarterIncrementor() {
        return quarterIncrementor;
    }

    public int getDimeIncrementor() {
        return dimeIncrementor;
    }

    public int getNickelIncrementor() {
        return nickelIncrementor;
    }

    public int getPennyIncrementor() {
        return pennyIncrementor;
    }

   
    public Change() {   // set the values for the enum coins. 

        // use enums to give all the coins BigDecimal values when the Change constructor is called
        CoinEnums coinQuarter = QUARTER;
        CoinEnums coinDime = DIME;
        CoinEnums coinNickel = NICKEL;
        CoinEnums coinPenny = PENNY;
        
        if(coinQuarter == QUARTER){
                BigDecimal q = new BigDecimal("0.25");
                BigDecimal qScale = q.setScale(2, RoundingMode.HALF_UP);
                this.quarter = qScale;
        }if(coinDime == DIME){
                BigDecimal d = new BigDecimal("0.10");
                BigDecimal dScale = d.setScale(2, RoundingMode.HALF_UP);
                this.dime = dScale;
        }if(coinNickel == NICKEL){
                BigDecimal n = new BigDecimal("0.05");
                BigDecimal nScale = n.setScale(2, RoundingMode.HALF_UP);
                this.nickel = nScale;
        }if(coinPenny == PENNY){
                BigDecimal p = new BigDecimal("0.01");  
                BigDecimal pScale = p.setScale(2, RoundingMode.HALF_UP);
                this.penny = pScale;
        }
        
        

    }
        
    public BigDecimal getQuarter() {    // be able to get the value of 0.25 for use in this class' calculate method
        return quarter;
    }

    public BigDecimal getDime() {    // be able to get the value of 0.10 for use in this class' calculate method   
        return dime;
    }

    public BigDecimal getNickel() {  // be able to get the value of 0.05 for use in this class' calculate method 
        return nickel;
    }

    public BigDecimal getPenny() {   // be able to get the value of 0.01 for use in this class' calculate method  
        return penny;
    }


//     to be called after using the constructor to calculate the change
//     the "BigDecimal coin" parameter will be a getter method call depending on which enum was selected. It will contain: 0.25, 0.10, 0.05 or 0.01
//     use a combination of subtraction and divison
    public BigDecimal calculateChange(BigDecimal cost, BigDecimal userMoney){
        
       MathContext mc = new MathContext(3);    // you want the answer display with 3 digits
       
       // to compare BigDecimal to 0
       BigDecimal zero = new BigDecimal("0.00");
       BigDecimal zeroScale = zero.setScale(2, RoundingMode.HALF_UP);
        
       BigDecimal bdSubtract = userMoney.subtract(cost, mc);  // subtract the money in the machine from the cost of an item  Ex. In Machine: 1.00  Cost: 0.25   Answer: 0.75
      
       
       // just the total change to be returned from this method 
       BigDecimal changeToReturn = userMoney.subtract(cost, mc);
       
       int compare = bdSubtract.compareTo(zeroScale); 
    // This method (compareTo) returns -1 if the BigDecimal is less than zeroScale, 1 if the BigDecimal is greater than zeroScale and 0 if the BigDecimal is equal to zeroScale
       
  
       while(compare > 0){  // while the amount of change is greater than 0

           //DISPENSE QUARTERS
            int compareToQuarter = bdSubtract.compareTo(this.quarter); 
            // compare the change with the value of a quarter (0.25)
            while(compareToQuarter >= 0){    
            // while the amount of change is greater than 0.25  BUT WHAT IF THE AMOUNT IN THE MACHINE WAS 0.00, WHAT IF THEY DIDN't PUT IN ANY MONEY
                bdSubtract = bdSubtract.subtract(this.quarter, mc);    
            // subtract 0.25 (1 quarter)
                compareToQuarter = bdSubtract.compareTo(this.quarter); 
            // need to update the compareToQuarter variable, otherwise it will stay at 1 and cause the loop to repeat infintely

                this.quarterIncrementor++;               
    
            } // end of Quarter while loop
           
           //DISPENSE DIMES
            int compareToDime  = bdSubtract.compareTo(this.dime);
            while(compareToDime >=0){     
                bdSubtract = bdSubtract.subtract(this.dime, mc);
                compareToDime = bdSubtract.compareTo(this.dime);
                this.dimeIncrementor++;               
            } // end of Dime while loop
            
             
            //DISPENSE NICKELS
            int compareToNickel  = bdSubtract.compareTo(this.nickel);
            while(compareToNickel >=0){     
                bdSubtract = bdSubtract.subtract(this.nickel, mc);
                compareToNickel = bdSubtract.compareTo(this.nickel);
                this.nickelIncrementor++;               
            } // end of Nickel while loop
           
           
            //DISPENSE PENNIES
            int compareToPenny  = bdSubtract.compareTo(this.penny);
            while(compareToPenny >=0){     
                bdSubtract = bdSubtract.subtract(this.penny, mc);
                compareToPenny = bdSubtract.compareTo(this.penny);
                this.pennyIncrementor++;
            } // end of Penny while loop
            
           break;  // while compare > 0, put this break here so the rest of the menu can be printed out
       }
        return changeToReturn;
    }
    
    // will empty the variables that contain the amount of coins so you don't have extra change
    public void emptyIncrementors(){
        
        this.quarterIncrementor = 0;
        this.dimeIncrementor = 0;
        this.nickelIncrementor = 0;
        this.pennyIncrementor = 0;
        
    }

}
