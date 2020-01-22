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
                                Storage Bin Info Update
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
                                    <i class="icon icon-edit"></i> Update Storage Bin
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessage.jsp" />

                    <%
                        List<StorageBinEntity> storageBins = (List<StorageBinEntity>) (session.getAttribute("storageBins"));
                        try {
                            String id = request.getParameter("id");
                            StorageBinEntity storageBin = new StorageBinEntity();
                            for (int i = 0; i < storageBins.size(); i++) {
                                if (storageBins.get(i).getId() == Integer.parseInt(id)) {
                                    storageBin = storageBins.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../StoreStorageBinManagement_UpdateServlet">                             
                                <div class="form-group">
                                    <label>Type</label>
                                    <input class="form-control" name="type" type="text" value="<%=storageBin.getType()%>" disabled >
                                </div>
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" name="name" type="text" value="<%=storageBin.getName()%>"required="true">
                                </div>
                                <div class="form-group">
                                    <label>Length</label>
                                    <input class="form-control" name="length" type="number" value="<%=storageBin.getLength()%>" required="true" >
                                </div>
                                <div class="form-group">
                                    <label>Width</label>
                                    <input class="form-control" name="width" type="number" value="<%=storageBin.getWidth()%>" required="true"/>
                                </div>
                                <div class="form-group">
                                    <label>Height</label>
                                    <input class="form-control" name="height" type="number" value="<%=storageBin.getHeight()%>" required="true"/>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=storageBin.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../StoreStorageBinManagement_UpdateServlet");
                        }%>
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