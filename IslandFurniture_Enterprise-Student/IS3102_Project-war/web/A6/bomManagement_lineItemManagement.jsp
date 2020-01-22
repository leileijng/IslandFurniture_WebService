<%@page import="EntityManager.RawMaterialEntity"%>
<%@page import="EntityManager.LineItemEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.FurnitureEntity"%>
<%@page import="EntityManager.BillOfMaterialEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function addLineItem() {
                document.lineItemManagement_Add.action = "../BomManagement_AddLineItemBomServlet";
                document.lineItemManagement_Add.submit();
            }
            function removeLineItem() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.lineItemManagement.submit();
                } else {

                    window.event.returnValue = true;
                    document.lineItemManagement.action = "../BomManagement_RemoveLineItemBomServlet";
                    document.lineItemManagement.submit();

                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Line Item Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-sitemap"></i>  <a href="../BomManagement_BomServlet">Bill of Material Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-calendar"></i>&nbsp;Line Item Management
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
                                            out.println("Add BOM Line Item");
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
                                <form name="lineItemManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary btnAdd" name="btnAdd" type="button" value="Add Line Item" />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Line Item</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th style="width:5%"><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th style="width:30%">Raw Material</th>
                                                            <th style="width:15%">Quantity</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<LineItemEntity> listOfLineItem = (List<LineItemEntity>) (session.getAttribute("bomListLineOfItems"));
                                                            try {
                                                                if (listOfLineItem != null) {
                                                                    for (int i = 0; i < listOfLineItem.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=listOfLineItem.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=listOfLineItem.get(i).getItem().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfLineItem.get(i).getQuantity()%>
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
                                                    <input class="btn btn-primary btnAdd" name="btnAdd" type="button" value="Add Line Item"/>
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Line Item</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value=""> 
                                            <input type="hidden" name="bomId" value="<%=session.getAttribute("bomId")%>"/>   

                                        </div>
                                    </div>           
                                </form>

                                <form name="lineItemManagement_Add" onsubmit="addLineItem()">
                                    <div id="addLineItemForm" hidden>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-3" style="padding-left: 30px"><br>
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                Raw Material SKU:&nbsp;
                                                            </td>
                                                            <td>
                                                                <input id="auto" class="form-control" name="sku" type="text" required/>
                                                            </td> 
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Quantity:&nbsp;
                                                            </td>
                                                            <td>
                                                                <input type ="number" class="form-control" name="qty" required/><br>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add" />
                                                    <input type="hidden" name="bomId" value="<%=session.getAttribute("bomId")%>"/>   
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.panel-body -->
                                </form>
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
                            <p id="messageBox">Line Item will be removed. Are you sure?</p>
                        </div>
                        <div class="modal-footer">                        
                            <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeLineItem()"  />
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

                $(".btnAdd").click(function() {
                    $("html, body").animate({scrollTop: $(document).height()}, "slow");
                    $("#addLineItemForm").show("slow", function() {
                    });
                });
                $(function() {
                    var array1 = [];
                    $.get('../SKU_ajax_servlet/*', function(responseText) {
                        var arr = responseText.trim().split(';');
                        arr.pop();
                        for (var i = 0; i < arr.length; i++) {
                            array1.push(arr[i]);
                        }
                    });

                    $("#auto").autocomplete({source: array1});
                });
            </script>

    </body>

</html>
