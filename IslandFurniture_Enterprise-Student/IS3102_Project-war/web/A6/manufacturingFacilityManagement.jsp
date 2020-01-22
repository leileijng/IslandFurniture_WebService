<%@page import="HelperClasses.ManufacturingFacilityHelper"%>
<%@page import="EntityManager.ManufacturingFacilityEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>

        <script>
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function removeManufacturingFacility() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = false;
                    document.getElementById("messageBox").innerHTML = "No items selected.";
                } else {
                    window.event.returnValue = true;

                    document.manufacturingFacilityManagement.action = "../FacilityManagement_ManufacturingFacilityServlet/createStore_GET";
                    document.manufacturingFacilityManagement.submit();
                }
            }
        </script>

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Manufacturing Facility Management
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                             
                                <li>
                                    <i class="icon icon-cogs"></i> Manufacturing Facility Management
                                </li>
                            </ol>
                        </div>
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
                                            out.println("Add and remove manufacturing facilities");
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

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <form name="manufacturingFacilityManagement" action="../FacilityManagement_ManufacturingFacilityServlet/createManufacturingFacility_GET">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input type="submit" name="submit-btn" value="Add Manufacturing Facility" class="btn btn-primary" data-loading-text="Loading...">
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Manufacturing Facility</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">

                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Manufacturing Facility Name</th>
                                                            <th>Regional Office</th>
                                                            <th>Address</th>
                                                            <th>Telephone</th>
                                                            <th>Email Address</th>
                                                            <th>Capacity</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<ManufacturingFacilityHelper> helperList = (List<ManufacturingFacilityHelper>) request.getAttribute("helperList");
                                                            if (helperList != null) {
                                                                for (ManufacturingFacilityHelper helper : helperList) {
                                                        %>
                                                        <tr>
                                                            <td><input type="checkbox" name="delete" value="<%= helper.manufacturingFacilityEntity.getId()%>" /></td>
                                                            <td><%= helper.manufacturingFacilityEntity.getName()%></td>
                                                            <td><%= helper.regionalOffice.getName()%></td>
                                                            <td><%= helper.manufacturingFacilityEntity.getAddress()%></td>
                                                            <td><%= helper.manufacturingFacilityEntity.getTelephone()%></td>
                                                            <td><%= helper.manufacturingFacilityEntity.getEmail()%></td>
                                                            <td><%= helper.manufacturingFacilityEntity.getCapacity()%></td>
                                                            <td><button class="btn btn-primary btn-block" name="submit-btn" value="<%= helper.manufacturingFacilityEntity.getId()%>">View</button></td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>

                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <input type="submit" name="submit-btn" value="Add Manufacturing Facility" class="btn btn-primary" data-loading-text="Loading...">
                                                        <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Manufacturing Facility</button></a>
                                                    </div>
                                                </div>

                                                <div role="dialog" class="modal fade" id="myModal">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h4>Alert</h4>
                                                            </div>
                                                            <div class="modal-body">
                                                                <p id="messageBox">Manufacturing Facility will be removed. Are you sure?</p>
                                                            </div>
                                                            <div class="modal-footer">                                                                                                                                
                                                                <button class="btn btn-primary" name="submit-btn" value="Delete Manufacturing Facility">Confirm</button>
                                                                <a class="btn btn-default" data-dismiss ="modal">Close</a>                        
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                        </form>    
                                    </div>
                                </div>
                                <!-- /.table-responsive -->
                            </div>
                            <!-- /.panel-body -->

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

    <%
        if (request.getAttribute("alertMessage") != null) {
    %>
    <script>
        alert("<%= request.getAttribute("alertMessage")%>");
    </script>
    <%
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
