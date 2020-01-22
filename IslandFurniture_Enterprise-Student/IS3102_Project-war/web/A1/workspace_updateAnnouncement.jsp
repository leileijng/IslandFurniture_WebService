<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.AnnouncementEntity"%>
<%@page import="java.util.List"%>
<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Announcement Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i> <a href="../Workspace_Servlet">Workspace</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-bell"></i> <a href="workspace_viewAnnouncement.jsp">View Announcements</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Announcement Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessage.jsp" />
                    <%

                        try {
                            String id = request.getParameter("id");
                            List<AnnouncementEntity> listOfAnnouncements = (List<AnnouncementEntity>) (session.getAttribute("listOfAnnouncements"));
                            AnnouncementEntity announcement = new AnnouncementEntity();
                            for (int i = 0; i < listOfAnnouncements.size(); i++) {
                                if (listOfAnnouncements.get(i).getId() == Integer.parseInt(id)) {
                                    announcement = listOfAnnouncements.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../WorkspaceAnnouncement_UpdateServlet">
                                <div class="form-group">
                                    <label>Sender</label>
                                    <input class="form-control" name="sender" type="text" value="<%=announcement.getSender()%>" disabled/>
                                </div>
                                <div class="form-group">
                                    <label>Title</label>
                                    <input class="form-control" name="title" type="text" value="<%=announcement.getTitle()%>">
                                </div>
                                <div class="form-group">
                                    <label>Message</label>
                                    <input class="form-control" required="true" type="text" name="message" value="<%=announcement.getMessage()%>"/>
                                </div>
                                <div class="form-group">
                                    <label>Expiry Date</label>
                                    <% DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        String formatedExpiryDate = df.format(announcement.getExpiryDate());%>
                                    <input class="form-control" required="true" type="date" name="expiryDate" value="<%=formatedExpiryDate%>"/>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=announcement.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../WorkspaceAnnouncement_Servlet");
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
        <script>
            var today = new Date().toISOString().split('T')[0];
            document.getElementsByName("expiryDate")[0].setAttribute('min', today);
        </script>
    </body>

</html>
