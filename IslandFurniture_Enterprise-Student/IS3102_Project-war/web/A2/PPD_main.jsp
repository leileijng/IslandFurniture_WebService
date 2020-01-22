<%@page import="EntityManager.SaleAndOperationPlanEntity"%>
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
                            <h1 class="page-header">Production Plan Distribution</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i> <a href="../PPD_index_GET/*">Production Plan Distribution</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i> Distribution Result List</a>
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
                    <%
                        RegionalOfficeEntity regionalOffice = (RegionalOfficeEntity) session.getAttribute("PPD_regionalOffice");
                    %>
                    <a href="../store_MF_connectionManagement_GET/*?regionalOffice=<%= regionalOffice.getId()%>"><button class="btn btn-primary"><span class="icon icon-cogs"></span> Distribution Schema Management</button></a>
                    <br><br>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">                                            
                                    <a href="../PPD_main_POST/*"><button class="btn btn-primary"><span class="icon icon-repeat"></span> Distribute Production Plan</button></a>                                                                                
                                    <a href="../MRP_main_POST/generateShippingOrderForFurniture"><span class="btn btn-primary"><span class="icon icon-truck"></span> Generate Shipping Order</span></a>                                                                            
                                </div>
                                <div class="panel-body">

                                    <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                        <thead>
                                            <tr>                                                            
                                                <th>Manufacturing Facility</th>
                                                <th>Product Group</th>                                                            
                                                <th>Production Amount</th>
                                                <th>Ship to Store</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                List<SaleAndOperationPlanEntity> sopList = (List<SaleAndOperationPlanEntity>) request.getAttribute("sopList");
                                                for (SaleAndOperationPlanEntity sop : sopList) {
                                            %>
                                            <tr>    
                                                <td><%= sop.getManufacturingFacility().getName()%></td>
                                                <td><%= sop.getProductGroup().getName()%></td>
                                                <td><%= sop.getProductionPlan()%></td>
                                                <td><%= sop.getStore().getName() %></td>
                                            </tr>
                                            <%
                                                }

                                            %>                                                                                                                                                                                                                                                                                                

                                        </tbody>
                                    </table>                                                    

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

        <%            if (request.getAttribute("alertMessage") != null) {
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