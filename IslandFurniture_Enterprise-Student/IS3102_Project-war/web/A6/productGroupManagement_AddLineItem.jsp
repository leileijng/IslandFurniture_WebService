<%@page import="EntityManager.ProductGroupEntity"%>
<%@page import="java.util.List"%>

<html lang="en">
    <jsp:include page="../header2.html" />
    <body>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <%
                List<ProductGroupEntity> productGroups = (List<ProductGroupEntity>) (session.getAttribute("productGroups"));
                String id = request.getParameter("id");
                if (productGroups == null || id == null) {
                    response.sendRedirect("../ProductGroupManagement_Servlet");
                } else {
                    ProductGroupEntity productGroup = new ProductGroupEntity();
                    for (int i = 0; i < productGroups.size(); i++) {
                        if (productGroups.get(i).getId() == Integer.parseInt(id)) {
                            productGroup = productGroups.get(i);
                        }
                    }

            %>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Add Line Item
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-cogs"></i> <a href="productGroupManagement.jsp">Product Group Management</a>
                                </li>
                                <li>
                                    <i class="icon icon-cogs"></i> <a href="productGroupManagement_Update.jsp?id=<%=productGroup.getId()%>">Product Group ID: <%=productGroup.getId()%></a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Line Item
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
                                    <h3 class="panel-title"> Product Group ID: <%=productGroup.getId()%> - Add Line Item </h3>
                                </div>
                                <div class="panel-body">
                                    <form role="form" action="../ProductGroupLineItemManagement_AddServlet">
                                        <div class="form-group">
                                            <label>SKU</label>
                                            <input id="auto" class="form-control" name="sku" type="text"  >
                                        </div>

                                        <div class="form-group">
                                            <input type="submit" value="Add Line Item" class="btn btn-lg btn-primary btn-block">
                                        </div>  
                                        <input type="hidden" value="<%=productGroup.getId()%>" name="id">
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
