<%@page import="EntityManager.RegionalOfficeEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>

        <div id="wrapper">

            <jsp:include page="../menu1.jsp" />

            <style>
                label{
                    font-size: 18px;
                }
                input{
                    max-width: 280px;
                }
            </style>

            <div id="page-wrapper">
                <div class="container-fluid">

                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Regional Office Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                                                             
                                <li>
                                    <i class="icon icon-globe"></i>  <a href="../FacilityManagement_RegionalOfficeServlet/regionalOfficeManagement_index">Regional Office Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-edit"></i>  Edit Regional Office
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6">                           
                            <% RegionalOfficeEntity regionalOffice = (RegionalOfficeEntity) request.getAttribute("regionalOffice");%>
                            <form class="myForm" action="../FacilityManagement_RegionalOfficeServlet/editRegionalOffice_POST">
                                <div class="form-group">
                                    <label for="input_regionalOfficeName">Regional Office Name</label>
                                    <input type="text" class="form-control" id="input_regionalOfficeName"  value="<%= regionalOffice.getName()%>" required="true" disabled/>
                                    <input type="hidden" name="regionalOfficeName" value="<%= regionalOffice.getName()%>" >

                                </div>

                                <div class="form-group">
                                    <label for="input_address">Address</label>
                                    <input type="text" class="form-control" id="input_address"  name="address" value="<%= regionalOffice.getAddress()%>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_telephone">Telephone</label>
                                    <input type="text" class="form-control" id="input_telephone"  name="telephone" value="<%= regionalOffice.getTelephone()%>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_email">Email</label>
                                    <input type="email" class="form-control" id="input_email"  name="email" value="<%= regionalOffice.getEmail()%>" >
                                </div>
                                <input type="hidden" name="regionalOfficeId" value="<%= regionalOffice.getId()%>">
                                <div class="form-group">
                                    <input type="submit" class="btn btn-primary" value="Submit">
                                </div>
                            </form>

                        </div>
                        <!-- /.col-lg-6 -->
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
        %><script>
            alert("<%= request.getAttribute("alertMessage")%>");
        </script><%
            }
        %>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
