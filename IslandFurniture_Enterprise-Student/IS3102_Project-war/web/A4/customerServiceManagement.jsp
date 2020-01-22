<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Customer Service</h1>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row featured-boxes">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <div class="featured-box featured-box-primary">
                                    <div class="box-content">
                                        <a href="../SalesRecordManagement_Servlet"><i class="icon-featured icon icon-align-center"> </i>
                                            <h4>Sales Record Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-secundary">
                                    <div class="box-content">
                                        <a target="_blank" href="../PickerJobList_Servlet"><i class="icon-featured icon icon-archive"> </i>
                                            <h4>Picking Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-tertiary">
                                    <div class="box-content">
                                        <a href="../FeedbackManagement_Servlet"><i class="icon-featured icon icon-fire"> </i>
                                            <h4>Feedback Management</h4>
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
