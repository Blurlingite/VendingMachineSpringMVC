<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">        
    <link href="${pageContext.request.contextPath}/css/notHome.css" rel="stylesheet">        


  </head>
  
    <body>

<!--  <body background ="${pageContext.request.contextPath}/aaaa.jpg">-->
    <div class="container-fluid">
      <h3 id="sitePageTitle">
        Vending Machine
        <hr />
      </h3>
        
        
             
    	<div class="row">
            <div class="col-md-9">

          <!--  DYNAMIC GOES IN allItemsOnLoadUp -->
    		<div id="allItemsOnLoadUp" class="row">
                    <div>
                            
                    </div>
                    <c:forEach var="currentItem" items = "${everyItem}">
                        <div class="col-md-4">
                            
                            <!--make each button-->
                            <!--whenever you click this button, let the Controller know which item ID you want to use by putting it in the url as a parameter-->
                            <a href="${pageContext.request.contextPath}/useItemButton?itemId=${currentItem.itemId}"  class ="btn itemButtons" value="${currentItem.itemCost}">
                                                        <!--hidden input to hold the item's ID because I already gave it a value equal to it's cost. Might need to put this inside the button tag?-->
                                <input type="hidden" value="${currentItem.itemId}"/>
                                <div class = "number buttonTextColor">
                                    <h7 id="${currentItem.itemId}">${currentItem.itemId}</h7><br />
                                </div>
                                <div class = "item buttonTextColor">
                                    <h7 id = "item${currentItem.itemName}">${currentItem.itemName}</h7> <br />
                                </div>
                                <div class = "price buttonTextColor">
                                    $${currentItem.itemCost} <br /><br />
                                </div>
                                <div class = "quantity buttonTextColor">
                                    <h7 id="${currentItem.itemName}quantity">Quantity Left: ${currentItem.itemInventory}</h7>
                                </div>
                             </a>


    			</div>
                    </c:forEach>
                  

                </div>
            </div>



          <!-- Rightmost column -->

        <div class="col-md-3">
<!--"action" tells the form where to send data once it is submitted (in this case, to my useMoneyButton endpoint in Controller-->
            <form class ="form-horizontal" role="form" method="POST" action="useMoneyButton" id="totalInForm">

      		<div class="row">
                    <div class="col-md-12">
                        <h4 id="totalInText" class="textLabels">
                            Total$ In
                        </h4>
                    </div>
      		</div>
                
                <br /><br />    

                <div class="row">
                <div class="col-md-12">
                    <!--<a href="useMoneyButton?dollar=1.00"/>    This causes an error, thinking I'm trying to use GET-->
                    <input id="moneyBox" class="theBoxes" type="text" value="${totalMoneyInPlease}" readonly />
                    <input id="hiddenMoney" type="hidden" value="0.00" />
                </div>
                </div>


                <div class="row">
                    <div class="col-md-6">
                        <!--<a href="${pageContext.request.contextPath}/useDollarButton"  class ="btn">Add Dollar</a>-->
                          <a href="${pageContext.request.contextPath}/useAnyMoneyButton?moneyVal=1.00"  class ="btn sideButtons">Add Dollar</a>

                    </div>

                    <div class="col-md-6 quarNick">
                        <a href="${pageContext.request.contextPath}/useAnyMoneyButton?moneyVal=0.25"  class ="btn sideButtons">Add Quarter</a>

                    </div>
                </div>
                    
                <br />
            
                <div class="row">
                    <div class="col-md-6">
                        
                         <a href="${pageContext.request.contextPath}/useAnyMoneyButton?moneyVal=0.10" class ="btn sideButtons">Add Dime</a>

                    </div>

                    <div class="col-md-6 quarNick">
                        
                         <a href="${pageContext.request.contextPath}/useAnyMoneyButton?moneyVal=0.05" class ="btn sideButtons">Add Nickel</a>

                    </div>
                </div>

            </form>


            <hr class="horizontalRules"/>



        <div class = "container lastColMid" >
            <form id="messagesForm">

                <div class="row">
                    <div class="col-md-12">
                        <h3 id="messagesText" class="textLabels">Messages</h3>
                    </div>
      		</div>
                
                <div class="row">
                    <div class="col-md-12">
                        <input id="messageBox" class="theBoxes" type="text" value="${error}" readonly>
                        <br /><br />
                    </div>
                </div>



      		<div class="row">
                    <div class="col-md-6">
      			<h4 id ="itemText" class="textLabels">
      				Item:
      			</h4>
                    </div>
      		
                    <div class="col-md-6">
                        <input id="itemBox" class="theBoxes" type="text" value="${gimmieBackThatId}" readonly>
                        <input type="hidden" id="hiddenItemId">
                    </div>
      		</div>



                <div class="row">
                    <div class="col-md-12">
                        <div class="container columnButtons">
                            <!--WHAT IF ID IS 0?-->
                           <a href="${pageContext.request.contextPath}/makePurchase?theAmountOfMoney=${totalMoneyInPlease}&theItemNumber=${gimmieBackThatId}" id="makePurchaseText" class ="btn sideButtons">
                               Make Purchase
                           </a>
      			</div>
                    </div>
      		</div>
                               <br/>
        </div>


        <hr class="horizontalRules"/>


            <!-- Third Cell -->

            <div class="row">
      		<div class="col-md-12">
                    <h4 class="textLabels">Change</h4>
                </div>
            </div>

            <div class="row">
              <div class="col-md-12">
                <input type="text" id="changeBox" class="theBoxes" value="${quarters} ${dimes} ${nickels} ${pennies}" readonly />
              </div>
            </div>

            <div class="row">
              <div id = "changeReturnButton" class="col-md-12">
                <div class="container columnButtons">
                <a href="${pageContext.request.contextPath}/changeReturn" class="btn sideButtons">
                  Change Return
                </a>
                </div>
              </div>
            </div>

      	</div>
        </div>
        
                            
          <!--when you can't buy the item-->
            <p> <img src ="${pageContext.request.contextPath}/${errorImage}"  align="right"/></p>
                                      
        
    </div>


    
  </body>
</html>
