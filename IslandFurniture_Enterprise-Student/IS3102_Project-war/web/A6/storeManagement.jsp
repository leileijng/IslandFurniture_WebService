<%@page import="HelperClasses.StoreHelper"%>
<%@page import="EntityManager.StoreEntity"%>
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
            function removeStore() {
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

                    document.storeManagement.action = "../FacilityManagement_StoreServlet/createStore_GET";
                    document.storeManagement.submit();
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
                                Store Management
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                             
                                <li>
                                    <i class="icon icon-home"></i> Store Management
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
                                            out.println("Add or remove stores");
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
                                        <form name="storeManagement" action="../FacilityManagement_StoreServlet/createStore_GET">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input type="submit" name="submit-btn" value="Add Store" class="btn btn-primary" data-loading-text="Loading...">
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Store</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">

                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Store Name</th> 
                                                            <th>Regional Office</th>
                                                            <th>Country</th>
                                                            <th>Address</th>
                                                            <th>Telephone</th>
                                                            <th>Email Address</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<StoreHelper> modelList = (List<StoreHelper>) request.getAttribute("modelList");
                                                            if (modelList != null) {
                                                                for (StoreHelper model : modelList) {
                                                        %>
                                                        <tr>
                                                            <td><input type="checkbox" name="delete" value="<%= model.store.getId()%>" /></td>
                                                            <td><%= model.store.getName()%></td>     
                                                            <td><%= model.regionalOffice.getName()%></td>
                                                            <td><%= model.store.getCountry().getName() %></td>
                                                            <td><%= model.store.getAddress()%></td>
                                                            <td><%= model.store.getTelephone()%></td>
                                                            <td><%= model.store.getEmail()%></td>
                                                            <td><button class="btn btn-primary btn-block" name="submit-btn" value="<%= model.store.getId()%>">View</button></a></td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>

                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <input type="submit" name="submit-btn" value="Add Store" class="btn btn-primary" data-loading-text="Loading...">
                                                        <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Store</button></a>
                                                    </div>
                                                </div>
                                                <div role="dialog" class="modal fade" id="myModal">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h4>Alert</h4>
                                                            </div>
                                                            <div class="modal-body">
                                                                <p id="messageBox">Store will be removed. Are you sure?</p>
                                                            </div>
                                                            <div class="modal-footer">                                                                                                                                
                                                                <button class="btn btn-primary" name="submit-btn" value="Delete Store">Confirm</button>
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
