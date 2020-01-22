<%@page import="EntityManager.LineItemEntity"%>
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
                String lineItemId = request.getParameter("lineItemId");
                if (shippingOrders == null || id == null || lineItemId == null) {
                    response.sendRedirect("../ShippingOrderManagement_Servlet");
                } else {
                    ShippingOrderEntity shippingOrder = new ShippingOrderEntity();
                    for (int i = 0; i < shippingOrders.size(); i++) {
                        if (shippingOrders.get(i).getId() == Integer.parseInt(id)) {
                            shippingOrder = shippingOrders.get(i);
                        }
                    }
                    List<LineItemEntity> lineItems = shippingOrder.getLineItems();
                    LineItemEntity lineItem = new LineItemEntity();
                    for (int i = 0; i < lineItems.size(); i++) {
                        if (lineItems.get(i).getId() == Integer.parseInt(lineItemId)) {
                            lineItem = lineItems.get(i);
                        }
                    }
            %>            
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Update Line Item
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="shippingOrderManagement.jsp">Shipping Order Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="shippingOrderManagement_Update.jsp?id=<%=shippingOrder.getId()%>">Shipping Order ID: <%=shippingOrder.getId()%></a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Update Line Item
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
                                    <h3 class="panel-title"> Shipping Order ID: <%=shippingOrder.getId()%> - Update Line Item ID: <%=lineItem.getId()%> </h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../ShippingOrderLineItemManagement_UpdateServlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <input class="form-control" name="sku" type="text"  value="<%=lineItem.getItem().getSKU()%>" disabled/>
                                        </div>
                                        <div class="form-group">
                                            <label>Quantity</label>
                                            <input class="form-control" name="quantity" type="number" required="true"  min="1" max="9999" step="1" value="<%=lineItem.getQuantity()%>">
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Update Line Item" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=shippingOrder.getId()%>" name="id">
                                        <input type="hidden" value="<%=lineItem.getId()%>" name="lineitemId">
                                        <input type="hidden" value="<%=lineItem.getItem().getSKU()%>" name="sku">                                    </form>
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
