<%@page import="EntityManager.MemberEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="java.text.DecimalFormat"%>
<html lang="en">
    <jsp:include page="../header2.html" />

    <body>

        <script>
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
                    document.rfm.action = "../Analytical_ValueAnalysisServlet";
                    document.rfm.submit();
                } else {
                    window.event.returnValue = true;
                    document.rfm.action = "../Analytical_ValueAnalysisSendLoyaltyServlet";
                    document.rfm.submit();
                }
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
                            <h1 class="page-header">Value Analysis</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="analytical.jsp">Analytical CRM</a>
                                </li>
                                <li>
                                    <i class="icon icon-user"></i> <a href="../Analytical_ValueAnalysisServlet">Value Analysis</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="rfmSelect.jsp">RFM Selection</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i> Recency, Frequency & Monetary
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
                                            out.println("Recency, Frequency & Monetary Analysis on Furnitures");
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
                                <form name="rfm">
                                            <%
                                                List<MemberEntity> members = (List<MemberEntity>) (session.getAttribute("members"));
                                            %>
                                            <!-- /.table-responsive -->

                                            <% Integer averageMemberRecency = (Integer) session.getAttribute("averageMemberRecency");
                                                Integer averageMemberFrequency = (Integer) session.getAttribute("averageMemberFrequency");
                                                Integer averageMemberMonetaryValue = (Integer) session.getAttribute("averageMemberMonetaryValue");
                                            %>
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <div class="panel-body">
                                                <div class="table-responsive">

                                                    <div class="col-lg-12">
                                                        <div class="row">

                                                            <div class="pricing-table">
                                                                <div class="col-md-4">
                                                                    <div class="plan">
                                                                        <h3>Recency<span><%=averageMemberRecency%></span></h3>
                                                                        <ul>
                                                                            <li><b>Average Recency</b> <%=averageMemberRecency%> days</li>
                                                                            <li><b>25th Percentile</b> <%=(averageMemberRecency / 2) + averageMemberRecency%></li>
                                                                            <li><b>75th Percentile</b> <%=averageMemberRecency / 2%></li>
                                                                            <li><b>Recency</b> is the average last purchase per member away from today</li>
                                                                        </ul>
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <div class="plan">
                                                                        <h3>Frequency<span><%=averageMemberFrequency%></span></h3>
                                                                        <ul>
                                                                            <li><b>Average Frequency</b> <%=averageMemberFrequency%> purchases</li>
                                                                            <li><b>25th Percentile</b> <%=averageMemberFrequency / 2%></li>
                                                                            <li><b>75th Percentile</b> <%=(averageMemberFrequency / 2) + averageMemberFrequency%></li>
                                                                            <li><b>Frequency</b> is the number of purchases per member through their membership</li>
                                                                        </ul>
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-4">
                                                                    <div class="plan">
                                                                        <h3>Monetary<span><%=averageMemberMonetaryValue%></span></h3>
                                                                        <ul>
                                                                            <li><b>Average Monetary</b> $<%=averageMemberMonetaryValue%></li>
                                                                            <li><b>25th Percentile</b> <%=averageMemberMonetaryValue / 2%></li>
                                                                            <li><b>75th Percentile</b> <%=(averageMemberMonetaryValue / 2) + averageMemberMonetaryValue%></li>
                                                                            <li><b>Monetary</b> is the average total spending per member</li>
                                                                        </ul>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                        </div>

                                                    </div>

                                                    <br>
                                                    <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" member="grid">
                                                        <table class="table table-bordered" id="dataTables-example">
                                                            <thead>
                                                                <tr>
                                                                    <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                                                    <th>Name</th>
                                                                    <th>Recency</th>
                                                                    <th>Frequency</th>
                                                                    <th>Monetary Value (USD)</th>
                                                                    <th>Total Value</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%
                                                                    List<Integer> memberRecencyValue = (List<Integer>) session.getAttribute("memberRecencyValue");
                                                                    List<Integer> memberFrequencyValue = (List<Integer>) session.getAttribute("memberFrequencyValue");
                                                                    List<Integer> memberMonetaryValue = (List<Integer>) session.getAttribute("memberMonetaryValue");
                                                                    if (members != null) {
                                                                        for (int i = 0; i < members.size(); i++) {
                                                                            MemberEntity member = members.get(i);
                                                                %>
                                                                <tr>                   
                                                                    <td>
                                                                        <input type="checkbox" name="delete" value="<%=member.getId()%>" />
                                                                    </td>
                                                                    <td>
                                                                        <%=member.getName()%>
                                                                    </td>
                                                                    <td <% 
                                                                    if (memberFrequencyValue.get(i) == 0 && memberMonetaryValue.get(i) == 0) {
                                                                        %>bgcolor="#ebccd1"<%
                                                                    }
                                                                    else if (memberRecencyValue.get(i) < (averageMemberRecency / 2)) {
                                                                        %>bgcolor="#bce8f1"<%
                                                                        } else if (memberRecencyValue.get(i) > ((averageMemberRecency / 2) + averageMemberRecency)) {
                                                                        %>
                                                                        bgcolor="#ebccd1"
                                                                        <% } else { %>
                                                                        bgcolor="#faebcc"
                                                                        <%
                                                                            }
                                                                        %>
                                                                        >
                                                                        <%=memberRecencyValue.get(i)%>
                                                                    </td>
                                                                    <td
                                                                        <% if (memberFrequencyValue.get(i) <= (averageMemberFrequency / 2)) {
                                                                        %>bgcolor="#ebccd1"<%
                                                                        } else if (memberFrequencyValue.get(i) > ((averageMemberFrequency / 2) + averageMemberFrequency)) {
                                                                        %>
                                                                        bgcolor="#bce8f1"
                                                                        <% } else { %>
                                                                        bgcolor="#faebcc"
                                                                        <%
                                                                            }
                                                                        %>
                                                                        >
                                                                        <%=memberFrequencyValue.get(i)%>
                                                                    </td>
                                                                    <td
                                                                        <% if (memberMonetaryValue.get(i) < (averageMemberMonetaryValue / 2)) {
                                                                        %>bgcolor="#ebccd1"<%
                                                                        } else if (memberMonetaryValue.get(i) > ((averageMemberMonetaryValue / 2) + averageMemberMonetaryValue)) {
                                                                        %>
                                                                        bgcolor="#bce8f1"
                                                                        <% } else { %>
                                                                        bgcolor="#faebcc"
                                                                        <%
                                                                            }
                                                                        %>
                                                                        >
                                                                        <%=memberMonetaryValue.get(i)%>
                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                        Integer weightedValue = 0;
                                                                        if (memberMonetaryValue.get(i) == 0) {
                                                                            
                                                                        } else if (memberMonetaryValue.get(i) < (averageMemberMonetaryValue/2)) {
                                                                            weightedValue += 1;
                                                                        } else if (memberMonetaryValue.get(i) > ((averageMemberMonetaryValue / 2) + averageMemberMonetaryValue)) {
                                                                            weightedValue += 3;
                                                                        } else {
                                                                            weightedValue += 2;
                                                                        }
                                                                        
                                                                        if (memberFrequencyValue.get(i) == 0) {
                                                                            
                                                                        } else if (memberFrequencyValue.get(i) <= (averageMemberFrequency / 2)) {
                                                                            weightedValue += 1;
                                                                        } else if (memberFrequencyValue.get(i) > ((averageMemberFrequency / 2) + averageMemberFrequency)) {
                                                                            weightedValue += 3;
                                                                        } else {
                                                                            weightedValue += 2;
                                                                        }
                                                                        
                                                                        if (memberFrequencyValue.get(i) == 0 && memberMonetaryValue.get(i) ==0) {
                                                                            
                                                                        } else if (memberRecencyValue.get(i) < (averageMemberRecency / 2)) {
                                                                            weightedValue += 3;
                                                                        } else if (memberRecencyValue.get(i) > ((averageMemberRecency / 2) + averageMemberRecency)) {
                                                                            weightedValue += 1;
                                                                        } else {
                                                                            weightedValue +=2;
                                                                        }
                                                                        out.print(weightedValue + "/9");
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                        }
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                    <!-- /.table-responsive -->
                                                    <div class="row">
                                                        <div class="col-md-12">                                                 
                                                            <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Send Loyalty Points</button></a>
                                                        </div>
                                                    </div>
                                                    <input type="hidden" name="id" value="">    
                                                </div>

                                            </div>
                                        </div>

                                        <div id="products" class="tab-pane">
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
        </script>
    </body>
</html>
