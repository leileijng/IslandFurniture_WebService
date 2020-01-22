<%@page import="EntityManager.ComboLineItemEntity"%>
<%@page import="EntityManager.ComboEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function addComboLineItem(id) {
                comboManagement.id.value = id;
                document.comboManagement.action = "comboManagement_AddLineItem.jsp";
                document.comboManagement.submit();
            }

            function removeComboLineItem() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                window.event.returnValue = true;
                document.comboManagement.action = "../ComboLineItemManagement_RemoveServlet";
                document.comboManagement.submit();
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
            <%
    List<ComboEntity> combos = (List<ComboEntity>) (session.getAttribute("combos"));
    String id = request.getParameter("id");
    if (combos == null || id == null) {
        response.sendRedirect("../ComboManagement_ComboServlet");
    } else {
        ComboEntity combo = new ComboEntity();
        for (int i = 0; i < combos.size(); i++) {
            if (combos.get(i).getId() == Integer.parseInt(id)) {
                combo = combos.get(i);
            }
        }

%>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Combo
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>

                                <li>
                                    <i class="icon icon-cogs"></i> <a href="../ComboManagement_ComboServlet">Combo Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Combo - Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />


                    <div class="row">
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">  Combo ID: <%=combo.getId()%></h3> 
                                </div>
                                <div class="panel-body">
                                    <form role="form" method="POST" enctype="multipart/form-data" action="../ComboManagement_UpdateServlet">
                                        <div class="form-group">
                                            <label>SKU: </label>
                                            <input class="form-control"  type="text" required="true" value="<%=combo.getSKU()%>" disabled/>
                                        </div>
                                        <div class="form-group">
                                            <label>Name: </label>
                                            <input class="form-control"  type="text" required="true" name="name" value="<%=combo.getName()%>">
                                        </div>
                                        <div class="form-group">
                                            <label>Description: </label>
                                            <input class="form-control"  type="text" required="true" name="description" value="<%=combo.getDescription()%>">
                                        </div>
                                        <div>
                                            <input type="file" name="javafile">
                                        </div>
                                        <br/>

                                        <div class="form-group">
                                            <input type="submit" value="Update Combo" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=combo.getId()%>" name="id">
                                        <input type="hidden" value="<%=combo.getSKU()%>" name="sku">
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Combo item Management</h3>
                                </div>
                                <!-- /.panel-heading -->
                                <form name="comboManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <input  type="button" name="btnAddLineItem" class="btn btn-primary" value="Add Combo Item" onclick="javascript:addComboLineItem('<%=combo.getId()%>')"/>
                                                        <a href="#removeLineItem" data-toggle="modal"><button class="btn btn-primary">Remove Combo Item</button></a>
                                                    </div>
                                                </div>
                                                <br>
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>SKU</th>
                                                            <th>Menu Item Name</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<ComboLineItemEntity> lineItems = combo.getLineItemList();
                                                            if (lineItems != null) {
                                                                for (int i = 0; i < lineItems.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=lineItems.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=lineItems.get(i).getMenuItem().getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=lineItems.get(i).getMenuItem().getName()%>
                                                            </td>                                                                                            
                                                        </tr>
                                                        <%}
                                                            }%>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <input  type="button" name="btnAddLineItem" class="btn btn-primary" value="Add Combo Item" onclick="javascript:addComboLineItem('<%=combo.getId()%>')"/>
                                                <a href="#removeLineItem" data-toggle="modal"><button class="btn btn-primary">Remove Combo Item</button></a>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="hidden" value="<%=combo.getId()%>" name="id">
                                    <input type="hidden" name="lineItemId">
                                    <input type="hidden" name="sku">
                                </form>
                            </div>
                        </div>
                    </div> 
                    <!-- /.row for line item management -->



                </div>
            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->

        <div role="dialog" class="modal fade" id="removeLineItem">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Combo Item will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeComboLineItem()"  />
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
