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
            String msg = request.getParameter("msg");
            if (msg == null || msg.equals("")) {
                msg = "";
            } else {
                msg +="<br/><br/>";
            }
        %>
        <div role="main" class="main">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="featured-box featured-boxes.login" style="height: auto;margin-top: 100px;">
                        <div class="panel-body">
                            <form role="form" name="registrationForm" action="../AccountManagement_ActivateServlet">
                                <div class="box-content">
                                    <h3>Staff Account Activation</h3>
                                    <%=msg%>
                                    <div class="row">
                                        <div class="form-group">
                                            <div class="col-md-12">
                                                <label>Registered E-mail Address</label>
                                                <input type="email" value="" name="email" class="form-control input-lg" required="true">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group">
                                            <div class="col-md-12">
                                                <label>Activation Code</label>
                                                <input type="text" name="activationCode" class="form-control input-lg" required="true">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <input type="hidden" name="accountType" value="staff"/>
                                            <input type="submit" value="Submit" class="btn btn-lg btn-primary btn-block">
                                        </div>
                                    </div>
                                    <input type="hidden" value="A1/staffActivateAccount.jsp" name="source">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
