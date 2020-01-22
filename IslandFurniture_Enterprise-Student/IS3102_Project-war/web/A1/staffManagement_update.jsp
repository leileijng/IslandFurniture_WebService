<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
        <style>
            label{
                font-size: 18px;
            }
        </style>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Staff Particulars Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-users"></i> <a href="staffManagement.jsp">Staff Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Staff Particulars Update
                                </li>
                            </ol>
                        </div>
                    </div>

                    <jsp:include page="../displayMessage.jsp" />


                    <%
                        List<StaffEntity> staffs = (List<StaffEntity>) session.getAttribute("staffs");
                        try {
                            String id = (String) session.getAttribute("staffUpdateId");
                            StaffEntity staff = new StaffEntity();
                            for (int i = 0; i < staffs.size(); i++) {
                                if (staffs.get(i).getId() == Long.parseLong(id)) {
                                    staff = staffs.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../StaffManagement_UpdateStaffServlet" onsubmit="return validatePassword()">
                                <div class="form-group">
                                    <label>Identification No</label>
                                    <input class="form-control" required="true" name="identificationNo" type="text" value="<%=staff.getIdentificationNo()%>">
                                </div>
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name" type="text" value="<%=staff.getName()%>">
                                </div>
                                <div class="form-group">
                                    <label>E-mail Address</label>
                                    <input class="form-control" required="true" type="email" name="email" value="<%=staff.getEmail()%>" disabled/>
                                </div>
                                <div class="form-group">
                                    <label>Phone</label>
                                    <input class="form-control" required="true" type="text" name="phone" value="<%=staff.getPhone()%>">
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input class="form-control" type="text" required="true" name="address" value="<%=staff.getAddress()%>">
                                </div>
                                <input type="hidden" name="securityQuestion" value="<%=staff.getSecurityQuestion()%>">
                                <input type="hidden"name="securityAnswer" value="<%=staff.getSecurityAnswer()%>">
                                <!-- <div class="form-group">
                                     <label>New Password (leave blank unless setting a new password).</label>-->
                                <input class="form-control" type="hidden" name="password" id="password">
                                <!--</div>
                                <div class="form-group">
                                    <label>Re-enter New Password</label>-->
                                <input class="form-control" type="hidden" name="repassword" id="repassword">
                                <!--</div>-->

                                <div class="form-group">
                                    <label>Roles Assignment</label><br/>
                                    <%
                                        List<RoleEntity> roles = (List<RoleEntity>) session.getAttribute("staffUpdateRoles");

                                    %>
                                    <table class="table table-hover">
                                        <%                                            List<RoleEntity> roleList = (List<RoleEntity>) session.getAttribute("allRoles");
                                            List<RoleEntity> roleList1 = roleList;

                                            StaffEntity currentUser = (StaffEntity) session.getAttribute("staffEntity");

                                            if (currentUser.getRoles().get(0).getName().equals("Regional Manager")) {
                                                for (int i = 0; i < roleList1.size(); i++) {
                                                    if (roleList1.get(i).getName().equals("Administrator") || roleList1.get(i).getName().equals("Global Manager") || roleList1.get(i).getName().equals("Marketing Director") || roleList1.get(i).getName().equals("Product Development Engineer")) {
                                                        roleList.remove(roleList1.get(i));
                                                    }
                                                }
                                            }
                                            for (RoleEntity role : roleList) {
                                        %>
                                        <tr>
                                            <td><input type="checkbox" name="roles" value="<%= role.getId()%>" <%if (roles.contains(role)) {%>checked<%}%>/> <%= role.getName()%> </td>
                                                <%if (role.getId().toString().equals("2") || role.getId().toString().equals("3") || role.getId().toString().equals("4") || role.getId().toString().equals("7") || role.getId().toString().equals("8") || role.getId().toString().equals("9") || role.getId().toString().equals("10") || role.getId().toString().equals("12")) {%>
                                            <td><span class="btn btn-default"><a href="../AccessRight_Servlet/AccessRight_GET?staffId=<%= staff.getId()%>&roleId=<%= role.getId()%>">Customize Access Right</a></span></td>
                                            <%} else {%>
                                            <td><input type="button" name="btnEdit" value="Cutomize Access Rights" class="btn btn-default"  disabled/>
                                                <%}%>
                                        </tr>
                                        <%
                                            }
                                        %>                                        
                                    </table>
                                </div>
                                <div class="form-group">
                                    <input type="hidden" name="update" value="yes"/>
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block"/>
                                </div>
                                <input type="hidden" value="<%=staff.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../StaffManagement_StaffServlet");
                            ex.printStackTrace();
                        }%>

                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
        <script>
            function validatePassword() {
                var password = document.getElementById("password").value;
                var repassword = document.getElementById("repassword").value;
                var ok = true;
                if ((password != null && repassword != null) || (password != "" && repassword != "")) {
                    if (password != repassword) {
                        //alert("Passwords Do not match");
                        document.getElementById("password").style.borderColor = "#E34234";
                        document.getElementById("repassword").style.borderColor = "#E34234";
                        alert("Passwords do not match. Please key again.");
                        ok = false;
                    }
                } else {
                    return ok;
                }
                return ok;
            }
        </script>     
    </body>

</html>
