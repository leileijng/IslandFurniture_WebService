<%@page import="EntityManager.ComboEntity"%>
<%@page import="java.util.List"%>
<%
    List<ComboEntity> combos = (List<ComboEntity>) (session.getAttribute("combos"));
    String id = request.getParameter("id");
    if (combos == null || id == null) {
        response.sendRedirect("../ComboManagement_ComboServlet");
    } else {
        ComboEntity combo = new ComboEntity();
        for (int i = 0; i < combos.size(); i++) {
            if (combos.get(i).getId() == Integer.parseInt(id)) {
                combo = combos.get(i);
            }
        }

%>
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
                                Add Combo Item
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="restaurantManagement.jsp">Restaurant Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-cogs"></i> <a href="comboManagement.jsp">Combo Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-cogs"></i> <a href="comboManagement_Update.jsp?id=<%=combo.getId()%>">Combo ID: <%=combo.getId()%></a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Combo Item
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />


                    <div class="row">
                        <div class="col-lg-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"> Combo ID: <%=combo.getId()%> - Add Combo Item </h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../ComboLineItemManagement_AddServlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <input id="auto" class="form-control" name="sku" type="text"  >
                                        </div>
                                     
                                        <div class="form-group">
                                            <input type="submit" value="Add Combo Item" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=combo.getId()%>" name="id">
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <!-- /#page-wrapper -->
            </div>
            <!-- /#container fluid -->
        </div>
        <!-- /#wrapper -->
        <script>
            $(function () {
                var array1 = [];
                $.get('../SKU_ajax_servlet/*', function (responseText) {
                    var arr = responseText.trim().split(';');
                    arr.pop();
                    for (var i = 0; i < arr.length; i++) {
                        array1.push(arr[i]);
                    }
                });

                $("#auto").autocomplete({source: array1});
            });
        </script>

    </body>
</html>
<%}%>
