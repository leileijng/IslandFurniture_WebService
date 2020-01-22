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
                    <div class="featured-box featured-boxes.login" style="height: auto;margin-top: 100px;">
                        <div class="panel-heading"> 
                            <i class="icon-4x icon icon-unlock-alt"  style="margin-top: 10px;"></i><h6 class="panel-title">Forget Password?</h6>
                        </div>
                        <h10 class="text-info"><%=errMsg%></h10>
                        <div class="panel-body">
                            <form role="form" name="LoginForm" action="../AccountManagement_SecurityChallengeServlet">
                            <!--form role="form" name="LoginForm" action="../AccountManagement_SendResetPasswordServlet"-->

                                <div class="row">
                                    <div class="form-group">
                                        <div class="col-md-12">
                                            <label>Enter Your E-mail Address</label>
                                            <input type="email" name="email" id="email" class="form-control input-lg" required="true">
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-12">
                                        <input type="submit" onclick="validateForm()" value="Send Email" class="btn btn-lg btn-primary btn-block" data-loading-text="Loading...">
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
