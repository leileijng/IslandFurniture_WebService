<%@page import="EntityManager.StaffEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	
<html> <!--<![endif]-->
    <jsp:include page="../header1.html" />
    <body class="dark"> 

        <%
            String errMsg = request.getParameter("errMsg");
            if (errMsg == null || errMsg.equals("")) {
                errMsg = "";
            }
        %>

        <div role="main" class="main">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">  
                    <div class="featured-box featured-boxes.login" style="height: 100%;width:100%;margin-top: 100px;">
                        <div class="panel-heading"> 
                            <i class="icon-4x icon icon-unlock-alt"  style="margin-top: 10px;"></i><h6 class="panel-title">Forget Password?</h6>
                        </div>
                        <h10 class="text-info"><%=errMsg%></h10>
                        <div class="panel-body">
                            <%
                                StaffEntity staff = (StaffEntity) (session.getAttribute("staffForgetPassword"));
                            %>
                            <div class="container" style="width:100%;">
                                <jsp:include page="/displayMessage.jsp" />
                                <div class="row">
                                    <div class="col-md-12">

                                        <div class="row featured-boxes login">
                                            <div class="col-md-6" style="width:100%">
                                                <div class="featured-box featured-box-secundary default info-content" >
                                                    <div class="box-content" >
                                                        <h4>Forgot Password</h4>
                                                        <%
                                                            if (staff.getSecurityQuestion() != null) {%>
                                                        <h5>Security Challenge Question</h5>
                                                        <form action="/IS3102_Project-war/AccountManagement_SendResetPasswordServlet">
                                                            <div class="row">
                                                                <div class="form-group">
                                                                    <div class="col-md-12">
                                                                        <label>Security Question</label>
                                                                        <%
                                                                            if (staff.getSecurityQuestion() == 1) {
                                                                                out.println("What is your mother's maiden name?");
                                                                            } else if (staff.getSecurityQuestion() == 2) {
                                                                                out.println("What is your first pet's name?");
                                                                            } else if (staff.getSecurityQuestion() == 3) {
                                                                                out.println("What is your favourite animal?");
                                                                            }
                                                                        %>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="form-group">
                                                                    <div class="col-md-12">
                                                                        <label>Security Answer</label>
                                                                        <input type="text" name="securityAnswer" class="form-control input-lg" required>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <input type="submit" value="Submit" class="btn btn-primary pull-right push-bottom">
                                                                </div>
                                                                <input type="hidden" value="<%=staff.getEmail()%>" name="email"/>
                                                            </div>
                                                        </form>
                                                        <%} else {%>
                                                        <p>This account password cannot be reset online. Please contact support for assistance.</p>
                                                        <%}%>
                                                    </div>
                                                </div>
                                            </div>                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
