<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Restaurant Management</h1>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row featured-boxes">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <div class="featured-box featured-box-primary">
                                    <div class="box-content">
                                        <a href="../RawIngredientManagement_RawIngredientServlet"><i class="icon-featured icon icon-align-center"> </i>
                                            <h4>Raw Ingredient Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-secundary">
                                    <div class="box-content">
                                        <a href="../MenuItemManagement_MenuItemServlet"><i class="icon-featured icon icon-archive"> </i>
                                            <h4>Menu Item Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="featured-box featured-box-tertiary">
                                    <div class="box-content">
                                        <a href="../ComboManagement_ComboServlet"><i class="icon-featured icon icon-coffee"> </i>
                                            <h4>Combo Management</h4>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                           
                            <div class="col-md-4">
                                <div class="featured-box featured-box-quartenary">
                                    <div class="box-content">
                                        <a href="../RecipeManagement_RecipeServlet"><i class="icon-featured icon icon-sitemap"> </i>
                                            <h4>Recipe Management</h4>
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
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>
    </body>
</html>
