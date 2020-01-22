/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.StaffEntity;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class DemandManagement_index_POST extends HttpServlet {

    @EJB
    private FacilityManagementBeanLocal fmBean;
    @EJB
    private AccountManagementBeanLocal amBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("servlet DemandManagement_index_POST is called");

        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        HttpSession session = request.getSession();
        String nextPage;
        
        String MF_Name = request.getParameter("MF_Name");
        ManufacturingFacilityEntity mf = fmBean.getManufacturingFacilityByName(MF_Name);
        StaffEntity currentUser = (StaffEntity) session.getAttribute("staffEntity");
        if (amBean.canStaffAccessToTheManufacturingFacility(currentUser.getId(), mf.getId())) {
            session.setAttribute("DemandManagement_mf", mf);
            nextPage = "/DemandManagement_main_GET/*";
        } else {
            request.setAttribute("alertMessage", "You are not allowed to access the manufacturing facility.");
            nextPage = "/DemandManagement_index_GET";
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
        protected void doGet
        (HttpServletRequest request, HttpServletResponse response)
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
        protected void doPost
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            processRequest(request, response);
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo
        
            () {
        return "Short description";
        }// </editor-fold>

    }
