<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Create Combo
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-cogs"></i> <a href="../ComboManagement_ComboServlet">Combo Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Create Combo
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />


                    <div class="row">
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">  Combo</h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" method="POST" enctype="multipart/form-data" action="../ComboManagement_AddServlet">
                                        <div class="form-group">
                                            <label>SKU: </label>
                                            <input class="form-control" name="sku" type="text" required="true"/>
                                        </div>
                                        <div class="form-group">
                                            <label>Combo Name: </label>
                                            <input class="form-control" name="name" type="text" required="true"/>
                                        </div>
                                        <div class="form-group">
                                            <label>Description: </label>
                                            <input class="form-control" name="description" type="text" required="true"/>
                                        </div>
                                        <div>
                                            <input type="file" name="javafile">
                                        </div>
                                        <br/>
                                        <div class="form-group">
                                            <input type="submit" value="Create Combo" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
    </body>
</html>
