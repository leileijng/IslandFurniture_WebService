<%@page import="EntityManager.AccessRightEntity"%>
<%@page import="EntityManager.RegionalOfficeEntity"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="EntityManager.StaffEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.CountryEntity"%>
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
                                Add Supplier
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-user"></i> <a href="supplierManagement.jsp">Supplier Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Add Supplier
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->

                    <jsp:include page="../displayMessage.jsp" />
                    <!-- /.warning -->

                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" action="../SupplierManagement_AddSupplierServlet">
                                <div class="form-group">
                                    <label>Name</label>
                                    <input class="form-control" required="true" name="name"  type="text">
                                </div>
                                <div class="form-group">
                                    <label>Phone</label>
                                    <input class="form-control" required="true" type="text" name="phone" >
                                </div>
                                <div class="form-group">
                                    <label>Email</label>
                                    <input class="form-control" required="true" type="email" name="email">
                                </div>
                                <div class="form-group">
                                    <label>Address</label>
                                    <input class="form-control" type="text" required="true" name="address" >
                                </div>
                                <div class="form-group">
                                    <label>Country</label>
                                    <select required="true" name="country" class="form-control">
                                        <option></option>
                                        <%
                                            List<CountryEntity> countries = (List<CountryEntity>) (session.getAttribute("countries"));
                                            if (countries != null) {
                                                for (int i = 0; i < countries.size(); i++) {
                                                    out.println("<option value='" + countries.get(i).getId() + "'>" + countries.get(i).getName() + "</option>");
                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Regional Office</label>
                                    <select required="true" name="regionalOffice" class="form-control">
                                        <%
                                            StaffEntity staff = (StaffEntity) (session.getAttribute("staffEntity"));
                                            List<RegionalOfficeEntity> listOfRegionalOffice = (List<RegionalOfficeEntity>) session.getAttribute("listOfRegionalOffice");

                                            boolean isAdminLevel = false;
                                            List<RoleEntity> listOfRoles = staff.getRoles();
                                            for (RoleEntity role : listOfRoles) {
                                                if (role.getName().equals("Administrator") || role.getName().equals("Global Manager")) {
                                                    isAdminLevel = true;
                                                    if (listOfRegionalOffice != null) {
                                                        for (int i = 0; i < listOfRegionalOffice.size(); i++) {
                                                            out.println("<option value='" + listOfRegionalOffice.get(i).getId() + "'>" + listOfRegionalOffice.get(i).getName() + "</option>");
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                            if (!isAdminLevel) {
                                                List<AccessRightEntity> accessList = staff.getAccessRightList();
                                                for (RoleEntity role : listOfRoles) {
                                                    if (role.getName().equals("Regional Manager") || role.getName().equals("Purchasing Manager")) {
                                                        for (AccessRightEntity accessRight : accessList) {
                                                            for (RegionalOfficeEntity RO : listOfRegionalOffice) {
                                                                if (accessRight.getRegionalOffice() != null && accessRight.getRegionalOffice().getId().equals(RO.getId())) {
                                                                    out.println("<option value='" + RO.getId() + "'>" + RO.getName() + "</option>");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
//
//                                                List<AccessRightEntity> listOfAccessRight = staff.getAccessRightList();
//                                                for (AccessRightEntity accessRight : listOfAccessRight) {
//                                                    for (RegionalOfficeEntity RO : listOfRegionalOffice) {
//                                                        if (accessRight.getRegionalOffice() != null && accessRight.getRegionalOffice().getId().equals(RO.getId())) {
//
//                                                            out.println("<option value='" + RO.getId() + "'>" + RO.getName() + "</option>");
//                                                        }
//                                                    }
//                                                }
                                            }
                                        %>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input type="submit" value="Add" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="A1/supplierManagement_add.jsp" name="source">
                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
    </body>

</html>
