<%@page import="EntityManager.AnnouncementEntity"%>
<%@page import="EntityManager.ToDoEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.MessageEntity"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>

        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%  
                StaffEntity staffEntity = (StaffEntity) (session.getAttribute("staffEntity"));
                if (staffEntity != null) {                 
                    List<AnnouncementEntity> announcement = (List<AnnouncementEntity>) (session.getAttribute("listOfAnnouncements"));
                    List<MessageEntity> unreadMessages = (List<MessageEntity>) (session.getAttribute("unreadMessages"));
                    List<MessageEntity> inbox = (List<MessageEntity>) (session.getAttribute("inboxMessages"));
                    List<ToDoEntity> toDoList = (List<ToDoEntity>) (session.getAttribute("toDoList"));
                    if (announcement == null)
                        announcement = new ArrayList();
                    if (unreadMessages == null)
                        unreadMessages = new ArrayList();
                    if (inbox == null)
                        inbox = new ArrayList();
                    if(toDoList == null)
                        toDoList = new ArrayList();
            %>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Holla! <%=staffEntity.getName()%></h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-dashboard"></i> Workspace
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->


                    <div class="row">
                        <div class="col-lg-3 col-md-6">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <div class="row">
                                        <div class="col-xs-3">
                                            <i class="icon icon-bell icon-5x"></i>
                                        </div>
                                        <div class="col-xs-9 text-right">
                                            <div class="huge"><%=announcement.size()%></div>
                                            <div>Announcement<%if(announcement.size()!=1){%>s<%}%></div>
                                        </div>
                                    </div>
                                </div>
                                <a href="../WorkspaceAnnouncement_Servlet">
                                    <div class="panel-footer" style="padding-bottom: 30px;">
                                        <span class="pull-left">View Details</span>
                                        <span class="pull-right"><i class="icon icon-arrow-circle-right"></i></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="panel panel-green">
                                <div class="panel-heading">
                                    <div class="row">
                                        <div class="col-xs-3">
                                            <i class="icon icon-comments icon-5x"></i>
                                        </div>
                                        <div class="col-xs-9 text-right">
                                            <% if(unreadMessages.size()>0) { %>
                                            <div class="huge"><%=unreadMessages.size()%></div>
                                            <div>New Message<%if(inbox.size()!=1){%>s<%}%>!</div>
                                            <%} else { %>
                                            <div class="huge"><%=inbox.size()%></div>
                                            <div>Message<%if(inbox.size()!=1){%>s<%}%> in Inbox</div>
                                            <%}%>
                                        </div>
                                    </div>
                                </div>
                                <a href="../WorkspaceMessage_Servlet">
                                    <div class="panel-footer" style="padding-bottom: 30px;">
                                        <span class="pull-left">View Details</span>
                                        <span class="pull-right"><i class="icon icon-arrow-circle-right"></i></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="panel panel-yellow">
                                <div class="panel-heading">
                                    <div class="row">
                                        <div class="col-xs-3">
                                            <i class="icon icon-list icon-5x"></i>
                                        </div>
                                        <div class="col-xs-9 text-right">
                                            <div class="huge"><%=toDoList.size()%></div>
                                            <div>Item<%if(toDoList.size()!=1){%>s<%}%> in Todo List</div>
                                        </div>
                                    </div>
                                </div>
                                <a href="../WorkspaceToDoList_Servlet">
                                    <div class="panel-footer" style="padding-bottom: 30px;">
                                        <span class="pull-left">View Details</span>
                                        <span class="pull-right"><i class="icon icon-arrow-circle-right"></i></span>
                                        <div class="clearfix"></div>
                                    </div>
                                </a>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->
        <%}%>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });
        </script>

    </body>

</html>
