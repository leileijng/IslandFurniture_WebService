<%@page import="EntityManager.SalesRecordEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.MemberEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
        <script>
            function viewSalesRecordDetails(id) {
                viewSalesRecord.id.value = id;
                document.viewSalesRecord.action = "../MemberManagement_SalesRecordServlet";
                document.viewSalesRecord.submit();
            }
        </script>

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">View Sales Record</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="memberManagement.jsp"> Member Management </a>                                 
                                </li>
                                <li class="active">
                                    <i class="icon icon-inbox"></i> View Sales Record</a>                                 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        out.println("Display sales records of a member");
                                    %>
                                </div>
                                <form name="viewSalesRecord">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>               
                                                            <th>Sales Record Id</th>
                                                            <th>Receipt No</th>
                                                            <th>Created Date</th>     
                                                            <th>Store</th>
                                                            <th>Amount Due</th>
                                                            <th>Amount Paid</th>
                                                            <th>Discount</th>
                                                            <th>Points Used</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            try {
                                                                String id = request.getParameter("id");
                                                                List<MemberEntity> members = (List<MemberEntity>) (session.getAttribute("members"));
                                                                MemberEntity member = new MemberEntity();
                                                                for (int i = 0; i < members.size(); i++) {
                                                                    if (members.get(i).getId() == Integer.parseInt(id)) {
                                                                        member = members.get(i);
                                                                    }
                                                                }
                                                                List<SalesRecordEntity> salesRecords = member.getPurchases();
                                                                for (int i = 0; i < salesRecords.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <%=salesRecords.get(i).getId()%>
                                                            </td>
                                                            <td>
                                                                <%=salesRecords.get(i).getReceiptNo()%>
                                                            </td>
                                                            <td>
                                                                <%=salesRecords.get(i).getCreatedDate()%>
                                                            </td>                                       
                                                            <td>
                                                                <%=salesRecords.get(i).getStore().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=salesRecords.get(i).getAmountDue()%>0 <%=salesRecords.get(i).getCurrency()%>
                                                            </td>
                                                            <td>
                                                                <%=salesRecords.get(i).getAmountPaid()%>0 <%=salesRecords.get(i).getCurrency()%>
                                                            </td>
                                                            <td>
                                                                <%=salesRecords.get(i).getAmountPaidUsingPoints()%>0 <%=salesRecords.get(i).getCurrency()%>
                                                            </td>
                                                            <td>
                                                                <%=salesRecords.get(i).getLoyaltyPointsDeducted()%>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=salesRecords.get(i).getId()%>" value="View Details" onclick="viewSalesRecordDetails('<%=salesRecords.get(i).getId()%>')"/>
                                                            </td>
                                                        </tr>                                                    
                                                        <%}
                                                            } catch (Exception ex) {
                                                                response.sendRedirect("../MemberManagement_MemberServlet");
                                                            }%>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <input type="hidden" name="id" value="">    
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
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>
    </body>
</html>
