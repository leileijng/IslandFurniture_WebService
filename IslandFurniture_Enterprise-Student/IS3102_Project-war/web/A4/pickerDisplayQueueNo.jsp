<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="EntityManager.PickRequestEntity"%>
<%@page import="EntityManager.StaffEntity"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    StaffEntity picker = (StaffEntity) (session.getAttribute("picker"));
    if (picker == null) {
%>
<jsp:forward page="pickerLogin.jsp?errMsg=Session Expired." />
<% }
    PickRequestEntity pickRequest = (PickRequestEntity) (session.getAttribute("pickRequest"));
%>
<html>
    <head>
        <jsp:include page="../header1.html" />
        <meta http-equiv="refresh" content="3; url=../PickerCompleteJobRefresh_Servlet?pickRequestId=<%=pickRequest.getId()%>">
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
                <div class="row">
                    <% String queueNo = request.getParameter("queueNo");%>
                    <hr class="tall">

                    <div class="row">
                        <div class="col-md-12">


                            <%
                                if (pickRequest.getCollectionStatus().equals("Collecting")) {
                            %>
                            <a href="../PickerCollectedJob_Servlet?pickRequestId=<%=pickRequest.getId()%>">
                                <input type="button" value="<%=queueNo%>"  style="min-height: 250px; font-size: 150px;"  class="btn btn-lg btn-success btn-block">
                            </a>
                            <%} else {%>
                            <a href="#">
                                <input type="button" value="<%=queueNo%>"  style="min-height: 250px; font-size: 150px;"  class="btn btn-lg btn-primary btn-block">
                            </a>
                            <% }%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
