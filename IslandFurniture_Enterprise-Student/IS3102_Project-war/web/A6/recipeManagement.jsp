<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.MenuItemEntity"%>
<%@page import="EntityManager.RecipeEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function lineItem(id) {
                recipeManagement.id.value = id;
                document.recipeManagement.action = "../RecipeManagement_LineItemRecipeServlet";
                document.recipeManagement.submit();
            }
            function linkMenuItem(id) {

                window.event.returnValue = true;
                recipeManagement.id.value = id;
                document.recipeManagement.action = "../RecipeManagement_LinkRecipeServlet";
                document.recipeManagement.submit();

            }
            function updateRecipe(id) {
                recipeManagement.id.value = id;
                document.recipeManagement.action = "recipeManagement_update.jsp";
                document.recipeManagement.submit();
            }
            function removeRecipe() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.recipeManagement.action = "../RecipeManagement_RecipeServlet";
                    document.recipeManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.recipeManagement.action = "../RecipeManagement_RemoveRecipeServlet";
                    document.recipeManagement.submit();
                }
            }
            function addRecipe() {
                window.event.returnValue = true;
                document.recipeManagement.action = "recipeManagement_add.jsp";
                document.recipeManagement.submit();
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
                            <h1 class="page-header">Recipe Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-sitemap"></i> Recipe Management
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
                                        out.println("Add or remove recipe");
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
                                <form name="recipeManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Recipe" onclick="addRecipe()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Recipe</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th style="width:5%"><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th style="width:15%">Recipe Name</th>
                                                            <th style="width:25%">Menu Item</th>
                                                            <th style="width:35%">Description</th>
                                                            <th style="width:10%">Line Item(s)</th>
                                                            <th style="width:10%">Edit Recipe</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<MenuItemEntity> listOfMenuItem = (List<MenuItemEntity>) (session.getAttribute("listOfMenuItem"));
                                                            List<RecipeEntity> listOfRecipe = (List<RecipeEntity>) (session.getAttribute("listOfRecipe"));
//                                                            ArrayList<MenuItemEntity> arrList = new ArrayList();
//                                                            for (MenuItemEntity f : listOfMenuItem) {
//                                                               if (f.getRecipe() == null) {
//                                                                    arrList.add(f);
//                                                                }
//                                                            }
//                                                            listOfMenuItem = arrList;
                                                            try {
                                                                if (listOfRecipe != null) {
                                                                    for (int i = 0; i < listOfRecipe.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=listOfRecipe.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=listOfRecipe.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    MenuItemEntity f = listOfRecipe.get(i).getMenuItem();
                                                                    String menuItemName = null;
                                                                    if (f != null) {
                                                                        menuItemName = f.getName();
                                                                        out.print(menuItemName);
                                                                    } else {
                                                                %>
                                                                <select class="form-inline" name="menuItemId<%=listOfRecipe.get(i).getId()%>">
                                                                    <option value="">Select</option>
                                                                    <%
                                                                        for (MenuItemEntity menuItem : listOfMenuItem) {
                                                                            out.print("<option value=\"" + menuItem.getId() + "\">");
                                                                            out.print(menuItem.getName());
                                                                            out.print("</option>");
                                                                        }
                                                                    %>
                                                                </select>
                                                                <a href="#UpdateLink" data-toggle="modal"><button class="btn btn-primary btn-block" style="width:30%;height:30px;float:right;">Link</button></a>
                                                                <div role="dialog" class="modal fade" id="UpdateLink">
                                                                    <div class="modal-dialog">
                                                                        <div class="modal-content">
                                                                            <div class="modal-header">
                                                                                <h4>Alert</h4>
                                                                            </div>
                                                                            <div class="modal-body">
                                                                                <p id="messageBox">The menu item will be linked to the BOM. Are you sure?</p>
                                                                            </div>
                                                                            <div class="modal-footer">                        
                                                                                <input class="btn btn-primary" name="btnLink" type="submit" value="Confirm" onclick="javascript:linkMenuItem('<%=listOfRecipe.get(i).getId()%>')" />
                                                                                <a class="btn btn-default" data-dismiss ="modal">Close</a>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>                                                               
                                                                <%
                                                                    }
                                                                %>
                                                            </td>
                                                            <td>
                                                                <%=listOfRecipe.get(i).getDescription()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnLineItem" class="btn btn-primary btn-block" value="Manage" onclick="javascript:lineItem('<%=listOfRecipe.get(i).getId()%>')"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" value="Edit" onclick="javascript:updateRecipe('<%=listOfRecipe.get(i).getId()%>')"/>
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Recipe" onclick="addRecipe()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Recipe</button></a>
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
                        <p id="messageBox">Recipe will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeRecipe()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
