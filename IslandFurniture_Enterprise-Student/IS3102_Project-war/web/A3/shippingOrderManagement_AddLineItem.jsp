<%@page import="HelperClasses.ItemStorageBinHelper"%>
<%@page import="EntityManager.ShippingOrderEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                List<ShippingOrderEntity> shippingOrders = (List<ShippingOrderEntity>) (session.getAttribute("shippingOrders"));
                String id = request.getParameter("id");
                if (shippingOrders == null || id == null) {
                    response.sendRedirect("../ShippingOrderManagement_Servlet");
                } else {
                    ShippingOrderEntity shippingOrder = new ShippingOrderEntity();
                    for (int i = 0; i < shippingOrders.size(); i++) {
                        if (shippingOrders.get(i).getId() == Integer.parseInt(id)) {
                            shippingOrder = shippingOrders.get(i);
                        }
                    }
            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Add Line Item
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="shippingOrderManagement.jsp">Shipping Order Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="shippingOrderManagement_Update.jsp?id=<%=shippingOrder.getId()%>">Shipping Order ID: <%=shippingOrder.getId()%></a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Line Item
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
                                    <h3 class="panel-title"> Shipping Order ID: <%=shippingOrder.getId()%> - Add Line Item </h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../ShippingOrderLineItemManagement_AddServlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <select required="true" name="sku" class="form-control">
                                                <%
                                                    List<ItemStorageBinHelper> listOfItems1 = (List<ItemStorageBinHelper>) session.getAttribute("listOfLineItems");
                                                    if (listOfItems1 != null) {
                                                        for (int i = 0; i < listOfItems1.size(); i++) {
                                                            out.println("<option value='" + listOfItems1.get(i).getSKU() + "'>" + listOfItems1.get(i).getSKU() + "</option>");
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Quantity</label>
                                            <input class="form-control" name="quantity" type="number" min="1" step="1" max="9999" required="true" >
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Add Line Item" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=shippingOrder.getId()%>" name="id">
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="panel panel-default">

                                <div class="panel-heading">
                                    <h3 class="panel-title"> Items in Outbound Bin </h3>
                                </div>
                                <div class="panel-body" style="">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                            <br>
                                            <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                <thead>
                                                    <tr>
                                                        <th>SKU</th>
                                                        <th>Item Name</th>
                                                        <th>Item Type</th>
                                                        <th>Quantity</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        List<ItemStorageBinHelper> listOfLineItems = (List<ItemStorageBinHelper>) session.getAttribute("listOfLineItems");
                                                        if (listOfLineItems != null) {
                                                            for (ItemStorageBinHelper item : listOfLineItems) {

                                                    %>
                                                    <tr>
                                                        <td>
                                                            <%=item.getSKU()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getItemName()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getItemType()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getItemQty()%>
                                                        </td>
                                                    </tr>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- /#page-wrapper -->
            </div>
            <!-- /#container fluid -->
        </div>
        <!-- /#wrapper -->
    </body>
</html>
<%}%>
