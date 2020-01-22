<%@page import="EntityManager.SupplierEntity"%>
<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.Supplier_ItemEntity"%>
<%@page import="java.util.ArrayList"%>
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
            function add() {
                document.supplierItemInfoManagement_add.action = "../SupplierItemInfoManagement_AddSupplierItemInfoServlet";
                document.supplierItemInfoManagement_add.submit();
            }
            function update(id) {
                supplierItemInfoManagement.id.value = id;
                document.supplierItemInfoManagement.action = "supplierItemInfoManagement_update.jsp";
                document.supplierItemInfoManagement.submit();
            }
            function removeRecord() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = false;
                    document.supplierItemInfoManagement.action = "../SupplierItemInfoManagement_Servlet";
                    document.supplierItemInfoManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.supplierItemInfoManagement.action = "../SupplierItemInfoManagement_RemoveSupplierItemInfoServlet";
                    document.supplierItemInfoManagement.submit();

                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Supplier Item Info Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="supplierManagement.jsp">Supplier Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-truck"></i>&nbsp;Supplier Item Info Management
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
                                    if (errMsg == null || errMsg.equals("")) {
                                        errMsg = "Welcome to Supplier Item Info Management";
                                    }
                                    out.println(errMsg);
                                    %>                                  
                                </div>
                                <!-- /.panel-heading -->
                                <form name="supplierItemInfoManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary btnAdd" name="btnAdd" type="button" value="Add Supplier Item Info" />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Record</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th style=""><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th style="">ID</th>
                                                            <th style="">Supplier Name</th>
                                                            <th style="">Supplier Address</th>
                                                            <th style="">SKU</th>
                                                            <th style="">Item Name</th>
                                                            <th style="">Cost Price</th>
                                                            <th style="">Lot Size</th>
                                                            <th style="">Lead Time (Days)</th>
                                                            <th style="">Update</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<Supplier_ItemEntity> listOfSupplierItemInfo = (List<Supplier_ItemEntity>) (session.getAttribute("listOfSupplierItemInfo"));
                                                            try {
                                                                if (listOfSupplierItemInfo != null) {
                                                                    for (int i = 0; i < listOfSupplierItemInfo.size(); i++) {

                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=listOfSupplierItemInfo.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getSupplier().getId()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getSupplier().getSupplierName()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getSupplier().getAddress()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getItem().getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getItem().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getCostPrice()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getLotSize()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfSupplierItemInfo.get(i).getLeadTime()%>
                                                            </td>
                                                            <td><input class="btn btn-primary" id="btnUpdate" onclick="update(<%=listOfSupplierItemInfo.get(i).getId()%>)" type="button" value="Update"/></td>
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
                                                    <input class="btn btn-primary btnAdd" name="btnAdd" type="button" value="Add Supplier Item Info"/>
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Record</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">  
                                            <input type="hidden" name="setPrice" value="">
                                        </div>
                                    </div>
                                    <!-- /.panel-body -->
                                </form>
                                <form name="supplierItemInfoManagement_add" onsubmit="add()">
                                    <div id="addItemPricingForm" hidden>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-3" style="padding-left: 30px"><br>
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                Supplier ID:&nbsp;
                                                            </td>
                                                            <td>
                                                                <select class="form-control" name="supplierId"> 
                                                                    <%
                                                                        List<SupplierEntity> listOfSuppliers = (List<SupplierEntity>) session.getAttribute("suppliers");
                                                                        for (SupplierEntity s : listOfSuppliers) {
                                                                            out.print("<option value=\"" + s.getId() + "\">ID " + s.getId() + ": " + s.getSupplierName() + "</option>");
                                                                        }
                                                                    %>
                                                                </select>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                SKU:&nbsp;
                                                            </td>
                                                            <td>
                                                                <input title="To see the list of items, please go to Corporate Management->Item Management" id="auto" class="form-control" name="sku" type="text" required/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Cost Price<br/> (Per Lot):&nbsp;
                                                            </td>
                                                            <td>
                                                                <input type="number" step="any" class="form-control" name="costPrice" required/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Lot Size:&nbsp;
                                                            </td>
                                                            <td>
                                                                <input type="number" class="form-control" name="lotSize" required/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Lead Time (Days):&nbsp;
                                                            </td>
                                                            <td>
                                                                <input type="number" class="form-control" name="leadTime" required/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <br>
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
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
                        <p id="messageBox">Record will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeRecord()"  />
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

            $(".btnAdd").click(function () {
                $("html, body").animate({scrollTop: $(document).height()}, "slow");
                $("#addItemPricingForm").show("slow", function () {
                });
            });
            $(function () {
                var array1 = [];
                $.get('../SKU_ajax_servlet/*', function (responseText) {
                    var arr = responseText.trim().split(';');
                    arr.pop();
                    for (var i = 0; i < arr.length; i++) {
                        array1.push(arr[i]);
                    }
                });

                $("#auto").autocomplete({source: array1});
                $(function () {
                    $(document).tooltip();
                });
            });

        </script>

        <style>
            label {
                display: inline-block;
                width: 5em;
            }
        </style>
    </body>

</html>
