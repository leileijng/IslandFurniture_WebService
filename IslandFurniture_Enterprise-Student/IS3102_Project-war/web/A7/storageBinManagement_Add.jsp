<%@page import="EntityManager.WarehouseEntity"%>

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
                                Add Storage Bin
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="storeWarehouseManagement_view.jsp">Store Inventory Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-home"></i> <a href="../RetailWarehouseManagement_Servlet?destination=storeWarehouseManagement.jsp&id=<%=warehouseEntity.getId()%>"><%=warehouseEntity.getWarehouseName()%></a>
                                </li>
                                <li>
                                    <i class="icon icon-archive"></i><a href="storageBinManagement.jsp"> Storage Bin Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Storage Bin
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />

                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../StoreStorageBinManagement_AddServlet">
                                <div class="form-group">
                                    <label>Type</label>
                                    <select  class="form-control" name="type" required="true">
                                        <option>Pallet (Warehouse)</option>
                                        <option>Shelf (Warehouse)</option>
                                        <option>Furniture Marketplace (Store)</option>
                                        <option>Retail Outlet (Store)</option>
                                        <option>Kitchen</option>
                                        <option>Inbound</option>
                                        <option>Outbound</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" name="name" type="text" required="true">
                                </div>
                                <div class="form-group">
                                    <label>Length</label>
                                    <input class="form-control" name="length" type="number" min="1" step="1" required="true">
                                </div>
                                <div class="form-group">
                                    <label>Width</label>
                                    <input class="form-control" name="width" type="number" min="1" step="1" required="true"/>
                                </div>
                                <div class="form-group">
                                    <label>Height</label>
                                    <input class="form-control" name="height" type="number" min="1" step="1" required="true"/>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Add Storage Bin" class="btn btn-lg btn-primary btn-block">
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