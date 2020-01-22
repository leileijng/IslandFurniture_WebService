<%@page import="EntityManager.StaffEntity"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.PromotionEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updatePromotion(id) {
                promotionManagement.id.value = id;
                document.promotionManagement.action = "promotionalSalesManagement_Update.jsp";
                document.promotionManagement.submit();
            }
            function removePromotion() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.promotionManagement.action = "../PromotionalSalesManagement_Servlet";
                    document.promotionManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.promotionManagement.action = "../PromotionalSalesManagement_RemoveServlet";
                    document.promotionManagement.submit();
                }
            }
            function addPromotion() {
                window.event.returnValue = true;
                document.promotionManagement.action = "promotionalSalesManagement_Add.jsp";
                document.promotionManagement.submit();
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
            <%
                StaffEntity staffEntity = (StaffEntity) (session.getAttribute("staffEntity"));
                boolean roleCanEditPromo = false;
                if (staffEntity != null) {
                    List<RoleEntity> roles = staffEntity.getRoles();
                    Long[] approvedRolesID = new Long[]{1L, 2L, 5L, 11L};
                    for (RoleEntity roleEntity : roles) {
                        for (Long ID : approvedRolesID) {
                            if (roleEntity.getId().equals(ID)) {
                                roleCanEditPromo = true;
                                break;
                            }
                        }
                    }
                }
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Promotion Management</h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> Promotion Management
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
                                            out.println("Add a new promotion or remove an existing promotion");
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
                                <form name="promotionManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <%if (roleCanEditPromo) {%>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Promotion" onclick="addPromotion()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Promotion</button></a>
                                                </div>
                                            </div>
                                            <%}%>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <%if (roleCanEditPromo) {%>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                                <%}%>
                                                            <th>Item Name</th>
                                                            <th>Country</th>
                                                            <th>Discount Rate</th>
                                                            <th>Start Date</th>
                                                            <th>End Date</th>
                                                            <th>Description</th>
                                                                <% if (roleCanEditPromo) {%>
                                                            <th>Action</th>
                                                                <%}%>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<PromotionEntity> promotions = (List<PromotionEntity>) (session.getAttribute("promotions"));
                                                            if (promotions
                                                                    != null) {
                                                                for (int i = 0; i < promotions.size(); i++) {

                                                        %>
                                                        <tr>
                                                            <%if (roleCanEditPromo) {%>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=promotions.get(i).getId()%>" />
                                                            </td>
                                                            <%}%>
                                                            <td>
                                                                <%=promotions.get(i).getItem().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=promotions.get(i).getCountry().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=promotions.get(i).getDiscountRate()%>%
                                                            </td>
                                                            <td>
                                                                <% SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
                                                                    String date = DATE_FORMAT.format(promotions.get(i).getStartDate());%>
                                                                <%=date%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    String date1 = DATE_FORMAT.format(promotions.get(i).getEndDate());%>
                                                                <%=date1%>
                                                            </td>
                                                            <td>
                                                                <%=promotions.get(i).getDescription()%>
                                                            </td>
                                                            <%if (roleCanEditPromo) {%>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" id="<%=promotions.get(i).getId()%>" value="Update" onclick="javascript:updatePromotion('<%=promotions.get(i).getId()%>')"/>
                                                            </td>
                                                            <%}%>
                                                        </tr>
                                                        <%
                                                                }

                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <%if (roleCanEditPromo) {%>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add Promotion" onclick="addPromotion()"  />                                                    
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Promotion</button></a>
                                                </div>
                                            </div>
                                            <%}%>
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
                        <p id="messageBox">Promotion will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removePromotion()"  />
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
