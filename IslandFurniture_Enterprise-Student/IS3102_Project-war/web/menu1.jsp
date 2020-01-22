<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="EntityManager.MessageInboxEntity"%>
<%@page import="CommonInfrastructure.AccountManagement.AccountManagementBeanLocal"%>
<%@page import="javax.ejb.EJB"%>
<%@page import="java.util.ArrayList"%>
<%@page import="EntityManager.RoleEntity"%>
<%@page import="java.util.List"%>
<%@page import="EntityManager.StaffEntity"%>
<%
    StaffEntity staffEntity = (StaffEntity) (session.getAttribute("staffEntity"));
    if (staffEntity == null) {
%>
<jsp:forward page="A1/staffLogin.jsp?errMsg=Session Expired." />
<%
} else {
    List<RoleEntity> roles = staffEntity.getRoles();
    Long[] approvedRolesID;
    boolean roleCanView;
    boolean roleCanView2;
    boolean roleCanView3;
    boolean roleCanView4;
    boolean roleCanView5;

%>

<%    List<MessageInboxEntity> listOfInboxMsg = (List<MessageInboxEntity>) session.getAttribute("inboxMessages");
    /*  MessageInboxEntity msg = new MessageInboxEntity();
     msg.setId(1L);
     msg.setMessage("helloooooooo how are you?");
     StaffEntity staff = new StaffEntity();
     staff.setName("Gabriel");
     msg.setSender(staff);
     msg.setSentDate(Calendar.getInstance().getTime());

     listOfInboxMsg.add(msg);*/
%>


<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation" >
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="../Workspace_Servlet" style="color: #C5C5C5;">Island Furniture - Staff Portal</a>
    </div>
    <!-- Top Menu Items -->
    <ul class="nav navbar-right top-nav">
        <li class="dropdown">
            <a style="color: #C5C5C5;" href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon icon-envelope"> Inbox</i> <b class="caret"></b></a>
            <ul class="dropdown-menu message-dropdown">
                <%
                    if (listOfInboxMsg.size() > 0) {
                        for (int i = 0; i < listOfInboxMsg.size() && i < 3; i++) {
                %>

                <li class="message-preview">
                    <a href="../WorkspaceMessage_Servlet">
                        <div class="media">
                            <div class="media-body">
                                <h5 class="media-heading"><strong><%=listOfInboxMsg.get(i).getSender().getName()%></strong></h5><!--Sender-->
                                <p class="small text-muted"><i class="icon icon-clock-o"><%=listOfInboxMsg.get(i).getSentDate().toString()%></i> </p><!--Date sent-->
                                <p><%=listOfInboxMsg.get(i).getMessage()%></p> <!--Message Content-->
                            </div>
                        </div>
                    </a>
                </li>
                <%
                    }
                %>
                <li class="message-footer">
                    <a href="../WorkspaceMessage_Servlet">Read All New Messages</a>
                </li>
                <%} else {%>

                <li class="message-footer">
                    <a href="#">No Message</a>
                </li>
                <%}%>
            </ul>
        </li>
        <% if (roles.size() != 0) {%>

        <li class="dropdown">
            <a style="color: #C5C5C5;" href="#" class="dropdown-toggle" data-toggle="dropdown">
                <i class="icon icon-group"></i> 
                <%=roles.get(0).getName()%>
                <%
                    if ((roles.get(0).getId() == 2L) || (roles.get(0).getId() == 7L)) {
                        for (int i = 0; i < roles.get(0).getAccessRightList().size(); i++) {
                            if (roles.get(0).getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                out.print("of " + roles.get(0).getAccessRightList().get(i).getRegionalOffice().getName());
                            }
                        }
                    }
                    if (roles.get(0).getId() == 8L) {
                        for (int i = 0; i < roles.get(0).getAccessRightList().size(); i++) {
                            if (roles.get(0).getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                out.print("of " + roles.get(0).getAccessRightList().get(i).getManufacturingFacility().getName());
                            }
                        }
                    }
                    if (roles.get(0).getId() == 3L) {
                        for (int i = 0; i < roles.get(0).getAccessRightList().size(); i++) {
                            if (roles.get(0).getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                out.print("of " + roles.get(0).getAccessRightList().get(i).getWarehouse().getWarehouseName());
                            }
                        }
                    }
                    if (roles.get(0).getId() == 4L || roles.get(0).getId() == 9L || roles.get(0).getId() == 10L || roles.get(0).getId() == 12L) {
                        for (int i = 0; i < roles.get(0).getAccessRightList().size(); i++) {
                            if (roles.get(0).getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                out.print("of " + roles.get(0).getAccessRightList().get(i).getStore().getName());
                            }
                        }
                    }
                %>
                <b class="caret"></b>
            </a>

            <ul class="dropdown-menu" style="min-width: 300px">
                <%
                    for (RoleEntity role : roles) {
                %>
                <li>
                    <a href="#"><i class="icon icon-user"></i> <%= role.getName()%>
                        <%if ((role.getId() == 2L) || (role.getId() == 7L)) {
                                for (int i = 0; i < role.getAccessRightList().size(); i++) {
                                    if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                        out.print("of " + role.getAccessRightList().get(i).getRegionalOffice().getName());
                                    }
                                }
                            }
                            if (role.getId() == 8L) {
                                for (int i = 0; i < role.getAccessRightList().size(); i++) {
                                    if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                        out.print("of " + role.getAccessRightList().get(i).getManufacturingFacility().getName());
                                    }
                                }
                            }
                            if (role.getId() == 3L) {
                                for (int i = 0; i < role.getAccessRightList().size(); i++) {
                                    if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                        out.print("of " + role.getAccessRightList().get(i).getWarehouse().getWarehouseName());
                                    }
                                }
                            }
                            if (role.getId() == 4L || role.getId() == 9L || role.getId() == 10L || role.getId() == 12L) {
                                for (int i = 0; i < role.getAccessRightList().size(); i++) {
                                    if (role.getAccessRightList().get(i).getStaff().getId() == staffEntity.getId()) {
                                        out.print("of " + role.getAccessRightList().get(i).getStore().getName());
                                    }
                                }
                            }%>
                    </a>
                </li>    
                <%
                    }
                %>                       
            </ul>

        </li><%}%>
        <li class="dropdown">
            <a style="color: #C5C5C5;" href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon icon-user"></i> <%=staffEntity.getName()%><b class="caret"></b></a>
            <ul class="dropdown-menu">
                <li>
                    <a href="../A1/staffProfile.jsp"><i class="icon icon-user"></i> Profile</a>
                </li>
                <li>
                    <a href="../WorkspaceMessage_Servlet?view=inbox"><i class="icon icon-envelope"></i> Inbox</a>
                </li>  
                <li class="divider"></li>
                <li>
                    <a href="../AccountManagement_LogoutServlet"><i class="icon icon-power-off"></i> Log Out</a>
                </li>
            </ul>
        </li>
    </ul>
    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav side-nav">
            <%
                approvedRolesID = new Long[]{1L, 2L, 11L};
                roleCanView = false;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                            break;
                        }
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#commonInfrastructure" style="color: #C5C5C5;">
                    <i class="icon icon-user"></i> Common Infrastructure <i class="icon icon-caret-down"></i>
                </a>
                <ul id="commonInfrastructure" class="collapse">
                    <li>
                        <a href="../A1/accountManagement.jsp">Account Management</a>
                    </li>
                </ul>
            </li>
            <%}
                approvedRolesID = new Long[]{1L, 2L, 8L, 11L};
                roleCanView = false;
                roleCanView2 = true;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                            break;
                        }
                    }
                    if (roleEntity.getId().equals(8L)) {//Manufacturing Facility Manager
                        roleCanView2 = false;
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
            <!--<li>
                <a href="javascript:;" data-toggle="collapse" data-target="#MRP" style="color: #C5C5C5;" >
                    <i class="icon icon-book"></i> MRP <i class="icon icon-caret-down"></i>
                </a>
                <ul id="MRP" class="collapse">
                    <%if (roleCanView2) {%>
                    <li>
                        <a href="../SaleForecast_Servlet/SaleForecast_index_GET">Sales Forecast</a>
                    </li>
                    <li>
                        <a href="../SaleAndOperationPlanning_Servlet/sop_index_GET">Sales and Operations Planning</a>
                    </li>
                    <li>
                        <a href="../PPD_index_GET/*">Production Plan Distribution</a>
                    </li>
                    <%}%>
                    <li>
                        <a href="../DemandManagement_index_GET/*">Demand Management</a>
                    </li>
                    <li>
                        <a href="../MRP_index_GET/*">Manufacturing Requirement Planning</a>
                    </li>

                </ul>
            </li>-->
            <% }
                approvedRolesID = new Long[]{1L, 2L, 3L, 4L, 7L, 8L, 11L};
                roleCanView = false;
                roleCanView2 = true;
                roleCanView3 = true;
                roleCanView4 = true;
                roleCanView5 = true;

                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                        }
                        if (roleEntity.getId().equals(8L)) {//Manufacturing Facility Manager
                            roleCanView2 = false;
                        }
                        if (roleEntity.getId().equals(3L)) {//Manufacturing Facility Manager
                            roleCanView3 = false;
                        }
                        if (roleEntity.getId().equals(4L)) {//Manufacturing Facility Manager
                            roleCanView4 = false;
                        }
                        if (roleEntity.getId().equals(7L)) {//Manufacturing Facility Manager
                            roleCanView5 = false;
                        }
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#SCM" style="color: #C5C5C5;">
                    <i class="icon icon-home"></i> SCM <i class="icon icon-caret-down"></i>
                </a>
                <ul id="SCM" class="collapse" style="color: #C5C5C5;">
                    <li>
                        <a href="../PurchaseOrderManagement_Servlet">Retail Products and Raw Materials Purchasing</a>
                    </li>
                    <%if ((roleCanView3) && (roleCanView2) && (roleCanView4)) {%>
                    <li>
                        <a href="../SupplierManagement_SupplierServlet">Supplier Management</a>
                    </li>
                    <%}%>
                    <%if (roleCanView5) {%>
                    <li>
                        <a href="../ShippingOrderManagement_Servlet">Inbound and Outbound Logistics</a>
                    </li>
                    <%}%>

                    <%if ((roleCanView4) && (roleCanView5)) {%>
                    <li>
                        <a href="../ManufacturingWarehouseManagement_Servlet">Manufacturing's Warehouse Management</a>
                    </li>
                    <%}%>

                </ul>
            </li>
            <% }
                approvedRolesID = new Long[]{1L, 2L, 3L, 4L, 11L};
                roleCanView = false;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                        }
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#store" style="color: #C5C5C5;">
                    <i class="icon icon-home"></i> Store Inventory <i class="icon icon-caret-down"></i>
                </a>
                <ul id="store" class="collapse">                 
                    <li>
                        <a href="../RetailWarehouseManagement_Servlet">Store's Inventory Management</a>
                    </li>
                </ul>
            </li>
            <% }
                approvedRolesID = new Long[]{1L, 2L, 11L};
                roleCanView = false;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                            break;
                        }
                        if (roleEntity.getId().equals(5L)) {
                            roleCanView2 = false;
                        }

                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
           <!-- <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#Kitchen" style="color: #C5C5C5;" >
                    <i class="icon icon-book"></i> Kitchen Management <i class="icon icon-caret-down"></i>
                </a>
                <ul id="Kitchen" class="collapse">
                    <li>
                        <a href="../KitchenManagement_servlet/KitchenSaleForecast_index_GET">Kitchen Requirement Planning</a>
                    </li>                                
                </ul>
            </li>-->

            <%}
                approvedRolesID = new Long[]{1L, 2L, 4L, 5L, 11L, 10L};
                roleCanView = false;
                roleCanView2 = true;
                roleCanView3 = true;
                roleCanView4 = true;
                roleCanView5 = true;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                            break;
                        }
                        if (roleEntity.getId().equals(5L)) {//Marketing Director 
                            roleCanView2 = false;
                        }
                        if (roleEntity.getId().equals(4L)) {
                            roleCanView3 = false;
                        }
                        if (roleEntity.getId().equals(2L)) {
                            roleCanView4 = false;
                        }
                        if (roleEntity.getId().equals(10L)) {//Receptionist
                            roleCanView2 = false;
                            roleCanView3 = false;
                            roleCanView5 = false;
                        }
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#operationalCRM" style="color: #C5C5C5;">
                    <i class="icon icon-cogs"></i> Operational CRM <i class="icon icon-caret-down"></i>
                </a>
                <ul id="operationalCRM" class="collapse">
                    <% if ((roleCanView3) && (roleCanView4)) { %>
                    <li>
                        <a href="../LoyaltyManagement_Servlet">Loyalty & Rewards</a>
                    </li>
                    <%}%>
                    <li>
                        <a href="../A4/loyaltyCardMgt.jsp">Loyalty Card Management</a>
                    </li>
                    <% if (roleCanView2) { %>
                    <li>
                        <a href="../A4/customerServiceManagement.jsp">Customer Service</a>
                    </li>                    
                    <%}%>
                    <% if (roleCanView5) {%>
                    <li>
                        <a href="../PromotionalSalesManagement_Servlet">Promotional Sales</a>
                    </li>
                    <%}%>
                </ul>
            </li>
            <% }
                approvedRolesID = new Long[]{1L, 5L, 6L, 11L};
                roleCanView = false;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                            break;
                        }
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
          <!--  <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#analyticalCRM" style="color: #C5C5C5;">
                    <i class="icon icon-bar-chart-o"></i> Analytical CRM <i class="icon icon-caret-down"></i>
                </a>
                <ul id="analyticalCRM" class="collapse">
                    <li>
                        <a href="../A5/analytical.jsp">Analytics</a>
                    </li>
                </ul>
            </li>-->
            <% }
                approvedRolesID = new Long[]{1L, 6L, 11L};
                roleCanView = false;
                roleCanView2 = true;
                for (RoleEntity roleEntity : roles) {
                    for (Long ID : approvedRolesID) {
                        if (roleEntity.getId().equals(ID)) {
                            roleCanView = true;
                            break;
                        }
                    }
                    if (roleEntity.getId().equals(6L)) {//Marketing Director 
                        roleCanView2 = false;
                    }
                    if (roleCanView) {
                        break;
                    }
                }
                if (roleCanView) {
            %>
            <li>
                <a href="javascript:;" data-toggle="collapse" data-target="#corporateCRM" style="color: #C5C5C5;">
                    <i class="icon icon-briefcase"></i> Corporate Management <i class="icon icon-caret-down"></i>
                </a>
                <ul id="corporateCRM" class="collapse">
                    <% if (roleCanView2) { %>
                    <li>
                        <a href="../A6/facilityManagement.jsp">Facility Management</a>
                    </li>
                    <%}%>
                    <li>
                        <a href="../A6/itemManagement.jsp">Item Management</a>
                    </li>
                    <li>
                        <a href="../A6/restaurantManagement.jsp">Restaurant Management</a>
                    </li>
                </ul>
            </li>
            <% }
            %>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>

<%}%>
