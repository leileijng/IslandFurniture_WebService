<%@page import="EntityManager.MenuItemEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateMenuItem(id) {
                menuItemManagement.id.value = id;
                document.menuItemManagement.action = "menuItemManagement_update.jsp";
                document.menuItemManagement.submit();
            }
            function removeMenuItem() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.menuItemManagement.action = "../MenuItemManagement_MenuItemServlet";
                    document.menuItemManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.menuItemManagement.action = "../MenuItemManagement_RemoveMenuItemServlet";
                    document.menuItemManagement.submit();
                }
            }
            function addMenuItem() {
                window.event.returnValue = true;
                document.menuItemManagement.action = "menuItemManagement_add.jsp";
                document.menuItemManagement.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Menu Item Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-archive"></i> Menu Item Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading"> <%

                                    String errMsg = request.getParameter("errMsg");
                                    String goodMsg = request.getParameter("goodMsg");
                                    if (errMsg == null && goodMsg == null) {
                                        out.println("Add or remove menu item");
                                    } else if ((errMsg != null) && (goodMsg == null)) {
                                        if (!errMsg.equals("")) {
                                            out.println(errMsg);
                                        }
                                    } else if ((errMsg == null && goodMsg != null)) {
                                        if (!goodMsg.equals("")) {
                                            out.println(goodMsg);
                                        }
                                    }
                                    %>                                  
                                </div>
                                <!-- /.panel-heading -->
                                <form name="menuItemManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Menu Item" onclick="addMenuItem()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Menu Item</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Name</th>
                                                            <th>Category</th>
                                                            <th>Description</th>
                                                            <th>Image URL</th>
                                                            <th>SKU</th>
                                                            <th>Length</th>
                                                            <th>Width</th>
                                                            <th>Height</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<MenuItemEntity> menuItems = (List<MenuItemEntity>) (session.getAttribute("menuItemList"));

                                                            try {
                                                                if (menuItems != null) {
                                                                    for (int i = 0; i < menuItems.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=menuItems.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getCategory()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getDescription()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getImageURL()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getLength()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getWidth()%>
                                                            </td>
                                                            <td>
                                                                <%=menuItems.get(i).getHeight()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=menuItems.get(i).getId()%>" value="Update" onclick="javascript:updateMenuItem('<%=menuItems.get(i).getId()%>')"/>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                    }
                                                                }
                                                            } catch (Exception ex) {
                                                                System.out.println(ex);
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Menu Item" onclick="addMenuItem()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Menu Item</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                        </div>

                                    </div>
                                    <!-- /.panel-body -->
                                </form>

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->


                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->

        <div role="dialog" class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Menu Item will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeMenuItem()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
