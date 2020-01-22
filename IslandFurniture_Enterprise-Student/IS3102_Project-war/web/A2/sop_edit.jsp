<%@page import="EntityManager.SaleAndOperationPlanEntity"%>
<%@page import="EntityManager.ProductGroupEntity"%>
<%@page import="HelperClasses.StoreHelper"%>
<%@page import="EntityManager.MonthScheduleEntity"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="HelperClasses.MessageHelper"%>
<%@page import="java.text.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
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
                            <h1 class="page-header">Sales And Operations Planning</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-pencil"></i>  <a href="../SaleAndOperationPlanning_Servlet/sop_index_GET">Sales And Operations Planning</a>
                                </li>   
                                <li>
                                    <i class="icon icon-calendar"></i>  <a href="../SaleAndOperationPlanning_Servlet/sop_schedule_GET">Schedule</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i>  <a href="../SaleAndOperationPlanning_Servlet/sop_main_GET">Dashboard</a>
                                </li>
                                <li>
                                    <i class="icon icon-edit"></i> Edit Sales And Operations Plan</a>
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
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="row">                             
                                        <div class="col-lg-3">
                                            <%  StoreHelper storeHelper = (StoreHelper) request.getAttribute("storeHelper");%>
                                            <h4><b> Store:  </b><%= storeHelper.store.getName()%></h4>
                                        </div>                                                
                                        <div class="col-lg-3">
                                            <% MonthScheduleEntity schedule = (MonthScheduleEntity) request.getAttribute("schedule");%>
                                            <h4><b> Period: </b><%= schedule.getYear()%> - <%= schedule.getMonth()%> </h4>
                                        </div>                                      
                                        <div class="col-lg-3">
                                            <% ProductGroupEntity productGroup = (ProductGroupEntity) request.getAttribute("productGroup"); %>
                                            <h4><b> Product Group:  </b><%= productGroup.getName()%></h4>
                                        </div>
                                        <div class="col-lg-3">                                            
                                            <h4><b> Lot Size:  </b><%= productGroup.getLotSize()%></h4>
                                        </div>
                                    </div>                                        
                                </div>
                                <div class="panel-body">
                                    <% SaleAndOperationPlanEntity sop = (SaleAndOperationPlanEntity)request.getAttribute("sopEntity");  %>
                                    <form action="../SaleAndOperationPlanning_Servlet/sop_edit_POST">
                                        <div class="form-group">
                                            <label>Sales Forecast</label> 
                                            <input id="input_saleForecast" type="number" class="form-control" name="saleForecast" value="<%= sop.getSaleForcastdata() %>" required="true" readonly="true">
                                        </div>

                                        <div class="form-group">
                                            <label>Production Plan (Must be multiple of lot size) </label>
                                            <input id="input_productionPlan" type="number" class="form-control" name="productionPlan" value="<%= sop.getProductionPlan() %>" required="true" readonly="true">
                                        </div>

                                        <div class="form-group">
                                            <label>Current Inventory level</label> 
                                            <input id="input_currentInventory" type="number" class="form-control" name="currentInventory" value="<%= sop.getCurrentInventoryLevel() %>" required="true" readonly="true">
                                        </div>

                                        <div class="form-group">
                                            <label>Target Inventory Level</label> 
                                            <input id="input_targetInventory" type="number" class="form-control" name="targetInventory" value="<%= sop.getTargetInventoryLevel() %>" required="true" >
                                        </div>
                                        
                                        <input type="hidden" name="sopId" value="<%= sop.getId() %>" >
                                        
                                        <input type="submit" class="btn btn-primary" value="Submit">

                                    </form>

                                </div>                               

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                        <div class="col-lg-6">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-long-arrow-right"></i> Past Target Inventory Level</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="flot-chart">
                                        <div class="flot-chart-content" id="flot-bar-chart"></div>
                                    </div>
                                    <div class="text-right">
                                        <a href="#">View Details <i class="fa fa-arrow-circle-right"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.row -->
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->        
        
        <script>
            $('#input_targetInventory').change(function(){     
                var lotSize = <%= productGroup.getLotSize()%>;
                var targetInventory = parseInt($('#input_targetInventory').val());
                var saleForecast = parseInt($('#input_saleForecast').val());
                var currentInventory = parseInt($('#input_currentInventory').val());                
                var test = false;
                
                if(targetInventory < currentInventory - saleForecast && !test){
                    targetInventory = currentInventory - saleForecast;
                    $('#input_targetInventory').val(targetInventory);
                    alert("Target Inventory Level cannot be less than ( current inventory level - sales forecast ).");                  
                    test = true;
                }
                
                if(targetInventory < 0 && !test){
                    targetInventory = 0;
                    $('#input_targetInventory').val(0);
                    alert("Target Inventory Level cannot be less than zero.");
                    test = true;
                }
                                
                var productionPlan = saleForecast - currentInventory + targetInventory;
                if (productionPlan % lotSize !== 0 && !test) {
                    productionPlan = ( Math.floor(productionPlan / lotSize) + 1) * lotSize;
                    alert("Production Plan has to be multiple of lot size. Target Inventory has been changed automatically.");
                    test = true;
                } 
                $('#input_productionPlan').val(productionPlan);
                $('#input_targetInventoty').val(productionPlan + currentInventory - saleForecast);
            });
        </script>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
                barChart();
            }
            );
        </script>
        
        <script>
            <% List<Integer> pastTargetInventoryLevel = (List<Integer>)request.getAttribute("pastTargetInventoryLevel"); %>
            function barChart() {
                Morris.Bar({
                    element: 'flot-bar-chart',
                    data: [
                        <%  int i = 0;
                            for(Integer a: pastTargetInventoryLevel){    
                            i ++;    
                        %>
                            {
                            device: '<%= schedule.getYear() + "-" + (schedule.getMonth()-i) %>',
                            inventory: <%= a %>
                            },
                        <% } %>
                        ],
                    xkey: 'device',
                    ykeys: ['inventory'],
                    labels: ['target inventory level'],
                    barRatio: 0.4,
                    xLabelAngle: 35,
                    hideHover: 'auto',
                    resize: true
                });
            }
        </script>
        
    </body>
</html>
