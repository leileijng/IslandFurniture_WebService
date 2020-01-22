<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="HelperClasses.MessageHelper"%>
<%@page import="java.text.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>        
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Demand Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-crop"></i> Demand Management</a>
                                </li>                                
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <style>
                        select {
                            max-width: 300px;
                        }
                    </style>                                                           

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <%
                                        String errMsg = request.getParameter("errMsg");
                                        if (errMsg == null || errMsg.equals("")) {
                                            errMsg = "Select regional office first, then select manufacturing facility";
                                        }
                                        out.println(errMsg);
                                    %>
                                </div>
                                <div class="panel-body">

                                    <form action="../DemandManagement_index_POST/*">
                                        <div class="form-group">
                                            <label for="select_regionalOffice">Regional Office</label>
                                            <select id="select_regionalOffice" class="form-control" name="regionalOffice" onchange="getStore()">
                                                <option>--Select regional office--</option>
                                                <%
                                                    List<RegionalOfficeEntity> regionalOfficeList = (List<RegionalOfficeEntity>) request.getAttribute("regionalOfficeList");
                                                    for (RegionalOfficeEntity r : regionalOfficeList) {
                                                %>
                                                <option value="<%= r.getId()%>"><%= r.getName()%></option>
                                                <%
                                                    }
                                                %>
                                            </select>                                                 
                                        </div>
                                        <div class="form-group">
                                            <label for="select_mf">Manufacturing Facility</label>
                                            <select id="select_mf" class="form-control" name="MF_Name" required="true">
                                            </select>
                                        </div>
                                        <input type="submit" class="btn btn-primary" value="Access">

                                    </form>

                                </div>                               

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

        <%
            if (request.getAttribute("alertMessage") != null) {
        %>
        <script>
            alert("<%= request.getAttribute("alertMessage")%>");
        </script>
        <%
            }
        %>

        <script>
            function getStore() {
                var regionalOfficeId = $("#select_regionalOffice").find('option:selected').val();
                $.get('../GetMFbyRO_ajax_Servlet/*', {regionalOfficeId: regionalOfficeId}, function (responseText) {
                    var stores = responseText.trim().split(';');
                    var x = document.getElementById("select_mf");
                    while (x.length > 0) {
                        x.remove(0);
                    }
                    for (var i = 0; i < stores.length - 1; i++) {
                        var option = document.createElement("option");
                        option.text = stores[i];
                        x.add(option);
                    }
                });
            }
        </script>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            }
            );
        </script>
    </body>
</html>
