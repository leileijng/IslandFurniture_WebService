<%@page import="EntityManager.StaffEntity"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <script>
            function selectUsers() {
                document.composeMessage.action = "../WorkspaceMessage_SearchUsersServlet";
                document.composeMessage.submit();
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <% String receiversString = (String) session.getAttribute("receiversString");
                if (receiversString == null) {
                    receiversString = "";
                }
                StaffEntity staffEntity = (StaffEntity) (session.getAttribute("staffEntity"));
                String sender = staffEntity.getName()+" <"+staffEntity.getEmail()+">;";
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Compose and Send Message</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i> <a href="../Workspace_Servlet">Workspace</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-inbox"></i> <a href="../WorkspaceMessage_Servlet?view=inbox">Inbox</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-envelope"></i> Compose and Send Message
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />

                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" name="composeMessage" action="../WorkspaceMessage_AddServlet">
                                <div class="form-group">
                                    <label>From</label>
                                    <input class="form-control" name="name" type="text" required="true" value="<%=sender%>" disabled>
                                           </div>
                                           <div class="form-group">
                                           <label>To</label>&nbsp;<i class="icon icon-arrow-circle-right" onclick="selectUsers()"></i>
                                    <input class="form-control" name="receiver" type="text" required="true" onfocus="selectUsers()" value="<%=receiversString%>"<% if (false) {%>disabled<%}%>>  
                                    <a href="#" onclick="selectUsers()">View Staff Directory</a>
                                </div>
                                <div class="form-group">
                                    <label>Message</label>
                                    <input class="form-control" required="true" type="text" name="message"/>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Send Message" class="btn btn-lg btn-primary btn-block">
                                </div>
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->


        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
