<%@page import="EntityManager.SupplierEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.RetailProductEntity"%>
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
                                Add Retail Product
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-coffee"></i><a href="../RetailProductManagement_RetailProductServlet"> Retail Product Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Retail Product
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />
                    <!-- /.warning -->
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" method="POST" enctype="multipart/form-data" action="../RetailProductManagement_AddRetailProductServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" name="name"  type="text" required="true">
                                </div>
                                <div class="form-group">
                                    <label>Category</label>
                                    <input class="form-control" required="true" type="text" name="category" >
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <input class="form-control" required="true" type="text" name="description">
                                </div>
                                <div class="form-group">
                                    <label>SKU</label>
                                    <input class="form-control" type="text" required="true" name="SKU" >
                                </div>
                                <div class="form-group">
                                    <label>Length per item</label>
                                    <input class="form-control" type="number" required="true" min="1" step="1" name="length" >
                                </div>
                                <div class="form-group">
                                    <label>Width per item</label>
                                    <input class="form-control" type="number" required="true" min="1" step="1" name="width" >
                                </div>
                                <div class="form-group">
                                    <label>Height per item</label>
                                    <input class="form-control" type="number" required="true" min="1" step="1" name="height" >
                                </div>
                                <div>
                                    <input type="file" name="javafile">
                                </div>
                                <br/>
                                <div class="form-group">
                                    <input type="submit" value="Add" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="A6/retailProductManagement_add.jsp" name="source">
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->

    </body>

</html>