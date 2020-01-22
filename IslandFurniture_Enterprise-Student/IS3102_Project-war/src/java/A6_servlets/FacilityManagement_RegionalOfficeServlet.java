package A6_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.RegionalOfficeEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.StaffEntity;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FacilityManagement_RegionalOfficeServlet extends HttpServlet {

    @EJB
    private FacilityManagementBeanLocal fmBean;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nextPage = "/A6/facilityManagement";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Long regionalOfficeId;
        String target = request.getPathInfo();

        HttpSession session = request.getSession();
        StaffEntity currentLoggedInStaffEntity = (StaffEntity) session.getAttribute("staffEntity");
        String currentLoggedInStaffID;
        if (currentLoggedInStaffEntity == null) {
            currentLoggedInStaffID = "System";
        } else {
            currentLoggedInStaffID = currentLoggedInStaffEntity.getId().toString();
        }

        switch (target) {

            case "/regionalOfficeManagement_index":
                List<RegionalOfficeEntity> regionalOfficeList = fmBean.viewListOfRegionalOffice();
                request.setAttribute("regionalOfficeList", regionalOfficeList);
                nextPage = "/A6/regionalOfficeManagement";
                break;

            case "/createRegionalOffice_GET":
                System.out.println("Create regional office in servlet");
                List<ManufacturingFacilityEntity> manufacturingFacilityList = fmBean.viewListOfManufacturingFacility();
                request.setAttribute("manufacturingFacilityList", manufacturingFacilityList);

                String submit_btn = request.getParameter("submit-btn");
                System.out.print(submit_btn);
                if (submit_btn.equals("Add Regional Office")) {
                    nextPage = "/A6/createRegionalOffice";
                } else if (submit_btn.equals("Delete Regional Office")) {
                    nextPage = "/FacilityManagement_RegionalOfficeServlet/deleteRegionalOffice";
                } else {
                    regionalOfficeId = Long.parseLong(submit_btn);
                    request.setAttribute("regionalOfficeId", regionalOfficeId);
                    nextPage = "/FacilityManagement_RegionalOfficeServlet/editRegionalOffice_GET";
                }
                break;

            case "/createRegionalOffice_POST":
                try {
                    String regionalOfficeName = request.getParameter("regionalOfficeName");
                    String address = request.getParameter("address");
                    String telephone = request.getParameter("telephone");
                    String email = request.getParameter("email");

                    if (fmBean.checkNameExistsOfRegionalOffice(regionalOfficeName)) {
                        //  request.setAttribute("alertMessage", "Fail to create regional office due to duplicated regional office name.");
                        //    request.setAttribute("regionalOffice", true);

                        result = "?errMsg=Fail to create regional office due to duplicated regional office name.";
                        nextPage = "/FacilityManagement_RegionalOfficeServlet/regionalOfficeManagement_index" + result;
                    } else {
                        boolean regionalOffice = fmBean.addRegionalOffice(currentLoggedInStaffID, regionalOfficeName, address, telephone, email);
                        if (regionalOffice != false) {
                            result = "?goodMsg=A new regional office record has been saved";
                        } else {
                            result = "?errMsg=Fail to create regional office due to duplicated regional office name.";
                        }
                        //request.setAttribute("regionalOffice", regionalOffice);
                        nextPage = "/FacilityManagement_RegionalOfficeServlet/regionalOfficeManagement_index" + result;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            case "/editRegionalOffice_GET":
                regionalOfficeId = (long) request.getAttribute("regionalOfficeId");
                System.out.println("Regional office ID is " + regionalOfficeId);
                RegionalOfficeEntity regionalOffice = fmBean.viewRegionalOffice(String.valueOf(regionalOfficeId));
                System.out.println("ID of regional office to be displayed" + regionalOffice.getId());
                request.setAttribute("regionalOffice", regionalOffice);
                nextPage = "/A6/editRegionalOffice";
                break;

            case "/editRegionalOffice_POST":
                String regionalOfficeName = request.getParameter("regionalOfficeName");
                String address = request.getParameter("address");
                String telephone = request.getParameter("telephone");
                String email = request.getParameter("email");
                Long id = Long.parseLong(request.getParameter("regionalOfficeId"));

                if (fmBean.editRegionalOffice(currentLoggedInStaffID, id, regionalOfficeName, address, telephone, email)) {
                    result = "?goodMsg=The regional office has been updated.";
                } else {
                    result = "?errMsg=Fail to edit regional office.";
                }
                nextPage = "/FacilityManagement_RegionalOfficeServlet/regionalOfficeManagement_index" + result;
                break;

            case "/deleteRegionalOffice":
                String[] deletes = request.getParameterValues("delete");
                int numDeleted = 0;
                int numOfErrors = 0;
                if (deletes != null) {
                    for (String regionalOfficeString : deletes) {
                        String regionalOffice_Id = regionalOfficeString;
                        if (fmBean.removeRegionalOffice(currentLoggedInStaffID, regionalOffice_Id)) {
                            numDeleted++;
                        } else {
                            numOfErrors++;
                        }
                    }
                }
                if (numDeleted == 0 && numOfErrors == 0) {
                    result = "?goodMsg=Nothing selected.";
                } else if (numOfErrors == 0) {
                    result = "?goodMsg=Successfully removed: " + numDeleted + " record(s).";
                } else if (numDeleted == 0) {
                    result = "?errMsg=Failed to delete: " + numOfErrors + " record(s). There are other facility tied to them.";
                } else {
                    result = "?errMsg=" + numDeleted + " of " + deletes.length + " records were deleted. " + numOfErrors + " records not deleted as there are other facility tied to them.";
                }
                nextPage = "/FacilityManagement_RegionalOfficeServlet/regionalOfficeManagement_index" + result;
                break;

        }
        dispatcher = servletContext.getRequestDispatcher(nextPage);
        dispatcher.forward(request, response);
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
