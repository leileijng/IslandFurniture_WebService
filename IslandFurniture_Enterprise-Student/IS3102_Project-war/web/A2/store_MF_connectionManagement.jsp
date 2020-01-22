<%@page import="EntityManager.StoreEntity"%>
<%@page import="EntityManager.ManufacturingFacilityEntity"%>
<%@page import="EntityManager.MonthScheduleEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>        
    <style type="text/css">
   .labels {
     color: white;
     background-color: red;
     font-family: "Lucida Grande", "Arial", sans-serif;
     font-size: 10px;
     text-align: center;       
     white-space: nowrap;
     padding: 2px;
   }
   </style>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Distribution Schema List
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../PPD_index_GET/*">Production Plan Distribution</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i> <a href="../PPD_main_GET/">Distribution Workspace</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i> Distribution Schema List</a>
                                </li>
                            </ol>
                        </div>
                    </div>

                    <% Long regionalOfficeId = (Long) request.getAttribute("regionalOfficeId"); %>

                    <!-- /.row -->
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <p>Each connection of facilities means the transportation cost between them is acceptable. Wrong connection may result in high transport cost.</p>
                                </div>
                                <!-- /.panel-heading -->

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                            <form action="../store_MF_connectionManagement_POST/removeConnection">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>                                                            
                                                            <th>Manufacturing Facility</th>
                                                            <th>Capacity (work hour)</th>
                                                            <th>Store</th>  
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <% List<ManufacturingFacilityEntity> mfList = (List<ManufacturingFacilityEntity>) request.getAttribute("mfList");
                                                            for (ManufacturingFacilityEntity mf : mfList) {
                                                                for (StoreEntity store : mf.getStoreList()) {
                                                        %>
                                                        <tr>                                                            
                                                            <td><%= mf.getName()%></td>
                                                            <td><%= mf.getCapacity()%></td>
                                                            <td><%= store.getName()%></td>
                                                            <td><span class="btn btn-default">
                                                                    <a href="../store_MF_connectionManagement_POST/removeConnection?mfId=<%= mf.getId()%>&storeId=<%= store.getId()%>&regionalOfficeId=<%= regionalOfficeId%>">Remove</a>
                                                                </span>
                                                            </td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>                                                    
                                            </form>  
                                            <hr/>
                                            <form class="form-inline" action="../store_MF_connectionManagement_POST/addConnection">
                                                <h4><b>Add Manufacturing Facility - Store Connection:</b></h4>

                                                <div class="form-group">
                                                    <label for="select_mf">Manufacturing Facility</label>
                                                    <select id="select_mf" class="form-control" name="mfId" required="true">
                                                        <option></option>
                                                        <% for (ManufacturingFacilityEntity mf : mfList) { %>                                                        
                                                        <option value="<%= mf.getId()%>"><%= mf.getName()%></option>
                                                        <% } %>
                                                    </select>
                                                </div>

                                                <div class="form-group">
                                                    <label for="select_store">Store</label>
                                                    <select id="select_store" class="form-control" name="storeId" required="true">
                                                        <option></option>
                                                        <% List<StoreEntity> storeList = (List<StoreEntity>) request.getAttribute("storeList");
                                                            for (StoreEntity s : storeList) {
                                                        %>
                                                        <option value="<%= s.getId()%>"><%= s.getName()%></option>
                                                        <%
                                                            }
                                                        %>

                                                    </select>
                                                </div>

                                                <input type="hidden" name="regionalOfficeId" value="<%=regionalOfficeId%>">

                                                <div class="form-group">
                                                    <input type="submit" name="submit-btn" value="Add Connection" class="btn btn-primary" data-loading-text="Loading...">
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <!-- /.table-responsive -->
                                </div>
                                <!-- /.panel-body -->

                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->


                    <div id="googleMap" style="width:100%;height:100%;"></div>

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

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->        
        <script>
            $(document).ready(function () {
                $('#dataTables-example').dataTable();
            }
            );
        </script>

        <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script src="../js/markerwithlabel.js"></script>
        
        <script>
            function initialize()
            {
                var mapProp = {
                    center: new google.maps.LatLng(<%= storeList.get(0).getLatitude()%>, <%= storeList.get(0).getLongitude()%>),
                    zoom: 12,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);


            <% for (StoreEntity s : storeList) {%>

                var <%= "store" + s.getId()%> = new MarkerWithLabel({
                    position: new google.maps.LatLng(<%= s.getLatitude()%>, <%= s.getLongitude()%>),
                    map: map,
                    title: '<%= s.getName() %>',
                    labelContent: "<%= s.getName() %>",
                    labelAnchor: new google.maps.Point(22, 0),
                    labelClass: "labels",
                });

            <% } %>

            <% for (ManufacturingFacilityEntity mf : mfList) {%>

                var <%= "mf" + mf.getId()%> = new MarkerWithLabel({
                    position: new google.maps.LatLng(<%= mf.getLatitude()%>, <%= mf.getLongitude()%>),
                    map: map,
                    title: '<%= mf.getName() %>',
                    labelContent: "<%= mf.getName() %>",
                    labelAnchor: new google.maps.Point(22, 0),
                    labelClass: "labels",
                });

            <% }%>
            
            
            <% for (ManufacturingFacilityEntity mf : mfList) {
                for (StoreEntity s : mf.getStoreList()) { %>                                    
                
                var <%= "line"+mf.getId()+s.getId() %> = [
                    new google.maps.LatLng(<%= mf.getLatitude()%>, <%= mf.getLongitude()%>),
                    new google.maps.LatLng(<%= s.getLatitude() %>, <%= s.getLongitude()%>),                    
                  ];

                var <%= "jkjk"+mf.getId()+s.getId() %> = new google.maps.Polyline({
                    path: <%= "line"+mf.getId()+s.getId() %>,
                    geodesic: true,
                    strokeColor: '#FF0000',
                    strokeOpacity: 1.0,
                    strokeWeight: 4
                });

                <%= "jkjk"+mf.getId()+ s.getId() %>.setMap(map);
                
                <% } } %>
            }

            google.maps.event.addDomListener(window, 'load', initialize);
        </script>

    </body>

</html>
