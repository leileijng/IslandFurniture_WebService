<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="EntityManager.TransferOrderEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>


<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateTO(id) {
                transferOrderManagement.id.value = id;
                document.transferOrderManagement.action = "../StoreTransferOrderLineItemManagement_OriginBinItemsServlet";

                document.transferOrderManagement.submit();
            }
            function removeTO() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.transferOrderManagement.action = "../StoreTransferOrderManagement_Servlet";
                    document.transferOrderManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.transferOrderManagement.action = "../StoreTransferOrderManagement_RemoveServlet";
                    document.transferOrderManagement.submit();
                }
            }
            function addTO() {
                window.event.returnValue = true;
                document.transferOrderManagement.action = "transferOrderManagement_Add.jsp";
                document.transferOrderManagement.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
                if (warehouseEntity == null) {
                    response.sendRedirect("../RetailWarehouseManagement_Servlet");
                } else {
                    try {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Transfer Order Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="storeWarehouseManagement_view.jsp">Store Inventory Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-home"></i> <a href="../RetailWarehouseManagement_Servlet?destination=storeWarehouseManagement.jsp&id=<%=warehouseEntity.getId()%>"><%=warehouseEntity.getWarehouseName()%></a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-exchange"></i> Transfer Order Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        String errMsg = request.getParameter("errMsg");
                                        String goodMsg = request.getParameter("goodMsg");
                                        if (errMsg == null && goodMsg == null) {
                                            out.println("Add or remove transfer orders");
                                        } else if ((errMsg != null) && (goodMsg == null)) {
                                            if (!errMsg.equals("")) {
                                                out.println(errMsg);
                                            }
                                        } else if ((errMsg == null && goodMsg != null)) {
                                            if (!goodMsg.equals("")) {
                                                out.println(goodMsg);
                                            }
                                        }
                                    %>
                                </div>
                                <!-- /.panel-heading -->
                                <form name="transferOrderManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Transfer Order" onclick="addTO()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Transfer Order</button></a>
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Date Created</th>
                                                            <th>Date Transferred</th>
                                                            <th>Origin Name</th>
                                                            <th>Origin Type</th>
                                                            <th>Target Name</th>
                                                            <th>Target Type</th>
                                                            <th>Status</th>
                                                            <th>Submitted By</th>
                                                            <th>Warehouse</th>
                                                            <th>Details</th>
                                                        </tr>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<TransferOrderEntity> transferOrders = (List<TransferOrderEntity>) (session.getAttribute("transferOrders"));
                                                            if (transferOrders != null) {
                                                                for (int i = 0; i < transferOrders.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=transferOrders.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=transferOrders.get(i).getDateCreated()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    if (transferOrders.get(i).getDateTransferred() == null) {
                                                                        out.println("-");
                                                                    } else {
                                                                        out.println(transferOrders.get(i).getDateTransferred());
                                                                    }%>
                                                            </td>
                                                            <td>
                                                                <%=((StorageBinEntity) transferOrders.get(i).getOrigin()).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=((StorageBinEntity) transferOrders.get(i).getOrigin()).getType()%>
                                                            </td>
                                                            <td>
                                                                <%=((StorageBinEntity) transferOrders.get(i).getTarget()).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=((StorageBinEntity) transferOrders.get(i).getTarget()).getType()%>
                                                            </td>
                                                            <td>
                                                                <%=transferOrders.get(i).getStatus()%>
                                                            </td>
                                                            <td>
                                                                <%=transferOrders.get(i).getSubmittedBy()%>
                                                            </td>
                                                            <td>
                                                                <%=warehouseEntity.getWarehouseName()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=transferOrders.get(i).getId()%>" value="View Details" onclick="javascript:updateTO('<%=transferOrders.get(i).getId()%>')"/>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                    }
                                                                }
                                                            } catch (Exception ex) {
                                                                response.sendRedirect("storeWarehouseManagement.jsp");
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Transfer Order" onclick="addTO()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Transfer Order</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                        </div>

                                    </div>
                                    <!-- /.panel-body -->
                                </form>

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->


                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->

        <div role="dialog" class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Transfer order will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeTO()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
<%}%>