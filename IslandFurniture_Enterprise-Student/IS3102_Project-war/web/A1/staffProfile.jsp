<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
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
                                User Profile
                            </h1>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />
                    <%
                        try {
                            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
                    %>
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="tabs">
                                <ul class="nav nav-tabs">
                                    <li class="active">
                                        <a href="#overview" data-toggle="tab"><i class="icon icon-user"></i> Overview</a>
                                    </li>
                                    <li>
                                        <a href="#roles" data-toggle="tab">Roles</a>
                                    </li>
                                </ul>
                                <div class="tab-content">
                                    <div id="overview" class="tab-pane active">
                                        <form role="form" action="../StaffManagement_UpdateStaffServlet" onsubmit="return validatePassword()">
                                            <h4>Personal Information</h4>
                                            <div class="form-group">
                                                <label>Identification No</label>
                                                <input class="form-control" required="true" name="identificationNo" type="text" value="<%=staffEntity.getIdentificationNo()%>">
                                            </div>
                                            <div class="form-group">
                                                <label>Name</label>
                                                <input class="form-control" required="true" name="name" type="text" value="<%=staffEntity.getName()%>">
                                            </div>
                                            <div class="form-group">
                                                <label>E-mail Address</label>
                                                <input class="form-control" required="true" value="<%=staffEntity.getEmail()%>" disabled/>
                                            </div>
                                            <div class="form-group">
                                                <label>Set Challenge Question</label>
                                                <select name="securityQuestion">
                                                    <%int securityQn = 0;
                                                        if (staffEntity.getSecurityQuestion() == null) {
                                                            securityQn = 0;
                                                        } else {
                                                            securityQn = staffEntity.getSecurityQuestion();
                                                        }%>
                                                    <option value="1" <%if (securityQn == 1) {
                                                            out.println("selected");
                                                        }%>>What is your mother's maiden name?</option>
                                                    <option value="2" <%if (securityQn == 2) {
                                                            out.println("selected");
                                                        }%>>What is your first pet's name?</option>
                                                    <option value="3" <%if (securityQn == 3) {
                                                            out.println("selected");
                                                        }%>>What is your favourite animal?</option>
                                                </select>
                                                        <input class="form-control" type="text" required="true" name="securityAnswer" value="<%if (staffEntity.getSecurityAnswer() == null || staffEntity.getSecurityAnswer().equals("null")) {
                                                        out.println("");
                                                    } else {
                                                        out.println(staffEntity.getSecurityAnswer());
                                                    }%>">
                                            </div>
                                            <div class="form-group">
                                                <label>Phone</label>
                                                <input class="form-control" required="true" type="text" name="phone" value="<%=staffEntity.getPhone()%>">
                                            </div>
                                            <div class="form-group">
                                                <label>Address</label>
                                                <input class="form-control" type="text" required="true" name="address" value="<%=staffEntity.getAddress()%>">
                                            </div>
                                            <hr class="more-spaced "/>
                                            <h4>Change Password</h4>
                                            <div class="form-group">
                                                <label>New Password (leave blank unless setting a new password).</label>
                                                <input class="form-control" type="password" name="password" id="password">
                                            </div>
                                            <div class="form-group">
                                                <label>Re-enter New Password</label>
                                                <input class="form-control" type="password"  name="repassword" id="repassword">
                                            </div>

                                            <div class="panel-footer" style="padding-bottom: 0px;">
                                                <div class="row">
                                                    <div class="form-group">
                                                        <input type="submit" value="Submit" class="btn btn-primary"/>
                                                        <input type="reset" value="Reset" class="btn btn-primary"/>
                                                    </div>
                                                    <input type="hidden" value="<%=staffEntity.getId()%>" name="id">
                                                    <input type="hidden" value="A1/staffProfile.jsp" name="source"/>
                                                    <input type="hidden" value="<%=staffEntity.getEmail()%>" name="email"/>
                                                </div>
                                            </div>
                                        </form>

                                    </div>
                                    <div id="roles" class="tab-pane">
                                        <h4>Position held in Island Furniture</h4>
                                        <ul>
                                            <%
                                                List<RoleEntity> roles = staffEntity.getRoles();
                                                for (int i = 0; i < roles.size(); i++) {
                                                    out.println("<li>" + roles.get(i).getName() + "</li>");
                                                }
                                            %>
                                        </ul>
                                    </div>
                                </div>

                            </div>


                        </div>
                    </div>

                </div>
                <!-- /#page-wrapper -->
            </div>
            <!-- /#wrapper -->

            <%                } catch (Exception ex) {
                    response.sendRedirect("../StaffManagement_StaffServlet");
                    ex.printStackTrace();
                }%>
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
