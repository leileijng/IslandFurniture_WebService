<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.PickRequestEntity"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    StaffEntity staff = (StaffEntity) (session.getAttribute("receptionist"));
    if (staff == null) {
%>
<jsp:forward page="../A4/receptionistLogin.jsp?errMsg=Session Expired." />
<% }%>
<html>
    <head>
        <jsp:include page="../header1.html" />
        <meta http-equiv="refresh" content="3; url=../ReceptionistJobList_Servlet">
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

                <form role="form" name="LoginForm"  action="../ReceptionistCallQueue_Servlet?requestType=3"">
                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-12">
                                <label>Scan Barcode</label>
                                <input type="text" name="barcode" class="form-control input-lg" autofocus="true" required="true">
                                <input type="hidden" name="requestType" value="3">
                                <input type="hidden" name="pickRequestID" value=" ">
                            </div>
                        </div>
                    </div>
                </form>

                <br>

                <div class="row">
                    <table class="table table-striped table-bordered" i>
                        <thead>
                            <tr>
                                <th>Queue No.</th>
                                <th>Status</th>
                                <th>Picker</th>
                                <th>Date/Time</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<PickRequestEntity> pickRequests = (List<PickRequestEntity>) (session.getAttribute("pickRequests"));
                                if (pickRequests != null) {
                                    for (int i = 0; i < pickRequests.size(); i++) {
                            %>
                            <tr>
                                <td>
                                    <%= pickRequests.get(i).getQueueNo()%>
                                </td>
                                <td>
                                    <%=pickRequests.get(i).getCollectionStatus()%>
                                </td>
                                <td>
                                    <% StaffEntity picker = pickRequests.get(i).getPicker();
                                        if (picker != null) {
                                            out.println(picker.getName());
                                        } else {
                                            out.println("Unassigned");
                                        }
                                    %>
                                </td>
                                <td>
                                    <%=pickRequests.get(i).getDateSubmitted()%>
                                </td>
                                <td>
                                    <a  href="../ReceptionistCallQueue_Servlet?requestType=1&pickRequestID=<%=pickRequests.get(i).getId()%>">
                                        <input type="button" value="Call" class="btn btn-lg btn-primary btn-block">
                                    </a>&nbsp;
                                    <a  href="../ReceptionistCallQueue_Servlet?requestType=2&pickRequestID=<%=pickRequests.get(i).getId()%>">
                                        <input type="button" value="Uncollected"  class="btn btn-lg btn-primary btn-block">
                                    </a>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>

                    <hr class="tall">
                </div>
            </div>
        </div>
    </body>
</html>
