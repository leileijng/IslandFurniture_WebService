package A1_servlets;

import EntityManager.StaffEntity;
import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CommonInfrastructure.Workspace.WorkspaceBeanLocal;
import EntityManager.AccessRightEntity;
import EntityManager.CountryEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.StoreEntity;
import EntityManager.WarehouseEntity;
import SCM.SupplierManagement.SupplierManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountManagement_LoginServlet extends HttpServlet {

    @EJB
    private AccountManagementBeanLocal accountManagementBean;
    @EJB
    private SupplierManagementBeanLocal supplierManagementBean;
    @EJB
    private WorkspaceBeanLocal workspaceBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();

            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            System.out.println("0");

            if (staffEntity == null) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                if (accountManagementBean.checkStaffInvalidLoginAttempts(email) >= 9) {
                    result = "Login fail. Account is locked. Please reset your password";
                    response.sendRedirect("A1/staffLogin.jsp?errMsg=" + result);
                }
                staffEntity = accountManagementBean.loginStaff(email, password);
            }
            List<CountryEntity> countries = supplierManagementBean.getListOfCountries();
            session.setAttribute("countries", countries);

            if (staffEntity == null) {
                result = "Login fail. Please try again.";
                response.sendRedirect("A1/staffLogin.jsp?errMsg=" + result);
            } else {
                List<RegionalOfficeEntity> regionalOfficeList = new ArrayList<>();
                List<StoreEntity> storeList = new ArrayList<>();
                List<ManufacturingFacilityEntity> manufacturingFacilityList = new ArrayList<>();
                List<WarehouseEntity> warehouseList = new ArrayList<>();

                if (staffEntity.getAccessRightList().size() != 0) {
                    List<AccessRightEntity> accessRights = staffEntity.getAccessRightList();

                    for (AccessRightEntity access : accessRights) {
                        if (access.getRegionalOffice() != null) {
                            regionalOfficeList.add(access.getRegionalOffice());
                        }
                        if (access.getStore() != null) {
                            storeList.add(access.getStore());
                        }
                        if (access.getManufacturingFacility() != null) {
                            manufacturingFacilityList.add(access.getManufacturingFacility());
                        }
                        if (access.getWarehouse() != null) {
                            warehouseList.add(access.getWarehouse());
                        }
                    }
                }

                session.setAttribute("access_RO", regionalOfficeList);
                session.setAttribute("access_S", storeList);
                session.setAttribute("access_MF", manufacturingFacilityList);
                session.setAttribute("access_W", warehouseList);
                session.setAttribute("staffEntity", staffEntity);
                List<StaffEntity> staffs = accountManagementBean.listAllStaff();
                session.setAttribute("staffs", staffs);
                session.setAttribute("staffRoles", accountManagementBean.listRolesHeldByStaff(staffEntity.getId()));
                session.setAttribute("listOfAnnouncements", workspaceBean.getListOfAllNotExpiredAnnouncement());
                session.setAttribute("unreadMessages", workspaceBean.listAllUnreadInboxMessages(staffEntity.getId()));
                session.setAttribute("inboxMessages", workspaceBean.listAllInboxMessages(staffEntity.getId()));
                session.setAttribute("sentMessages", workspaceBean.listAllOutboxMessages(staffEntity.getId()));
                session.setAttribute("toDoList", workspaceBean.getAllToDoListOfAStaff(staffEntity.getId()));
                response.sendRedirect("A1/workspace.jsp");
            }
        } catch (Exception ex) {
            out.println(ex);
            ex.printStackTrace();
        }

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
