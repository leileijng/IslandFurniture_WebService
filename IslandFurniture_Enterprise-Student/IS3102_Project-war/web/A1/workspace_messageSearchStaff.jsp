<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function selectSingleStaff(id) {
                selectMessageReceiver.id.value = id;
                document.selectMessageReceiver.action = "../WorkspaceMessage_AddServlet";
                document.selectMessageReceiver.submit();
            }
            function selectMultipleStaff() {
                checkboxes = document.getElementsByName('select');
                var numOfTicks = 0;
                for (var i = 0; i < checkboxes.length; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }

                if (checkboxes.length === 0 || numOfTicks === 0) {
                    alert("No items selected.");
                    window.event.returnValue = false;
                } else {
                    window.event.returnValue = true;
                    document.selectMessageReceiver.action = "../WorkspaceMessage_AddServlet";
                    document.selectMessageReceiver.submit();
                }
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('select');
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
                            <h1 class="page-header">Select Message Recipients</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../Workspace_Servlet">Workspace</a>
                                </li>
                                <li>
                                    <i class="icon icon-inbox"></i> <a href="../WorkspaceMessage_Servlet">Messages</a>
                                </li>
                                <li>
                                    <i class="icon icon-envelope"></i> <a href="workspace_messageAdd.jsp">Compose and Send Message</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user-md"></i> Select Message Recipients
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
                                            out.println("Use the checkbox to select the list of recipients for the message. Click on the button on the right to send the message to one person.");
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
                                <form name="selectMessageReceiver">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Name</th>
                                                            <th>Email</th>
                                                            <th>Phone</th>
                                                            <th>Roles</th>
                                                            <th>Select</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%                                                            StaffEntity staffEntity = (StaffEntity) (session.getAttribute("staffEntity"));
                                                            List<StaffEntity> staffs = (List<StaffEntity>) (session.getAttribute("staffs"));
                                                            if (staffs != null) {
                                                                for (int i = 0; i < staffs.size(); i++) {
                                                                    //if (!staffs.get(i).getEmail().equals(staffEntity.getEmail())) {
%>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="select" value="<%=staffs.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=staffs.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=staffs.get(i).getEmail()%>
                                                            </td>
                                                            <td>
                                                                <%=staffs.get(i).getPhone()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    List<RoleEntity> roles = (List<RoleEntity>) (staffs.get(i).getRoles());
                                                                    if (roles.isEmpty()) {
                                                                        out.println("-");
                                                                    } else {
                                                                        for (int k = 0; k < roles.size(); k++) {
                                                                            out.println(roles.get(k).getName());
                                                                        }
                                                                    }
                                                                %>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnSelect" class="btn btn-primary btn-block" id="<%=staffs.get(i).getId()%>" value="Select" onclick="javascript:selectSingleStaff('<%=staffs.get(i).getId()%>')"/>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                    //}
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnMultiple" type="submit" value="Select Staff(s)" onclick="selectMultipleStaff()"  />

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


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
