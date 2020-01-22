<%@page import="java.util.List"%>
<%@page import="EntityManager.FurnitureEntity"%>
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
                                Add Loyalty
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i><a href="../LoyaltyManagement_Servlet"> Loyalty Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Loyalty
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />
                    <!-- /.warning -->
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../LoyaltyManagement_AddServlet">
                                <div class="form-group">
                                    <label>Tier</label>
                                    <input class="form-control" required="true" type="text" name="tier" >
                                </div>
                                <div class="form-group">
                                    <label>Required amount spent</label>
                                    <input class="form-control" type="number" required="true" min="0" name="requiredAmount" >
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Add" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="A4/loyaltyManagement_Add.jsp" name="source">
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
    </body>

</html>
