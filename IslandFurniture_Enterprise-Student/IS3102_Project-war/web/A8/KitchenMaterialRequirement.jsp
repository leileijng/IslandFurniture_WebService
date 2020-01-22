<%@page import="EntityManager.MaterialRequirementEntity"%>
<%@page import="EntityManager.SaleAndOperationPlanEntity"%>
<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="HelperClasses.MessageHelper"%>
<%@page import="java.text.Format"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>        
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Raw Ingredient Requirement</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_index_GET">Kitchen Requirement Planning</a>
                                </li>   
                                <li>
                                    <i class="icon icon-calendar"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_schedule_GET">Schedule</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_main_GET">Sales Forecast</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i>  <a href="../KitchenManagement_servlet/KitchenDemandManagement_GET">Demand Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i> Raw Ingredient Requirement</a>
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <style>
                        select {
                            max-width: 300px;
                        }
                    </style>                        

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">                                    
                                    <b>Master Raw Ingredient Requirement Plan</b>
                                </div>
                                <div class="panel-body">

                                    <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                        <thead>
                                            <tr>                                                            
                                                <th>SKU</th>
                                                <th>Ingredient Name</th>                                                            
                                                <th>Required Amount</th>                                                
                                                <th>Required Date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                List<MaterialRequirementEntity> mrList = (List<MaterialRequirementEntity>) request.getAttribute("mrList");
                                                for (MaterialRequirementEntity mr : mrList) {
                                            %>
                                            <tr>    
                                                <td><%= mr.getRawIngredient().getSKU() %></td>
                                                <td><%= mr.getRawIngredient().getName() %></td>
                                                <td><%= mr.getQuantity() %></td>
                                                <td><%= mr.getSchedule().getYear() %>-<%= mr.getSchedule().getMonth()%>-<%= mr.getDay() %></td>                                                
                                            </tr>
                                            <%
                                                }
                                            %>                                                                                                                                                                                                                                                                                                
                                        </tbody>
                                    </table> 

                                    <form role="form" action="Kitchen_purchaseOrder">

                                        <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Generate Purchase Orders</button></a>
                                        
                                        <div role="dialog" class="modal fade" id="myModal">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h4>Alert</h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <p id="messageBox"><b>Purchase Orders for all Material Requirements in this month will be submitted. Continue?</b></p>
                                                    </div>
                                                    <div class="modal-footer">                                                                                                                                
                                                        <button class="btn btn-primary" name="submit-btn" value="Delete Warehouse">Confirm</button>
                                                        <a class="btn btn-default" data-dismiss ="modal">Close</a>                        
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>

                                </div>                               

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->                    
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->

        <%            
            if (request.getAttribute("alertMessage") != null) {
        %>
        <script>
            alert("<%= request.getAttribute("alertMessage")%>");
        </script>
        <%
            }
        %>

        <script>
            function getStore() {
                var regionalOfficeId = $("#select_regionalOffice").find('option:selected').val();
                $.get('../SOP_ajax_Servlet', {regionalOfficeId: regionalOfficeId}, function (responseText) {
                    var stores = responseText.trim().split(';');
                    var x = document.getElementById("select_store");
                    while (x.length > 0) {
                        x.remove(0);
                    }
                    for (var i = 0; i < stores.length - 1; i++) {
                        var option = document.createElement("option");
                        option.text = stores[i];
                        x.add(option);
                    }
                });
            }
        </script>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            }
            );
        </script>
    </body>
</html>