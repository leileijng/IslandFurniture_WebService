<%@page import="EntityManager.RawMaterialEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateRawMaterial(id) {
                rawMaterialManagement.id.value = id;
                document.rawMaterialManagement.action = "rawMaterialManagement_update.jsp";
                document.rawMaterialManagement.submit();
            }
            function removeRawMaterial() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.rawMaterialManagement.action = "../RawMaterialManagement_RawMaterialServlet";
                    document.rawMaterialManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.rawMaterialManagement.action = "../RawMaterialManagement_RemoveRawMaterialServlet";
                    document.rawMaterialManagement.submit();
                }
            }
            function addRawMaterial() {
                window.event.returnValue = true;
                document.rawMaterialManagement.action = "rawMaterialManagement_add.jsp";
                document.rawMaterialManagement.submit();
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
                            <h1 class="page-header">Raw Material Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-align-center"></i> Raw Material Management
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
                                            out.println("Add and Delete Raw Materials");
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
                                <form name="rawMaterialManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Raw Material" onclick="addRawMaterial()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Raw Material</button></a>
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
                                                            List<RawMaterialEntity> rawMaterials = (List<RawMaterialEntity>) (session.getAttribute("rawMaterialList"));

                                                            try {
                                                                if (rawMaterials != null) {
                                                                    for (int i = 0; i < rawMaterials.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=rawMaterials.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getCategory()%>
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getDescription()%>
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getLength()%>
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getWidth()%>
                                                            </td>
                                                            <td>
                                                                <%=rawMaterials.get(i).getHeight()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=rawMaterials.get(i).getId()%>" value="Update" onclick="javascript:updateRawMaterial('<%=rawMaterials.get(i).getId()%>')"/>
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Raw Material" onclick="addRawMaterial()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Raw Material</button></a>
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
                        <p id="messageBox">Raw material will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeRawMaterial()"  />
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
