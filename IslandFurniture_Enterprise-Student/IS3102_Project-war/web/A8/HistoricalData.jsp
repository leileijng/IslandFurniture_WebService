<%@page import="EntityManager.MenuItemEntity"%>
<%@page import="EntityManager.SalesFigureEntity"%>
<%@page import="EntityManager.StoreEntity"%>
<%@page import="EntityManager.MonthScheduleEntity"%>
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
                            <h1 class="page-header">
                                Historical Sales Figures
                            </h1>
                            <ol class="breadcrumb">                                
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_index_GET">Kitchen Requirement Planning</a>
                                </li>   
                                <li>
                                    <i class="icon icon-calendar"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_schedule_GET">Schedule</a>
                                </li>
                                <li>
                                    <i class="icon icon-list"></i>  <a href="../KitchenManagement_servlet/KitchenSaleForecast_main_GET">Sales Forecast</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-list"></i> Historical Sales Figures
                                </li>
                            </ol>
                        </div>
                    </div>
                    <%
                        MenuItemEntity menuItem = (MenuItemEntity) request.getAttribute("menuItem");
                    %>
                    
                    <div class="row">                             
                        <div class="col-lg-4">
                            <%
                                StoreEntity store = (StoreEntity) request.getAttribute("store");
                                MonthScheduleEntity schedule = (MonthScheduleEntity) request.getAttribute("schedule");
                            %>
                            <h4><b> Store:  </b><%= store.getName()%></h4>
                        </div>                                                                                                            
                    </div>
                    <br>
                    
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-green">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="icon icon-bar-chart-o"></i> Year <% if(schedule.getMonth()==1){ out.print(schedule.getYear()-1);  }
                                        else{ out.print(schedule.getYear()); } %> - Menu Item SKU <%= menuItem.getSKU()%> - <%= menuItem.getName() %> Sales Figure</h3>
                                </div>
                                <div class="panel-body">
                                    <div id="morris-area-chart3"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                                
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-green">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="icon icon-bar-chart-o"></i> Year <% if(schedule.getMonth()==1){ out.print(schedule.getYear()-2);  }
                                        else{ out.print(schedule.getYear()-1); } %> - Menu Item SKU <%= menuItem.getSKU()%> - <%= menuItem.getName() %> Sales Figure</h3>
                                </div>
                                <div class="panel-body">
                                    <div id="morris-area-chart2"></div>
                                </div>
                            </div>
                        </div>
                    </div>            

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-green">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="icon icon-bar-chart-o"></i> Year 
                                        <% if(schedule.getMonth()==1){ out.print(schedule.getYear()-3);  }
                                        else{ out.print(schedule.getYear()-2); } %> - Menu Item SKU <%= menuItem.getSKU()%> - <%= menuItem.getName() %> Sales Figure </h3>
                                </div>
                                <div class="panel-body">
                                    <div id="morris-area-chart1"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    
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
            alert("<%= request.getAttribute("alertMessage")%>");</script>
            <%
                }
            %>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->        
        <script>
            $(document).ready(function () {
                $('#dataTable1').dataTable();
                $('#dataTable2').dataTable();
            }
            );</script>
            <%
                List<SalesFigureEntity> saleDate1 = (List<SalesFigureEntity>) request.getAttribute("saleDate1");
                List<SalesFigureEntity> saleDate2 = (List<SalesFigureEntity>) request.getAttribute("saleDate2");
                List<SalesFigureEntity> saleDate3 = (List<SalesFigureEntity>) request.getAttribute("saleDate3");                
            %>

        <script>
            Morris.Area({
                element: 'morris-area-chart1',
                data: [
            <%
            for (SalesFigureEntity s : saleDate1) {
            %>
                {
                period: '<% if(s.getSchedule().getMonth()>=10){ out.print(s.getSchedule().getYear()+"-"+s.getSchedule().getMonth()); }
                else{ out.print(s.getSchedule().getYear()+"-0"+s.getSchedule().getMonth()); } %>',
                <%= s.getMenuItem().getSKU() %>: <%= s.getQuantity() %>,
                },
            <%                
            }
            %>
                ],
                xkey: 'period',
                ykeys: ['<%= menuItem.getSKU() %>'],
                labels: ['<%= menuItem.getSKU() %>'],
                pointSize: 2,
                hideHover: 'auto',
                resize: true
            });
        </script>
        
        <script>
            Morris.Area({
                element: 'morris-area-chart2',
                data: [
            <%
            for (SalesFigureEntity s : saleDate2) {
            %>
                {
                period: '<% if(s.getSchedule().getMonth()>=10){ out.print(s.getSchedule().getYear()+"-"+s.getSchedule().getMonth()); }
                else{ out.print(s.getSchedule().getYear()+"-0"+s.getSchedule().getMonth()); } %>',
                <%= s.getMenuItem().getSKU() %>: <%= s.getQuantity() %>,
                },
            <%                
            }
            %>
                ],
                xkey: 'period',
                ykeys: ['<%= menuItem.getSKU() %>'],
                labels: ['<%= menuItem.getSKU() %>'],
                pointSize: 2,
                hideHover: 'auto',
                resize: true
            });
        </script>
        
        <script>
            Morris.Area({
                element: 'morris-area-chart3',
                data: [
            <%
            for (SalesFigureEntity s : saleDate3) {
            %>
                {
                period: '<% if(s.getSchedule().getMonth()>=10){ out.print(s.getSchedule().getYear()+"-"+s.getSchedule().getMonth()); }
                else{ out.print(s.getSchedule().getYear()+"-0"+s.getSchedule().getMonth()); } %>',
                <%= s.getMenuItem().getSKU() %>: <%= s.getQuantity() %>,
                },
            <%                
            }
            %>
                ],
                xkey: 'period',
                ykeys: ['<%= menuItem.getSKU() %>'],
                labels: ['<%= menuItem.getSKU() %>'],
                pointSize: 2,
                hideHover: 'auto',
                resize: true
            });
        </script>

    </body>

</html>
