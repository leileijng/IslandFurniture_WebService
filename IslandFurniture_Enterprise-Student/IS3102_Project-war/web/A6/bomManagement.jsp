<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.FurnitureEntity"%>
<%@page import="EntityManager.BillOfMaterialEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function lineItem(id) {
                bomManagement.id.value = id;
                document.bomManagement.action = "../BomManagement_LineItemBomServlet";
                document.bomManagement.submit();
            }
            function linkFurniture(id) {

                window.event.returnValue = true;
                bomManagement.id.value = id;
                document.bomManagement.action = "../BomManagement_LinkBomServlet";
                document.bomManagement.submit();


            }
            function updateBOM(id) {
                bomManagement.id.value = id;
                document.bomManagement.action = "bomManagement_update.jsp";
                document.bomManagement.submit();
            }
            function removeBOM() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.bomManagement.action = "../BomManagement_BomServlet";
                    document.bomManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.bomManagement.action = "../BomManagement_RemoveBomServlet";
                    document.bomManagement.submit();
                }
            }
            function addBOM() {
                window.event.returnValue = true;
                document.bomManagement.action = "bomManagement_add.jsp";
                document.bomManagement.submit();
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
                            <h1 class="page-header">Bill of Materials Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-sitemap"></i> Bill of Material Management
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
                                        out.println("Add or remove Bill of Materials");
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
                                <form name="bomManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add BOM" onclick="addBOM()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove BOM</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th style="width:5%"><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th style="width:15%">BOM Name</th>
                                                            <th style="width:25%">Furniture</th>
                                                            <th style="width:35%">Description</th>
                                                            <th style="width:10%">Line Item(s)</th>
                                                            <th style="width:10%">Edit BOM</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<FurnitureEntity> listOfFurniture = (List<FurnitureEntity>) (session.getAttribute("listOfFurniture"));
                                                            List<BillOfMaterialEntity> listOfBOM = (List<BillOfMaterialEntity>) (session.getAttribute("listOfBOM"));
//                                                            ArrayList<FurnitureEntity> arrList = new ArrayList();
//                                                            for (FurnitureEntity f : listOfFurniture) {
//                                                               if (f.getBOM() == null) {
//                                                                    arrList.add(f);
//                                                                }
//                                                            }
//                                                            listOfFurniture = arrList;
                                                            try {
                                                                if (listOfBOM != null) {
                                                                    for (int i = 0; i < listOfBOM.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=listOfBOM.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=listOfBOM.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    FurnitureEntity f = listOfBOM.get(i).getFurniture();
                                                                    String furnitureName = null;
                                                                    if (f != null) {
                                                                        furnitureName = f.getName();
                                                                        out.print(furnitureName);
                                                                    } else {
                                                                %>
                                                                <select class="form-inline" name="furnitureId<%=listOfBOM.get(i).getId()%>">
                                                                    <option value="">Select</option>
                                                                    <%
                                                                        for (FurnitureEntity furniture : listOfFurniture) {
                                                                            out.print("<option value=\"" + furniture.getId() + "\">");
                                                                            out.print(furniture.getName());
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
                                                                                <p id="messageBox">The furniture will be linked to the BOM. Are you sure?</p>
                                                                            </div>
                                                                            <div class="modal-footer">                        
                                                                                <input class="btn btn-primary" name="btnLink" type="submit" value="Confirm" onclick="javascript:linkFurniture('<%=listOfBOM.get(i).getId()%>')" />
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
                                                                <%=listOfBOM.get(i).getDescription()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnLineItem" class="btn btn-primary btn-block" value="Manage" onclick="javascript:lineItem('<%=listOfBOM.get(i).getId()%>')"/>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" value="Edit" onclick="javascript:updateBOM('<%=listOfBOM.get(i).getId()%>')"/>
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add BOM" onclick="addBOM()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove BOM</button></a>
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
                        <p id="messageBox">BOM will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeBOM()"  />
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
