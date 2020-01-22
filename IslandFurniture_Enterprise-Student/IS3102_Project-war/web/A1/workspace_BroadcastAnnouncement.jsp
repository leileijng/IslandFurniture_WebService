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
                                Broadcast Announcement
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-dashboard"></i> <a href="../Workspace_Servlet">Workspace</a>
                                </li>
                                <li>
                                    <i class="icon icon-bell"></i> <a href="../WorkspaceAnnouncement_Servlet">Announcement</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-bullhorn"></i> Broadcast Announcement
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessage.jsp" />
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../WorkspaceAnnouncement_AddServlet">
                                <div class="form-group">
                                    <label>Sender</label>
                                    <input class="form-control" name="sender" type="text" required="true" value="Announcement Messenger">
                                </div>
                                <div class="form-group">
                                    <label>Title</label>
                                    <input class="form-control" required="true" type="text" name="title" >
                                </div>
                                <div class="form-group">
                                    <label>Message</label>
                                    <input class="form-control" required="true" type="text" name="message" >
                                </div>
                                <div class="form-group">
                                    <label>Expiry Date</label>
                                    <input class="form-control" required="true" type="Date" name="expiryDate" min="today">
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Broadcast Message" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="A1/workspace_viewAnnouncement.jsp" name="source">
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
    <script>
    var today = new Date().toISOString().split('T')[0];
    document.getElementsByName("expiryDate")[0].setAttribute('min', today);
    </script>
    </body>
</html>
  
