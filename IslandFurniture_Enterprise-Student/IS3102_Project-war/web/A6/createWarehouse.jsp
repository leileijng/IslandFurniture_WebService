<%@page import="EntityManager.ManufacturingFacilityEntity"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="java.util.List"%>
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
                            <h1 class="page-header">Warehouse Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-home"></i>  <a href="../A6/facilityManagement.jsp">Facility Management</a>
                                </li>                                                             
                                <li>
                                    <i class="icon icon-archive"></i>  <a href="../FacilityManagement_Servlet/warehouseManagement_index">Warehouse Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-edit"></i> Add New Warehouse
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6">                           

                            <form class="myForm" action="../FacilityManagement_Servlet/createWarehouse_POST">
                                <div class="form-group">
                                    <label for="input_warehouseName">Warehouse Name</label>
                                    <input type="text" class="form-control" id="input_warehouseName" name="warehouseName" required="true">
                                </div>                                                                
                                
                                <div class="form-group">
                                    <label for="input_Store">Store</label>
                                    <select id="input_Store" name="StoreId" class="form-control">
                                        <option></option>
                                        <% 
                                            List<StoreEntity> storeList = (List<StoreEntity>) request.getAttribute("storeList"); 
                                            for(StoreEntity s: storeList){
                                        %>
                                        <option value="<%= s.getId() %>"><%= s.getName() %></option>
                                        <%
                                            }
                                        %>                                        
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label for="input_manufacturingFacility">Manufacturing Facility</label>
                                    <select id="input_manufacturingFacility" name="mfId" class="form-control">
                                        <option></option>
                                        <% 
                                            List<ManufacturingFacilityEntity> mfList = (List<ManufacturingFacilityEntity>) request.getAttribute("mfList"); 
                                            for(ManufacturingFacilityEntity mf: mfList){
                                        %>
                                        <option value="<%= mf.getId() %>"><%= mf.getName() %></option>
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
                                    <label for="input_telephone">Telephone</label>
                                    <input type="text" class="form-control" id="input_telephone"  name="telephone" >
                                </div>

                                <div class="form-group">
                                    <label for="input_email">Email</label>
                                    <input type="email" class="form-control" id="input_email"  name="email" >
                                </div>

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


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
