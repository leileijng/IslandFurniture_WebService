<%@page import="HelperClasses.ItemStorageBinHelper"%>
<%@page import="EntityManager.LineItemEntity"%>
<%@page import="EntityManager.TransferOrderEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function submitTO() {
                document.submitTOForm.action = "../StoreTransferOrderLineItemManagement_UpdateServlet";
                document.submitTOForm.submit();
            }
            function removeTOLineItem() {
                document.removeTOForm.action = "../StoreTransferOrderLineItemManagement_RemoveServlet";
                document.removeTOForm.submit();
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
                if (warehouseEntity == null) {
                    pageContext.forward("storeWarehouseManagement_view.jsp");
                } else {
                    try {
                        String transferOrderID = request.getParameter("id");
                        List<TransferOrderEntity> transferOrders = (List<TransferOrderEntity>) (session.getAttribute("transferOrders"));
                        TransferOrderEntity transferOrder = new TransferOrderEntity();
                        for (int i = 0; i < transferOrders.size(); i++) {
                            if (transferOrders.get(i).getId() == Integer.parseInt(transferOrderID)) {
                                transferOrder = transferOrders.get(i);
                            }
                        }
                        LineItemEntity lineItem = transferOrder.getLineItem();

            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Transfer Order
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="storeWarehouseManagement_view.jsp">Store Inventory Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-home"></i> <a href="../RetailWarehouseManagement_Servlet?destination=storeWarehouseManagement.jsp&id=<%=warehouseEntity.getId()%>"><%=warehouseEntity.getWarehouseName()%></a>
                                </li>
                                <li>
                                    <i class="icon icon-exchange"></i><a href="transferOrderManagement.jsp"> Transfer Order Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Transfer Order <%=transferOrder.getId()%>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessageLong.jsp" />

                    <div class="row">
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"> Transfer Order ID: <%=transferOrder.getId()%> - Line Item Management</h3>
                                </div>
                                <div class="panel-body">
                                    <%if (lineItem == null) {%>
                                    <form role="form" action="../StoreTransferOrderLineItemManagement_Servlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <input class="form-control" id="auto" name="sku" type="text"  >
                                        </div>
                                        <div class="form-group">
                                            <label>Quantity</label>
                                            <input class="form-control" name="quantity" type="number" min="1" step="1" required="true" >
                                        </div>
                                        <div class="form-group">
                                            <input type="submit" value="Add Line Item" class="btn btn-lg btn-primary btn-block">
                                        </div>
                                        <input type="hidden" value="<%=transferOrder.getId()%>" name="id">
                                    </form>
                                    <%} else {%>
                                    <form role="form" name="removeTOForm">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <input class="form-control" name="sku" type="text" value="<%=transferOrder.getLineItem().getItem().getSKU()%>" disabled >
                                        </div>
                                        <div class="form-group">
                                            <label>Quantity</label>
                                            <input class="form-control" name="quantity" type="number" value="<%=transferOrder.getLineItem().getQuantity()%>" disabled >
                                        </div>
                                        <div class="form-group">
                                            <a href="#removeLineItem" data-toggle="modal"><button class="btn btn-primary btn-lg btn-block" <%if (transferOrder.getStatus().equals("Completed") || transferOrder.getStatus().equals("Unfulfillable")) {%>disabled<%}%>>Remove Line Item</button></a>
                                        </div>
                                        <input type="hidden" value="<%=transferOrder.getId()%>" name="id">
                                    </form>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                        <!-- /.row -->
                        <div class="col-lg-6">
                            <div class="panel panel-default">

                                <div class="panel-heading">
                                    <h3 class="panel-title"> Items in Origin Bin Available For Transfer </h3>
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
                                                        <th>Bin Type</th>
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
                                                        <td>
                                                            <%=item.getStorageBinType()%>
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
                        <%
                            if (lineItem != null) {
                        %>
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"> Transfer Order Status: <span class="label label-success"><%=transferOrder.getStatus()%></span></h3>
                                </div>
                                <div class="panel-body">
                                    <%
                                        if (!transferOrder.getStatus().equals("Pending")) {
                                    %>
                                    <form role="form" >
                                        <div class="form-group">
                                            <label>Status</label>
                                            <select class="form-control" name="status" disabled>
                                                <option><%=transferOrder.getStatus()%></option>
                                            </select>
                                            <br>
                                            <div class="form-group">
                                                <input type="submit" value="Update Transfer Order" class="btn btn-lg btn-primary btn-block" disabled="">
                                            </div>
                                        </div>
                                    </form>
                                    <%
                                    } else {
                                    %>
                                    <form role="form" name="submitTOForm">
                                        <div class="form-group">
                                            <label>Status</label>
                                            <select class="form-control" name="status" required="true">
                                                <option>Completed</option>
                                                <option>Unfulfillable</option>
                                            </select>
                                            <br>
                                            <div class="form-group">
                                                <a href="#submitConfirmation" data-toggle="modal"><button class="btn btn-lg btn-primary btn-block">Update Transfer Order</button></a>
                                            </div>
                                        </div>
                                        <input type="hidden" value="<%=transferOrder.getId()%>" name="id">
                                    </form>
                                    <%}%>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%                           }
                            } catch (Exception ex) {
                                response.sendRedirect("../StoreTransferOrderManagement_Servlet");
                                ex.printStackTrace();
                            }
                        }%>

                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
        <div role="dialog" class="modal fade" id="removeLineItem">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Line Item will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeTOLineItem()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <div role="dialog" class="modal fade" id="submitConfirmation">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Confirmation</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnSubmit" type="submit" value="Confirm"  onclick="submitTO()"/>
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(function () {
                var array1 = [];
                $.get('../SKU_ajax_servlet/*', function (responseText) {
                    var arr = responseText.trim().split(';');
                    arr.pop();
                    for (var i = 0; i < arr.length; i++) {
                        array1.push(arr[i]);
                    }
                });

                $("#auto").autocomplete({source: array1});
            });
        </script>
    </body>

</html>
