<%@page import="EntityManager.SupplierEntity"%>
<%@page import="EntityManager.RetailProductEntity"%>
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
                                Retail Product Details Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-coffee"></i><a href="../RetailProductManagement_RetailProductServlet"> Retail Product Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Retail Product Details Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            String id = request.getParameter("id");
                            List<RetailProductEntity> retailProducts = (List<RetailProductEntity>) (session.getAttribute("retailProductList"));
                            RetailProductEntity retailProduct = new RetailProductEntity();
                            for (int i = 0; i < retailProducts.size(); i++) {
                                if (retailProducts.get(i).getId() == Integer.parseInt(id)) {
                                    retailProduct = retailProducts.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" method="POST" enctype="multipart/form-data" action="../RetailProductManagement_UpdateRetailProductServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name" type="text" value="<%=retailProduct.getName()%>">
                                </div>
                                <div class="form-group">
                                    <label>Category</label>
                                    <input class="form-control" required="true" type="text" name="category" value="<%=retailProduct.getCategory()%>">
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <input class="form-control" type="text"  name="description"required="true" value="<%=retailProduct.getDescription()%>" >
                                </div>                             
                                <div class="form-group">
                                    <label>SKU</label>
                                    <input class="form-control" type="text" required="true" name="SKU" value="<%=retailProduct.getSKU()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Length per item</label>
                                    <input class="form-control" type="text" required="true" name="length" value="<%=retailProduct.getHeight()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Width per item</label>
                                    <input class="form-control" type="text" required="true" name="width" value="<%=retailProduct.getWidth()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Height per item</label>
                                    <input class="form-control" type="text" required="true" name="height" value="<%=retailProduct.getWidth()%>" disabled>
                                </div>                              
                                <div>
                                    <input type="file" name="javafile">
                                </div><br/>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=retailProduct.getId()%>" name="id">
                                <input type="hidden" value="<%=retailProduct.getSKU()%>" name="SKU">
                                <input type="hidden" value="<%=retailProduct.getName()%>" name="retailProductName">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%} catch (Exception ex) {

                            //response.sendRedirect("../RetailProductManagement_RetailProductServlet");
                        }%>
                </div>

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
