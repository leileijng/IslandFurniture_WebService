<%@page import="EntityManager.CountryEntity"%>
<%@page import="EntityManager.Item_CountryEntity"%>
<%@page import="EntityManager.LineItemEntity"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.FurnitureEntity"%>
<%@page import="EntityManager.BillOfMaterialEntity"%>
<%@page import="java.util.List"%>
<html lang="en">

    <jsp:include page="../header2.html" />

    <body>
        <script>
            function checkAll(source) {
                checkboxes = document.getElementsByName('delete');
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    checkboxes[i].checked = source.checked;
                }
            }
            function add() {
                document.itemPricingManagement_Add.action = "../CountryItemPricingManagement_AddCountryItemPricingServlet";
                document.itemPricingManagement_Add.submit();
            }
            function update(id) {
                itemPricingManagement.id.value = id;
                //make sure there is only one decimal place. convert to float instead of int
                itemPricingManagement.setPrice.value =parseFloat($("#price" + id).val()).toFixed(1);
                document.itemPricingManagement.action = "../CountryItemPricingManagement_UpdateCountryItemPricingServlet";
                document.itemPricingManagement.submit();
            }
            function removeRecord() {
                checkboxes = document.getElementsByName('delete');
                var numOfTicks = 0;
                for (var i = 0, n = checkboxes.length; i < n; i++) {
                    if (checkboxes[i].checked) {
                        numOfTicks++;
                    }
                }
                if (checkboxes.length == 0 || numOfTicks == 0) {
                    window.event.returnValue = true;
                    document.itemPricingManagement.submit();
                } else {

                    window.event.returnValue = true;
                    document.itemPricingManagement.action = "../CountryItemPricingManagement_RemoveCountryItemPricingServlet";
                    document.itemPricingManagement.submit();
                }
            }
        </script>
        <div id="wrapper">
            <jsp:include page="../menu1.jsp" />
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">Item Pricing Management</h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="icon icon-user"></i>  <a href="itemManagement.jsp">Item Management</a>
                                </li>
                                <li class="active">
                                    <i class="icon icon-dollar"></i>&nbsp;Item Pricing Management
                                </li>
                            </ol>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->

                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-default">
                                <div class="panel-heading"> <%
                                    String errMsg = request.getParameter("errMsg");
                                    if (errMsg == null || errMsg.equals("")) {
                                        errMsg = "Welcome to Item Pricing Management";
                                    }
                                    out.println(errMsg);
                                    %>                                  
                                </div>
                                <!-- /.panel-heading -->
                                <form name="itemPricingManagement">
                                    <div class="panel-body">
                                        <div class="table-responsive">

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary btnAdd" name="btnAdd" type="button" value="Add Item Pricing" />
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Record</button></a>
                                                </div>
                                            </div>
                                            <br/>
                                            <div id="dataTables-example_wrapper" class="dataTables_wrapper form-inline" role="grid">
                                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                                    <thead>
                                                        <tr>
                                                            <th style="width:5%"><input type="checkbox"onclick="checkAll(this)" /></th>
                                                            <th style="width:20%">Country</th>
                                                            <th style="width:15%">SKU</th>
                                                            <th style="width:15%">Price</th>
                                                            <th style="width:10%">Update</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%
                                                            List<Item_CountryEntity> listOfCountryItemPricing = (List<Item_CountryEntity>) (session.getAttribute("listOfCountryItemPricing"));
                                                            try {
                                                                if (listOfCountryItemPricing != null) {
                                                                    for (int i = 0; i < listOfCountryItemPricing.size(); i++) {
                                                        %>
                                                        <tr>
                                                            <td>
                                                                <input type="checkbox" name="delete" value="<%=listOfCountryItemPricing.get(i).getId()%>" />
                                                            </td>
                                                            <td>
                                                                <%=listOfCountryItemPricing.get(i).getCountry().getName()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfCountryItemPricing.get(i).getItem().getSKU()%>
                                                            </td>
                                                            <td>
                                                                <%=listOfCountryItemPricing.get(i).getRetailPrice()%>
                                                                <div hidden id="updateItemPricing<%=listOfCountryItemPricing.get(i).getId()%>" style="float:right">
                                                                    <input type="number" min="0" max="99999" class="form-control" style="width:80px;" id="price<%=listOfCountryItemPricing.get(i).getId()%>" required/>
                                                                    <input class="btn btn-primary" type="button" value="Submit" onclick="update(<%=listOfCountryItemPricing.get(i).getId()%>)"/>
                                                                </div>
                                                            </td>
                                                            <td><input class="btn btn-primary" id="btnUpdate" onclick="showEditItemPricing(<%=listOfCountryItemPricing.get(i).getId()%>)" type="button" value="Update"/></td>
                                                        </tr>
                                                        <%
                                                                    }
                                                                }
                                                            } catch (Exception ex) {
                                                                System.out.println(ex);
                                                            }
                                                        %>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <!-- /.table-responsive -->
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <input class="btn btn-primary btnAdd" name="btnAdd" type="button" value="Add Item Pricing"/>
                                                    <a href="#myModal" data-toggle="modal"><button class="btn btn-primary">Remove Record</button></a>
                                                </div>
                                            </div>
                                            <input type="hidden" name="id" value="">  
                                            <input type="hidden" name="setPrice" value="">
                                        </div>
                                    </div>
                                    <!-- /.panel-body -->
                                </form>
                                <form name="itemPricingManagement_Add" onsubmit="add()">
                                    <div id="addItemPricingForm" hidden>
                                        <div class="row">
                                            <div class="form-group">
                                                <div class="col-md-3" style="padding-left: 30px"><br>
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                Country:&nbsp;
                                                            </td>
                                                            <td>
                                                                <select class="form-control" name="country"> 
                                                                    <%
                                                                        List<CountryEntity> listOfCountry = (List<CountryEntity>) session.getAttribute("listOfCountry");
                                                                        for (CountryEntity c : listOfCountry) {
                                                                            Long countryId = c.getId();
                                                                            String countryName = c.getName();
                                                                            out.print("<option value=\"" + countryId + "\">" + countryName + "</option>");
                                                                        }
                                                                    %>
                                                                </select>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                SKU:&nbsp;
                                                            </td>
                                                            <td>
                                                                <input id="auto" class="form-control" name="sku" type="text" required/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                Price:&nbsp;
                                                            </td>
                                                            <td>
                                                                <input type="number" min="0" max="99999" step="any" class="form-control" name="price" required/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <input class="btn btn-primary" name="btnAdd" type="submit" value="Add" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->


                </div>
                <!-- /.container-fluid -->

            </div>
            <!-- /#page-wrapper -->

        </div>
        <!-- /#wrapper -->

        <div role="dialog" class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Alert</h4>
                    </div>
                    <div class="modal-body">
                        <p id="messageBox">Record will be removed. Are you sure?</p>
                    </div>
                    <div class="modal-footer">                        
                        <input class="btn btn-primary" name="btnRemove" type="submit" value="Confirm" onclick="removeRecord()"  />
                        <a class="btn btn-default" data-dismiss ="modal">Close</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Page-Level Demo Scripts - Tables - Use for reference -->
        <script>
            $(document).ready(function() {
                $('#dataTables-example').dataTable();
            });

            $(".btnAdd").click(function() {
                $("html, body").animate({scrollTop: $(document).height()}, "slow");
                $("#addItemPricingForm").show("slow", function() {
                });
            });
            function showEditItemPricing(id) {
                $("#updateItemPricing" + id).show("fast", function() {
                });
            }
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