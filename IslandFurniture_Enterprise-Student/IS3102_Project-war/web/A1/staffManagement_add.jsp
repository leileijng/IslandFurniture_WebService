<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function addStaff() {
                document.addStaffForm.action = "../StaffManagement_AddStaffServlet";
                document.addStaffForm.submit();
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Register Staff
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-users"></i> <a href="staffManagement.jsp">Staff Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Register Staff
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />
                    <form role="form" name="addStaffForm" onsubmit="addStaff()">
                        <div class="row">
                            <div class="col-lg-6">                                
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" name="name"  type="text" required="true">
                                </div>
                                <div class="form-group">
                                    <label>Identification No</label>
                                    <input class="form-control" type="text" required="true" name="identificationNo" >
                                </div>
                                <div class="form-group">
                                    <label>E-mail Address</label>
                                    <input class="form-control" required="true" type="email" name="email" >
                                </div>
                                <div class="form-group">
                                    <label>Phone</label>
                                    <input class="form-control" required="true" type="text" name="phone" >
                                </div>
                                <div class="form-group" hidden>
                                    <label>Password</label>
                                    <input class="form-control" type="password" value="" name="password" id="password">
                                </div>
                                <div class="form-group" hidden>
                                    <label>Re-enter Password</label>
                                    <input class="form-control" type="password" value="" name="repassword" id="repassword">
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input class="form-control" type="text" required="true" name="address" >
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Register" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="A1/staffManagement_add.jsp" name="source">
                            </div>
                        </div>
                    </form>

                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->

    </body>

</html>
