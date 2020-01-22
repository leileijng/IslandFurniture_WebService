<%@page import="HelperClasses.ManufacturingFacilityHelper"%>
<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.ManufacturingFacilityEntity"%>
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
                select{
                    max-width: 280px;
                }
            </style>

            <div id="page-wrapper">
                <div class="container-fluid">

                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Manufacturing Facility Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                                                             
                                <li>
                                    <i class="icon icon-cogs"></i>  <a href="../FacilityManagement_ManufacturingFacilityServlet/manufacturingFacilityManagement_index">Manufacturing Facility Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-edit"></i> Edit Manufacturing Facility
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6">                           
                            <% ManufacturingFacilityHelper mfHelper = (ManufacturingFacilityHelper) request.getAttribute("mfHelper");%>
                            <form class="myForm" action="../FacilityManagement_ManufacturingFacilityServlet/editManufacturingFacility_POST">
                                <div class="form-group">
                                    <label for="input_manufacturingFacilityName">Manufacturing Facility Name</label>
                                    <input type="text" class="form-control" id="input_manufacturingFacilityName" value="<%= mfHelper.manufacturingFacilityEntity.getName()%>" required="true" disabled/>
                                    <input type="hidden" name="manufacturingFacilityName"  value="<%= mfHelper.manufacturingFacilityEntity.getName()%>" >

                                </div>

                                <div class="form-group">
                                    <label>Regional Office</label>                                    
                                    <select name="regionalOfficeId" class="form-control" required="true">
                                        <option value="<%= mfHelper.regionalOffice.getId()%>"> <%= mfHelper.regionalOffice.getName()%> </option>

                                        <% List<RegionalOfficeEntity> regionalOfficeList = (List<RegionalOfficeEntity>) request.getAttribute("regionalOfficeList");
                                            for (RegionalOfficeEntity ro : regionalOfficeList) {
                                                if (ro.getId() != mfHelper.regionalOffice.getId()) {
                                        %>
                                        <option value="<%= ro.getId()%>"><%= ro.getName()%></option>
                                        <%
                                                }
                                            }
                                        %>

                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="input_address">Address</label>
                                    <input type="text" class="form-control" id="input_address"  name="address" value="<%= mfHelper.manufacturingFacilityEntity.getAddress()%>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_telephone">Telephone</label>
                                    <input type="text" class="form-control" id="input_telephone"  name="telephone" value="<%= mfHelper.manufacturingFacilityEntity.getTelephone()%>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_email">Email</label>
                                    <input type="email" class="form-control" id="input_email"  name="email" value="<%= mfHelper.manufacturingFacilityEntity.getEmail()%>" >
                                </div>
                                
                                <div class="form-group">
                                    <label for="input_city">Latitude</label>
                                    <input type="text" class="form-control" id="input_city"  name="latitude" value="<%= mfHelper.manufacturingFacilityEntity.getLatitude() %>" >
                                </div>

                                <div class="form-group">
                                    <label for="input_city">Longitude</label>
                                    <input type="text" class="form-control" id="input_city"  name="longitude" value="<%= mfHelper.manufacturingFacilityEntity.getLongitude() %>" >
                                </div>                                

                                <div class="form-group">
                                    <label for="input_email">Capacity</label>
                                    <input type="number" class="form-control" id="input_email"  name="capacity" value="<%= mfHelper.manufacturingFacilityEntity.getCapacity()%>" >
                                </div>
                                <input type="hidden" name="manufacturingFacilityId" value="<%= mfHelper.manufacturingFacilityEntity.getId()%>">
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
