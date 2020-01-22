<%@page import="EntityManager.StaffEntity"%>
<%@page import="HelperClasses.MessageHelper"%>
<%@page import="EntityManager.MessageEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function viewInbox() {
                document.location.href = "../WorkspaceMessage_Servlet?view=inbox";
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                String view = (String) session.getAttribute("view");
                if (view == null) {
                    response.sendRedirect("../WorkspaceMessage_Servlet");
                } else {
            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Read Message</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../Workspace_Servlet">Workspace</a>
                                </li>
                                <li>
                                    <i class="icon icon-inbox"></i> <a href="../WorkspaceMessage_Servlet">Messages</a>
                                </li>
                                <li>
                                    <i class="icon icon-inbox"></i> <a href="../WorkspaceMessage_Servlet?view=inbox">Inbox</a>
                                </li>
                                <li class="active">
                                    <i class ="icon icon-envelope"></i> Read Message
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%
                        try {
                            MessageHelper messageHelper = (MessageHelper) (session.getAttribute("message"));
                    %>
                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../WorkspaceMessage_RemoveServlet">
                                <input type="hidden" name="deleteMessageType" value="inbox" />
                                <input type="hidden" name="messageID" value="<%=messageHelper.getMessageId()%>" />
                                <div class="form-group">
                                    <label>From</label>
                                    <input class="form-control" name="from" type="text" value="<%=messageHelper.getSenderName()%>" disabled/>
                                </div>
                                <div class="form-group">
                                    <label>Sent</label>
                                    <input class="form-control" name="sent date" type="text" value="<%=messageHelper.getSentDate()%>" disabled>
                                </div>
                                <div class="form-group">
                                    <label>To</label>
                                    <%
                                        String receiverDisplayString = "";
                                        for (int i = 0; i < messageHelper.getReceiversName().size(); i++) {
                                            receiverDisplayString += messageHelper.getReceiversName().get(i);
                                            receiverDisplayString += " <" + messageHelper.getReceiversEmail().get(i) + ">;";
                                        }
                                    %>
                                    <input class="form-control" required="true" type="text" value="<%=receiverDisplayString%>" disabled/>
                                </div>
                                <div>
                                    <label>Message</label><br/>
                                    <%=messageHelper.getMessage()%>
                                    <br/><br/>
                                </div>

                                <div class="form-group">
                                    <button type="button" class="btn btn-lg btn-primary btn-block" onclick="javascript:viewInbox()">Back to Inbox</button>
                                    <input type="submit" value="Delete" class="btn btn-lg btn-primary btn-block">
                                </div>
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../WorkspaceMessage_Servlet");
                            ex.printStackTrace();
                        }%>

                </div>

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
<%}%>