<%@page import="java.net.URLEncoder"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@page import="net.tanesha.recaptcha.ReCaptchaImpl"%>
<jsp:include page="checkCountry.jsp" />
<html> <!--<![endif]-->
    <jsp:include page="header.html" />
    <body>
        <script>
            function validatePassword() {
                var password = document.getElementById("password").value;
                var repassword = document.getElementById("repassword").value;
                var ok = true;
                if ((password != null && repassword != null) || (password != "" && repassword != "")) {
                    if (password != repassword) {
                        //alert("Passwords Do not match");
                        document.getElementById("password").style.borderColor = "#E34234";
                        document.getElementById("repassword").style.borderColor = "#E34234";
                        alert("Passwords do not match. Please key again.");
                        ok = false;
                    } else if (password == repassword) {
                        if (password.length < 8) {
                            alert("Passwords too short. At least 8 characters.");
                            ok = false;
                        }
                    }
                } else {
                    return ok;
                }
                return ok;
            }
        </script>
        <jsp:include page="menu2.jsp" />
        <div role="main" class="main">
            <section class="page-top">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <h2>Login / Register</h2>
                        </div>
                    </div>
                </div>
            </section>
            <div class="container">
                <jsp:include page="/displayMessageLong.jsp" />
                <div class="row">
                    <div class="col-md-12">
                        <div class="row featured-boxes login">

                            <div class="col-md-12">
                                <div class="featured-box featured-box-secundary default info-content">
                                    <div class="box-content">
                                        <h4>Virtual Store</h4>
                                        <img src="../../img/islandFurnitureStoreMap.jpg" alt="Store Map" usemap="#storeMap" width="1050" height="768" id="storeMap">

                                        <map name="storeMap">
                                            <area  alt="" title="tablesdesks" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Tables & Desks")%>" shape="rect" coords="284,197,488,247" style="outline:none;" target="_self" onclick="../../ECommerce_TablesDesksServlet" onmouseover="if (document.images)
                                                                document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapTablesDesks.jpg';" onmouseout="if (document.images)
                                                                            document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="retailproducts" href="../../ECommerce_AllRetailProductsServlet" shape="rect" coords="159,20,557,73" style="outline:none;" target="_self" onclick="../../ECommerce_RetailProductsServlet" onmouseover="if (document.images)
                                                                document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapRetailProducts.jpg';" onmouseout="if (document.images)
                                                                            document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="bathroom" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Bathroom")%>" shape="rect" coords="121,167,183,289" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapBathroom.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="bedsmattresses" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Beds & Mattresses")%>" shape="rect" coords="123,329,182,546" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapBedsMattresses.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="lightings" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Lightings")%>" shape="rect" coords="250,447,393,491" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapLightings.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="sofaschair" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Sofas & Chair")%>" shape="rect" coords="258,313,419,358" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapSofasChairs.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';" />
                                            <area  alt="" title="cabinetsStorage" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Cabinets & Storage")%>" shape="rect" coords="508,368,622,546" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapCabinetsStorage.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"    />
                                            <area  alt="" title="children" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Children")%>" shape="rect" coords="636,75,891,229" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapChildren.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="study" href="../../ECommerce_FurnitureCategoryServlet?cat=<%=URLEncoder.encode("Study")%>" shape="rect" coords="585,77,628,220" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapStudy.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="restaurant" href="#" shape="rect" coords="709,268,925,531" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapRestaurant.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area  alt="" title="pointOfSales" href="#" shape="rect" coords="351,557,986,751" style="outline:none;" target="_self"  onmouseover="if (document.images)
                                                        document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMapPointOfSales.jpg';" onmouseout="if (document.images)
                                                                    document.getElementById('storeMap').src = '../../img/islandFurnitureStoreMap.jpg';"  />
                                            <area shape="rect" coords="1048,766,1050,768" alt="Image Map" style="outline:none;" title="Image Map" href="http://www.image-maps.com/index.php?aff=mapped_users_0" />
                                        </map>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.html" />

    </body>
</html>
