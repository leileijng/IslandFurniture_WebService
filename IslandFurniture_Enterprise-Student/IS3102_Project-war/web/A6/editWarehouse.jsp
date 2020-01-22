<%@page import="EntityManager.ManufacturingFacilityEntity"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.WarehouseEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>

        <div id="wrapper">

            <jsp:include page="../menu1.jsp" />

            <style>
                label{
                    font-size: 18px;
                }
                input{
                    max-width: 280px;
                }
            </style>

            <div id="page-wrapper">
                <div class="container-fluid">

                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Warehouse Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                                                             
                                <li>
                                    <i class="icon icon-archive"></i>  <a href="../FacilityManagement_Servlet/warehouseManagement_index">Warehouse Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-edit"></i> Edit Warehouse</a>
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6">                           
                            <% WarehouseEntity warehouse = (WarehouseEntity) request.getAttribute("warehouse");%>
                            <form class="myForm" action="../FacilityManagement_Servlet/editWarehouse_POST">
                                <div class="form-group">
                                    <label for="input_warehouseName">Warehouse Name</label>
                                    <input type="text" class="form-control" id="input_warehouseName" value="<%= warehouse.getWarehouseName()%>" required="true" disabled/>
                                    <input type="hidden" name="warehouseName"  value="<%= warehouse.getWarehouseName()%>" >
                                </div>

                                <% if (warehouse.getStore() != null) {  %>                                
                                <div class="form-group">
                                    <label for="input_Store">Store</label>
                                    <input type="text" class="form-control" id="input_Store"  name="store" value="<%= warehouse.getStore().getName() %>" readonly="true" >
                                </div>
                                <% } %>

                                <% if (warehouse.getManufaturingFacility() != null) {  %>
                                <div class="form-group">
                                    <label for="input_manufacturingFacility">Manufacturing Facility</label>
                                    <input type="text" class="form-control" id="input_manufacturingFacility"  name="manufacturingFacility" value="<%= warehouse.getManufaturingFacility().getName() %>" readonly="true" >
                                </div>
                                <% } %>

                                <div class="form-group">
                                    <label for="input_address">Address</label>
                                    <input type="text" class="form-control" id="input_address"  name="address" value="<%= warehouse.getAddress()%>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_telephone">Telephone</label>
                                    <input type="text" class="form-control" id="input_telephone"  name="telephone" value="<%= warehouse.getTelephone()%>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_email">Email</label>
                                    <input type="email" class="form-control" id="input_email"  name="email" value="<%= warehouse.getEmail()%>" >
                                </div>
                                <input type="hidden" name="warehouseId" value="<%= warehouse.getId()%>">
                                <div class="form-group">
                                    <input type="submit" class="btn btn-primary" value="Submit">
                                </div>
                            </form>

                        </div>
                        <!-- /.col-lg-6 -->
                    </div>
                    <!-- /.row -->

                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->
        <%
            if (request.getAttribute("alertMessage") != null) {
        %><script>
            alert("<%= request.getAttribute("alertMessage")%>");
        </script><%
            }
        %>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
