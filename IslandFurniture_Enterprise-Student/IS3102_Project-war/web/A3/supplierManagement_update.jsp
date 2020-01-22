<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.SupplierEntity"%>
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
                                Supplier Details Update
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="supplierManagement.jsp">Supplier Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Supplier Details Update
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessage.jsp" />
                    <%
                        List<SupplierEntity> suppliers = (List<SupplierEntity>) (session.getAttribute("suppliers"));
                        try {
                            String id = request.getParameter("id");
                            SupplierEntity supplier = new SupplierEntity();
                            for (int i = 0; i < suppliers.size(); i++) {
                                if (suppliers.get(i).getId() == Integer.parseInt(id)) {
                                    supplier = suppliers.get(i);
                                }
                            }
                    %>

                    <div class="row">
                        <div class="col-lg-6">

                            <form role="form" action="../SupplierManagement_UpdateSupplierServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name" type="text" value="<%=supplier.getSupplierName()%>">
                                </div>
                                <div class="form-group">
                                    <label>Phone</label>
                                    <input class="form-control" required="true" type="text" name="phone" value="<%=supplier.getContactNo()%>">
                                </div>
                                <div class="form-group">
                                    <label>Email</label>
                                    <input class="form-control" type="email"  name="email"required="true" value="<%=supplier.getEmail()%>" >
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input class="form-control" type="text" required="true" name="address" value="<%=supplier.getAddress()%>">
                                </div>
                                <div class="form-group">
                                    <label>Country</label>
                                    <select required="" name="country" class="form-control">
                                        <option value="<%=(supplier.getCountry().getId())%>"><%=supplier.getCountry().getName()%></option>
                                        <%
                                            List<CountryEntity> countries = (List<CountryEntity>) (session.getAttribute("countries"));
                                            if (countries != null) {
                                                for (CountryEntity country : countries) {
                                                    if (!country.getName().equals(supplier.getCountry().getName())) {
                                        %>
                                        <option value="<%= country.getId()%>"> <%= country.getName()%> </option>
                                        <%
                                                    }
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=supplier.getId()%>" name="id">
                                <input type="hidden" value="<%=supplier.getSupplierName()%>" name="supplierName">
                            </form>
                        </div>
                        <!-- /.row -->
                    </div>
                    <%
                        } catch (Exception ex) {
                            response.sendRedirect("../SupplierManagement_SupplierServlet");
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
