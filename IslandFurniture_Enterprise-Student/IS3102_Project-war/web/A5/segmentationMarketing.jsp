<%@page import="EntityManager.MemberEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function sendLoyaltyPoints() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.segmentation.action = "../Analytical_SegmentationSendLoyaltyServlet";
                    document.segmentation.submit();
                } else {
                    window.event.returnValue = true;
                    document.segmentation.action = "../Analytical_SegmentationSendLoyaltyServlet";
                    document.segmentation.submit();
                }
            }
        </script>
        
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
 
            Integer cumulativeSpendingAgeGrp1 = (Integer) session.getAttribute("cumulativeSpendingAgeGrp1");
            Integer cumulativeSpendingAgeGrp2 = (Integer) session.getAttribute("cumulativeSpendingAgeGrp2");
            Integer cumulativeSpendingAgeGrp3 = (Integer) session.getAttribute("cumulativeSpendingAgeGrp3");
            Integer cumulativeSpendingAgeGrp4 = (Integer) session.getAttribute("cumulativeSpendingAgeGrp4");

            Integer numOfMembersInAgeGroup1 = (Integer) session.getAttribute("numOfMembersInAgeGroup1");
            Integer numOfMembersInAgeGroup2 = (Integer) session.getAttribute("numOfMembersInAgeGroup2");
            Integer numOfMembersInAgeGroup3 = (Integer) session.getAttribute("numOfMembersInAgeGroup3");
            Integer numOfMembersInAgeGroup4 = (Integer) session.getAttribute("numOfMembersInAgeGroup4");

            List<Integer> cumulativeGroupAge = new ArrayList();
            cumulativeGroupAge.add(cumulativeSpendingAgeGrp1 / numOfMembersInAgeGroup1);
            cumulativeGroupAge.add(cumulativeSpendingAgeGrp2 / numOfMembersInAgeGroup2);
            cumulativeGroupAge.add(cumulativeSpendingAgeGrp3 / numOfMembersInAgeGroup3);
            cumulativeGroupAge.add(cumulativeSpendingAgeGrp4 / numOfMembersInAgeGroup4);

            Integer ageGroup1Points = 1;
            Integer ageGroup2Points = 1;
            Integer ageGroup3Points = 1;
            Integer ageGroup4Points = 1;

            for (int i = 0; i < cumulativeGroupAge.size(); i++) {
                for (int j = 0; j < cumulativeGroupAge.size(); j++) {
                    if (!(i == j)) {
                        if (cumulativeGroupAge.get(i) > cumulativeGroupAge.get(j)) {
                            if (i == 0) {
                                ageGroup1Points++;
                            } else if (i == 1) {
                                ageGroup2Points++;
                            } else if (i == 2) {
                                ageGroup3Points++;
                            } else if (i == 3) {
                                ageGroup4Points++;
                            }
                        }
                    }
                }
            }
   
            Integer cumulativeSpendingIncomeGrp1 = (Integer) session.getAttribute("cumulativeSpendingIncomeGrp1");
            Integer cumulativeSpendingIncomeGrp2 = (Integer) session.getAttribute("cumulativeSpendingIncomeGrp2");
            Integer cumulativeSpendingIncomeGrp3 = (Integer) session.getAttribute("cumulativeSpendingIncomeGrp3");
            Integer cumulativeSpendingIncomeGrp4 = (Integer) session.getAttribute("cumulativeSpendingIncomeGrp4");

            Integer numOfMembersInIncomeGroup1 = (Integer) session.getAttribute("numOfMembersInIncomeGroup1");
            Integer numOfMembersInIncomeGroup2 = (Integer) session.getAttribute("numOfMembersInIncomeGroup2");
            Integer numOfMembersInIncomeGroup3 = (Integer) session.getAttribute("numOfMembersInIncomeGroup3");
            Integer numOfMembersInIncomeGroup4 = (Integer) session.getAttribute("numOfMembersInIncomeGroup4");

            List<Integer> cumulativeGroupIncome = new ArrayList();
            cumulativeGroupIncome.add(cumulativeSpendingIncomeGrp1 / numOfMembersInIncomeGroup1);
            cumulativeGroupIncome.add(cumulativeSpendingIncomeGrp2 / numOfMembersInIncomeGroup2);
            cumulativeGroupIncome.add(cumulativeSpendingIncomeGrp3 / numOfMembersInIncomeGroup3);
            cumulativeGroupIncome.add(cumulativeSpendingIncomeGrp4 / numOfMembersInIncomeGroup4);

            Integer incomeGroup1Points = 1;
            Integer incomeGroup2Points = 1;
            Integer incomeGroup3Points = 1;
            Integer incomeGroup4Points = 1;

            for (int i = 0; i < cumulativeGroupIncome.size(); i++) {
                for (int j = 0; j < cumulativeGroupIncome.size(); j++) {
                    if (!(i == j)) {
                        if (cumulativeGroupIncome.get(i) > cumulativeGroupIncome.get(j)) {
                            if (i == 0) {
                                incomeGroup1Points++;
                            } else if (i == 1) {
                                incomeGroup2Points++;
                            } else if (i == 2) {
                                incomeGroup3Points++;
                            } else if (i == 3) {
                                incomeGroup4Points++;
                            }
                        }
                    }
                }
            }

                    Integer totalCumulativeSpendingOfCountry1 = (Integer) session.getAttribute("totalCumulativeSpendingOfCountry1");
            Integer totalCumulativeSpendingOfCountry2 = (Integer) session.getAttribute("totalCumulativeSpendingOfCountry2");

            Integer numOfMembersInCountry1 = (Integer) session.getAttribute("numOfMembersInCountry1");
            Integer numOfMembersInCountry2 = (Integer) session.getAttribute("numOfMembersInCountry2");

            List<Integer> cumulativeGroupCountry = new ArrayList();
            cumulativeGroupCountry.add(totalCumulativeSpendingOfCountry1 / numOfMembersInCountry1);
            cumulativeGroupCountry.add(totalCumulativeSpendingOfCountry2 / numOfMembersInCountry2);

            Integer countryGroup1Points = 1;
            Integer countryGroup2Points = 1;

            for (int i = 0; i < cumulativeGroupCountry.size(); i++) {
                for (int j = 0; j < cumulativeGroupCountry.size(); j++) {
                    if (!(i == j)) {
                        if (cumulativeGroupCountry.get(i) > cumulativeGroupCountry.get(j)) {
                            if (i == 0) {
                                countryGroup1Points++;
                            } else if (i == 1) {
                                countryGroup2Points++;
                            }
                        }
                    }
                }
            }

            Integer getRevenueOfJoinDate1 = (Integer) session.getAttribute("getRevenueOfJoinDate1");
            Integer getRevenueOfJoinDate2 = (Integer) session.getAttribute("getRevenueOfJoinDate2");
            Integer getRevenueOfJoinDate3 = (Integer) session.getAttribute("getRevenueOfJoinDate3");
            Integer getRevenueOfJoinDate4 = (Integer) session.getAttribute("getRevenueOfJoinDate4");

            Integer numOfMembersInJoinDate1 = (Integer) session.getAttribute("numOfMembersInJoinDate1");
            Integer numOfMembersInJoinDate2 = (Integer) session.getAttribute("numOfMembersInJoinDate2");
            Integer numOfMembersInJoinDate3 = (Integer) session.getAttribute("numOfMembersInJoinDate3");
            Integer numOfMembersInJoinDate4 = (Integer) session.getAttribute("numOfMembersInJoinDate4");

            List<Integer> joinDateGroupIncome = new ArrayList();
            joinDateGroupIncome.add(getRevenueOfJoinDate1 / numOfMembersInJoinDate1);
            joinDateGroupIncome.add(getRevenueOfJoinDate2 / numOfMembersInJoinDate2);
            joinDateGroupIncome.add(getRevenueOfJoinDate3 / numOfMembersInJoinDate3);
            joinDateGroupIncome.add(getRevenueOfJoinDate4 / numOfMembersInJoinDate4);

            Integer joinDateGroup1Points = 1;
            Integer joinDateGroup2Points = 1;
            Integer joinDateGroup3Points = 1;
            Integer joinDateGroup4Points = 1;

            for (int i = 0; i < joinDateGroupIncome.size(); i++) {
                for (int j = 0; j < joinDateGroupIncome.size(); j++) {
                    if (!(i == j)) {
                        if (joinDateGroupIncome.get(i) > joinDateGroupIncome.get(j)) {
                            if (i == 0) {
                                joinDateGroup1Points++;
                            } else if (i == 1) {
                                joinDateGroup2Points++;
                            } else if (i == 2) {
                                joinDateGroup3Points++;
                            } else if (i == 3) {
                                joinDateGroup4Points++;
                            }
                        }
                    }
                }
            }

        %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Segmentation Marketing</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="analytical.jsp">Analytical CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-users"></i> Segmentation Marketing
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
                                    <%                                        String errMsg = request.getParameter("errMsg");
                                        String goodMsg = request.getParameter("goodMsg");
                                        if (errMsg == null && goodMsg == null) {
                                            out.println("Market Segments");
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
                                <form name="segmentation">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <br>
                                            <%
                                                List<MemberEntity> members = (List<MemberEntity>) session.getAttribute("members");
                                            %>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">

                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <div class="tabs">
                                                                <ul class="nav nav-tabs">
                                                                    <li class="active">
                                                                        <a href="#Demographics" data-toggle="tab"><i class="icon icon-user"></i> Demographics</a>
                                                                    </li>

                                                                </ul>
                                                                <div class="tab-content">
                                                                    <div id="Demographics" class="tab-pane active">

                                                                        <div class="panel-body">
                                                                            <div class="table-responsive">
                                                                                <div class="col-md-6">
                                                                                    <h4>Age Group</h4>
                                                                                    <div id="ageGroupChart"></div>
                                                                                    <h4>Country Group</h4>
                                                                                    <div id="countryChart"></div>

                                                                                </div>
                                                                                <div class="col-md-6">
                                                                                    <h4>Income Group</h4>
                                                                                    <div id="incomeGroupChart"></div>
                                                                                    <h4>Join Date Group</h4>
                                                                                    <div id="joinDateChart"></div>
                                                                                </div>

                                                                            </div>
                                                                        </div>
                                                                    </div>


                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" member="grid">
                                                        <table class="table table-bordered" id="dataTables-example">
                                                            <thead>
                                                                <tr>
                                                                    <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                                    <th>Name</th>
                                                                    <th>Age</th>
                                                                    <th>Income</th>
                                                                    <th>Country</th>
                                                                    <th>Join Date</th>
                                                                    <th>Points (/15)</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    if (members != null) {
                                                                        for (int i = 0; i < members.size(); i++) {
                                                                            MemberEntity member = members.get(i);
                                                                            if (member.getName() != null && member.getAge() != null && member.getIncome() != null && member.getCity() != null && member.getJoinDate() != null) {
                                                                %>
                                                                <tr>                   
                                                                    <td>
                                                                        <input type="checkbox" name="delete" value="<%=member.getId()%>" />
                                                                    </td>
                                                                    <td>
                                                                        <%=member.getName()%>
                                                                    </td>
                                                                    <td>
                                                                        <%=member.getAge()%>
                                                                        <%
                                                                            Integer totalPoints = 0;
                                                                            if (member.getAge() <= 25 && member.getAge() >= 18) {
                                                                                out.println("(" + ageGroup1Points + ")");
                                                                                totalPoints += ageGroup1Points;
                                                                            } else if (member.getAge() > 25 && member.getAge() <= 40) {
                                                                                out.println("(" + ageGroup2Points + ")");
                                                                                totalPoints += ageGroup2Points;
                                                                            } else if (member.getAge() > 40 && member.getAge() <= 55) {
                                                                                out.println("(" + ageGroup3Points + ")");
                                                                                totalPoints += ageGroup3Points;
                                                                            } else if (member.getAge() <= 75) {
                                                                                out.println("(" + ageGroup4Points + ")");
                                                                                totalPoints += ageGroup4Points;
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%=member.getIncome()%>

                                                                        <% if (member.getIncome() <= 30000) {
                                                                                out.println("(" + incomeGroup1Points + ")");
                                                                                totalPoints += incomeGroup1Points;
                                                                            } else if (member.getIncome() > 30000 && member.getIncome() <= 60000) {
                                                                                out.println("(" + incomeGroup2Points + ")");
                                                                                totalPoints += incomeGroup2Points;
                                                                            } else if (member.getIncome() > 60000 && member.getIncome() <= 100000) {
                                                                                out.println("(" + incomeGroup3Points + ")");
                                                                                totalPoints += incomeGroup3Points;
                                                                            } else if (member.getIncome() > 100000) {
                                                                                out.println("(" + incomeGroup4Points + ")");
                                                                                totalPoints += incomeGroup4Points;
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (member.getCity() != null) {
                                                                                out.print(member.getCity());
                                                                            }
                                                                        %>

                                                                        <%
                                                                            if (member.getCity() != null) {
                                                                                if (member.getCity().equalsIgnoreCase("Malaysia")) {
                                                                                    out.println("(" + countryGroup2Points + ")");
                                                                                    totalPoints += countryGroup2Points;
                                                                                } else if (member.getCity().equalsIgnoreCase("Singapore")) {
                                                                                    out.println("(" + countryGroup1Points + ")");
                                                                                    totalPoints += countryGroup1Points;
                                                                                }
                                                                            } else {
                                                                                out.print("this member no city");
                                                                            }

                                                                        %>
                                                                    </td>

                                                                    <td>
                                                                        <%                                                                            if (member.getJoinDate() != null) {
                                                                                out.print(dateFormat.format(member.getJoinDate()));
                                                                            }

                                                                        %>
                                                                        <%                                                                            Calendar c = Calendar.getInstance();
                                                                            Date date = new Date();
                                                                            c.setTime(date);
                                                                            c.add(Calendar.DATE, (-365));
                                                                            Date churnDate = c.getTime();
                                                                            
                                                                            if (member.getJoinDate() != null) {
                                                                                Long days = member.getJoinDate().getTime() - churnDate.getTime();
                                                                                days = TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
                                                                                if (member.getJoinDate().getTime() > churnDate.getTime()) {
                                                                                    out.println("(" + joinDateGroup1Points + ")");
                                                                                    totalPoints += joinDateGroup1Points;
                                                                                } else {
                                                                                    c.add(Calendar.DATE, (-365));
                                                                                    churnDate = c.getTime();

                                                                                    if (member.getJoinDate().getTime() > churnDate.getTime()) {
                                                                                        out.println("(" + joinDateGroup2Points + ")");
                                                                                        totalPoints += joinDateGroup2Points;
                                                                                    } else {
                                                                                        c.add(Calendar.DATE, (-365));
                                                                                        churnDate = c.getTime();

                                                                                        if (member.getJoinDate().getTime() > churnDate.getTime()) {
                                                                                            out.println("(" + joinDateGroup3Points + ")");
                                                                                            totalPoints += joinDateGroup3Points;
                                                                                        } else {
                                                                                            c.add(Calendar.DATE, (-365));
                                                                                            churnDate = c.getTime();

                                                                                            if (member.getJoinDate().getTime() > churnDate.getTime()) {
                                                                                                out.println("(" + joinDateGroup4Points + ")");
                                                                                                totalPoints += joinDateGroup4Points;
                                                                                            } else {
                                                                                                out.println("(5)");
                                                                                                totalPoints += 5;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                out.print("this member no join date");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%=totalPoints%>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12">                                                 
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Send Loyalty Points</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">    
                                        </div>

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
                        <p id="messageBox">Enter Loyalty Amount : 
                            <input type="number" name="loyaltyPoints"></p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="sendLoyaltyPoints()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
    <script>
        $(document).ready(function() {
            $('#dataTables-example').dataTable();
        });
        new Morris.Bar({
            // ID of the element in which to draw the chart.
            element: 'ageGroupChart',
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.


            data: [
                {y: '18-25', a: <%=cumulativeSpendingAgeGrp1%>, b: <%=numOfMembersInAgeGroup1%>, c: <%=cumulativeSpendingAgeGrp1 / numOfMembersInAgeGroup1%>},
                {y: '26-40', a: <%=cumulativeSpendingAgeGrp2%>, b: <%=numOfMembersInAgeGroup2%>, c: <%=cumulativeSpendingAgeGrp2 / numOfMembersInAgeGroup2%>},
                {y: '41-55', a: <%=cumulativeSpendingAgeGrp3%>, b: <%=numOfMembersInAgeGroup3%>, c: <%=cumulativeSpendingAgeGrp3 / numOfMembersInAgeGroup3%>},
                {y: '56-75', a: <%=cumulativeSpendingAgeGrp4%>, b: <%=numOfMembersInAgeGroup4%>, c: <%=cumulativeSpendingAgeGrp4 / numOfMembersInAgeGroup4%>}
            ],
            xkey: 'y',
            ykeys: ['a', 'b', 'c'],
            labels: ['Total Revenue', 'Num Of Members', 'Avg Value']
        });</script>

    <script>
        new Morris.Bar({
            // ID of the element in which to draw the chart.
            element: 'incomeGroupChart',
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.


            data: [
                {y: '30k', a: <%=cumulativeSpendingIncomeGrp1%>, b: <%=numOfMembersInIncomeGroup1%>, c: <%=cumulativeSpendingIncomeGrp1 / numOfMembersInIncomeGroup1%>},
                {y: '60k', a: <%=cumulativeSpendingIncomeGrp2%>, b: <%=numOfMembersInIncomeGroup2%>, c: <%=cumulativeSpendingIncomeGrp2 / numOfMembersInIncomeGroup2%>},
                {y: '100k', a: <%=cumulativeSpendingIncomeGrp3%>, b: <%=numOfMembersInIncomeGroup3%>, c: <%=cumulativeSpendingIncomeGrp3 / numOfMembersInIncomeGroup3%>},
                {y: '250k', a: <%=cumulativeSpendingIncomeGrp4%>, b: <%=numOfMembersInIncomeGroup4%>, c: <%=cumulativeSpendingIncomeGrp4 / numOfMembersInIncomeGroup4%>}
            ],
            xkey: 'y',
            ykeys: ['a', 'b', 'c'],
            labels: ['Total Revenue', 'Num Of Members', 'Avg Value']
        });</script>
    <script>
        new Morris.Bar({
            // ID of the element in which to draw the chart.
            element: 'countryChart',
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.


            data: [
                {y: 'Singapore', a: <%=totalCumulativeSpendingOfCountry1%>, b: <%=numOfMembersInCountry1%>, c: <%=totalCumulativeSpendingOfCountry1 / numOfMembersInCountry1%>},
                {y: 'Malaysia', a: <%=totalCumulativeSpendingOfCountry2%>, b: <%=numOfMembersInCountry2%>, c: <%=totalCumulativeSpendingOfCountry2 / numOfMembersInCountry2%>},
            ],
            xkey: 'y',
            ykeys: ['a', 'b', 'c'],
            labels: ['Total Revenue', 'Num Of Members', 'Avg Value']
        });</script>
    <script>
        new Morris.Bar({
            // ID of the element in which to draw the chart.
            element: 'joinDateChart',
            // Chart data records -- each entry in this array corresponds to a point on
            // the chart.


            data: [
                {y: '1 yr', a: <%=getRevenueOfJoinDate1%>, b: <%=numOfMembersInJoinDate1%>, c: <%=getRevenueOfJoinDate1 / numOfMembersInJoinDate1%>},
                {y: '2 yr', a: <%=getRevenueOfJoinDate2%>, b: <%=numOfMembersInJoinDate2%>, c: <%=getRevenueOfJoinDate2 / numOfMembersInJoinDate2%>},
                {y: '3 yr', a: <%=getRevenueOfJoinDate3%>, b: <%=numOfMembersInJoinDate3%>, c: <%=getRevenueOfJoinDate3 / numOfMembersInJoinDate3%>},
                {y: '4 yr', a: <%=getRevenueOfJoinDate4%>, b: <%=numOfMembersInJoinDate4%>, c: <%=getRevenueOfJoinDate4 / numOfMembersInJoinDate4%>},
            ],
            xkey: 'y',
            ykeys: ['a', 'b', 'c'],
            labels: ['Total Revenue', 'Num Of Members', 'Avg Value']
        });
    </script>
</body>
</html>
