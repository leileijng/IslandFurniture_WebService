<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="EntityManager.PickRequestEntity"%>
<%@page import="EntityManager.PickerEntity"%>
<%@ page import="java.io.*,java.util.*" %>
<%
    PickerEntity picker = (PickerEntity) (session.getAttribute("picker"));
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
                <div class="row">
                    <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                        <thead>
                            <tr>
                                <th>Location</th>
                                <th>SKU</th>
                                <th>Quantity</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<PickRequestEntity> pickRequestLinkedList = (List<PickRequestEntity>) (session.getAttribute("pickRequestLinkedList"));
                                List<List<StorageBinEntity>> storageBinsList = (List<List<StorageBinEntity>>) (session.getAttribute("storageBinsList"));
                                if (pickRequestLinkedList != null) {
                                    PickRequestEntity pickRequest = pickRequestLinkedList.get(0);
                                    for (int i = 0; i < pickRequest.getItems().size(); i++) {
                                        String storageBinsString = "";
                                        for (int j = 0; j < storageBinsList.get(i).size(); j++) {
                                            storageBinsString += storageBinsList.get(i).get(j).getName() + "," + storageBinsList.get(i).get(j).getType() + "<br/>";
                                        }
                            %>
                            <tr>
                                <td>
                                    <%=storageBinsString%>
                                </td>
                                <td>
                                    <%=pickRequest.getItems().get(i).getItem().getSKU()%>
                                </td>
                                <td>
                                    <%=pickRequest.getItems().get(i).getQuantity()%>
                                </td>
                            </tr>
                            <%   }%>
                        </tbody>
                    </table>

                    <hr class="tall">

                    <div class="row">
                        <div class="col-md-12">
                            <a  href="../PickerCompleteJob_Servlet?pickRequestId=<%=pickRequest.getId()%>">
                                <input type="button" value="Complete"  style="min-height: 150px; font-size: 50px;"  class="btn btn-lg btn-primary btn-block">
                            </a>
                        </div>
                    </div>
                    <%}%>
                </div>
            </div>
        </div>
    </body>
</html>
