<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Facility Management
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> Facility Management
                                </li>                                                             
                            </ol>
                        </div>
                    </div>
                    <div class="row featured-boxes">
                        <div class="col-md-12">
                            <div class="col-md-3">
                                <div class="featured-box featured-box-primary">
                                    <div class="box-content">
                                        <a href="../FacilityManagement_RegionalOfficeServlet/regionalOfficeManagement_index"><i class="icon-featured icon icon-globe"> </i>
                                            <h4>Regional <br>Office</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-3">
                                <div class="featured-box featured-box-secundary">
                                    <div class="box-content">
                                        <a href="../FacilityManagement_StoreServlet/storeManagement_index"><i class="icon-featured icon icon-home"> </i>
                                            <h4>Store <br>Facility</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="featured-box featured-box-tertiary">
                                    <div class="box-content">
                                        <a href="../FacilityManagement_ManufacturingFacilityServlet/manufacturingFacilityManagement_index"><i class="icon-featured icon icon-cogs"> </i>
                                            <h4>Manufacturing <br>Facility</h4>    
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="featured-box featured-box-sixenary">
                                    <div class="box-content">
                                        <a href="../FacilityManagement_Servlet/warehouseManagement_index"><i class="icon-featured icon icon-archive"> </i>
                                            <h4>Warehouse <br>Facility</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>
    </body>
</html>
