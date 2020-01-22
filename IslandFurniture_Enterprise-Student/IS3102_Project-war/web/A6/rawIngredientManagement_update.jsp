<%@page import="EntityManager.RawIngredientEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.SupplierEntity"%>

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
                                Raw Ingredient Details Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-align-center"></i><a href="../RawIngredientManagement_RawIngredientServlet"> Raw Ingredient Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Raw Ingredient Details Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            String id = request.getParameter("id");
                            List<RawIngredientEntity> rawIngredients = (List<RawIngredientEntity>) (session.getAttribute("rawIngredientList"));
                            RawIngredientEntity rawIngredient = new RawIngredientEntity();
                            for (int i = 0; i < rawIngredients.size(); i++) {
                                if (rawIngredients.get(i).getId() == Integer.parseInt(id)) {
                                    rawIngredient = rawIngredients.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../RawIngredientManagement_UpdateRawIngredientServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name" type="text" value="<%=rawIngredient.getName()%>">
                                </div>
                                <div class="form-group">
                                    <label>Category</label>
                                    <input class="form-control"  type="text" name="category" value="<%=rawIngredient.getCategory()%>">
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <input class="form-control" type="text"  name="description" required="true" value="<%=rawIngredient.getDescription()%>" >
                                </div>
                                <div class="form-group">
                                    <label>SKU</label>
                                    <input class="form-control" type="text"  name="SKU" required="true" value="<%=rawIngredient.getSKU()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Length per item</label>
                                    <input class="form-control" type="text"  name="length" required="true" value="<%=rawIngredient.getLength()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Width per item</label>
                                    <input class="form-control" type="text"  name="width" required="true" value="<%=rawIngredient.getWidth()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Height per item</label>
                                    <input class="form-control" type="text"  name="height" required="true" value="<%=rawIngredient.getHeight()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=rawIngredient.getId()%>" name="id">
                                <input type="hidden" value="<%=rawIngredient.getSKU()%>" name="SKU">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%} catch (Exception ex) {

                            //response.sendRedirect("../RawIngredientManagement_RawIngredientServlet");
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