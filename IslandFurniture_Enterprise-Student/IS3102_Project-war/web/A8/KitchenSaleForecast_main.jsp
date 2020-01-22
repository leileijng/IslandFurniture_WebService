<%@page import="EntityManager.SaleForecastEntity"%>
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
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
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
                                Sales Forecast
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_index_GET">Kitchen Requirement Planning</a>
                                </li>   
                                <li>
                                    <i class="icon icon-calendar"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_schedule_GET">Schedule</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i> Sales Forecast</a>
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
                                    <h4><b>Menu Items</b></h4>
                                </div>
                                <!-- /.panel-heading -->

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">

                                            <table class="table table-striped table-bordered table-hover" id="dataTable1">
                                                <thead>
                                                    <tr>
                                                        <th>SKU</th>
                                                        <th>Menu item</th>
                                                        <th>Sales Forecast</th>
                                                        <th>Forecast Method <i class="icon icon-question-circle" <a href="#myModal" data-toggle="modal"></a></i></th>
                                                        <th>Action</th>                                                                                                                        
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        List<SaleForecastEntity> saleForecastList = (List<SaleForecastEntity>) request.getAttribute("saleForecastList");

                                                        for (SaleForecastEntity s : saleForecastList) {

                                                    %>
                                                    <tr>
                                                        <td style="width: 10%"><%= s.getMenuItem().getSKU()%></td>
                                                        <td style="width: 10%"><%= s.getMenuItem().getName()%></td>
                                                        <td style="width: 12%"><%= s.getQuantity()%></td>
                                                        <td style="width: 48%">
                                                            <div class="btn-group btn-toggle"> 
                                                                <span id="<%= s.getMenuItem().getId()%>" onclick="getMultipleRegressionSaleForecast(this)" class="btn btn-default <% if (s.getMethod().equals("M")) {out.print("active");}%> ">Multiple Linear Regression</span>
                                                                <span id="<%= s.getMenuItem().getId()%>" onclick="getRegressionSaleForecast(this)" class="btn btn-default <% if (s.getMethod().equals("R")) {out.print("active");}%>">Simple Linear Regression</span>
                                                                <span id="<%= s.getMenuItem().getId()%>" onclick="getSaleForecast(this)" class="btn btn-default <% if (s.getMethod().equals("A")) {out.print("active");}%> ">Average Method</span>                                                                    
                                                                <a href="#myModal<%= s.getId()%>" data-toggle="modal"><span id="<%= s.getMenuItem().getId()%>" class="btn btn-default <% if (s.getMethod().equals("E")) {out.print("active");}%>">Manual</span></a>
                                                                <div role="dialog" class="modal fade" id="myModal<%= s.getId()%>">
                                                                    <div class="modal-dialog">
                                                                        <form action="../KitchenManagement_servlet/Kitchen_editSaleForecast">
                                                                            <input type="hidden" name="saleForecastId" value="<%= s.getId()%>" >
                                                                            <div class="modal-content">
                                                                                <div class="modal-header">
                                                                                    <h4>Manual Create Sales Forecast</h4>
                                                                                </div>
                                                                                <div class="modal-body">
                                                                                    <div class="form-group">
                                                                                        <label>Sales Forecast Quantity: </label>
                                                                                        <input type="number" min="0" class="form-control" name="quantity" required>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="modal-footer">                                                                                                                                
                                                                                    <button class="btn btn-primary" name="submit-btn" value="Delete Warehouse">Confirm</button>
                                                                                    <a class="btn btn-default" data-dismiss ="modal">Close</a>                        
                                                                                </div>
                                                                            </div>
                                                                        </form>
                                                                    </div>
                                                                </div>
                                                            </div>                                                                                                                                                                                                                                                 
                                                        </td> 
                                                        <td style="width: 20%">
                                                            <form action="../KitchenManagement_servlet/ViewSaleFigure_GET">
                                                                <button class="btn btn-primary" name="menuItemSKU" value="<%= s.getMenuItem().getSKU()%>">View historical data</button>
                                                            </form>
                                                        </td>                                                   
                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                </tbody>
                                            </table>                                                    

                                            <a href="KitchenDemandManagement_GET"><span class="btn btn-primary">Kitchen Demand Management</span></a>
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

                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->

        <script>
            function getSaleForecast(a) {
                var menuitemId = $(a).attr('id');
                $.get('../kitchen_ajax_servlet/average', {menuitemId: menuitemId}, function (responseText) {
                    var val = responseText.trim().split(';');
                    $(a).closest('tr').find('td:eq(2)').html(val[1]);
                    $(a).addClass('active');
                    $(a).closest('td').find('span:eq(0)').removeClass('active');
                    $(a).closest('td').find('span:eq(1)').removeClass('active');
                    $(a).closest('td').find('span:eq(3)').removeClass('active');
                });
            }
        </script>

        <script>
            function getRegressionSaleForecast(a) {
                var menuitemId = $(a).attr('id');
                $.get('../kitchen_ajax_servlet/regression', {menuitemId: menuitemId}, function (responseText) {
                    var val = responseText.trim().split(';');
                    $(a).closest('tr').find('td:eq(2)').html(val[1]);
                    $(a).addClass('active');
                    $(a).closest('td').find('span:eq(0)').removeClass('active');
                    $(a).closest('td').find('span:eq(2)').removeClass('active');
                    $(a).closest('td').find('span:eq(3)').removeClass('active');
                });
            }
        </script>

        <script>
            function getMultipleRegressionSaleForecast(a) {
                var menuitemId = $(a).attr('id');
                $.get('../kitchen_ajax_servlet/multiple', {menuitemId: menuitemId}, function (responseText) {
                    var val = responseText.trim().split(';');
                    $(a).closest('tr').find('td:eq(2)').html(val[1]);
                    $(a).addClass('active');
                    $(a).closest('td').find('span:eq(1)').removeClass('active');
                    $(a).closest('td').find('span:eq(2)').removeClass('active');
                    $(a).closest('td').find('span:eq(3)').removeClass('active');
                });
            }
        </script>

        <div role="dialog" class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox" style="color: #000"><b>Explanation on the sales forecast method:<br>1.Linear Regression Method use all historical data without incorporate sales shock. <br> 2.Multiple Linear Regression Method use all data and can take seasonal effect into consideration. <br> 3.Average Method should be used when short term historical data are more trustful.</b></p>
                    </div>
                    <div class="modal-footer">                        
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>


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
            }
            );
        </script>
    </body>

</html>
