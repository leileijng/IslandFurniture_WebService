<%@page import="EntityManager.AccessRightEntity"%>
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
                                    <i class="icon icon-edit"></i>  Edit Access Right
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
                        if (request.getAttribute("accessRight") == null) {
                            System.out.println("request.getAttribute = null ");
                        }
                        AccessRightEntity accessRight = (AccessRightEntity) request.getAttribute("accessRight");

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

                                    <form role="form" action="../AccessRight_Servlet/AccessRight_edit_POST">
                                        <%if (role.getId() == 2L || (role.getId() == 7L)) {%>
                                        <div class="form-group">
                                            <label for="">Regional Office</label>
                                            <select id="" class="form-control" name="regionalOffice">
                                                <%
                                                    String select = "";
                                                    out.print("<option value=''></option>");
                                                    for (RegionalOfficeEntity ro : regionalOfficeList) {
                                                        if (accessRight.getRegionalOffice() != null && ro.getId() == accessRight.getRegionalOffice().getId()) {
                                                            select = "selected";
                                                        }
                                                        out.print("<option value='" + ro.getId() + "' " + select + ">" + ro.getName() + "</option>");
                                                        select = "";
                                                    }
                                                %>
                                            </select>                                            
                                        </div>                
                                        <%}%> 
                                        <%if (role.getId() == 4L || (role.getId() == 5L) || (role.getId() == 9L) || (role.getId() == 10L) || (role.getId() == 12L)) {%>
                                        <div class="form-group">
                                            <label for="">Store</label>
                                            <select id="" class="form-control" name="store">
                                                <%
                                                    String select = "";
                                                    out.print("<option value=''></option>");

                                                    for (StoreEntity s : storeList) {
                                                        if (accessRight.getStore() != null && s.getId() == accessRight.getStore().getId()) {
                                                            select = "selected";
                                                        }
                                                        out.print("<option value='" + s.getId() + "' " + select + ">" + s.getName() + "</option>");
                                                        select = "";
                                                    }
                                                %>
                                            </select>                                            
                                        </div>   
                                        <%}%> 
                                        <%if (role.getId() == 8L) {%>
                                        <div class="form-group">
                                            <label for="">Manufacturing Facility</label>
                                            <select id="" class="form-control" name="manufacturingFacility">
                                                <%
                                                    String select = "";
                                                    out.print("<option value=''></option>");

                                                    for (ManufacturingFacilityEntity m : manufacturingFacilityList) {
                                                        if (accessRight.getManufacturingFacility() != null && m.getId() == accessRight.getManufacturingFacility().getId()) {
                                                            select = "selected";
                                                        }
                                                        out.print("<option value='" + m.getId() + "' " + select + ">" + m.getName() + "</option>");
                                                        select = "";
                                                    }
                                                %>
                                            </select>                                            
                                        </div>   
                                        <%}%>
                                        <%if (role.getId() == 3L) {%>
                                        <div class="form-group">
                                            <label for="">Warehouse</label>
                                            <select id="" class="form-control" name="warehouse">
                                                <%
                                                    String select = "";
                                                    out.print("<option value=''></option>");

                                                    for (WarehouseEntity w : warehouseList) {
                                                        if (accessRight.getWarehouse() != null && w.getId() == accessRight.getWarehouse().getId()) {
                                                            select = "selected";
                                                        }
                                                        out.print("<option value='" + w.getId() + "' " + select + ">" + w.getWarehouseName() + "</option>");
                                                        select = "";
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
