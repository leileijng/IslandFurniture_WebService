<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Role Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-users"></i> <a href="roleManagement.jsp">Role Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Role Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%

                        try {
                            String id = request.getParameter("id");
                            List<RoleEntity> roles = (List<RoleEntity>) (session.getAttribute("roles"));
                            RoleEntity role = new RoleEntity();
                            for (int i = 0; i < roles.size(); i++) {
                                if (roles.get(i).getId() == Integer.parseInt(id)) {
                                    role = roles.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../RoleManagement_UpdateRoleServlet">
                                <div class="form-group">
                                    <label>Role Name</label>
                                    <input class="form-control" name="name" type="text" value="<%=role.getName()%>" disabled/>
                                </div>
                                <div class="form-group">
                                    <label>Access Level</label>
                                    <input class="form-control" name="accessLevel" type="text" value="<%=role.getAccessLevel()%>" required="true">
                                </div>                                

                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=role.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../RoleManagement_RoleServlet");
                        }%>

                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
