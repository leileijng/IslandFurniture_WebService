<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.ManufacturingFacilityEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="HelperClasses.MessageHelper"%>
<%@page import="java.text.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>        
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Access Right</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="../A1/accountManagement.jsp">Account Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-users"></i> <a href="../A1/staffManagement.jsp">Staff Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Customize Access Right
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->


                    <style>
                        select {
                            max-width: 300px;
                        }
                    </style>                                       

                    <%
                        StaffEntity staff = (StaffEntity) request.getAttribute("staff");
                        RoleEntity role = (RoleEntity) request.getAttribute("role");
                        List<RegionalOfficeEntity> regionalOfficeList = (List<RegionalOfficeEntity>) request.getAttribute("regionalOfficeList");
                        List<StoreEntity> storeList = (List<StoreEntity>) request.getAttribute("storeList");
                        List<WarehouseEntity> warehouseList = (List<WarehouseEntity>) request.getAttribute("warehouseList");
                        List<ManufacturingFacilityEntity> manufacturingFacilityList = (List<ManufacturingFacilityEntity>) request.getAttribute("manufacturingFacilityList");
                    %>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <p><b>Staff: </b><%= staff.getName()%> </p>
                                    <p><b>Role to customize: </b><%= role.getName()%></p>
                                </div>
                                <div class="panel-body">
                                    <%
                                        StaffEntity currentUser = (StaffEntity) session.getAttribute("staffEntity");
                                        if (currentUser.getRoles().get(0).getName().equals("Regional Manager")) {
                                            regionalOfficeList = new ArrayList<RegionalOfficeEntity>();
                                            storeList = new ArrayList<StoreEntity>();
                                            manufacturingFacilityList = new ArrayList<ManufacturingFacilityEntity>();
                                            warehouseList = new ArrayList<WarehouseEntity>();

                                            if (!currentUser.getAccessRightList().isEmpty()) {
                                                RegionalOfficeEntity regionalOffice = currentUser.getAccessRightList().get(0).getRegionalOffice();
                                                regionalOfficeList.add(regionalOffice);
                                                
                                                for(StoreEntity store: regionalOffice.getStoreList()){
                                                    storeList.add(store);
                                                }
                                                for(ManufacturingFacilityEntity mf: regionalOffice.getManufacturingFacilityList()){
                                                    manufacturingFacilityList.add(mf);
                                                }
                                                for (WarehouseEntity warehouse: regionalOffice.getWarehouseList())
                                                    warehouseList.add(warehouse);
                                            }
                                            
                                        }
                                    %>
                                    <form role="form" action="../AccessRight_Servlet/AccessRight_POST">
                                        <%if (role.getId() == 2L || (role.getId() == 7L)) {%>
                                        <div class="form-group">
                                            <label for="">Regional Office</label>
                                            <select id="" class="form-control" name="regionalOffice">
                                                <option></option>
                                                <%
                                                    for (RegionalOfficeEntity ro : regionalOfficeList) {
                                                %>
                                                <option value="<%= ro.getId()%>"><%= ro.getName()%></option>
                                                <%
                                                    }
                                                %>
                                            </select>                                            
                                        </div>        
                                        <%}%>    
                                        <%if (role.getId() == 4L || (role.getId() == 5L) || (role.getId() == 9L) || (role.getId() == 10L) || (role.getId() == 12L)) {%>
                                        <div class="form-group">
                                            <label for="">Store</label>
                                            <select id="" class="form-control" name="store">
                                                <option></option>
                                                <%
                                                    for (StoreEntity s : storeList) {
                                                %>
                                                <option value="<%= s.getId()%>"><%= s.getName()%></option>
                                                <%
                                                    }
                                                %>
                                            </select>                                            
                                        </div>    
                                        <%}%> 
                                        <%if (role.getId() == 8L) {%>
                                        <div class="form-group">
                                            <label for="">Manufacturing Facility</label>
                                            <select id="" class="form-control" name="manufacturingFacility">
                                                <option></option>
                                                <%
                                                    for (ManufacturingFacilityEntity m : manufacturingFacilityList) {
                                                %>
                                                <option value="<%= m.getId()%>"><%= m.getName()%></option>
                                                <%
                                                    }
                                                %>
                                            </select>                                            
                                        </div>
                                        <%}%>
                                        <%if (role.getId() == 3L) {%>
                                        <div class="form-group">
                                            <label for="">Warehouse</label>
                                            <select id="" class="form-control" name="warehouse">
                                                <option></option>
                                                <%
                                                    for (WarehouseEntity w : warehouseList) {
                                                %>
                                                <option value="<%= w.getId()%>"><%= w.getWarehouseName()%></option>
                                                <%
                                                    }
                                                %>
                                            </select>                                            
                                        </div>       
                                        <%}%>
                                        <button type="submit" class="btn btn-default">Submit</button>
                                        <input type="hidden" value="" name="regionalOffice">
                                        <input type="hidden" value="" name="warehouse">
                                        <input type="hidden" value="" name="manufacturingFacility">
                                        <input type="hidden" value="" name="store">

                                    </form>

                                </div>                               

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

        <script>
            function getStore() {
                var regionalOfficeId = $("#select_regionalOffice").find('option:selected').val();
                $.get('../SOP_ajax_Servlet', {regionalOfficeId: regionalOfficeId}, function(responseText) {
                    var stores = responseText.trim().split(';');
                    var x = document.getElementById("select_store");
                    while (x.length > 0) {
                        x.remove(0);
                    }
                    for (var i = 0; i < stores.length - 1; i++) {
                        var option = document.createElement("option");
                        option.text = stores[i];
                        x.add(option);
                    }
                });
            }
        </script>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            }
            );
        </script>
    </body>
</html>
