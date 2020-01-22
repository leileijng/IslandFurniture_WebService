<%@page import="EntityManager.RawIngredientEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateRawIngredient(id) {
                rawIngredientManagement.id.value = id;
                document.rawIngredientManagement.action = "rawIngredientManagement_update.jsp";
                document.rawIngredientManagement.submit();
            }
            function removeRawIngredient() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.rawIngredientManagement.action = "../RawIngredientManagement_RawIngredientServlet";
                    document.rawIngredientManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.rawIngredientManagement.action = "../RawIngredientManagement_RemoveRawIngredientServlet";
                    document.rawIngredientManagement.submit();
                }
            }
            function addRawIngredient() {
                window.event.returnValue = true;
                document.rawIngredientManagement.action = "rawIngredientManagement_add.jsp";
                document.rawIngredientManagement.submit();
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
                            <h1 class="page-header">Raw Ingredient Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-align-center"></i> Raw Ingredient Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">                                    <%
                                          String errMsg = request.getParameter("errMsg");
                                        String goodMsg = request.getParameter("goodMsg");
                                        if (errMsg == null && goodMsg == null) {
                                            out.println("Add and Delete Raw Ingredients");
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
                                <form name="rawIngredientManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Raw Ingredient" onclick="addRawIngredient()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Raw Ingredient</button></a>
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
                                                            <th>SKU</th>
                                                            <th>Length</th>
                                                            <th>Width</th>
                                                            <th>Height</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<RawIngredientEntity> rawIngredients = (List<RawIngredientEntity>) (session.getAttribute("rawIngredientList"));

                                                            try {
                                                                if (rawIngredients != null) {
                                                                    for (int i = 0; i < rawIngredients.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=rawIngredients.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getCategory()%>
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getDescription()%>
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getLength()%>
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getWidth()%>
                                                            </td>
                                                            <td>
                                                                <%=rawIngredients.get(i).getHeight()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=rawIngredients.get(i).getId()%>" value="Update" onclick="javascript:updateRawIngredient('<%=rawIngredients.get(i).getId()%>')"/>
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Raw Ingredient" onclick="addRawIngredient()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Raw Ingredient</button></a>
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
                        <p id="messageBox">Raw ingredient will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeRawIngredient()"  />
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
