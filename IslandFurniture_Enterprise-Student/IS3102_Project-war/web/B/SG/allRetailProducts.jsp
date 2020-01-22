<%@page import="HelperClasses.RetailProduct"%>
<%@page import="EntityManager.PromotionEntity"%>
<%@page import="EntityManager.Item_CountryEntity"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="checkCountry.jsp" />
<html> <!--<![endif]-->
    <jsp:include page="header.html" />
    <body>
        <%
            
            //get all retail products and store them into an array
            List<RetailProduct> retailProducts = (List<RetailProduct>) (session.getAttribute("retailProducts"));

            Boolean isMemberLoggedIn = false;
            String memberEmail = (String) (session.getAttribute("memberEmail"));
            if (memberEmail == null) {
                isMemberLoggedIn = false;
            } else {
                isMemberLoggedIn = true;
            }


        %>
        <div class="body">
            <jsp:include page="menu2.jsp" />
            <div class="body">
                <div role="main" class="main">
                    <section class="page-top">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12">
                                    <h2>Retail Products</h2>
                                </div>
                            </div>
                        </div>
                    </section>
                    <div class="container">
                        <div class="row">
                            <div class="col-md-6">
                                <h2 class="shorter"><strong>All Retail Products</strong></h2>
                            </div>
                        </div>
                        <div class="row">
                            <ul class="products product-thumb-info-list" data-plugin-masonry>
                                <%                                    
                                    try {
                                        for (int i = 0; i < retailProducts.size(); i++) {
                                        
                                %>
                                <li class="col-md-3 col-sm-6 col-xs-12 product">
                                    <span class="product-thumb-info">
                                        <span class="product-thumb-info-image">
                                            <img alt="" class="img-responsive" src="../../..<%=retailProducts.get(i).getImageUrl()%>">
                                        </span>
                                        <span class="product-thumb-info-content">
                                            <h4><%=retailProducts.get(i).getName()%></h4>
                                            <%
                                                String normalPrice = "$" + retailProducts.get(i).getPrice() + "0";
                                            %>
                                            <span class="product-thumb-info-act-left"><em>Price: <%=normalPrice%></em></span>
                                            <br/>
                                            <form action="retailProductDetails.jsp">
                                                <input type="hidden" name="sku" value="<%=retailProducts.get(i).getSKU()%>"/>
                                                <input type="submit" class="btn btn-primary btn-block" value="More Details"/>
                                            </form>
                                            <%
                                                //only when the use log in, can add to cart
                                                if (isMemberLoggedIn == true) {
                                            %>
                                            <form action="../../ECommerce_AddRetailToListServlet">
                                                <input type="hidden" name="id" value="<%=retailProducts.get(i).getId()%>"/>
                                                <input type="hidden" name="SKU" value="<%=retailProducts.get(i).getSKU()%>"/>
                                                <input type="hidden" name="price" value="<%=retailProducts.get(i).getPrice()%>"/>
                                                <input type="hidden" name="name" value="<%=retailProducts.get(i).getName()%>"/>
                                                <input type="hidden" name="imageURL" value="<%=retailProducts.get(i).getImageUrl()%>"/>
                                                <input type="submit" name="btnEdit" class="btn btn-primary btn-block" value="Add To Cart"/>
                                            </form>
                                            <%
                                                }
                                            %>
                                        </span>
                                    </span>
                                </li>
                                <%}
                                    } catch (Exception ex) {
                                        System.out.println(ex);
                                    }
                                %>

                            </ul>
                        </div>
                        <hr class="tall">
                    </div>
                </div>
            </div>
            <jsp:include page="footer.html" />
        </div>
    </body>
</html>
