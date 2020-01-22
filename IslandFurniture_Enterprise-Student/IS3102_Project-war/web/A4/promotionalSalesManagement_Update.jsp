<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="EntityManager.PromotionEntity"%>
<%@page import="EntityManager.CountryEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.FurnitureEntity"%>
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
                                Add Promotion
                            </h1>
                            <ol class="breadcrumb">
                                <li class="active">
                                    <i class="icon icon-users"></i><a href="../A4/operationalCRM.jsp"> Operational CRM</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-user"></i><a href="../PromotionalSalesManagement_Servlet"> Promotion Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-edit"></i> Update Promotion
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <jsp:include page="../displayMessage.jsp" />
                   
                    <%
                        try {
                            String id = request.getParameter("id");
                            List<PromotionEntity> promotions = (List<PromotionEntity>) (session.getAttribute("promotions"));
                            PromotionEntity promotion = new PromotionEntity();
                            for (int i = 0; i < promotions.size(); i++) {
                                if (promotions.get(i).getId() == Integer.parseInt(id)) {
                                    promotion = promotions.get(i);
                                }
                            }
                        
                    %>
                       
                    <!-- /.warning -->
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form" method="POST" enctype="multipart/form-data" action="../PromotionalSalesManagement_UpdateServlet">                        
                                <div class="form-group">
                                    <label>Item SKU</label>
                                    <input class="form-control" id="auto" required="true" type="text" name="sku" value="<%=promotion.getItem().getSKU()%>" >
                                </div>
                                <div class="form-group">
                                    <label>Country</label>
                                    <select required="true" name="country" class="form-control">
                                        <option value="<%=(promotion.getCountry().getId())%>"><%=promotion.getCountry().getName()%></option>
                                        <%
                                            List<CountryEntity> countries = (List<CountryEntity>) (session.getAttribute("countries"));
                                            if (countries != null) {
                                                for (CountryEntity country : countries) {
                                                    if (!country.getName().equals(promotion.getCountry().getName())) {
                                        %>
                                        <option value="<%= country.getId()%>"> <%= country.getName()%> </option>
                                        <% 
                                                    }
                                                }
                                            }
                                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                                        String formatedDate = df.format(promotion.getStartDate());
                                                        String formatedDate1 = df.format(promotion.getEndDate());
                                        %>
                                    </select>                                
                                </div>
                                <div class="form-group">
                                    <label>Discount Rate</label>
                                    <input class="form-control" type="number" required="true" min="1" max="100" step="1" name="discountRate" value="<%=promotion.getDiscountRate()%>" >
                                </div>
                                <div class="form-group">
                                    <label>Start Date</label>
                                    <input class="form-control" type="date" required="true" name="startDate" value="<%=formatedDate%>" >
                                </div>
                                <div class="form-group">
                                    <label>End Date</label>
                                    <input class="form-control" type="date" required="true" name="endDate" value="<%=formatedDate1%>">
                                </div>
                                <div class="form-group">
                                    <label>Description</label>
                                    <input class="form-control" type="text" required="true" name="description" value="<%=promotion.getDescription()%>">
                                </div>
                                <div>
                                    <label>Promotion Poster</label>
                                    <input type="file" name="javafile">
                                </div>
                                <br>
                                <div class="form-group">
                                    <input type="submit" value="Update" class="btn btn-lg btn-primary btn-block">
                                </div>
                                <input type="hidden" value="<%=promotion.getId()%>" name="id">
                                <input type="hidden" value="A4/promotionalSalesManagement_Update.jsp" name="source">

                            </form>
                        </div>
                        <!-- /.row -->

                    </div>
                    <%} catch (Exception ex) {

                            //response.sendRedirect("../PromotionalSalesManagement_Servlet");
                        }%>
                </div>

            </div>
            <!-- /#page-wrapper -->
        </div>
        <!-- /#wrapper -->
        <script>
            var today = new Date().toISOString().split('T')[0];
            document.getElementsByName("startDate")[0].setAttribute('min', today);
            document.getElementsByName("endDate")[0].setAttribute('min', today);
        
            var today = new Date().toISOString().split('T')[0];
            document.getElementsByName("startDate")[0].setAttribute('min', today);
            document.getElementsByName("endDate")[0].setAttribute('min', today);
       
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
