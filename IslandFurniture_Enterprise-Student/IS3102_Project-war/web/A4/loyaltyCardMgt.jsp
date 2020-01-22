<%@page import="java.text.DecimalFormat"%>
<%@page import="EntityManager.LoyaltyTierEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>

<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateLoyalty(id) {
                loyaltyManagement.id.value = id;
                document.loyaltyManagement.action = "loyaltyManagement_Update.jsp";
                document.loyaltyManagement.submit();
            }
            function removeLoyalty() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.loyaltyManagement.action = "../LoyaltyManagement_Servlet";
                    document.loyaltyManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.loyaltyManagement.action = "../LoyaltyManagement_RemoveServlet";
                    document.loyaltyManagement.submit();
                }
            }
            function addLoyalty() {
                window.event.returnValue = true;
                document.loyaltyManagement.action = "loyaltyManagement_Add.jsp";
                document.loyaltyManagement.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% String memberEmailParameter = request.getParameter("memberEmail");
                String loyaltyCardID = request.getParameter("loyaltyCardID");%>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Loyalty Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i> Operational CRM
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> Loyalty Card Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessage.jsp" />
                    <!-- /.warning -->
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../LoyaltyCardManagement_Servlet">
                                <div class="form-group">
                                    <label>Member Email</label>
                                    <%String memberEmailForm = "";
                                        String memberEmail = memberEmailParameter;
                                        if (memberEmailParameter != null) {
                                            memberEmailForm = "disabled";
                                        } else {
                                            memberEmail = "";
                                        }
                                    %>
                                    <input class="form-control" required="true" type="text" value="<%=memberEmail%>" name="memberEmail" <%=memberEmailForm%>>
                                </div>
                                <div class="form-group">
                                    <label>Loyalty Card ID</label>
                                    <%String loyaltyCardForm = "";
                                        if (memberEmailParameter == null) {
                                            loyaltyCardForm = "disabled";
                                            loyaltyCardID = "";
                                        }
                                        if (loyaltyCardID == null) {
                                            loyaltyCardID = "";
                                        }
                                    %>
                                    <input class="form-control" type="text" required="true" value="<%=loyaltyCardID%>" name="loyaltyCardID" <%=loyaltyCardForm%>>
                                    <input class="form-control" required="true" type="hidden" value="<%=memberEmail%>" name="memberEmail2">
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Next" class="btn btn-lg btn-primary btn-block">
                                </div>
                            </form>
                        </div>
                        <!-- /.row -->

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
                        <p id="messageBox">Loyalty will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeLoyalty()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
