<%@page import="EntityManager.MemberEntity"%>
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
                                Member Update
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-users"></i> <a href="accountManagement.jsp">Account Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-users"></i> <a href="memberManagement.jsp">Member Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Member Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <%

                        try {
                            String id = request.getParameter("id");
                            List<MemberEntity> members = (List<MemberEntity>) (session.getAttribute("members"));
                            MemberEntity member = new MemberEntity();
                            for (int i = 0; i < members.size(); i++) {
                                if (members.get(i).getId() == Integer.parseInt(id)) {
                                    member = members.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form member="form" action="../MemberManagement_UpdateMemberServlet">
                                <div class="form-group">
                                    <label>Member Name</label>
                                    <input class="form-control" name="name" type="text" value="<%=member.getName()%>" disabled/>
                                </div>
                                <div class="form-group">
                                    <label>Access Level</label>
                                    <input class="form-control" name="accessLevel" type="text" value="<%=member.getAddress()%>" required="true">
                                </div>                                

                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=member.getId()%>" name="id">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../MemberManagement_MemberServlet");
                        }%>

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
