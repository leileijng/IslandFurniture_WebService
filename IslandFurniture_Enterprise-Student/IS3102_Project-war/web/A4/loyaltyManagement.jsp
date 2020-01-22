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
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Loyalty Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> Loyalty Management
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
                                            out.println("Update the spending required to reach a tier");
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
                                <form name="loyaltyManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Name</th>
                                                            <th>Spending Amount Required (in USD)</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<LoyaltyTierEntity> loyaltyTiers = (List<LoyaltyTierEntity>) (session.getAttribute("loyaltyTiers"));
                                                            if (loyaltyTiers != null) {
                                                                for (int i = 0; i < loyaltyTiers.size(); i++) {

                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=loyaltyTiers.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=loyaltyTiers.get(i).getTier()%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    Double amount = loyaltyTiers.get(i).getAmtOfSpendingRequired();
                                                                    if (amount==0.0) {
                                                                        out.println(0);
                                                                    } else {
                                                                    DecimalFormat df = new DecimalFormat("#.00");
                                                                    out.println(df.format(amount));
                                                                    }
                                                                %>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=loyaltyTiers.get(i).getId()%>" value="Update" onclick="javascript:updateLoyalty('<%=loyaltyTiers.get(i).getId()%>')"/>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                }

                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->

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
