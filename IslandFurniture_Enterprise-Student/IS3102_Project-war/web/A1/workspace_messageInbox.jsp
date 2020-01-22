<%@page import="HelperClasses.MessageHelper"%>
<%@page import="java.text.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function readMessage(id) {
                messageManagement.id.value = id;
                document.messageManagement.action = "../WorkspaceMessage_ViewServlet";
                document.messageManagement.submit();
            }
            function deleteMessage() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.messageManagement.action = "../WorkspaceMessage_Servlet";
                    document.messageManagement.submit();
                } else {
                    window.event.returnValue = true;
                    document.messageManagement.action = "../WorkspaceMessage_RemoveServlet";
                    document.messageManagement.submit();
                }
            }
            function sendMessage() {
                window.event.returnValue = true;
                document.messageManagement.action = "workspace_messageAdd.jsp";
                document.messageManagement.submit();
            }
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function viewSentMsg() {
                document.location.href = "../WorkspaceMessage_Servlet?view=sentMessages";
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Inbox</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i>  <a href="../Workspace_Servlet">Workspace</a>
                                </li>
                                <li>
                                    <i class="icon icon-inbox"></i> <a href="../WorkspaceMessage_Servlet">Messages</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-inbox"></i> Inbox
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
                                        if (errMsg == null || errMsg.equals("")) {
                                            errMsg = "Create message, delete inbox message and view sent messages";
                                        }
                                        out.println(errMsg);
                                    %>
                                </div>
                                <!-- /.panel-heading -->
                                <form name="messageManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Message" onclick="sendMessage()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Message</button></a>
                                                    <input type="hidden" name="view" value="inbox"/>
                                                    <button type="button" class="btn btn-primary" onclick="javascript:viewSentMsg()">View Sent Messages</button>
                                                </div>
                                            </div>
                                            <br>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th><input type="checkbox" onclick="checkAll(this)" /></th>
                                                            <th>From</th>
                                                            <th>Date Received</th>
                                                            <th>To</th>
                                                            <th>Message</th>
                                                            <th>Open</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<MessageHelper> inbox = (List<MessageHelper>) (session.getAttribute("inboxMessagesHelpers"));
                                                            if (inbox != null) {
                                                                for (int i = 0; i < inbox.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="hidden" name="deleteMessageType" value="inbox" />
                                                                <input type="hidden" name="deleteType" value="many"/>
                                                                <input type="checkbox" name="delete" value="<%=inbox.get(i).getMessageId()%>" />
                                                            </td>
                                                            <td>
                                                                <% if (!inbox.get(i).isMessageRead()) {%><b><%}%>
                                                                    <%=inbox.get(i).getSenderName()%>
                                                                    <% if (!inbox.get(i).isMessageRead()) {%></b><%}%>
                                                            </td>
                                                            <td>
                                                                <% if (!inbox.get(i).isMessageRead()) {%><b><%}%>
                                                                    <%
                                                                        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                        String dateString = formatter.format(inbox.get(i).getSentDate());
                                                                        out.println(dateString);
                                                                    %>
                                                                    <% if (!inbox.get(i).isMessageRead()) {%></b><%}%>
                                                            </td>
                                                            <td>
                                                                <% if (!inbox.get(i).isMessageRead()) {%><b><%}%>
                                                                    <%
                                                                        List<String> receviers = (List<String>) (inbox.get(i).getReceiversName());
                                                                        if (receviers.isEmpty()) {
                                                                            out.println("-");
                                                                        } else {
                                                                            for (int k = 0; k < receviers.size(); k++) {
                                                                                out.println(receviers.get(k) + "; ");
                                                                            }
                                                                        }
                                                                    %>
                                                                    <% if (!inbox.get(i).isMessageRead()) {%></b><%}%>
                                                            </td>
                                                            <td>
                                                                <%
                                                                    if (inbox.get(i).getMessage().length() < 90) {
                                                                        out.print(inbox.get(i).getMessage());
                                                                    } else {
                                                                        out.print(inbox.get(i).getMessage().substring(0, 90) + "...");
                                                                    }
                                                                %>
                                                            </td>
                                                            <td>
                                                                <input type="button" name="btnEdit" class="btn btn-primary btn-block" value="Read" id="<%=inbox.get(i).getMessageId()%>" onclick="readMessage(<%=inbox.get(i).getMessageId()%>)" />
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
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Create Message" onclick="sendMessage()"  />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Message</button></a>

                                                    <input type="hidden" name="view" value="inbox"/>
                                                    <button type="button" class="btn btn-primary" onclick="javascript:viewSentMsg()">View Sent Messages</button>
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
                        <p id="messageBox">Message will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="deleteMessage()"  />
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
