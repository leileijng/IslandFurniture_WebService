<%@page import="EntityManager.LineItemEntity"%>
<%@page import="EntityManager.SupplierEntity"%>
<%@page import="EntityManager.PurchaseOrderEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                List<PurchaseOrderEntity> purchaseOrders = (List<PurchaseOrderEntity>) (session.getAttribute("purchaseOrders"));
                String id = request.getParameter("id");
                String lineItemId = request.getParameter("lineItemId");
                if (purchaseOrders == null || id == null || lineItemId == null) {
                    response.sendRedirect("../PurchaseOrderManagement_Servlet");
                } else {
                    PurchaseOrderEntity purchaseOrder = new PurchaseOrderEntity();
                    for (int i = 0; i < purchaseOrders.size(); i++) {
                        if (purchaseOrders.get(i).getId() == Integer.parseInt(id)) {
                            purchaseOrder = purchaseOrders.get(i);
                        }
                    }
                    List<LineItemEntity> lineItems = purchaseOrder.getLineItems();
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
                                    <i class="icon icon-exchange"></i> <a href="purchaseOrderManagement.jsp">Purchase Order Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="purchaseOrderManagement_Update.jsp?id=<%=purchaseOrder.getId()%>">Purchase Order ID: <%=purchaseOrder.getId()%></a>
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
                                    <h3 class="panel-title"> Purchase Order ID: <%=purchaseOrder.getId()%> - Update Line Item ID: <%=lineItem.getId()%> </h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../PurchaseOrderLineItemManagement_UpdateServlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <input class="form-control" name="sku" type="text"  value="<%=lineItem.getItem().getSKU()%>" disabled/>
                                        </div>
                                        <div class="form-group">
                                            <label>Quantity (In Lot Size)</label>
                                            <input class="form-control" name="quantity" type="number" required="true"  min="1" max="9999" value="<%=lineItem.getQuantity()%>">
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Update Line Item" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=purchaseOrder.getId()%>" name="id">
                                        <input type="hidden" value="<%=lineItem.getId()%>" name="lineitemId">
                                        <input type="hidden" value="<%=lineItem.getItem().getSKU()%>" name ="sku">
                                    </form>
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
