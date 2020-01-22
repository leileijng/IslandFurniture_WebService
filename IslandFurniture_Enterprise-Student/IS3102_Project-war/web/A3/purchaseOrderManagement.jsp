<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.AccessRightEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="EntityManager.SupplierEntity"%>
<%@page import="EntityManager.PurchaseOrderEntity"%>
<%@page import="java.util.List"%>


<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updatePO(id) {
                purchaseOrderManagement.id.value = id;
                document.purchaseOrderManagement.action = "../PurchaseOrderLineItemManagement_Servlet";
                document.purchaseOrderManagement.submit();
            }
            function addPO() {
                window.event.returnValue = true;
                document.purchaseOrderManagement.action = "purchaseOrderManagement_Add.jsp";
                document.purchaseOrderManagement.submit();
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                List<PurchaseOrderEntity> purchaseOrders = (List<PurchaseOrderEntity>) (session.getAttribute("purchaseOrders"));
                if (purchaseOrders == null) {
                    response.sendRedirect("../PurchaseOrderManagement_Servlet");
                } else {
                    String disable = "";
                    try {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Purchase Order Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <a href="../PurchaseOrderManagement_Servlet"><i class="icon icon-exchange"></i> Purchase Order Management</a>
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
                                    <%                                        String errMsg = request.getParameter("errMsg");
                                        if (errMsg == null || errMsg.equals("")) {
                                            errMsg = "Add purchase orders";
                                        }
                                        out.println(errMsg);
                                    %>
                                </div>
                                <!-- /.panel-heading -->
                                <form name="purchaseOrderManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary btnCreate" name="btnAdd" type="submit" value="Create Purchase Order" onclick="addPO()"/>
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th hidden></th>
                                                            <th style="width: 8%;">PO ID</th>
                                                            <th style="width: 10%;">Supplier</th>
                                                            <th style="width: 30%;">Shipping Destination</th>
                                                            <th style="width: 12%;">Expected Delivery On</th>
                                                            <th style="width: 10%;">Submitted By</th>
                                                            <th style="width: 15%;">Created Date</th>
                                                            <th style="width: 8%;">Status</th>
                                                            <th style="width: 5%;">Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<PurchaseOrderEntity> finalListOfPO = new ArrayList<PurchaseOrderEntity>();
                                                            StaffEntity staff = (StaffEntity) (session.getAttribute("staffEntity"));
                                                            List<RoleEntity> listOfRoles = staff.getRoles();
                                                            boolean isAdmin = false;
                                                            for (RoleEntity role : listOfRoles) {
                                                                if (role.getName().equals("Administrator") || role.getName().equals("Global Manager")) {
                                                                    isAdmin = true;
                                                                    finalListOfPO = purchaseOrders;
                                                                    break;
                                                                }
                                                            }

                                                            boolean isRegional = false;
                                                            if (!isAdmin) {
                                                                for (RoleEntity role : listOfRoles) {
                                                                    if (role.getName().equals("Regional Manager") || role.getName().equals("Purchasing Manager")) {
                                                                        isRegional = true;
                                                                        List<AccessRightEntity> accessList = role.getAccessRightList();
                                                                        for (AccessRightEntity accessRight : accessList) {
                                                                            for (PurchaseOrderEntity PO : purchaseOrders) {
                                                                                if (accessRight.getStaff().getId().equals(staff.getId()) && accessRight.getRegionalOffice() != null && ((accessRight.getRegionalOffice().getId().equals(PO.getSupplier().getRegionalOffice().getId())) || (accessRight.getRegionalOffice().getId().equals(PO.getDestination().getRegionalOffice().getId())))) {
                                                                                    if (!finalListOfPO.contains(PO)) {
                                                                                        finalListOfPO.add(PO);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                    }
                                                                }
                                                                if (!isRegional) {
                                                                    disable = "disabled";
                                                        %>
                                                    <script>
                                                        $(".btnCreate").attr('disabled', 'disabled');
                                                    </script>
                                                    <%                                                                for (RoleEntity role : listOfRoles) {
                                                                    if (role.getName().equals("Warehouse Manager")) {
                                                                        List<AccessRightEntity> accessList = role.getAccessRightList();
                                                                        for (AccessRightEntity accessRight : accessList) {
                                                                            for (PurchaseOrderEntity PO : purchaseOrders) {
                                                                                if (accessRight.getStaff().getId().equals(staff.getId()) && accessRight.getWarehouse() != null && (accessRight.getWarehouse().getId().equals(PO.getDestination().getId()) && !PO.getStatus().equals("Pending"))) {
                                                                                    if (!finalListOfPO.contains(PO)) {
                                                                                        finalListOfPO.add(PO);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if (role.getName().equals("Store Manager")) {
                                                                        List<AccessRightEntity> accessList = role.getAccessRightList();
                                                                        for (AccessRightEntity accessRight : accessList) {
                                                                            for (PurchaseOrderEntity PO : purchaseOrders) {
                                                                                if (accessRight.getStaff().getId().equals(staff.getId()) && accessRight.getStore().getWarehouse() != null && (accessRight.getStore().getWarehouse().getId().equals(PO.getDestination().getId()) && !PO.getStatus().equals("Pending"))) {
                                                                                    if (!finalListOfPO.contains(PO)) {
                                                                                        finalListOfPO.add(PO);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    if (role.getName().equals("Manufacturing Facility Manager")) {
                                                                        List<AccessRightEntity> accessList = role.getAccessRightList();
                                                                        for (AccessRightEntity accessRight : accessList) {
                                                                            for (PurchaseOrderEntity PO : purchaseOrders) {
                                                                                if (accessRight.getStaff().getId().equals(staff.getId()) && accessRight.getManufacturingFacility().getWarehouse() != null && (accessRight.getManufacturingFacility().getWarehouse().getId().equals(PO.getDestination().getId()) && !PO.getStatus().equals("Pending"))) {
                                                                                    if (!finalListOfPO.contains(PO)) {
                                                                                        finalListOfPO.add(PO);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (finalListOfPO != null) {
                                                            for (int i = 0; i < finalListOfPO.size(); i++) {
                                                                SupplierEntity supplier = finalListOfPO.get(i).getSupplier();
                                                                WarehouseEntity warehouse = finalListOfPO.get(i).getDestination();
                                                    %>
                                                    <tr>
                                                        <td hidden></td>
                                                        <td>
                                                            <%=finalListOfPO.get(i).getId()%>
                                                        </td>
                                                        <td>
                                                            <%=supplier.getSupplierName()%>
                                                        </td>
                                                        <td>
                                                            <%=warehouse.getWarehouseName()%>
                                                        </td>
                                                        <td>
                                                            <% SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
                                                                String date = DATE_FORMAT.format(finalListOfPO.get(i).getExpectedReceivedDate());%>
                                                            <%=date%>
                                                        </td>
                                                        <td>                                                            
                                                            <%= finalListOfPO.get(i).getSubmittedBy()%>
                                                        </td>
                                                        <td>
                                                            <% SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("dd-MM-yyyy hh:mm");%>
                                                            <%= DATE_FORMAT2.format(finalListOfPO.get(i).getCreatedDate())%> </td>
                                                        <td>
                                                            <%=finalListOfPO.get(i).getStatus()%>
                                                        </td>
                                                        <td style="width:80px">
                                                            <input type="button" name="btnEdit" class="btn btn-primary btn-block"  value="View" onclick="javascript:updatePO('<%=finalListOfPO.get(i).getId()%>')"/>
                                                        </td>
                                                    </tr>
                                                    <%
                                                                }
                                                            }
                                                        } catch (Exception ex) {
                                                            response.sendRedirect("../A1/workspace.jsp");
                                                        }
                                                    %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary btnCreate" name="btnAdd" type="submit" value="Create Purchase Order" onclick="addPO()"  <%=disable%>/>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                            <input type="hidden" name="source" value="">  

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


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
<%}%>
