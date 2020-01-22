<%@page import="EntityManager.SupplierEntity"%>
<%@page import="EntityManager.PurchaseOrderEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% List<PurchaseOrderEntity> purchaseOrders = (List<PurchaseOrderEntity>) (session.getAttribute("purchaseOrders"));
                if (purchaseOrders == null) {
                    response.sendRedirect("../PurchaseOrderManagement_Servlet");
                } else {
                    List<SupplierEntity> activeSuppliers = (List<SupplierEntity>) (session.getAttribute("suppliers"));
                    List<WarehouseEntity> warehouses = (List<WarehouseEntity>) (session.getAttribute("warehouses"));
            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Create Purchase Order
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="purchaseOrderManagement.jsp">Purchase Order Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Purchase Order
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />


                    <div class="row">
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"> Purchase Order</h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../PurchaseOrderManagement_AddServlet">
                                        <div class="form-group">
                                            <label>Supplier</label>
                                            <select class="form-control" name="supplier" required="true">
                                                <%
                                                    for (int i = 0; i < activeSuppliers.size(); i++) {
                                                        out.println("<option value='" + activeSuppliers.get(i).getId() + "'>" + activeSuppliers.get(i).getSupplierName() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Destination</label>
                                            <select class="form-control" name="destination" required="true">
                                                <%
                                                    for (int i = 0; i < warehouses.size(); i++) {
                                                        out.println("<option value='" + warehouses.get(i).getId() + "'>" + warehouses.get(i).getWarehouseName() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Expected Receiving Date:</label>
                                            <input class="form-control" name="expectedDate" type="date" required="true"/>
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Create Purchase Order" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
        <script>
            var today = new Date().toISOString().split('T')[0];
            document.getElementsByName("expectedDate")[0].setAttribute('min', today);
        </script>
    </body>

</html>
<%}%>
