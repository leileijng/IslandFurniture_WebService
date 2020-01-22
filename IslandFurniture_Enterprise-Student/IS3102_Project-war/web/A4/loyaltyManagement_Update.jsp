<%@page import="EntityManager.LoyaltyTierEntity"%>
a<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.FurnitureEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Loyalty Tier Update
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li>
                                    <i class="icon icon-user"></i>  <a href="../LoyaltyManagement_Servlet">Loyalty Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Loyalty Tier Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            String id = request.getParameter("id");
                            List<LoyaltyTierEntity> loyaltyTiers = (List<LoyaltyTierEntity>) (session.getAttribute("loyaltyTiers"));
                            LoyaltyTierEntity loyaltyTierEntity = new LoyaltyTierEntity();
                            for (int i = 0; i < loyaltyTiers.size(); i++) {
                                if (loyaltyTiers.get(i).getId() == Integer.parseInt(id)) {
                                    loyaltyTierEntity = loyaltyTiers.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../LoyaltyManagement_UpdateServlet">

                                <div class="form-group">
                                    <label>Tier</label>
                                    <input class="form-control" disabled="true" type="text" name="tier" value="<%=loyaltyTierEntity.getTier()%>">
                                </div>
                                <div class="form-group">
                                    <label>Required amount spent</label>
                                    <input class="form-control" type="number" required="true" min="0" name="requiredAmount" value="<%=loyaltyTierEntity.getAmtOfSpendingRequired()%>">
                                </div>
                                
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=loyaltyTierEntity.getId()%>" name="id">
                                <input type="hidden" value="<%=loyaltyTierEntity.getTier()%>" name="loyaltyName">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%} catch (Exception ex) {

                            //response.sendRedirect("../FurnitureManagement_FurnitureServlet");
                        }%>
                </div>

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
