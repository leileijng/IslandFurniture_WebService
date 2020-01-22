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
                                Create Sales Figures
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Historical Sales Figures Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Create Sales Figures
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />

                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../HistoricalSalesFigureManagement_AddServlet">
                                <div class="form-group">
                                    <label>Month</label>
                                    <input class="form-control" required="true" type="date" name="month" >
                                </div>
                                <div class="form-group">
                                    <label>Store ID</label>
                                    <input class="form-control" required="true" type="number" name="store" >
                                </div>
                                <div class="form-group">
                                    <label>Item ID:</label>
                                    <input class="form-control" required="true" type="number" name="itemId" >
                                </div>
                                <div class="form-group">
                                    <label>Quantity</label>
                                    <input class="form-control" required="true" type="number"name="quantity" >
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Create Sales Figures" class="btn btn-lg btn-primary btn-block">
                                </div>
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->

        <script>
            function validatePassword() {
                var password = document.getElementById("password").value;
                var repassword = document.getElementById("repassword").value;
                var ok = true;
                if (password != repassword) {
                    //alert("Passwords Do not match");
                    document.getElementById("password").style.borderColor = "#E34234";
                    document.getElementById("repassword").style.borderColor = "#E34234";
                    alert("Passwords do not match. Please key again.");
                    ok = false;
                }
                return ok;
            }
        </script>
    </body>

</html>
