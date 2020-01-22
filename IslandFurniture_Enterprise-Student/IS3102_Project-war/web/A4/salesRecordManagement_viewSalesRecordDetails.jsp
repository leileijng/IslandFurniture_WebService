<%@page import="EntityManager.LineItemEntity"%>
<%@page import="EntityManager.SalesRecordEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.MemberEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
        <%
            try {
                String id = (String) (session.getAttribute("id"));
                List<SalesRecordEntity> salesRecords = (List<SalesRecordEntity>) (session.getAttribute("salesRecords"));
                SalesRecordEntity salesRecord = new SalesRecordEntity();
                for (int i = 0; i < salesRecords.size(); i++) {
                    if (salesRecords.get(i).getId() == Integer.parseInt(id)) {
                        salesRecord = salesRecords.get(i);
                    }
                }
        %>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">

                            <h1 class="page-header">View Sales Record Details</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li>
                                    <i class="icon icon-user"></i> <a href="customerServiceManagement.jsp"> Customer Service </a>                                 
                                </li>
                                <li class="active">
                                    <i class="icon icon-align-center"></i> <a href="salesRecordManagement_View.jsp?id=<%=salesRecord.getStore().getId()%>">View Sales Record</a>                                 
                                </li>
                                <li class="active">
                                    <i class="icon icon-envelope"></i> View Sales Record Details</a>                                 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        out.println("Display sales records details");
                                    %>
                                </div>
                                <form name="viewSalesRecordDetails">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>               
                                                            <th>Item SKU</th>
                                                            <th>Item Name</th>
                                                            <th>Quantity</th>                                                              
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%   List<LineItemEntity> lineItems = salesRecord.getItemsPurchased();
                                                            for (int i = 0; i < lineItems.size(); i++) {%>
                                                        <tr>
                                                            <td>
                                                                <%=lineItems.get(i).getItem().getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=lineItems.get(i).getItem().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=lineItems.get(i).getQuantity()%>
                                                            </td>                                       
                                                        </tr>
                                                    </tbody>
                                            </div>
                                        </div>
                                        <%}
                                            } catch (Exception ex) {
                                                response.sendRedirect("../MemberManagement_MemberServlet");
                                            }
                                        %>
                                    </div>
                                    </table>
                            </div>
                        </div>
                    </div>
                    </form>
                </div>
            </div>
            <!-- /.panel-body -->

        </div>
    </div>
</div>
</div>
<script>
    $(document).ready(function () {
        $('#dataTables-example').dataTable();
    }
    );
</script>
</body>
</html>
