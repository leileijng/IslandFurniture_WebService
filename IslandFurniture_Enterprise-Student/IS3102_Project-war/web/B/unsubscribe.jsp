<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@page import="net.tanesha.recaptcha.ReCaptchaImpl"%>
<html> <!--<![endif]-->
    <jsp:include page="/B/header.html" />
    <body>

        <jsp:include page="menu2.jsp" />
        <div role="main" class="main">
            <section class="page-top">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <h2>Unsubscribe</h2>
                        </div>
                    </div>
                </div>
            </section>
            <div class="container">
                <jsp:include page="/displayMessageLong.jsp" />
                <br/>
                <div class="row">
                    <div class="col-md-12">
                        You will be redirected to our home page in 5 seconds . . .
                        <script type="text/JavaScript">
                            <!--
                            setTimeout("location.href = 'index.jsp';",5000);
                            -->
                        </script>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="footer.html" />

    </body>
</html>
