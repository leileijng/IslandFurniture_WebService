<%@page import="EntityManager.TransferOrderEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.WarehouseEntity"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <%
                try {
                    WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
                    double[] warehousesCapacity = (double[]) (session.getAttribute("warehousesCapacity"));
                    List<TransferOrderEntity> latestTransferOrders = (List<TransferOrderEntity>) session.getAttribute("latestTransferOrders");
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Manufacturing Warehouse Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="manufacturingWarehouseManagement_view.jsp">Manufacturing Warehouse Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-home"></i> <%=warehouseEntity.getWarehouseName()%>
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row">
                        <div class="row featured-boxes">
                            <div class="col-md-12">
                                <div class="col-md-4">
                                    <div class="featured-box featured-box-primary">
                                        <div class="box-content">
                                            <a href="../StorageBinManagement_Servlet"><i class="icon-featured icon icon-archive"> </i>
                                                <h4>Storage Bin Management</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="featured-box featured-box-secundary">
                                        <div class="box-content">
                                            <a href="../TransferOrderManagement_Servlet"><i class="icon-featured icon icon-exchange"> </i>
                                                <h4>Transfer Order Management</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="featured-box featured-box-secundary">
                                        <div class="box-content">
                                            <a href="../ManufacturingInventoryControl_Servlet"><i class="icon-featured icon icon-th"> </i>
                                                <h4>Inventory Control</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>    
                    </div>
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="icon icon-long-arrow-right icon-fw"></i> Warehouse Capacity</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="progress-bars">
                                        <div class="progress-label">
                                            <span>Pallet</span>
                                        </div>
                                        <%
                                            if (warehousesCapacity[0] < 10 && warehousesCapacity[0] > 0) {
                                        %>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="2" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[0]%>%;">
                                                <%=warehousesCapacity[0]%>%
                                            </div>
                                        </div>
                                        <% } else {%>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[0]%>%;">
                                                <%=warehousesCapacity[0]%>%
                                            </div>
                                        </div>
                                        <% }%>


                                        <div class="progress-label">
                                            <span>Shelf</span>
                                        </div>
                                        <%
                                            if (warehousesCapacity[1] < 10 && warehousesCapacity[1] > 0) {
                                        %>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="2" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[1]%>%;">
                                                <%=warehousesCapacity[1]%>%
                                            </div>
                                        </div>
                                        <% } else {%>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[1]%>%;">
                                                <%=warehousesCapacity[1]%>%
                                            </div>
                                        </div>
                                        <% }%>



                                        <div class="progress-label">
                                            <span>Inbound</span>
                                        </div>
                                        <%
                                            if (warehousesCapacity[2] < 10 && warehousesCapacity[2] > 0) {
                                        %>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="2" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[2]%>%;">
                                                <%=warehousesCapacity[2]%>%
                                            </div>
                                        </div>
                                        <% } else {%>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[2]%>%;">
                                                <%=warehousesCapacity[2]%>%
                                            </div>
                                        </div>
                                        <% }%>


                                        <div class="progress-label">
                                            <span>Outbound</span>
                                        </div>
                                        <%
                                            if (warehousesCapacity[3] < 10 && warehousesCapacity[3] > 0) {
                                        %>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="2" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[3]%>%;">
                                                <%=warehousesCapacity[3]%>%
                                            </div>
                                        </div>
                                        <% } else {%>
                                        <div class="progress">
                                            <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: <%=warehousesCapacity[3]%>%;">
                                                <%=warehousesCapacity[3]%>%
                                            </div>
                                        </div>
                                        <% }%>

                                    </div>
                                    <div class="text-right">
                                        <a href="#">View Details <i class="icon icon-arrow-circle-right"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-8">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="icon icon-money icon-fw"></i> Recent Transfer Orders</h3>
                                </div>
                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Order #</th>
                                                    <th>Order Placed</th>
                                                    <th>Order Completed</th>
                                                    <th>Dest Bin ID</th>
                                                    <th>Dest Name</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%for (int i = 0; i < latestTransferOrders.size(); i++) {%>
                                                <tr>
                                                    <td><%=latestTransferOrders.get(i).getId()%></td>
                                                    <td><%=latestTransferOrders.get(i).getDateCreated()%></td>
                                                    <td><%if (latestTransferOrders.get(i).getDateTransferred() == null) {
                                                            out.println("Pending");
                                                        } else {
                                                            out.println(latestTransferOrders.get(i).getDateTransferred());
                                                        }%></td>
                                                    <td><%=latestTransferOrders.get(i).getTarget().getId()%></td>
                                                    <td><%=latestTransferOrders.get(i).getTarget().getName()%></td>
                                                </tr>
                                                <%}%>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="text-right">
                                        <a href="../TransferOrderManagement_Servlet">View All Transfer Orders <i class="icon icon-arrow-circle-right"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.container-fluid -->
                    </div>
                    <!-- /#page-wrapper -->

                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../ManufacturingWarehouseManagement_Servlet");
                            ex.printStackTrace();
                        }%>
                </div>
            </div>
            <!-- /#wrapper -->

    </body>
</html>
