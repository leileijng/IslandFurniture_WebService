<%@page import="java.util.Date"%>
<%@page import="EntityManager.MasterProductionScheduleEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="EntityManager.StorageBinEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <style>
        select{
            min-width: 100px;
        }
    </style>
    <body>
        <script>
            function updateStorageBin(id) {
                storagebinManagement.id.value = id;
                document.storagebinManagement.action = "storageBinManagement_Update.jsp";
                document.storagebinManagement.submit();
            }
            function removeStorageBin() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.storagebinManagement.action = "../StorageBinManagement_Servlet";
                    document.storagebinManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.storagebinManagement.action = "../StorageBinManagement_RemoveServlet";
                    document.storagebinManagement.submit();
                }
            }
            function addStorageBin() {
                window.event.returnValue = true;
                document.storagebinManagement.action = "storageBinManagement_Add.jsp";
                document.storagebinManagement.submit();
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
            <% WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
                if (warehouseEntity == null) {
                    response.sendRedirect("../ManufacturingWarehouseManagement_Servlet");
                } else {
                    try {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Storage Bin Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i> <a href="manufacturingWarehouseManagement_view.jsp">Manufacturing Warehouse Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-home"></i> <a href="../ManufacturingWarehouseManagement_Servlet?destination=manufacturingWarehouseManagement.jsp&id=<%=warehouseEntity.getId()%>"><%=warehouseEntity.getWarehouseName()%></a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-archive"></i> Storage Bin Management
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
                                            out.println("Add and remove storage bins");
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
                                <form name="storagebinManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Storage Bin" onclick="addStorageBin()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Storage Bin</button></a>
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Name</th>
                                                            <th>Type</th>
                                                            <th>Length</th>
                                                            <th>Width</th>
                                                            <th>Height</th>
                                                            <th>Total Volume</th>
                                                            <th>Avaliable Volume</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<StorageBinEntity> storageBins = (List<StorageBinEntity>) (session.getAttribute("storageBins"));
                                                            if (storageBins != null) {
                                                                for (int i = 0; i < storageBins.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=storageBins.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getType()%>
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getLength()%>
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getWidth()%>
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getHeight()%>
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getVolume()%>
                                                            </td>
                                                            <td>
                                                                <%=storageBins.get(i).getFreeVolume()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    int volume = storageBins.get(i).getVolume();
                                                                    int freeVolume = storageBins.get(i).getFreeVolume();
                                                                    if (volume == freeVolume) {
                                                                %>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=storageBins.get(i).getId()%>" value="Update" onclick="javascript:updateStorageBin('<%=storageBins.get(i).getId()%>')"/>
                                                                <%} else {%>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block"  value="Update" disabled/>
                                                                <%}%>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                    }
                                                                }
                                                            } catch (Exception ex) {
                                                                response.sendRedirect("manufacturingWarehouseManagement.jsp");
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Storage Bin" onclick="addStorageBin()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Storage Bin</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                        </div>
                                    </div>
                                    <!-- /.panel-body -->
                                </form>

                            </div>


                            <script>
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
                                });
                            </script>
                            <div class="row">
                                <div class="col-md-4">

                                    <div class="panel-heading">
                                        <h3 style="display: inline">
                                            Add Item to Bin
                                        </h3>
                                    </div>

                                    <form name="addItemToBin" action="../StorageBinManagement_AddItemToBinServlet">

                                        <div id="addItemToBinForm">
                                            <div class="row">
                                                <div class="form-group">
                                                    <div class="col-md-3" style="padding-left: 30px"><br>
                                                        <table>
                                                            <tr>
                                                                <td>
                                                                    Storage Bin:&nbsp;
                                                                </td>
                                                                <td>
                                                                    <select class="form-control" name="storageBinID"> 
                                                                        <%
                                                                            List<StorageBinEntity> storageBins = (List<StorageBinEntity>) (session.getAttribute("storageBins"));
                                                                            for (StorageBinEntity s : storageBins) {
                                                                                Long storageBinID = s.getId();
                                                                                String storageBinName = s.getName();
                                                                                out.print("<option value=\"" + storageBinID + "\">Bin Id " + storageBinID + ": " + storageBinName + "</option>");
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    Item SKU:&nbsp;
                                                                </td>
                                                                <td>
                                                                    <input id="auto" required class="form-control" name="SKU" type="text"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    Quantity:&nbsp;
                                                                </td>
                                                                <td>
                                                                    <input type="number" min="1" max="3000" required  class="form-control" name="qty">
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <br>
                                                        <input class="btn btn-primary" type="submit" value="Add to Bin"/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>

                                <div class="col-md-8">
                                    <h3>
                                        Goods Receipt
                                    </h3>
                                    <table class="table table-striped table-bordered table-hover" id="dataTables-example1">
                                        <thead>
                                            <tr>
                                                <th>SKU</th>
                                                <th>Product Name</th>
                                                <th>Quantity</th>                                                                                       
                                            </tr>
                                        </thead>
                                        <tbody>

                                            <%
                                                Integer week = (Integer) session.getAttribute("week");
                                                List<MasterProductionScheduleEntity> listOfMPS = (List<MasterProductionScheduleEntity>) session.getAttribute("listOfMPS");
                                                int quantity = 0;
                                                for (MasterProductionScheduleEntity mps : listOfMPS) {

                                                    switch (week) {
                                                        case 1:
                                                            if (mps.getSchedule().getWorkDays_firstWeek() != 0) {
                                                                quantity = mps.getAmount_week1() / mps.getSchedule().getWorkDays_firstWeek();
                                                            }
                                                        case 2:
                                                            quantity = mps.getAmount_week2() / mps.getSchedule().getWorkDays_secondWeek();
                                                        case 3:
                                                            quantity = mps.getAmount_week3() / mps.getSchedule().getWorkDays_thirdWeek();
                                                        case 4:
                                                            quantity = mps.getAmount_week4() / mps.getSchedule().getWorkDays_forthWeek();
                                                        case 5:
                                                            quantity = mps.getAmount_week5() / mps.getSchedule().getWorkDays_fifthWeek();

                                                        default:
                                                    }
                                            %>    
                                            <tr>`
                                                <td>
                                                    <%=mps.getFurniture().getSKU()%>
                                                </td>
                                                <td>
                                                    <%= mps.getFurniture().getName()%>
                                                </td>
                                                <td>
                                                    <%=quantity%>
                                                </td>                                                
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
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
                        <p id="messageBox">Storage bin will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeStorageBin()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
                $('#dataTables-example1').dataTable();
            });
        </script>

    </body>

</html>
<%}%>