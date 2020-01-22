<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Operational CRM</h1>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row featured-boxes">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <div class="featured-box featured-box-primary">
                                    <div class="box-content">
                                        <a href="../LoyaltyManagement_Servlet"><i class="icon-featured icon icon-user"> </i>
                                            <h4>Loyalty Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-secundary">
                                    <div class="box-content">
                                        <a href="../A4/customerServiceManagement.jsp"><i class="icon-featured icon icon-user"> </i>
                                            <h4>Customer Service</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-secundary">
                                    <div class="box-content">
                                        <a href="../PromotionalSalesManagement_Servlet"><i class="icon-featured icon icon-user"> </i>
                                            <h4>Promotional Sales</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                        
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
