<%@page import="EntityManager.ItemEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.LineItemEntity"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<html lang="en">
    <jsp:include page="../header2.html" />

    <body>

        <script>

            function sendLoyaltyPoints() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.rfm.action = "../Analytical_ValueAnalysisServlet";
                    document.rfm.submit();
                } else {
                    window.event.returnValue = true;
                    document.rfm.action = "../Analytical_ValueAnalysisSendLoyaltyServlet";
                    document.rfm.submit();
                }
            }

            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }

            <%
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            %>
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Value Analysis</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="analytical.jsp">Analytical CRM</a>
                                </li>
                                <li>
                                    <i class="icon icon-user"></i> <a href="../Analytical_ValueAnalysisServlet">Value Analysis</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> Products Analysis
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
                                    Furniture Analysis
                                </div>
                                <!-- /.panel-heading -->
                                <form name="rfm">

                                    <%
                                        List<LineItemEntity> sortBestSellingFurniture = (List<LineItemEntity>) (session.getAttribute("sortBestSellingFurniture"));
                                        List<LineItemEntity> sortBestSellingFurniture1Year = (List<LineItemEntity>) (session.getAttribute("sortBestSellingFurniture1Year"));
                                        List<LineItemEntity> listOfSecondProduct = (List<LineItemEntity>) (session.getAttribute("listOfSecondProduct"));
                                        List<Date> dateOfLastPurchaseFurniture = (List<Date>) (session.getAttribute("dateOfLastPurchaseFurniture"));

                                        DecimalFormat df = new DecimalFormat("#.##");
                                    %>
                                    <!-- /.table-responsive -->

                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div id="regions_div" style="width: 900px; height: 500px;"></div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-lg-12">

                                            <div class="panel-body">
                                                <div class="table-responsive">

                                                    <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" member="grid">
                                                        <table class="table table-bordered" id="dataTables-example">
                                                            <thead>
                                                                <tr>
                                                                    <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                                    <th>Name</th>
                                                                    <th>Quantity Sold</th>
                                                                    <th>Last 365 Days</th>
                                                                    <th>Last Purchase</th>
                                                                    <th>Items Purchased With</th>
                                                                    <th>Probability</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    if (sortBestSellingFurniture != null) {
                                                                        for (int i = 0; i < sortBestSellingFurniture.size(); i++) {
                                                                            LineItemEntity item = sortBestSellingFurniture.get(i);
                                                                            ItemEntity itemEntity = item.getItem();
                                                                %>
                                                                <tr>                   
                                                                    <td>
                                                                        <input type="checkbox" name="delete" value="<%=item.getId()%>" />
                                                                    </td>
                                                                    <td>
                                                                        <%=itemEntity.getName()%>
                                                                    </td>
                                                                    <td >
                                                                        <%=item.getQuantity()%>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            out.print(sortBestSellingFurniture1Year.get(i).getQuantity());
                                                                        %>

                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            if (dateOfLastPurchaseFurniture.get(i) != null) {
                                                                                out.print(dateFormat.format(dateOfLastPurchaseFurniture.get(i)));
                                                                            }%>
                                                                    </td>
                                                                    <td>
                                                                        <%

                                                                            if (listOfSecondProduct.get(i).getItem() != null) {
                                                                                out.println(listOfSecondProduct.get(i).getItem().getName() + " (" + listOfSecondProduct.get(i).getQuantity() + ")");

                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            out.println(df.format((double) listOfSecondProduct.get(i).getQuantity() / (double) item.getQuantity()));
                                                                        %> 

                                                                    </td>

                                                                </tr>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->

                                                    <input type="hidden" name="id" value="">    
                                                </div>
                                            </div>   
                                        </div>
                                    </div>
                                    <input type="hidden" name="id" value="">    
                                    </div>
                                    </div>
                                    <!-- /.panel-body -->
                                </form>
                            </div>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            Retail Products Analysis
                                        </div>
                                        <!-- /.panel-heading -->
                                        <form name="rfm">

                                            <%
                                                List<LineItemEntity> sortBestSellingRetailProducts = (List<LineItemEntity>) (session.getAttribute("sortBestSellingRetailProducts"));
                                                List<LineItemEntity> listOfSecondProductRP = (List<LineItemEntity>) (session.getAttribute("listOfSecondProductRP"));
                                                List<LineItemEntity> sortBestSellingRetailProduct1Year = (List<LineItemEntity>) (session.getAttribute("sortBestSellingRetailProduct1Year"));
                                                List<Date> dateOfLastPurchaseRetailProduct = (List<Date>) (session.getAttribute("dateOfLastPurchaseRetailProduct"));
                                            %>
                                            <!-- /.table-responsive -->

                                            <div class="row">
                                                <div class="col-lg-12">

                                                    <div class="panel-body">
                                                        <div class="table-responsive">



                                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" member="grid">
                                                                <table class="table table-bordered" id="dataTables-example2">
                                                                    <thead>
                                                                        <tr>
                                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                                            <th>Name</th>
                                                                            <th>Quantity Sold</th>
                                                                            <th>Last 365 Days</th>
                                                                            <th>Last Purchase</th>
                                                                            <th>Items Purchased With</th>
                                                                            <th>Probability</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <%
                                                                            if (sortBestSellingRetailProducts != null) {
                                                                                for (int i = 0; i < sortBestSellingRetailProducts.size(); i++) {
                                                                                    LineItemEntity item = sortBestSellingRetailProducts.get(i);
                                                                                    ItemEntity itemEntity = item.getItem();
                                                                        %>
                                                                        <tr>                   
                                                                            <td>
                                                                                <input type="checkbox" name="delete" value="<%=item.getId()%>" />
                                                                            </td>
                                                                            <td>
                                                                                <%=itemEntity.getName()%>
                                                                            </td>
                                                                            <td >
                                                                                <%=item.getQuantity()%>
                                                                            </td>
                                                                            <td><%=sortBestSellingRetailProduct1Year.get(i).getQuantity()%></td>
                                                                            <td>
                                                                                <% if (dateOfLastPurchaseRetailProduct.get(i) != null) {
                                                                                         out.print(dateFormat.format(dateOfLastPurchaseRetailProduct.get(i)));
                                                                                     }%>
                                                                            </td>
                                                                            <td>
                                                                                <%

                                                                                    if (listOfSecondProductRP.get(i).getItem() != null) {
                                                                                        out.println(listOfSecondProductRP.get(i).getItem().getName() + " (" + listOfSecondProductRP.get(i).getQuantity() + ")");

                                                                                    }
                                                                                %>
                                                                            </td>
                                                                            <td>
                                                                                <%
                                                                                    out.print(df.format((double) listOfSecondProductRP.get(i).getQuantity() / (double) item.getQuantity()));
                                                                                %>

                                                                            </td>

                                                                        </tr>
                                                                        <%
                                                                                }
                                                                            }
                                                                        %>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                            <!-- /.table-responsive -->

                                                            <input type="hidden" name="id" value="">    
                                                        </div>
                                                    </div>   
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                            </div>
                                            </div>
                                            <!-- /.panel-body -->
                                        </form>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    Menu Items Analysis
                                                </div>
                                                <!-- /.panel-heading -->
                                                <form name="rfm">

                                                    <%
                                                        List<LineItemEntity> sortBestSellingMenuItem = (List<LineItemEntity>) (session.getAttribute("sortBestSellingMenuItem"));
                                                        List<LineItemEntity> listOfSecondProductMenuItem = (List<LineItemEntity>) (session.getAttribute("listOfSecondProductMenuItem"));
                                                        List<LineItemEntity> sortBestSellingMenuItem1Year = (List<LineItemEntity>) (session.getAttribute("sortBestSellingMenuItem1Year"));

                                                        List<Date> dateOfLastPurchaseMenuItem = (List<Date>) (session.getAttribute("dateOfLastPurchaseMenuItem"));
                                                    %>
                                                    <!-- /.table-responsive -->

                                                    <div class="row">
                                                        <div class="col-lg-12">

                                                            <div class="panel-body">
                                                                <div class="table-responsive">



                                                                    <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" member="grid">
                                                                        <table class="table table-bordered" id="dataTables-example3">
                                                                            <thead>
                                                                                <tr>
                                                                                    <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                                                    <th>Name</th>
                                                                                    <th>Quantity Sold</th>
                                                                                    <th>Last 365 Days</th>
                                                                                    <th>Last Purchase</th>
                                                                                    <th>Items Purchased With</th>
                                                                                    <th>Probability</th>
                                                                                </tr>
                                                                            </thead>
                                                                            <tbody>
                                                                                <%
                                                                                    if (sortBestSellingMenuItem != null) {
                                                                                        for (int i = 0; i < sortBestSellingMenuItem.size(); i++) {

                                                                                            LineItemEntity item = sortBestSellingMenuItem.get(i);
                                                                                            ItemEntity itemEntity = item.getItem();
                                                                                %>
                                                                                <tr>                   
                                                                                    <td>
                                                                                        <input type="checkbox" name="delete" value="<%=item.getId()%>" />
                                                                                    </td>
                                                                                    <td>
                                                                                        <%=itemEntity.getName()%>
                                                                                    </td>
                                                                                    <td >
                                                                                        <%=item.getQuantity()%>
                                                                                    </td>
                                                                                    <td>
                                                                                        <%=sortBestSellingMenuItem1Year.get(i).getQuantity()%>
                                                                                    </td>
                                                                                    <td>
                                                                                        <%
                                                                                            if (dateOfLastPurchaseRetailProduct.get(i) != null) {
                                                                                                out.print(dateFormat.format(dateOfLastPurchaseRetailProduct.get(i)));
                                                                                     }%>
                                                                                    </td>
                                                                                    <td>
                                                                                        <%

                                                                                            if (listOfSecondProductMenuItem.get(i).getItem() != null) {
                                                                                                out.println(listOfSecondProductMenuItem.get(i).getItem().getName() + " (" + listOfSecondProductMenuItem.get(i).getQuantity() + ")");

                                                                                            }
                                                                                        %>
                                                                                    </td>
                                                                                    <td>
                                                                                        <%
                                                                                            out.print(df.format((double) listOfSecondProductMenuItem.get(i).getQuantity() / (double) item.getQuantity()));
                                                                                        %>

                                                                                    </td>

                                                                                </tr>
                                                                                <%
                                                                                        }
                                                                                    }
                                                                                %>
                                                                            </tbody>
                                                                        </table>
                                                                    </div>
                                                                    <!-- /.table-responsive -->

                                                                    <input type="hidden" name="id" value="">    
                                                                </div>
                                                            </div>   
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
                                        <p id="messageBox">Enter Loyalty Amount : 
                                            <input type="number" name="loyaltyPoints"></p>
                                    </div>
                                    <div class="modal-footer">                        
                                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="sendLoyaltyPoints()"  />
                                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
                        <script>
                            $(document).ready(function() {
                                $('#dataTables-example').dataTable();
                                $('#dataTables-example2').dataTable();
                                $('#dataTables-example3').dataTable();
                            });
                        </script>
                        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
                        <script type="text/javascript">
                            google.load("visualization", "1", {packages: ["geochart"]});
                            google.setOnLoadCallback(drawRegionsMap);
                            <%
                                Integer totalNumberOfFurnitureInSG = (Integer) session.getAttribute("totalNumberOfFurnitureInSG");
                                Integer totalNumberOfFurnitureInMS = (Integer) session.getAttribute("totalNumberOfFurnitureInMS");
                                Integer totalNumberOfFurnitureInID = (Integer) session.getAttribute("totalNumberOfFurnitureInID");
                                Integer getTotalRetailProductsSoldInSG = (Integer) session.getAttribute("getTotalRetailProductsSoldInSG");
                                Integer getTotalRetailProductsSoldInMS = (Integer) session.getAttribute("getTotalRetailProductsSoldInSG");
                                Integer getTotalRetailProductsSoldInID = (Integer) session.getAttribute("totalNumberOfFurnitureInID");

                            %>
                            function drawRegionsMap() {

                                var data = google.visualization.arrayToDataTable([
                                    ['Country', 'Furniture Sold', 'Retail Products Sold'],
                                    ['Singapore', <%=totalNumberOfFurnitureInSG%>, <%=getTotalRetailProductsSoldInSG%>],
                                    ['Malaysia', <%=totalNumberOfFurnitureInMS%>, <%=getTotalRetailProductsSoldInMS%>],
                                    ['Indonesia', <%=totalNumberOfFurnitureInID%>, <%=getTotalRetailProductsSoldInID%>]
                                ]);

                                var options = {
                                    region: '035'
                                };

                                var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));

                                chart.draw(data, options);
                            }
                        </script>

                        </body>
                        </html>
