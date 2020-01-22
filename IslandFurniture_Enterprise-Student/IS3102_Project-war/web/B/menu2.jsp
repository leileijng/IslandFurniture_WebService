<%@page import="java.net.URLEncoder"%>
<%@page import="EntityManager.MemberEntity"%>
<header id="header">
    <div class="container">
        <h1 class="logo">
            <a href="index.jsp">
                <img alt="Island Furniture" width="180" height="80" data-sticky-width="82" data-sticky-height="40" src="../img/logo.png">
            </a>
        </h1>
        <%
            String memberName = (String) (session.getAttribute("memberName"));
            if (memberName == null) {
        %>
        <nav>
            <ul class="nav nav-pills nav-top">
                <li>
                    <a href="#"><i class="icon icon-unlock-alt"></i>Login/Register</a>
                </li>
                <li>
                    <a href="#"><i class="icon icon-shopping-cart"></i>Contact Us</a>
                </li>
            </ul>
            <button class="btn btn-responsive-nav btn-inverse" data-toggle="collapse" data-target=".nav-main-collapse">
                <i class="icon icon-bars"></i>
            </button>
        </nav>
        <%
        } else {
        %>
        <nav>
            <ul class="nav nav-pills nav-top">
                <li>
                    <a>Welcome <%=memberName%>!</a>
                </li>
                <li>
                    <!--###-->
                    <a href="#"><i class="icon icon-shopping-cart"></i>Shopping Cart</a>
                </li>
                <li>
                    <a href="#"><i class="icon icon-user"></i>Profile</a>
                </li>
                <li>
                    <a href="#"><i class="icon icon-shopping-cart"></i>Contact Us</a>
                </li>
                <li>
                    <a href="#"><i class="icon icon-unlock-alt"></i>Logout</a>
                </li>
            </ul>
            <button class="btn btn-responsive-nav btn-inverse" data-toggle="collapse" data-target=".nav-main-collapse">
                <i class="icon icon-bars"></i>
            </button>
        </nav>
        <%}%>
    </div>
    <div class="navbar-collapse nav-main-collapse collapse">
        <div class="container">
            <nav class="nav-main mega-menu">
                <ul class="nav nav-pills nav-main" id="mainMenu">
                    <li class="dropdown">
                        <a class="dropdown-toggle" href="#">
                            All Departments
                            <i class="icon icon-angle-down"></i>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="#"><i class="icon icon-map-marker"></i> Tables & Desk</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Bathroom</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Beds & Mattresses</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Sofas & Chair</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Cabinets & Storage</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Lightings</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Study</a></li>
                            <li><a href="#"><i class="icon icon-map-marker"></i> Children</a></li>
                            <li><a href="#"><i class="icon icon-coffee"></i> Retail Products</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="selectCountry.jsp">Change Country</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</header>
