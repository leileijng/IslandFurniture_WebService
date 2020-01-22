<%@page import="EntityManager.Supplier_ItemEntity"%>
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
                if (purchaseOrders == null || id == null) {
                    response.sendRedirect("../PurchaseOrderManagement_Servlet");
                } else {
                    PurchaseOrderEntity purchaseOrder = new PurchaseOrderEntity();
                    for (int i = 0; i < purchaseOrders.size(); i++) {
                        if (purchaseOrders.get(i).getId() == Integer.parseInt(id)) {
                            purchaseOrder = purchaseOrders.get(i);
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
                                    <i class="icon icon-exchange"></i> <a href="purchaseOrderManagement.jsp">Purchase Order Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-exchange"></i> <a href="purchaseOrderManagement_Update.jsp?id=<%=purchaseOrder.getId()%>">Purchase Order ID: <%=purchaseOrder.getId()%></a>
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
                                    <h3 class="panel-title"> Purchase Order ID: <%=purchaseOrder.getId()%> - Add Line Item </h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../PurchaseOrderLineItemManagement_AddServlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <select required="true" name="sku" class="form-control">
                                                <%
                                                    List<Supplier_ItemEntity> listOfItems = (List<Supplier_ItemEntity>) session.getAttribute("listOfItems");
                                                    if (listOfItems != null) {
                                                        for (int i = 0; i < listOfItems.size(); i++) {
                                                            out.println("<option value='" + listOfItems.get(i).getItem().getSKU() + "'>" + listOfItems.get(i).getItem().getSKU() + "</option>");
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Quantity (In Lot Size)</label>
                                            <input class="form-control" name="quantity" type="number" min="1" max="9999" required="true" >
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Add Line Item" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=purchaseOrder.getId()%>" name="id">
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="panel panel-default">

                                <div class="panel-heading">
                                    <h3 class="panel-title"> Items supplied by supplier </h3>
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
                                                        <th>Price (Per <p style="color: black;">Lot Size)</p></th>
                                                <th>Lot Size</th>
                                                <th>Lead Time</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        if (listOfItems != null) {
                                                            for (Supplier_ItemEntity item : listOfItems) {
                                                    %>
                                                    <tr>
                                                        <td>
                                                            <%=item.getItem().getSKU()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getItem().getName()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getItem().getType()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getCostPrice()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getLotSize()%>
                                                        </td>
                                                        <td>
                                                            <%=item.getLeadTime()%>
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
