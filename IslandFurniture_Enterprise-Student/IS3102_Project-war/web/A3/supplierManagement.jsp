<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.AccessRightEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.ItemEntity"%>
<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.SupplierEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateSupplier(id) {
                supplierManagement.id.value = id;
                document.supplierManagement.action = "supplierManagement_update.jsp";
                document.supplierManagement.submit();
            }
            function removeSupplier() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.supplierManagement.action = "../SupplierManagement_SupplierServlet";
                    document.supplierManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.supplierManagement.action = "../SupplierManagement_RemoveSupplierServlet";
                    document.supplierManagement.submit();
                }
            }
            function viewSupplierInfo() {
                window.event.returnValue = true;
                document.supplierManagement.action = "../SupplierItemInfoManagement_Servlet";
                document.supplierManagement.submit();
            }
            function addSupplier() {
                window.event.returnValue = true;
                document.supplierManagement.action = "supplierManagement_add.jsp";
                document.supplierManagement.submit();
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
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Supplier Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-user"></i> Supplier Management
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
                                            out.println("Add or remove suppliers");
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
                                <form name="supplierManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Supplier" onclick="addSupplier()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Supplier</button></a>
                                                    <input class="btn btn-primary" name="btnSupplierInfo" type="submit" value="Supplier Item Infomation Management" onclick="viewSupplierInfo()"  />
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox" onclick="checkAll(this)" /></th>
                                                            <th>Managed By</th>
                                                            <th>Name</th>
                                                            <th>Phone</th>
                                                            <th>Email</th>
                                                            <th>Country</th>
                                                            <th>Address</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<SupplierEntity> suppliers = (List<SupplierEntity>) (session.getAttribute("suppliers"));

                                                            if (suppliers!= null) {
                                                                for (int i = 0; i < suppliers.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=suppliers.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=suppliers.get(i).getRegionalOffice().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=suppliers.get(i).getSupplierName()%>
                                                            </td>
                                                            <td>
                                                                <%=suppliers.get(i).getContactNo()%>
                                                            </td>
                                                            <td>
                                                                <%=suppliers.get(i).getEmail()%>
                                                            </td>
                                                            <td>
                                                                <%CountryEntity country = suppliers.get(i).getCountry();
                                                                    if (country != null) {
                                                                        out.print(country.getName());
                                                                    } else {
                                                                        out.print("NIL");
                                                                    }
                                                                %>
                                                            </td>
                                                            <td>
                                                                <%=suppliers.get(i).getAddress()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=suppliers.get(i).getId()%>" value="Update" onclick="javascript:updateSupplier('<%=suppliers.get(i).getId()%>')"/>
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
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Supplier" onclick="addSupplier()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Supplier</button></a>
                                                    <input class="btn btn-primary" name="btnSupplierInfo" type="submit" value="Supplier Item Infomation Management" onclick="viewSupplierInfo()"  />
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
                        <p id="messageBox">Supplier will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeSupplier()"  />
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
