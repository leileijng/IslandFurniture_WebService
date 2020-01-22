<%@page import="EntityManager.LineItemEntity"%>
<%@page import="EntityManager.SalesRecordEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.MemberEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />
    <body>

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
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
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">

                            <h1 class="page-header">View Sales Record Details</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="memberManagement.jsp"> Member Management </a>                                 
                                </li>

                                <li class="active">
                                    <i class="icon icon-inbox"></i> <a href="memberManagement_viewSalesRecord.jsp?id=<%=salesRecord.getMember().getId()%>"> View Sales Record</a>                                 
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
                </div>
            </div>
            <!-- /.panel-body -->
        </form>
    </div>
    <script>
        $(document).ready(function () {
            $('#dataTables-example').dataTable();
        }
        );
    </script>
</body>
</html>
