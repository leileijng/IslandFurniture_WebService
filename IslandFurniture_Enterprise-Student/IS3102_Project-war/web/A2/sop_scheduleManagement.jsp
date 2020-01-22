<%@page import="EntityManager.MonthScheduleEntity"%>
<%@page import="EntityManager.WarehouseEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>

        <script>
            function removeSchedule() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.scheduleManagement.submit();
                    
                } else {
                    window.event.returnValue = true;
                    document.scheduleManagement.action = "../SaleAndOperationPlanning_Servlet/deleteSchedule";
                    document.scheduleManagement.submit();
                }
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
        </script>

        <style>
            input{
                max-width: 150px;
            }
        </style>

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Schedule Management
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../SaleForecast_Servlet/SaleForecast_index_GET">Sales Forecast</a>
                                </li>                              
                                <li>
                                    <i class="icon icon-calendar"></i> Schedule Management</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <p>Access one scheme to start planning</p>
                                </div>
                                <!-- /.panel-heading -->

                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                            <form name="scheduleManagement">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th>Year</th>
                                                            <th>Month</th>   
                                                            <th>Week 1</th>
                                                            <th>Week 2</th>
                                                            <th>Week 3</th>
                                                            <th>Week 4</th>
                                                            <th>Week 5</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<MonthScheduleEntity> scheduleList = (List<MonthScheduleEntity>) request.getAttribute("scheduleList");
                                                            if (scheduleList != null) {
                                                                for (MonthScheduleEntity schedule : scheduleList) {
                                                        %>
                                                        <tr>   
                                                            <td><input type="checkbox" name="delete" value="<%=schedule.getId()%>" /></td>
                                                            <td><%= schedule.getYear()%></td>
                                                            <td><%= schedule.getMonth()%></td>                                                            
                                                            <td><%= schedule.getWorkDays_firstWeek()%></td>
                                                            <td><%= schedule.getWorkDays_secondWeek()%></td>
                                                            <td><%= schedule.getWorkDays_thirdWeek()%></td>
                                                            <td><%= schedule.getWorkDays_forthWeek()%></td>
                                                            <td><%= schedule.getWorkDays_fifthWeek()%></td>
                                                        </tr>
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>    
                                                <div class="row">
                                                    <div class="col-md-12">                                                        
                                                        <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Delete Schedule</button></a>
                                                    </div>
                                                </div>
                                            </form>  
                                            <hr/>
                                            <form class="form-inline" action="../SaleAndOperationPlanning_Servlet/addSchedule">
                                                <h4><b>Add Schedule:</b></h4>
                                                <div class="form-group">
                                                    <label><b>Year</b></label>
                                                    <input type="number" name="year" class="form-control" min="2014" max="2050" required="true">
                                                </div>                                                
                                                <div class="form-group">
                                                    <label><b>Month</b></label>
                                                    <select class="form-control" name="month" required="true">
                                                        <option value="1">1</option>
                                                        <option value="2">2</option>
                                                        <option value="3">3</option>
                                                        <option value="4">4</option>
                                                        <option value="5">5</option>
                                                        <option value="6">6</option>
                                                        <option value="7">7</option>
                                                        <option value="8">8</option>
                                                        <option value="9">9</option>
                                                        <option value="10">10</option>
                                                        <option value="11">11</option>
                                                        <option value="12">12</option>
                                                    </select>
                                                </div>     
                                                <div class="form-group">
                                                    <label><b>Week 1</b></label>
                                                    <input type="number" name="week1" class="form-control"  min="0" max="7" required="true">
                                                </div>
                                                <div class="form-group">
                                                    <label><b>Week 2</b></label>
                                                    <input type="number" name="week2" class="form-control"  min="0" max="7" required="true">
                                                </div>
                                                <div class="form-group">
                                                    <label><b>Week 3</b></label>
                                                    <input type="number" name="week3" class="form-control"  min="0" max="7" required="true">
                                                </div>
                                                <div class="form-group">
                                                    <label><b>Week 4</b></label>
                                                    <input type="number" name="week4" class="form-control"  min="0" max="7" required="true">
                                                </div>
                                                <div class="form-group">
                                                    <label><b>Week 5</b></label>
                                                    <input type="number" name="week5" class="form-control"  min="0" max="7" required="true">
                                                </div>
                                                <div class="form-group">
                                                    <input type="submit" name="submit-btn" value="Add Schedule" class="btn btn-primary" data-loading-text="Loading...">
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
                        <p id="messageBox">Schedule will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeSchedule()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>
        <!-- Page-Level Demo Scripts - Tables - Use for reference -->        
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            }
            );
        </script>
    </body>

</html>
