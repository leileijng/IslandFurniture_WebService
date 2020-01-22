<%@page import="EntityManager.MemberEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function updateStaff(id) {
                staffManagement.id.value = id;
                document.staffManagement.action = "../StaffManagement_UpdateStaffServlet";
                document.staffManagement.submit();
            }
            function removeStaff() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.staffManagement.action = "../StaffManagement_StaffServlet";
                    document.staffManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.staffManagement.action = "../StaffManagement_RemoveStaffServlet";
                    document.staffManagement.submit();
                }
            }
            function addStaff() {
                window.event.returnValue = true;
                document.staffManagement.action = "staffManagement_add.jsp";
                document.staffManagement.submit();
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
                            <h1 class="page-header">Query And Reporting</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="analytical.jsp">Analytical CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> Query And Reporting
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
                                            out.println("Register a new staff or remove an existing staff");
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
                                <form name="staffManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <br>
                                            <%
                                                Double totalCustomerRevenue = (Double) session.getAttribute("totalCustomerRevenue");
                                                List<MemberEntity> members = (List<MemberEntity>) session.getAttribute("members");

                                            %>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">                                                    
                                                    <h4>Simple Regression Analysis</h4>
                                                    <div class="col-md-6">
                                                        <form action="">
                                                            Select Independent Variable<br/>
                                                            <input type="checkbox" name="income" value="yes">Income<br/>
                                                            <input type="checkbox" name="age" value="yes">Age<br/>
                                                            <input type="checkbox" name="joinDate" value="yes">Join Date<br/>
                                                            <input type="checkbox" name="cumulativeSpending" value="yes">Cumulative Spending<br/>                                                        
                                                    </div>
                                                    <div class="col-md-6">
                                                        Select Dependent Variable
                                                        <select name="dependentVariable">                                                            
                                                            <option value="income">Income</option>
                                                            <option value="age">Age</option>
                                                            <option value="joinDate">Join Date</option>
                                                            <option value="CumulativeSpending">Cumulative Spending</option>
                                                        </select>
                                                        <br/>
                                                        <input type="submit" value="Generate Regression Analysis Table"/>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <h4>Query And Reporting</h4>
                                                    Search Options

                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <div class="tabs">
                                                                <ul class="nav nav-tabs">
                                                                    <li class="active">
                                                                        <a href="#member" data-toggle="tab"><i class="icon icon-user"></i> Members</a>
                                                                    </li>
                                                                    <li>
                                                                        <a href="#sales" data-toggle="tab"> Sales Records</a>
                                                                    </li>
                                                                </ul>
                                                                <div class="tab-content">
                                                                    <div id="member" class="tab-pane active">
                                                                        <h4>Search Options</h4>
                                                                        <div class="panel-body">
                                                                            <div class="table-responsive">
                                                                                <br>
                                                                                <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline">
                                                                                    <table class="table table-striped table-bordered table-hover table-condensed" id="dataTables-example">
                                                                                        <thead>
                                                                                            <tr>
                                                                                                <td>Attribute</td>
                                                                                                <td>Search Options</td>
                                                                                            </tr>
                                                                                        </thead>
                                                                                        <tbody>
                                                                                            <tr>                                                      
                                                                                                <td>
                                                                                                    Income Range 
                                                                                                </td>
                                                                                                <td>Min: <select name="incomeMin">
                                                                                                        <option value="nomin">No Min</option>
                                                                                                        <option value="1">1</option>
                                                                                                        <option value="10000">10000</option>
                                                                                                        <option value="20000">20000</option>
                                                                                                        <option value="30000">30000</option>
                                                                                                    </select> Max: <select name="incomeMax">    
                                                                                                        <option value="nomax">No Max</option>
                                                                                                        <option value="10000">10000</option>
                                                                                                        <option value="20000">20000</option>
                                                                                                        <option value="30000">30000</option>
                                                                                                        <option value="40000">40000</option>
                                                                                                    </select>
                                                                                                </td>
                                                                                            </tr>
                                                                                        <br/>
                                                                                        <tr><td>
                                                                                        Age</td><td>Min: <input type="text" name="ageMin"/> Max: <input type="text" name="ageMax"/><br/>
                                                                                            </td></tr>
                                                                                        <tr><td>
                                                                                                Join Date</td><td> Min: <input type="text" name="joinDateMin"/> Max: <input type="text" name="joinDateMax"/><br/>
                                                                                            </td></tr>
                                                                                        <tr><td>
                                                                                                Cumulative Spending</td><td> - Min: <input type="text" name="cumulativeSpendingMin"/> Max: <input type="text" name="cumulativeSpendingMax"/><br/>
                                                                                            </td></tr>
                                                                                        <tr><td>
                                                                                                Purchase Recency</td><td> Min: <input type="text" name="recencyMin"/> Max: <input type="text" name="recencyMax"/><br/>
                                                                                            </td></tr>
                                                                                        <tr><td>
                                                                                                Purchase Frequency</td><td> Min: <input type="text" name="frequencyMin"/> Max: <input type="text" name="frequencyMax"/><br/>
                                                                                            </td></tr>
                                                                                        <tr><td>
                                                                                                Purchase Monetary Value</td><td> Min: <input type="text" name="monetaryMin"/> Max: <input type="text" name="monetaryMax"/><br/>
                                                                                            </td></tr>
                                                                                        <tr><td>
                                                                                                CLV</td><td> Min: <input type="text" name="clvMin"/> Max: <input type="text" name="clvMax"/><br/>
                                                                                            </td></tr>
                                                                                        
                                                                                        </tbody>
                                                                                    </table>
                                                                                    <input type="button" value="Submit" name="Submit"/>
                                                                                </div>
                                                                                <!-- /.table-responsive -->
                                                                                <input type="hidden" name="id" value="">    
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div id="sales" class="tab-pane">
                                                                        <h4>Customer Lifetime Value</h4>
                                                                        <table class="table">
                                                                            <tr>
                                                                                <td>
                                                                                    Name
                                                                                </td>
                                                                                <td>
                                                                                    Group 2 Members Profile
                                                                                </td>
                                                                                <td>
                                                                                    Group 3 Members Profile
                                                                                </td>
                                                                                <td>
                                                                                    Group 4 Members Profile
                                                                                </td>
                                                                            </tr>

                                                                        </table>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
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
        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
            new Morris.Bar({
                // ID of the element in which to draw the chart.
                element: 'myfirstchart',
                // Chart data records -- each entry in this array corresponds to a point on
                // the chart.
                data: [
                    {y: '18-25', a: <%=totalCustomerRevenue%>, b: 90},
                    {y: '26-40', a: 75, b: 65},
                    {y: '41-55', a: 50, b: 40},
                    {y: '56-75', a: 75, b: 65}
                ],
                xkey: 'y',
                ykeys: ['a', 'b'],
                labels: ['Series A', 'Series B']
            });
        </script>
    </body>
</html>