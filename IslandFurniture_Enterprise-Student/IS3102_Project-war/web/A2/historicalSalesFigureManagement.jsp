<%@page import="EntityManager.SalesFigureEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function addSF() {
                window.event.returnValue = true;
                document.salesFiguresManagement.action = "historicalSalesFigureManagement_add.jsp";
                document.salesFiguresManagement.submit();
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                List<SalesFigureEntity> salesFigures = (List<SalesFigureEntity>) (session.getAttribute("salesFigures"));
                if (salesFigures == null) {
                    response.sendRedirect("../HistoricalSalesFigureManagement_Servlet");
                } else {
                    try {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Historical Sales Figures Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-archive"></i> Historical Sales Figures Management
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
                                            out.println("Create Sales Figures");
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
                                <form name="salesFiguresManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Sales Forecast Figure" onclick="addSF()"  />
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th>Month</th>
                                                            <th>Quantity</th>
                                                            <th>Store</th>
                                                            <th>SKU</th>
                                                            <th>Item Name</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            if (salesFigures != null) {
                                                                for (int i = 0; i < salesFigures.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <%=salesFigures.get(i).getMonth()%>
                                                            </td>
                                                            <td>
                                                                <%=salesFigures.get(i).getQuantity()%>
                                                            </td>
                                                            <td>
                                                                <%=salesFigures.get(i).getStore().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=salesFigures.get(i).getItem().getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=salesFigures.get(i).getItem().getName()%>
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Sales Forecast Figure" onclick="addSF()"  />
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


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
<%}%>