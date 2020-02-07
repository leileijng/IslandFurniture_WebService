<%@page import="java.util.ArrayList"%>
<%@page import="HelperClasses.ShoppingCartLineItem"%>
<%@page import="EntityManager.WishListEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.Item_CountryEntity"%>
<%@page import="EntityManager.FurnitureEntity"%>
<%@page import="EntityManager.RetailProductEntity"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="checkCountry.jsp" />


<html> <!--<![endif]-->
    <jsp:include page="header.html" />
    <body>
        <%
            double finalPrice = 0.0;
        %>
        <script>
            var totalPrice = 0;

            for (var i = 0, n = shoppingCart.getItems().size; i < n; i++) {
                totalPrice += shoppingCart.getItems().get(i).get();
            }

            function removeItem() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.shoppingCart.action = "/IS3102_Project-war/B/SG/shoppingCart.jsp?errMsg=No item(s) selected for deletion.";
                    document.shoppingCart.submit();
                } else {
                    window.event.returnValue = true;
                    document.shoppingCart.action = "../../ECommerce_RemoveItemFromListServlet";
                    document.shoppingCart.submit();
                }
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function minus(SKU) {
                window.event.returnValue = true;
                document.shoppingCart.action = "../../ECommerce_MinusFurnitureToListServlet?SKU=" + SKU;
                document.shoppingCart.submit();
            }
            function plus(SKU, name, price, imageURL) {
                window.event.returnValue = true;
                document.shoppingCart.action = "../../ECommerce_AddFurnitureToListServlet?SKU=" + SKU + "&price=" + price + "&name=" + name + "&imageURL=" + imageURL;
                document.shoppingCart.submit();
            }
            function finalTotalPrice() {
                checkboxes = document.getElementsById('totalPrice');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function checkOut() {
                $(".plus").prop("disabled", true);
                $(".minus").prop("disabled", true);
                $("#btnCheckout").prop("disabled", true);
                $("#btnRemove").prop("disabled", true);
                $(".productDetails").removeAttr("href");
                $("html, body").animate({scrollTop: $(document).height() / 3}, "slow");
                $("#makePaymentForm").show("slow", function () {
                });
            }
            
            function makePayment() {
                window.event.returnValue = true;
                cardName = $('#txtName').val();
                cardNo = $('#txtCardNo').val();
                securityCode = $('#txtSecuritycode').val();
                year = $('#year').val();
                var numbers = /^[0-9]+$/;
                if (cardName.trim() == "") {
                    alert("Please fill in the Name on the card!");
                } else if (cardNo.trim() == "") {
                    alert("Please fill in the card number!");
                } else if (securityCode.trim() == "") {
                    alert("Please fill in the securityCode!");
                } else if (year.trim() == "") {
                    alert("Please fill in the expire year of the card!");
                } else if (!securityCode.match(numbers) || securityCode.length != 3){
                    alert("Please provide valid security code!");
                } else if (!year.match(numbers) || year.length != 4 || year < 2020){
                    alert("Please provide valid expiry year!");
                } else if (!cardNo.match(numbers)){
                    alert("Card number cannot contain letters!");
                } 
                else if (cardNo.match(numbers)){
                    console.log("here?");
                    var creditcardInt = [];
                    for (var i = 0; i < cardNo.length; i++) {
                        creditcardInt[i] = parseInt(cardNo.substring(i, i + 1), 10);
                    }
                    for (var i = creditcardInt.length - 2; i >= 0; i = i - 2) {
                        var j = creditcardInt[i];
                        j = j * 2;
                        if (j > 9) {
                            j = j % 10 + 1;
                        }
                        creditcardInt[i] = j;
                    }
                    var sum = 0;
                    for (var i = 0; i < creditcardInt.length; i++) {
                        sum += creditcardInt[i];
                    }
                    if (sum % 10 != 0) {
                        //the card number is valid
                        alert("The card number is not valid!");
                    }
                    else{
                        document.makePaymentForm.action = "../../ECommerce_PaymentServlet";
                        document.makePaymentForm.submit();
                    }
                }
            }
            
            
        </script>

        <div class="body">
            <jsp:include page="menu2.jsp" />
            <div role="main" class="main shop">
                <section class="page-top">
                    <div class="container">
                        <div class="row">
                            <div class="col-md-12">
                                <h2>Shopping Cart</h2>
                            </div>
                        </div>
                    </div>
                </section> 

                <div class="container" id="printableArea">
                    <hr class="tall">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="row featured-boxes">
                                <div class="col-md-12">
                                    <div class="featured-box featured-box-secundary featured-box-cart">
                                        <div class="box-content">
                                            <form method="post" action="" name="shoppingCart">
                                                <jsp:include page="/displayMessageLong.jsp" />
                                                <table cellspacing="0" class="shop_table cart">
                                                    <thead>
                                                        <tr>                                                                
                                                            <th class="product-remove">
                                                                <input type="checkbox" onclick="checkAll(this)" />
                                                            </th>                                                                
                                                            <th class="product-thumbnail">
                                                                Image
                                                            </th>
                                                            <th class="product-name" >
                                                                Product
                                                            </th>

                                                            <th class="product-price" style="width: 15%">
                                                                Price
                                                            </th>
                                                            <th class="product-quantity">
                                                                Quantity
                                                            </th>
                                                            <th class="product-subtotal" style="width: 15%">
                                                                Subtotal
                                                            </th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            ArrayList<ShoppingCartLineItem> myCart = (ArrayList<ShoppingCartLineItem>) (session.getAttribute("myCart"));
                                                            try {

                                                                if (myCart != null && myCart.size() > 0) {
                                                                    for (ShoppingCartLineItem item : myCart) {
                                                                        finalPrice += item.getPrice() * item.getQuantity();
                                                        %>
                                                        <tr class="cart_table_item">
                                                            <td class="product-remove">
                                                                <input type="checkbox" name="delete" value="<%=item.getSKU()%>" />
                                                            </td>
                                                            <td class="product-thumbnail">
                                                                <a href="furnitureProductDetails.jsp">
                                                                    <img width="100" height="100" alt="" class="img-responsive" src="../../..<%=item.getImageURL()%>">
                                                                </a>
                                                            </td>
                                                            <td class="product-name">
                                                                <a class="productDetails" href="furnitureProductDetails.jsp"><%=item.getName()%></a>
                                                            </td>
                                                            <td class="product-price">
                                                                $<span class="amount" id="price<%=item.getSKU()%>">
                                                                    <%=item.getPrice()%>
                                                                </span>
                                                            </td>
                                                            <td class="product-quantity">
                                                                <form enctype="multipart/form-data" method="post" class="cart">
                                                                    <div class="quantity">
                                                                        <input type="button" class="minus" value="-" onclick="minus('<%=item.getSKU()%>')">
                                                                        <input type="text" disabled="true" class="input-text qty text" title="Qty" value="<%=item.getQuantity()%>" name="quantity" min="1" step="1" id="<%=item.getSKU()%>">
                                                                        <input type="button" class="plus" value="+" onclick="plus('<%=item.getSKU()%>', '<%=item.getName()%>',<%=item.getPrice()%>, '<%=item.getImageURL()%>')">
                                                                    </div>
                                                                </form>
                                                            </td>
                                                            <td class="product-subtotal">
                                                                $<span class="amount" id="totalPrice<%=item.getSKU()%>">
                                                                    <%=item.getQuantity() * item.getPrice()%>

                                                                </span>
                                                            </td>
                                                        </tr>
                                                        <%       }                                                          //   }
                                                                }
                                                            } catch (Exception ex) {
                                                                System.out.println(ex);
                                                            }
                                                        %>
                                                        <tr>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td></td>
                                                            <td class="product-subtotal" style="font-weight: bold">

                                                            </td>
                                                            <td class="product-subtotal">
                                                                $<span class="amount" id="finalPrice" name="finalPrice">
                                                                    Total: <%=finalPrice%>
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <%if (myCart != null && myCart.size() > 0) {%>
                                                <div align="left"><a href="#myModal" data-toggle="modal"><button id="btnRemove" class="btn btn-primary">Remove Item(s)</button></a></div>
                                                <div align="right"><a href="#checkoutModal" data-toggle="modal"><button id="btnCheckout" class="btn btn-primary btn-lg">Check Out</button></a></div>
                                                <%} else {%>
                                                <div align="right"><a href="#checkoutModal" data-toggle="modal"><button disabled="true" id="btnCheckout" class="btn btn-primary btn-lg">Check Out</button></a></div>
                                                <%}%>
                                            </form>


                                            <form id="makePaymentForm" name="makePaymentForm" method="post" hidden>
                                                <div class="col-md-8">
                                                    <br>
                                                    <table>
                                                        <tbody>
                                                            <tr>
                                                        <h4 style="text-align: left">Credit Card Payment Details</h4>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 5px">
                                                                <label>Name on Card: </label>
                                                            </td>
                                                            <td style="padding: 5px">
                                                                <input type="text" class="input-text text" title="name"id="txtName" required>                                                            
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 5px">
                                                                <label>Card Number: </label>
                                                            </td>
                                                            <td style="padding: 5px">
                                                                <input type="text" class="input-text text " title="cardno" name="card" id="txtCardNo" required>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 5px">
                                                                <label>CVV/CVC2: </label>
                                                            </td>
                                                            <td style="padding: 5px">
                                                                <input type="text" class="input-text text " title="securitycode" id="txtSecuritycode" required>
                                                            </td>
                                                        </tr>

                                                        <tr>
                                                            <td style="padding: 5px;">
                                                                <label>Expiry Date: </label>
                                                            </td>
                                                            <td style="width: 300px">
                                                                <select style="width: 120px; display: inline-block" class="dropdown-header" title="Month">
                                                                    <option>January</option>
                                                                    <option>February</option>
                                                                    <option>March</option>
                                                                    <option>April</option>
                                                                    <option>May</option>
                                                                    <option>June</option>
                                                                    <option>July</option>
                                                                    <option>August</option>
                                                                    <option>September</option>
                                                                    <option>October</option>
                                                                    <option>November</option>
                                                                    <option>December</option>
                                                                </select>
                                                                <input type="text" style="width: 60px" class="input-text text" title="year" id="year" required>(eg: 2015)                                                        
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="">
                                                            </td>
                                                            <td style="padding-top: 20px">
                                                                <div align="right"><a href="#makePaymentModal" data-toggle="modal"><button class="btn btn-primary">Make Payment</button></a></div>
                                                            </td>
                                                        </tr>
                                                        </tbody></table>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div role="dialog" class="modal fade" id="myModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>Remove item(s)</h4>
                        </div>
                        <div class="modal-body">
                            <p id="messageBox">The selected item(s) will be removed from your shopping cart. Are you sure you want to continue?</p>
                        </div>
                        <div class="modal-footer">                        
                            <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeItem()"  />
                            <a class="btn btn-default" data-dismiss ="modal">Close</a>
                        </div>
                    </div>
                </div>
            </div>
            <div role="dialog" class="modal fade" id="checkoutModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>Checking out...</h4>
                        </div>
                        <div class="modal-body">
                            <p id="messageBox">Please check the cart items before checkout. Are you sure you want to continue?</p>
                        </div>
                        <div class="modal-footer">                        
                            <input class="btn btn-primary" data-dismiss="modal" name="btnCheckout" type="button" value="Confirm" onclick="checkOut()">
                            <a class="btn btn-default" data-dismiss="modal">Close</a>
                        </div>
                    </div>
                </div>
            </div>  

            <div role="dialog" class="modal fade" id="makePaymentModal">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4>Confirm Payment</h4>
                        </div>
                        <div class="modal-body">
                            <p id="messageBox">You are making payment now. Are you sure you want to continue?</p>
                        </div>
                        <div class="modal-footer">                        
                            <input class="btn btn-primary" name="btnPayment" type="submit" value="Confirm" onclick="makePayment()"  />
                            <a class="btn btn-default" data-dismiss ="modal">Close</a>
                        </div>
                    </div>
                </div>
            </div>  

            <jsp:include page="footer.html" />

            <!-- Theme Initializer -->
            <script src="../../js/theme.plugins.js"></script>
            <script src="../../js/theme.js"></script>

            <!-- Current Page JS -->
            <script src="../../vendor/rs-plugin/js/jquery.themepunch.tools.min.js"></script>
            <script src="../../vendor/rs-plugin/js/jquery.themepunch.revolution.js"></script>
            <script src="../../vendor/circle-flip-slideshow/js/jquery.flipshow.js"></script>
            <script src="../../js/views/view.home.js"></script>   
        </div>
    </body>
</html>
