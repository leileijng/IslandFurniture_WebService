<%@page import="EntityManager.RecipeEntity"%>
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
                                Recipe Details Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-sitemap"></i><a href="../RecipeManagement_RecipeServlet"> Recipe Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Recipe Details Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            String id = request.getParameter("id");
                            List<RecipeEntity> listOfRecipe = (List<RecipeEntity>) (session.getAttribute("listOfRecipe"));
                            RecipeEntity recipe = new RecipeEntity();
                            for (int i = 0; i < listOfRecipe.size(); i++) {
                                if (listOfRecipe.get(i).getId() == Integer.parseInt(id)) {
                                    recipe = listOfRecipe.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../RecipeManagement_UpdateRecipeServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name" type="text" value="<%=recipe.getName()%>">
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <input class="form-control" type="text"  name="description" required="true" value="<%=recipe.getDescription()%>" >
                                </div>
                                <div class="form-group">
                                    <label>Board Lot Size</label>
                                    <input class="form-control" required="true" type="number" name="lotSize" value="<%=recipe.getBroadLotSize() %>" >
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=recipe.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%} catch (Exception ex) {
                            //response.sendRedirect("../MenuItemManagement_MenuItemServlet");
                        }%>
                </div>

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
