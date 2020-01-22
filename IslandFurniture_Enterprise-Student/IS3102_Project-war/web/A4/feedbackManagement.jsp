<%@page import="EntityManager.FeedbackEntity"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="EntityManager.SalesRecordEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.MemberEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
       

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Feedback Management</h1>
                            <ol class="breadcrumb">
                               <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="customerServiceManagement.jsp"> Customer Service </a>                                 
                                </li>
                                <li class="active">
                                    <i class="icon icon-fire"></i> Feedback Management</a>                                 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        out.println("Display all feedbacks");
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
                                                            <th>Feedback Id</th>
                                                            <th>Subject</th>
                                                            <th>Customer Name</th>
                                                            <th>Email</th>
                                                            <th>Message</th>                                                      
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            try {
                                                                List<FeedbackEntity> feedbacks = (List<FeedbackEntity>)session.getAttribute("feedbacks");
                                                                for (int i = 0; i < feedbacks.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <%=feedbacks.get(i).getId()%>
                                                            </td>
                                                            <td>
                                                                <%=feedbacks.get(i).getSubject()%>
                                                            </td>
                                                            <td>
                                                                <%=feedbacks.get(i).getName()%>
                                                            </td>
                                                            <td>
                                                                <%=feedbacks.get(i).getEmail()%>
                                                            </td>                                                                                              
                                                            <td>
                                                                <%=feedbacks.get(i).getMessage()%>
                                                            </td>
                                                        </tr>                                                    
                                                        <%}
                                                            } catch (Exception ex) {
                                                                response.sendRedirect("../A4/feedbackManagement.jsp");
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
