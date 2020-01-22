<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateRole(id) {
                rolesManagement.id.value = id;
                document.rolesManagement.action = "roleManagement_update.jsp";
                document.rolesManagement.submit();
            }
            function removeRole() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.rolesManagement.action = "../RoleManagement_RoleServlet";
                    document.rolesManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.rolesManagement.action = "../RoleManagement_RemoveRoleServlet";
                    document.rolesManagement.submit();
                }
            }
            function addRole() {
                window.event.returnValue = true;
                document.rolesManagement.action = "roleManagement_add.jsp";
                document.rolesManagement.submit();
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
                            <h1 class="page-header">Roles Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-users"></i> Role Management
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
                                            out.println("View existing roles");
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
                                <form name="rolesManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">                                          
                                       
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <!--th><input type="checkbox" onclick="checkAll(this)" /></th-->
                                                            <th>Name</th>
                                                            <th>Access Level</th>
                                                            <th>Staff</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<RoleEntity> roles = (List<RoleEntity>) (session.getAttribute("roles"));
                                                            if (roles != null) {
                                                                for (RoleEntity role : roles) {
                                                        %>
                                                        <tr>
                                                            <!--td>
                                                                <input type="checkbox" name="delete" value="<%=role.getId()%>" />
                                                            </td-->
                                                            <td>
                                                                <%=role.getName()%>
                                                            </td>
                                                            <td>
                                                                <%=role.getAccessLevel()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    List<StaffEntity> staffs = (List<StaffEntity>) (role.getStaffs());
                                                                    if (staffs.isEmpty()) {
                                                                        out.println("-");
                                                                    } else {
                                                                        for (StaffEntity staff : staffs) {
                                                                            out.println("<span>" + staff.getName() + ", <span>");
                                                                        }
                                                                    }
                                                                %>
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
        <div role="dialog" class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Role will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeRole()"  />
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
