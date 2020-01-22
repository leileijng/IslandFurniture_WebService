<%@page import="EntityManager.MonthScheduleEntity"%>
<%@page import="EntityManager.MasterProductionScheduleEntity"%>
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
                            <h1 class="page-header">Demand Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-crop"></i><a href="../DemandManagement_index_GET/*"> Demand Management</a>
                                </li>                                
                                <li>
                                    <i class="icon icon-calendar"></i> Master Production Schedule List</a>
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
                                    <% MonthScheduleEntity schedule = (MonthScheduleEntity) request.getAttribute("schedule"); %>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <b>Master Production Schedule List</b>
                                        </div>
                                        <div class="col-md-6">
                                            <b>Schedule: <%= schedule.getYear()%> - <%= schedule.getMonth() %></b>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-body">

                                    <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                        <thead>
                                            <tr>                                                            
                                                <th>SKU</th>
                                                <th>Product Name</th>                                                            
                                                <th>Production Amount</th>
                                                <th>Week 1</th>
                                                <th>Week 2</th>
                                                <th>Week 3</th>
                                                <th>Week 4</th>
                                                <th>Week 5</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                List<MasterProductionScheduleEntity> mpsList = (List<MasterProductionScheduleEntity>) request.getAttribute("mpsList");
                                                for (MasterProductionScheduleEntity mps : mpsList) {
                                            %>
                                            <tr>    
                                                <td><%= mps.getFurniture().getSKU() %></td>
                                                <td><%= mps.getFurniture().getName() %></td>
                                                <td><%= mps.getAmount_month() %></td>
                                                <td><%= mps.getAmount_week1() %></td>
                                                <td><%= mps.getAmount_week2() %></td>
                                                <td><%= mps.getAmount_week3() %></td>
                                                <td><%= mps.getAmount_week4() %></td>
                                                <td><%= mps.getAmount_week5() %></td>
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