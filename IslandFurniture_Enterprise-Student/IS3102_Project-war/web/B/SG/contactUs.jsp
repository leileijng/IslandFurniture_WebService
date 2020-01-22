<%@page import="EntityManager.FurnitureEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.RetailProductEntity"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="checkCountry.jsp" />
<html> <!--<![endif]-->


    <jsp:include page="header.html" />
    <body>
        <div class="body">
            <jsp:include page="menu2.jsp" />
            <div class="body">
                <div role="main" class="main">
                    <section class="page-top">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12">
                                    <ul class="breadcrumb">
                                        <li><a href="#">Home</a></li>
                                        <li class="active">Contact Us</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <h2>Contact Us</h2>
                                </div>
                            </div>
                        </div>
                    </section>

                    <div class="container">
                        <jsp:include page="/displayMessageLong.jsp" />

                        <div class="row">
                            <div class="col-md-6">

                                <h2 class="short"><strong>Contact</strong> Us</h2>
                                <form role="form" action="../../ECommerce_ContactUsServlet">
                                    <div class="row">
                                        <div class="form-group">
                                            <div class="col-md-6">
                                                <label>Your name *</label>
                                                <input class="form-control" required="true" name="name" type="text">
                                            </div>
                                            <div class="col-md-6">
                                                <label>Your email address *</label>
                                                <input class="form-control" required="true" name="email" type="email">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group">
                                            <div class="col-md-12">
                                                <label>Subject</label>
                                                <input class="form-control" required="true" name="subject" type="text">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group">
                                            <div class="col-md-12">
                                                <label>Message *</label>
                                                <textarea maxlength="5000" data-msg-required="Please enter your message." rows="10" class="form-control" name="message" id="message" required></textarea>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <input type="submit" value="Send Message" class="btn btn-primary btn-lg" data-loading-text="Loading...">
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col-md-6">

                                <h4 class="push-top">Get in <strong>touch</strong></h4>
                                <p>Contact us for more information!</p>
                                <hr />
                                <h4>Business <strong>Hours</strong></h4>
                                <ul class="list-unstyled">
                                    <li><i class="fa fa-time"></i> Monday - Friday 9am to 5pm</li>
                                    <li><i class="fa fa-time"></i> Saturday - 9am to 2pm</li>
                                    <li><i class="fa fa-time"></i> Sunday - Closed</li>
                                </ul>

                            </div>

                        </div>

                    </div>

                </div>

            </div>
            <jsp:include page="footer.html" />
        </div>
        <!-- Theme Initializer -->
        <script src="../../js/theme.plugins.js"></script>
        <script src="../../js/theme.js"></script>

        <!-- Current Page JS -->
        <script src="../../vendor/rs-plugin/js/jquery.themepunch.tools.min.js"></script>
        <script src="../../vendor/rs-plugin/js/jquery.themepunch.revolution.js"></script>
        <script src="../../vendor/circle-flip-slideshow/js/jquery.flipshow.js"></script>
        <script src="../../js/views/view.home.js"></script>   
    </div>

    <script>
                                                $(document).ready(function () {
                                                    $('#dataTables-example').dataTable();
                                                });
    </script>
</body>
</html>
