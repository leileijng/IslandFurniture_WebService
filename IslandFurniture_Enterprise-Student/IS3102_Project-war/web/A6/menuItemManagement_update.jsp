<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.MenuItemEntity"%>
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
                                Menu Item Details Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-archive"></i><a href="../MenuItemManagement_MenuItemServlet"> Menu Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i>  Menu Item Details Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            String id = request.getParameter("id");
                            List<MenuItemEntity> menuItems = (List<MenuItemEntity>) (session.getAttribute("menuItemList"));
                            MenuItemEntity menuItem = new MenuItemEntity();
                            for (int i = 0; i < menuItems.size(); i++) {
                                if (menuItems.get(i).getId() == Integer.parseInt(id)) {
                                    menuItem = menuItems.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" method="POST" enctype="multipart/form-data" action="../MenuItemManagement_UpdateMenuItemServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name" type="text" value="<%=menuItem.getName()%>">
                                </div>
                                <div class="form-group">
                                    <label>Category</label>
                                    <input class="form-control" required="true" type="text" name="category" value="<%=menuItem.getCategory()%>">
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <input class="form-control" type="text"  name="description"required="true" value="<%=menuItem.getDescription()%>" >
                                </div>                           
                                <div class="form-group">
                                    <label>SKU</label>
                                    <input class="form-control" type="text" required="true" name="SKU" value="<%=menuItem.getSKU()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Length per item</label>
                                    <input class="form-control" type="text" required="true" name="length" value="<%=menuItem.getLength()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Width per item</label>
                                    <input class="form-control" type="text" required="true" name="width" value="<%=menuItem.getWidth()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>Height per item</label>
                                    <input class="form-control" type="text" required="true" name="height" value="<%=menuItem.getHeight()%>" disabled>
                                </div>
                                <div>
                                        <input type="file" name="javafile">
                                </div>
                                <br/>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=menuItem.getId()%>" name="id">
                                <input type="hidden" value="<%=menuItem.getName()%>" name="menuItemName">
                               <input type="hidden" value="<%=menuItem.getSKU()%>" name="SKU">

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
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
