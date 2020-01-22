<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
                if (warehouseEntity == null) {
                    response.sendRedirect("../RetailWarehouseManagement_Servlet");
                } else {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Create Transfer Order
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="storeWarehouseManagement_view.jsp">Store Inventory Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-home"></i> <a href="../RetailWarehouseManagement_Servlet?destination=storeWarehouseManagement.jsp&id=<%=warehouseEntity.getId()%>"><%=warehouseEntity.getWarehouseName()%></a>
                                </li>
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="transferOrderManagement.jsp">Transfer Order Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Transfer Order
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />

                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../StoreTransferOrderManagement_AddServlet">
                                <div class="form-group">
                                    <label>Origin</label>
                                    <select class="form-control" name="origin" required="true">
                                        <%
                                            List<StorageBinEntity> storageBins = (List<StorageBinEntity>) (session.getAttribute("storageBins"));
                                            for (int i = 0; i < storageBins.size(); i++) {
                                                out.println("<option value='" + storageBins.get(i).getId() + "'>Bin ID" + storageBins.get(i).getId() + " | " + storageBins.get(i).getName() + " - " + storageBins.get(i).getType() + "</option>");
                                            }
                                        %>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Target</label>
                                    <select class="form-control" name="target" required="true">
                                        <%
                                            for (int i = 0; i < storageBins.size(); i++) {
                                                out.println("<option value='" + storageBins.get(i).getId() + "'>Bin ID" + storageBins.get(i).getId() + " | " + storageBins.get(i).getName() + " - " + storageBins.get(i).getType() + "</option>");
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Create Transfer Order" class="btn btn-lg btn-primary btn-block">
                                </div>  
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
<%}%>
