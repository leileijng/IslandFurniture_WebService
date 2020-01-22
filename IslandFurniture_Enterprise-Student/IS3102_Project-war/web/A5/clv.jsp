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
                    document.clv.action = "../Analytical_ValueAnalysisServlet";
                    document.clv.submit();
                } else {
                    window.event.returnValue = true;
                    document.clv.action = "../Analytical_ValueAnalysisSendLoyaltyCLVServlet";
                    document.clv.submit();
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
                                    <i class="icon icon-user"></i> Customer Lifetime Value
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
                                            out.println("Customer Lifetime Value");
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
                                    <form name="clv">
                                </div>
                                <!-- /.panel-heading -->
                                <form name="staffManagement">
                                    <%
                                        List<MemberEntity> members = (List<MemberEntity>) (session.getAttribute("members"));
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        DecimalFormat noDecimal = new DecimalFormat("#");

                                    %>
                                    <!-- /.table-responsive -->
                                    <div class="row">
                                        <div class="col-lg-12">

                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <td>

                                                        </td>
                                                        <th>
                                                            Acquisition Year
                                                        </th>
                                                        <th>
                                                            Second Year
                                                        </th>

                                                    </tr>
                                                </thead>
                                                <tr>
                                                    <td>
                                                        Customers
                                                    </td>
                                                    <td>
                                                        <%                                                                Double customerRetentionRate = (Double) session.getAttribute("customerRetentionRate");

                                                        %>
                                                        <%=members.size()%>
                                                    </td>
                                                    <td>
                                                        <%=noDecimal.format(customerRetentionRate * members.size())%>
                                                    </td>

                                                </tr>
                                                <tr>
                                                    <td>
                                                        Retention Rate
                                                    </td>
                                                    <td>

                                                        <% out.print(df.format(customerRetentionRate * 100));%>%

                                                    </td>
                                                    <td>
                                                        <%
                                                            Double getRetainedCustomerRetentionRate = (Double) session.getAttribute("getRetainedCustomerRetentionRate");
                                                        %>
                                                        <% out.print(df.format(getRetainedCustomerRetentionRate * 100));%>%
                                                    </td>

                                                </tr>
                                                <tr>
                                                    <td>
                                                        Orders per Year
                                                    </td>
                                                    <td>
                                                        <% Double averageOrdersPerAcquiredYear = (Double) session.getAttribute("averageOrdersPerAcquiredYear"); %>
                                                        <% out.print(df.format(averageOrdersPerAcquiredYear));%>
                                                    </td>
                                                    <td>
                                                        <%
                                                            Double averageOrdersPerRetainedMember = (Double) session.getAttribute("averageOrdersPerRetainedMember");
                                                            out.print(df.format(averageOrdersPerRetainedMember));
                                                        %>
                                                    </td>

                                                </tr>

                                                <tr>
                                                    <td>
                                                        Avg Order Price (USD)
                                                    </td>
                                                    <td>
                                                        <% Double averageOrderPriceInAcquiredYear = (Double) session.getAttribute("averageOrderPriceInAcquiredYear"); %>
                                                        <% out.print(df.format(averageOrderPriceInAcquiredYear));%>
                                                    </td>
                                                    <td>
                                                        <% Double averageOrderPriceForRetainedMembers = (Double) session.getAttribute("averageOrderPriceForRetainedMembers"); %>
                                                        <% out.print(df.format(averageOrderPriceForRetainedMembers)); %>
                                                    </td>

                                                </tr>

                                                <tr>
                                                    <td>
                                                        Profit Margin
                                                    </td>
                                                    <td>
                                                        <input type="button" class="minus" value="-" onclick="minus()">
                                                        <input type="number" value="20" id="profitMargin"/>%
                                                        <input type="button" class="plus" value="+" onclick="plus()">
                                                    </td>
                                                    <td>
                                                        <input type="button" class="minus" value="-" onclick="minus2()">
                                                        <input type="number" value="20" id="profitMargin2"/>%
                                                        <input type="button" class="plus" value="+" onclick="plus2()">
                                                    </td>

                                                </tr>

                                                <tr>
                                                    <td>
                                                        Customer LTV (USD)
                                                    </td>
                                                    <td>
                                                        <p id="acquiredYearLTV">
                                                            <%out.print(df.format(averageOrdersPerAcquiredYear * averageOrderPriceInAcquiredYear * 0.2 * customerRetentionRate));%>
                                                        </p>
                                                    </td>
                                                    <td>
                                                        <p id="acquiredYearLTV2">
                                                            <%out.print(df.format(averageOrdersPerRetainedMember * averageOrderPriceForRetainedMembers * 0.2 * getRetainedCustomerRetentionRate));%>
                                                        </p>
                                                    </td>

                                                </tr>
                                            </table>

                                            <table class ="table">
                                                <thead>
                                                    <tr><td></td><th>Estimated Lifetime</th><th>Avg Monetary Value</th><th>LifeTime Value (USD)</th></tr>
                                                </thead>
                                                <tr>
                                                    <td></td><td><%
                                                        Double getEstimatedCustomerLife = (Double) (session.getAttribute("getEstimatedCustomerLife"));
                                                        out.println(df.format(getEstimatedCustomerLife) + " years");
                                                        %></td>
                                                    <td>
                                                        <%
                                                            Integer avgMonetaryValue = (Integer) (session.getAttribute("avgMonetaryValue"));
                                                            out.println(df.format(avgMonetaryValue));
                                                        %>

                                                    </td>
                                                    <td>

                                                        <%
                                                            Integer averageMemberMonetaryValue = (Integer) session.getAttribute("averageMemberMonetaryValue");
                                                        %>
                                                        <p id="lifeTimeValue">
                                                            <%
                                                                out.print(df.format(averageMemberMonetaryValue * getEstimatedCustomerLife * 0.2));
                                                            %></p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <div id="products" class="tab-pane">
                                        </div>
                                    </div>
                            </div>

                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" member="grid">
                                <table class="table table-bordered" id="dataTables-example">
                                    <thead>
                                        <tr>
                                            <th><input type="checkbox"onclick="checkAll(this)" /></th>
                                            <th>Name</th>
                                            <th>Num of Orders</th>
                                            <th>Avg Order Price (USD)</th>
                                            <th>Monetary Value (USD)</th>
                                            <th>Cumulative Customer Value (USD)</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
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
                                            <td>
                                                <%
                                                    out.print(member.getPurchases().size());
                                                %>
                                            </td>
                                            <td>
                                                <%
                                                    Double totalSalesOfMember = (double) 0;
                                                    for (int j = 0; j < member.getPurchases().size(); j++) {
                                                        Double exchangeRate = member.getPurchases().get(j).getStore().getCountry().getExchangeRate();
                                                        Double amountDue = member.getPurchases().get(j).getAmountDue();
                                                        Double amountDueInUSD = amountDue / exchangeRate;
                                                        totalSalesOfMember += amountDueInUSD;
                                                    }
                                                    if (member.getPurchases().size() == 0) {
                                                        out.println(0.00);
                                                    } else {
                                                        out.print(df.format(totalSalesOfMember / member.getPurchases().size()));
                                                    }
                                                %>
                                            </td>
                                            <td>
                                                <%
                                                    out.print(df.format(totalSalesOfMember));
                                                %>
                                            </td>

                                            <td>
                                                <% out.print(df.format(customerRetentionRate * totalSalesOfMember * 0.2)); %>
                                            </td>

                                        </tr>
                                        <%
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
    $(document).ready(function () {
        $('#dataTables-example').dataTable();
    });

    function minus() {
        var profitMargin = document.getElementById("profitMargin").value;
        if (profitMargin > 1) {
            document.getElementById("profitMargin").value--;
            var acquiredYearLTV = <%=averageOrdersPerAcquiredYear * averageOrderPriceInAcquiredYear%> * (document.getElementById("profitMargin").value / 100) * <%=customerRetentionRate%>;
            document.getElementById("acquiredYearLTV").innerHTML = parseFloat(Math.round(acquiredYearLTV * 100) / 100).toFixed(2);
            var lifeTimeValue = (document.getElementById("profitMargin").value / 100) * <%=getEstimatedCustomerLife * averageMemberMonetaryValue%>;
            document.getElementById("lifeTimeValue").innerHTML = parseFloat(Math.round(lifeTimeValue * 100) / 100).toFixed(2);
        }
    }
    function plus() {
        var profitMargin = document.getElementById("profitMargin").value;
        document.getElementById("profitMargin").value++;
        var acquiredYearLTV = <%=averageOrdersPerAcquiredYear * averageOrderPriceInAcquiredYear%> * (document.getElementById("profitMargin").value / 100) * <%=customerRetentionRate%>;
        document.getElementById("acquiredYearLTV").innerHTML = parseFloat(Math.round(acquiredYearLTV * 100) / 100).toFixed(2);
        var lifeTimeValue = (document.getElementById("profitMargin").value / 100) * <%=getEstimatedCustomerLife * averageMemberMonetaryValue%>;
        document.getElementById("lifeTimeValue").innerHTML = parseFloat(Math.round(lifeTimeValue * 100) / 100).toFixed(2);
    }

    function minus2() {
        var profitMargin = document.getElementById("profitMargin2").value;
        if (profitMargin > 1) {
            document.getElementById("profitMargin2").value--;
            var acquiredYearLTV = <%=averageOrdersPerRetainedMember * averageOrderPriceForRetainedMembers%> * (document.getElementById("profitMargin2").value / 100) * <%=getRetainedCustomerRetentionRate%>;
            document.getElementById("acquiredYearLTV2").innerHTML = parseFloat(Math.round(acquiredYearLTV * 100) / 100).toFixed(2);

        }
    }
    function plus2() {
        var profitMargin = document.getElementById("profitMargin2").value;
        document.getElementById("profitMargin2").value++;
        var acquiredYearLTV = <%=averageOrdersPerRetainedMember * averageOrderPriceForRetainedMembers%> * (document.getElementById("profitMargin2").value / 100) * <%=getRetainedCustomerRetentionRate%>;
        document.getElementById("acquiredYearLTV2").innerHTML = parseFloat(Math.round(acquiredYearLTV * 100) / 100).toFixed(2);
    }
</script>
</body>
</html>
