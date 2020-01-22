<%@page import="EntityManager.Supplier_ItemEntity"%>
<%@page import="EntityManager.RawMaterialEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.SupplierEntity"%>

<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Supplier Item Info Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-truck"></i><a href="../SupplierItemInfoManagement_Servlet"> Supplier Item Info Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Supplier Item Info Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            String id = request.getParameter("id");
                            List<Supplier_ItemEntity> listOfSupplierItemInfo = (List<Supplier_ItemEntity>) (session.getAttribute("listOfSupplierItemInfo"));
                            Supplier_ItemEntity supplierItemInfo = new Supplier_ItemEntity();
                            for (int i = 0; i < listOfSupplierItemInfo.size(); i++) {
                                if (listOfSupplierItemInfo.get(i).getId() == Integer.parseInt(id)) {
                                    supplierItemInfo = listOfSupplierItemInfo.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../SupplierItemInfoManagement_UpdateSupplierItemInfoServlet">
                                <div class="form-group">
                                    <label>Supplier ID</label>
                                    <input class="form-control" required="true" name="supplierId" type="text" value="<%=supplierItemInfo.getSupplier().getId()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>SKU</label>
                                    <input class="form-control"  type="text" name="SKU" value="<%=supplierItemInfo.getItem().getSKU()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Cost Price (Per Lot)</label>
                                    <input class="form-control" type="number" step="any" name="costPrice" required="true" value="<%=supplierItemInfo.getCostPrice()%>" >
                                </div>
                               <div class="form-group">
                                    <label>Lot Size</label>
                                    <input class="form-control" type="number"  name="lotSize" required="true" value="<%=supplierItemInfo.getLotSize()%>" >
                                </div>
                                <div class="form-group">
                                    <label>Lead Time (Days)</label>
                                    <input class="form-control" type="number"  name="leadTime" required="true" value="<%=supplierItemInfo.getLeadTime()%>" >
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=supplierItemInfo.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%} catch (Exception ex) {
                        
                            //response.sendRedirect("../RawMaterialManagement_RawMaterialServlet");
                        }%>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
