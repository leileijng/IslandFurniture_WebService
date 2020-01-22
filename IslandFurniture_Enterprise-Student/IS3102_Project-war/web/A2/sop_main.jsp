<%@page import="MRP.SalesAndOperationPlanning.SOP_Helper"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="EntityManager.SaleAndOperationPlanEntity"%>
<%@page import="EntityManager.ProductGroupEntity"%>
<%@page import="EntityManager.MonthScheduleEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>

        <script>
            function editSOPRetailProduct() {
                document.sopManagement.action = "../SaleAndOperationPlanning_Servlet/sopManagement";
                document.sopManagement.submit();
            }
            function editSOP() {
                document.sopManagement1.action = "../SaleAndOperationPlanning_Servlet/sopManagement";
                document.sopManagement1.submit();
            }
            function removeSOP() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                window.event.returnValue = true;
                document.sopManagement.action = "../SaleAndOperationPlanning_Servlet/deleteSOP";
                document.sopManagement.submit();
            }

            function removeSOP1() {
                checkboxes = document.getElementsByName('delete1');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                window.event.returnValue = true;
                document.sopManagement1.action = "../SaleAndOperationPlanning_Servlet/deleteSOP1";
                document.sopManagement1.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function checkAll1(source) {
                checkboxes = document.getElementsByName('delete1');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
        </script>

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Sales And Operations Planning
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-pencil"></i>  <a href="../SaleAndOperationPlanning_Servlet/sop_index_GET">Sales And Operations Planning</a>
                                </li>   
                                <li>
                                    <i class="icon icon-calendar"></i>  <a href="../SaleAndOperationPlanning_Servlet/sop_schedule_GET">Schedule</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i> Dashboard</a>
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">                             
                        <div class="col-lg-4">
                            <%  StoreEntity store = (StoreEntity) request.getAttribute("store");%>
                            <h4><b> Store:  </b><%= store.getName()%></h4>
                        </div>                                                
                        <div class="col-lg-4">
                            <% MonthScheduleEntity schedule = (MonthScheduleEntity) request.getAttribute("schedule");%>
                            <h4><b> Period: </b><%= schedule.getYear()%> - <%= schedule.getMonth()%> </h4>
                        </div>                                      
                    </div>
                    <br>
                    <!-- /.row -->
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4><b>Unplanned Product Groups</b></h4>
                                </div>
                                <!-- /.panel-heading -->

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                            <form action="../SaleAndOperationPlanning_Servlet/sop_main_POST">
                                                <table class="table table-striped table-bordered table-hover" id="dataTable1">
                                                    <thead>
                                                        <tr>
                                                            <th>Product Group Name</th>
                                                            <th>Action</th>                                                                                                                        
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<ProductGroupEntity> unplannedProductGroupList = (List<ProductGroupEntity>) request.getAttribute("unplannedProductGroupList");
                                                            for (ProductGroupEntity p : unplannedProductGroupList) {
                                                        %>
                                                        <tr>
                                                            <td><%= p.getProductGroupName()%></td>                                                            
                                                            <td>
                                                                <button class="btn btn-primary" name="productGroupId" value="<%= p.getId()%>">Plan</button>
                                                            </td>
                                                        </tr>
                                                        <%
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>                                                    
                                            </form>                                              

                                        </div>
                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.panel-body -->

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4><b>Sales And Operations Plans - Furniture </b></h4>
                                </div>
                                <!-- /.panel-heading -->

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                            <form name="sopManagement1">
                                                <table class="table table-striped table-bordered table-hover" id="dataTable2">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll1(this)" /></th>
                                                            <th>Product Group</th>
                                                            <th>Sales Forecast</th>
                                                            <th>Production Plan (Must be multiple of lot size)</th>
                                                            <th>Current Inventory</th>
                                                            <th>Target Inventory</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>

                                                        <%
                                                            List<SOP_Helper> sopHelperList = (List<SOP_Helper>) request.getAttribute("sopHelperList");
                                                            for (SOP_Helper model : sopHelperList) {
                                                        %>
                                                        <tr>
                                                            <td><input type="checkbox" name="delete1" value="<%= model.sop.getId()%>" /></td>
                                                            <td><%= model.productGroup.getProductGroupName()%></td>
                                                            <td><%= model.sop.getSaleForcastdata()%></td>
                                                            <td><%= model.sop.getProductionPlan()%></td>
                                                            <td><%= model.sop.getCurrentInventoryLevel()%></td>
                                                            <td><%= model.sop.getTargetInventoryLevel()%></td>
                                                            <td><button class="btn btn-primary" name="submit-btn" onclick="editSOP()" value="<%= model.sop.getId()%>">Edit</button></td>
                                                        </tr>
                                                        <%
                                                            }

                                                        %>

                                                    </tbody>
                                                </table>    
                                                <div class="row">
                                                    <div class="col-md-12">  
                                                        <a href="#deleteSOP" data-toggle="modal"><button class="btn btn-primary">Delete Sales and Operations Plan</button></a>
                                                        <div role="dialog" class="modal fade" id="deleteSOP">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h4>Alert</h4>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <p id="messageBox">SOP will be removed. Are you sure?</p>
                                                                    </div>
                                                                    <div class="modal-footer">                        
                                                                        <input class="btn btn-primary" name="btnRemove" value="Confirm" onclick="removeSOP1()"  />
                                                                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>                                              

                                        </div>
                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.panel-body -->

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4><b>Procurement Plans - Retail Product</b></h4>
                                </div>
                                <!-- /.panel-heading -->

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                            <form name="sopManagement" action="../SaleAndOperationPlanning_Servlet/sopManagement" >
                                                <table class="table table-striped table-bordered table-hover" id="dataTable3">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Product Group</th>
                                                            <th>Sales Forecast</th>
                                                            <th>Procurement Plan</th>
                                                            <th>Current Inventory</th>
                                                            <th>Target Inventory</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%                                                            List<SaleAndOperationPlanEntity> retailSopList = (List<SaleAndOperationPlanEntity>) request.getAttribute("retailSopList");
                                                            for (SaleAndOperationPlanEntity model : retailSopList) {
                                                        %>
                                                        <tr>
                                                            <td><input type="checkbox" name="delete" value="<%= model.getId()%>" /></td>
                                                            <td><%= model.getProductGroup().getProductGroupName()%></td>
                                                            <td><%= model.getSaleForcastdata()%></td>
                                                            <td><%= model.getProductionPlan()%></td>
                                                            <td><%= model.getCurrentInventoryLevel()%></td>
                                                            <td><%= model.getTargetInventoryLevel()%></td>
                                                            <td><button class="btn btn-primary" name="submit-btn" onclick="editSOPRetailProduct()" value="<%= model.getId()%>">Edit</button></td>
                                                        </tr>
                                                        <%
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>    
                                                <div class="row">          
                                                    <div class="col-md-12">
                                                        <a href="#deleteSOPRetailProduct" data-toggle="modal"><button class="btn btn-primary">Delete Procurement Plan</button></a>
                                                        <div role="dialog" class="modal fade" id="deleteSOPRetailProduct">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h4>Alert</h4>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <p id="messageBox">SOP will be removed. Are you sure?</p>
                                                                    </div>
                                                                    <div class="modal-footer">                        
                                                                        <input class="btn btn-primary" name="btnRemoveDOP" value="Confirm" onclick="removeSOP()"  />
                                                                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>


                                                        <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Generate Purchase Orders</button></a>

                                                        <div role="dialog" class="modal fade" id="myModal">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h4>Alert</h4>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        <p id="messageBox"><b>Purchase Orders for the retail products will be submitted. Continue?</b></p>
                                                                    </div>
                                                                    <div class="modal-footer">                                                                                                                                
                                                                        <button class="btn btn-primary" name="submit-btn" value="Purchase Order">Confirm</button>
                                                                        <a class="btn btn-default" data-dismiss ="modal">Close</a>                        
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                            </form>                                              

                                        </div>
                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.panel-body -->

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

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->        
        <script>
            $(document).ready(function () {
                $('#dataTable1').dataTable();
                $('#dataTable2').dataTable();
                $('#dataTable3').dataTable();
            }
            );
        </script>
    </body>

</html>
