<%@page import="EntityManager.RegionalOfficeEntity"%>
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
            function removeRegionalOffice() {
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

                    document.regionalOfficeManagement.action = "../FacilityManagement_RegionalOfficeServlet/createRegionalOffice_GET";
                    document.regionalOfficeManagement.submit();
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
                                Regional Office Management
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                             
                                <li>
                                    <i class="icon icon-globe"></i> Regional Office Management
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
                                            out.println("Add and remove regional offices");
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
                                        <form name="regionalOfficeManagement" action="../FacilityManagement_RegionalOfficeServlet/createRegionalOffice_GET">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input type="submit" name="submit-btn" value="Add Regional Office" class="btn btn-primary" data-loading-text="Loading...">
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Regional Office</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">

                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Regional Office Name</th>
                                                            <th>Address</th>
                                                            <th>Telephone</th>
                                                            <th>Email Address</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<RegionalOfficeEntity> regionalOfficeList = (List<RegionalOfficeEntity>) request.getAttribute("regionalOfficeList");
                                                            if (regionalOfficeList != null) {
                                                                for (RegionalOfficeEntity regionalOffice : regionalOfficeList) {
                                                        %>
                                                        <tr>
                                                            <td><input type="checkbox" name="delete" value="<%= regionalOffice.getId()%>" /></td>
                                                            <td><%= regionalOffice.getName()%></td>
                                                            <td><%= regionalOffice.getAddress()%></td>
                                                            <td><%= regionalOffice.getTelephone()%></td>
                                                            <td><%= regionalOffice.getEmail()%></td>
                                                            <td><button class="btn btn-primary btn-block" name="submit-btn" value="<%= regionalOffice.getId()%>">View</button></td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>

                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <input type="submit" name="submit-btn" value="Add Regional Office" class="btn btn-primary" data-loading-text="Loading...">
                                                        <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Regional Office</button></a>
                                                    </div>
                                                </div>

                                                <div role="dialog" class="modal fade" id="myModal">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h4>Alert</h4>
                                                            </div>
                                                            <div class="modal-body">
                                                                <p id="messageBox">Regional office will be removed. Are you sure?</p>
                                                            </div>
                                                            <div class="modal-footer">                                                                                                                                
                                                                <button class="btn btn-primary" name="submit-btn" value="Delete Regional Office">Confirm</button>
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
