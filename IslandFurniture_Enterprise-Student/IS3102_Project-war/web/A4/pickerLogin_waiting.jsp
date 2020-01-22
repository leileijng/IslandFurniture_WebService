<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="EntityManager.PickRequestEntity"%>
<%@page import="EntityManager.StaffEntity"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    StaffEntity picker = (StaffEntity) (session.getAttribute("picker"));
    if (picker == null) {
%>
<jsp:forward page="pickerLogin.jsp?errMsg=Session Expired." />
<% }%>
<html>
    <head>
        <jsp:include page="../header1.html" />
    </head>
    <body class="dark">


        <div role="main" class="main">

            <div class="header-container">
                <div class="row" style="background-color : rgb(153, 0, 0); margin-bottom: 50px" >
                    <div class="col-md-4 col-md-offset-4">  
                        <img class="center-block img-responsive"  src="../img/logo-label.png" style="margin-top: 20px; margin-bottom: 20px;">
                    </div>
                </div>
            </div>


            <div class="container">
                <jsp:include page="../displayMessageLong.jsp" />
                <div class="row">
                    <div class="col-md-12">
                        <a  href="../PickerAcceptJob_Servlet">
                            <input type="button" value="Start Picking"  style="min-height: 150px; font-size: 50px;"  class="btn btn-lg btn-primary btn-block">
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </body>
</html>
