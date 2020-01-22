<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.AccessRightEntity"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>

<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
        <script>
            function updateStoreWarehouse(id, destination) {
                window.location.href = "../RetailWarehouseManagement_Servlet?id=" + id + "&destination=" + destination;
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                StaffEntity staffEntity = (StaffEntity) (session.getAttribute("staffEntity"));
                RoleEntity role = staffEntity.getRoles().get(0);

                boolean roleIsAdmin = false;
                boolean roleIsRegionalManager = false;
                boolean roleIsWarehouseManager = false;
                boolean roleIsStoreManager = false;
                if (staffEntity != null) {
                    List<RoleEntity> roles = staffEntity.getRoles();
                    Long[] approvedRolesID = new Long[]{1L, 11L};
                    for (RoleEntity roleEntity : roles) {
                        for (Long ID : approvedRolesID) {
                            if (roleEntity.getId().equals(ID)) {
                                roleIsAdmin = true;
                                break;
                            }
                        }
                    }
                }
                if (staffEntity != null) {
                    List<RoleEntity> roles = staffEntity.getRoles();
                    Long[] approvedRolesID = new Long[]{2L};
                    for (RoleEntity roleEntity : roles) {
                        for (Long ID : approvedRolesID) {
                            if (roleEntity.getId().equals(ID)) {
                                roleIsRegionalManager = true;
                                break;
                            }
                        }
                    }
                }
                if (staffEntity != null) {
                    List<RoleEntity> roles = staffEntity.getRoles();
                    Long[] approvedRolesID = new Long[]{3L};
                    for (RoleEntity roleEntity : roles) {
                        for (Long ID : approvedRolesID) {
                            if (roleEntity.getId().equals(ID)) {
                                roleIsWarehouseManager = true;
                                break;
                            }
                        }
                    }
                }
                if (staffEntity != null) {
                    List<RoleEntity> roles = staffEntity.getRoles();
                    Long[] approvedRolesID = new Long[]{4L};
                    for (RoleEntity roleEntity : roles) {
                        for (Long ID : approvedRolesID) {
                            if (roleEntity.getId().equals(ID)) {
                                roleIsStoreManager = true;
                                break;
                            }
                        }
                    }
                }

                Long warehouseId = null;
                Long regionalOfficeId = null;
                Long id = null;

                if (roleIsRegionalManager) {
                    for (int i = 0; i < role.getAccessRightList().size(); i++) {
                        if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                            regionalOfficeId = role.getAccessRightList().get(i).getRegionalOffice().getId();
                        }
                    }
                }
                if (roleIsWarehouseManager) {
                    for (int i = 0; i < role.getAccessRightList().size(); i++) {
                        if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                            warehouseId = role.getAccessRightList().get(i).getWarehouse().getId();
                        }
                    }
                }
                if (roleIsStoreManager) {
                    for (int i = 0; i < role.getAccessRightList().size(); i++) {
                        if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                            id = role.getAccessRightList().get(i).getStore().getWarehouse().getId();
                        }
                    }
                }
                Boolean canAccessByWarehouseManager = false;
                Boolean canAccessByRegionalManager = false;
                Boolean canAccessByStoreManager = false;
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header"> Store Inventory Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-home"></i> Store Inventory Management
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
                                    Select a warehouse
                                </div>
                                <!-- /.panel-heading -->
                                <form name="storeWarehouseManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th>ID</th>
                                                            <th>Name</th>
                                                            <th>Country</th>
                                                            <th>Address</th>
                                                            <th>Email</th>
                                                            <th>Phone</th>
                                                            <th>Select</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<WarehouseEntity> warehouses = (List<WarehouseEntity>) (session.getAttribute("warehouses"));
                                                            if (warehouses != null) {
                                                                for (int i = 0; i < warehouses.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <%=warehouses.get(i).getId()%>
                                                            </td>
                                                            <td>
                                                                <%=warehouses.get(i).getWarehouseName()%>
                                                            </td>
                                                            <td>
                                                                <%=warehouses.get(i).getCountry().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=warehouses.get(i).getAddress()%>
                                                            </td>
                                                            <td>
                                                                <%=warehouses.get(i).getEmail()%>
                                                            </td>
                                                            <td>
                                                                <%=warehouses.get(i).getTelephone()%>
                                                            </td>                                                          
                                                            <td>
                                                                <% if (roleIsRegionalManager) {
                                                                        canAccessByRegionalManager = false;
                                                                        if (regionalOfficeId.equals(warehouses.get(i).getStore().getRegionalOffice().getId())) {
                                                                            canAccessByRegionalManager = true;
                                                                        }
                                                                    }
                                                                    if (roleIsWarehouseManager) {
                                                                        canAccessByWarehouseManager = false;
                                                                        if (warehouseId.equals(warehouses.get(i).getId())) {
                                                                            canAccessByWarehouseManager = true;
                                                                        }
                                                                    }
                                                                    if (roleIsStoreManager) {
                                                                        canAccessByStoreManager = false;
                                                                        if (id.equals(warehouses.get(i).getId())) {
                                                                            canAccessByStoreManager = true;
                                                                        }
                                                                    }

                                                                    if (canAccessByWarehouseManager || roleIsAdmin || canAccessByRegionalManager || canAccessByStoreManager) {
                                                                %>
                                                                <input type="button" name="btnEdit" value="Select" class="btn btn-primary btn-block"  onclick="javascript:updateStoreWarehouse('<%=warehouses.get(i).getId()%>', 'storeWarehouseManagement.jsp')"/>                                                                                                                            
                                                                <%} else {%>
                                                                <input type="button" name="btnEdit" value="Select" class="btn btn-primary btn-block"  disabled/>
                                                                <%}%>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
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


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
