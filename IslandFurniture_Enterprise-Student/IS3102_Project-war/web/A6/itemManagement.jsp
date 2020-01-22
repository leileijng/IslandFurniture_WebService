<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Item Management</h1>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row featured-boxes">
                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-5">
                                    <div class="featured-box featured-box-primary">
                                        <div class="box-content">
                                            <a href="../RawMaterialManagement_RawMaterialServlet"><i class="icon-featured icon icon-align-center"> </i>
                                                <h4>Raw Material Management</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <div class="featured-box featured-box-secundary">
                                        <div class="box-content">
                                            <a href="../FurnitureManagement_FurnitureServlet"><i class="icon-featured icon icon-archive"> </i>
                                                <h4>Furniture Management</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-5">
                                    <div class="featured-box featured-box-tertiary">
                                        <div class="box-content">
                                            <a href="../RetailProductManagement_RetailProductServlet"><i class="icon-featured icon icon-coffee"> </i>
                                                <h4>Retail Product Management</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-5">
                                    <div class="featured-box featured-box-sixenary">
                                        <div class="box-content">
                                            <a href="../CountryItemPricingManagement_Servlet"><i class="icon-featured icon icon-dollar"> </i>
                                                <h4>Item Pricing Management</h4>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- <div class="col-md-12">
 
                             <div class="col-md-4">
                                 <div class="featured-box featured-box-quartenary">
                                     <div class="box-content">
                                         <a href="../BomManagement_BomServlet"><i class="icon-featured icon icon-sitemap"> </i>
                                             <h4>Bill of Material Management</h4>
                                         </a>
                                     </div>
                                 </div>
                             </div>
                             <div class="col-md-4">
                                 <div class="featured-box featured-box-fiveneary">
                                     <div class="box-content">
                                         <a href="../ProductGroupManagement_Servlet"><i class="icon-featured icon icon-cogs"> </i>
                                             <h4>Product Group Management</h4>
                                         </a>
                                     </div>
                                 </div>
                             </div>
                         </div>-->
                    </div>
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
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
