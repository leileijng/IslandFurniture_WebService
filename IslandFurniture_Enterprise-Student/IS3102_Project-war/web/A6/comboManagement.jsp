<%@page import="EntityManager.ComboLineItemEntity"%>
<%@page import="EntityManager.ComboEntity"%>
<%@page import="EntityManager.ProductGroupLineItemEntity"%>
<%@page import="EntityManager.ProductGroupEntity"%>
<%@page import="java.util.List"%>

<%
    List<ComboEntity> combos = (List<ComboEntity>) (session.getAttribute("combos"));
    if (combos == null) {
        response.sendRedirect("../ComboManagement_ComboServlet");
    } else {
%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateCombo(id) {
                comboManagement.id.value = id;
                document.comboManagement.action = "comboManagement_Update.jsp";
                document.comboManagement.submit();
            }
            function addCombo() {
                window.event.returnValue = true;
                document.comboManagement.action = "comboManagement_Add.jsp";
                document.comboManagement.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function removeCombo() {

                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.comboManagement.action = "../ComboManagement_ComboServlet";
                    document.comboManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.comboManagement.action = "../ComboManagement_RemoveServlet";
                    document.comboManagement.submit();
                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                try {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Combo Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-cogs"></i> Combo Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        String errMsg = request.getParameter("errMsg");
                                        String goodMsg = request.getParameter("goodMsg");
                                        if (errMsg == null && goodMsg == null) {
                                            out.println("Add Combo");
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
                                <form name="comboManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Combo" onclick="addCombo()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Combo</button></a>
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Combo</th>
                                                            <th>Description</th>
                                                            <th>SKU</th>
                                                            <th>Image URL</th>
                                                            <th>Menu Item SKUs</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            if (combos != null) {
                                                                for (int i = 0; i < combos.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=combos.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=combos.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=combos.get(i).getDescription()%>
                                                            </td>
                                                            <td>
                                                                <%=combos.get(i).getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=combos.get(i).getImageURL()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    List<ComboLineItemEntity> lineItems = combos.get(i).getLineItemList();
                                                                    for (int k = 0; k < lineItems.size(); k++) {
                                                                        if (k == lineItems.size() - 1) {
                                                                            out.println(lineItems.get(k).getMenuItem().getSKU());
                                                                        } else {
                                                                            out.println(lineItems.get(k).getMenuItem().getSKU() + " , ");
                                                                        }
                                                                    }
                                                                %>
                                                            </td>                                                           
                                                            <td style="width:200px">
                                                                <input  type="button" name="btnEdit" class="btn btn-primary btn-block"  value="Update" onclick="javascript:updateCombo('<%=combos.get(i).getId()%>')"/>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                    }
                                                                }
                                                            } catch (Exception ex) {
                                                                response.sendRedirect("../A1/workspace.jsp");
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Combo" onclick="addCombo()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Combo</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                            <input type="hidden" name="source" value="">   
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
                        <p id="messageBox">Combo will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeCombo()"  />
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
<%}%>