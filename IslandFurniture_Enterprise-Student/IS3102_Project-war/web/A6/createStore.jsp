<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="java.util.List"%>
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
                            <h1 class="page-header">Store Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                                                             
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../FacilityManagement_StoreServlet/storeManagement_index">Store Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-edit"></i> Add New Store
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6">                           

                            <form role="form" method="POST" enctype="multipart/form-data" action="../FacilityManagement_StoreServlet/createStore_POST">
                                <fieldset>
                                    <div class="form-group">
                                        <label for="input_storeOfficeName">Store Name</label>
                                        <input type="text" class="form-control" id="input_storeName" name="storeName" required="true">
                                    </div>

                                    <div class="form-group">
                                        <label for="input_regionalOffice">Regional Office</label>                                    
                                        <select name="regionalOfficeId" class="form-control" required="true">                                        
                                            <% List<RegionalOfficeEntity> regionalOfficeList = (List<RegionalOfficeEntity>) request.getAttribute("regionalOfficeList");

                                                for (RegionalOfficeEntity ro : regionalOfficeList) {
                                            %>
                                            <option value="<%= ro.getId()%>"><%= ro.getName()%></option>
                                            <%
                                                }
                                            %>

                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="input_country">Country</label>                                    
                                        <select name="countryID" class="form-control" required="true">                                        
                                            <% List<CountryEntity> countryList = (List<CountryEntity>) request.getAttribute("countryList");

                                                for (CountryEntity ce : countryList) {
                                            %>
                                            <option value="<%= ce.getId()%>"><%= ce.getName()%></option>
                                            <%
                                                }
                                            %>

                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label for="input_address">Address</label>
                                        <input type="text" class="form-control" id="input_address"  name="address">
                                    </div>

                                    <div class="form-group">
                                        <label for="input_email">Postal Code</label>
                                        <input type="text" class="form-control" id="input_postalCode"  name="postalCode" >
                                    </div>

                                    <div class="form-group">
                                        <label for="input_telephone">Telephone</label>
                                        <input type="text" class="form-control" id="input_telephone"  name="telephone" >
                                    </div>

                                    <div class="form-group">
                                        <label for="input_email">Email</label>
                                        <input type="email" class="form-control" id="input_email"  name="email" >
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="input_city">Latitude</label>
                                        <input type="text" class="form-control" id="input_city"  name="latitude" >
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="input_city">Longitude</label>
                                        <input type="text" class="form-control" id="input_city"  name="longitude" >
                                    </div>
                                            
                                    <div>
                                        <label>Store Map</label>
                                        <input type="file" name="javafile">
                                    </div>
                                    <br/>
                                    <div class="form-group">
                                        <input type="submit" class="btn btn-primary" value="Submit">
                                    </div>
                                </fieldset>
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


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
