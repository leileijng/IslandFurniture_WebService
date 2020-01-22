<%@page import="EntityManager.ShippingOrderEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% List<ShippingOrderEntity> shippingOrders = (List<ShippingOrderEntity>) (session.getAttribute("shippingOrders"));
                if (shippingOrders == null) {
                    response.sendRedirect("../ShippingOrderManagement_Servlet");
                } else {
                    List<WarehouseEntity> warehouses = (List<WarehouseEntity>) (session.getAttribute("warehouses"));
                    List<WarehouseEntity> warehouses1 = (List<WarehouseEntity>) (session.getAttribute("warehouse1"));
            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Create Shipping Order
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="shippingOrderManagement.jsp">Shipping Order Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Shipping Order
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
                                    <h3 class="panel-title"> Shipping Order</h3>
                                </div>
                                <div class="panel-body">
                                    <form id="myForm" role="form" action="../ShippingOrderManagement_AddServlet">
                                        <div class="form-group">
                                            <label>Source</label>
                                            <select class="form-control" id="select_source" name="source" required="true">
                                                <%
                                                    for (int i = 0; i < warehouses.size(); i++) {
                                                        out.println("<option value='" + warehouses.get(i).getId() + "'>" + warehouses.get(i).getWarehouseName() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Destination</label>
                                            <select class="form-control" id="select_destination" name="destination" required="true">
                                                <%
                                                    for (int i = 0; i < warehouses1.size(); i++) {
                                                        out.println("<option value='" + warehouses1.get(i).getId() + "'>" + warehouses1.get(i).getWarehouseName() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Expected Receiving Date:</label>
                                            <input class="form-control" name="expectedDate" type="date" required="true"/>
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Create Shipping Order" class="btn btn-lg btn-primary btn-block">
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

    </body>

    <script>
        $("#myForm").submit(function (e) {
            var source = $("#select_source").find(":selected").text();
            var destination = $("#select_destination").find(":selected").text();
            if (source === destination) {
                alert("Source warehouse should not be the same as destination warehouse.");
                e.preventDefault();
            }
            else
                return true;
        });
    </script>
    <script>
        var today = new Date().toISOString().split('T')[0];
        document.getElementsByName("expectedDate")[0].setAttribute('min', today);
    </script>
</html>
<%}%>
