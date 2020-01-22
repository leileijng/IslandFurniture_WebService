<%@page import="EntityManager.ProductGroupLineItemEntity"%>
<%@page import="EntityManager.ProductGroupEntity"%>
<%@page import="java.util.List"%>


<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updatePG(id) {
                productGroupManagement.id.value = id;
                document.productGroupManagement.action = "productGroupManagement_Update.jsp";
                document.productGroupManagement.submit();
            }
            function addPG() {
                window.event.returnValue = true;
                document.productGroupManagement.action = "productGroupManagement_Add.jsp";
                document.productGroupManagement.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function removePG() {

                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.productGroupManagement.action = "../ProductGroupManagement_Servlet";
                    document.productGroupManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.productGroupManagement.action = "../ProductGroupManagement_RemoveServlet";
                    document.productGroupManagement.submit();
                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                List<ProductGroupEntity> productGroups = (List<ProductGroupEntity>) (session.getAttribute("productGroups"));
                if (productGroups == null) {
                    response.sendRedirect("../ProductGroupManagement_Servlet");
                } else {
                    try {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Product Group Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-cogs"></i> Product Group Management
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
                                            out.println("Add Product Group");
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
                                <form name="productGroupManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Product Group" onclick="addPG()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Product Group</button></a>
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Product Group</th>
                                                            <th>Work Hours</th>
                                                            <th>Item SKUs</th>
                                                            <th>Lot Size</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            if (productGroups != null) {
                                                                for (int i = 0; i < productGroups.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=productGroups.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=productGroups.get(i).getProductGroupName()%>
                                                            </td>
                                                            <td>
                                                                <%=productGroups.get(i).getWorkHours()%>
                                                            </td>                                                          
                                                            <td>
                                                                <%
                                                                    List<ProductGroupLineItemEntity> lineItems = productGroups.get(i).getLineItemList();
                                                                    for (int k = 0; k < lineItems.size(); k++) {
                                                                        if (k == lineItems.size() - 1) {
                                                                            out.println(lineItems.get(k).getItem().getSKU());
                                                                        } else {
                                                                            out.println(lineItems.get(k).getItem().getSKU() + " , ");
                                                                        }
                                                                    }
                                                                %>
                                                            </td>
                                                            <td>
                                                                <%=productGroups.get(i).getLotSize()%>
                                                            </td>
                                                            <td style="width:200px">
                                                                <input  type="button" name="btnEdit" class="btn btn-primary btn-block"  value="Update" onclick="javascript:updatePG('<%=productGroups.get(i).getId()%>')"/>
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Product Group" onclick="addPG()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Product Group</button></a>
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
                        <p id="messageBox">Product Group will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removePG()"  />
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
<%}%>