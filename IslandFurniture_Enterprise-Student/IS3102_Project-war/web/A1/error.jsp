<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	
<html> <!--<![endif]-->
    <jsp:include page="../header1.html" />
    <%
        String errMsg = request.getParameter("errMsg");
        if (errMsg == null || errMsg.equals("")) {
            errMsg = "";
        }
    %>
    <body class="dark">
        <div role="main" class="main">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="featured-box featured-boxes.login" style="height: auto;margin-top: 100px;">
                        <div class="panel-body">
                            <div class="box-content">
                                <h3>Internal Server Error</h3>
                            </div>
                            <%=errMsg%><br/><br/>
                            Please contact tech support if you were not expecting this.
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
