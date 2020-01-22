/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MasterProductionScheduleEntity;
import EntityManager.MonthScheduleEntity;
import MRP.DemandManagement.DemandManagementBeanLocal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class DemandManagement_main_GET extends HttpServlet {
    @EJB
    private DemandManagementBeanLocal dmBean;
        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        System.out.println("servlet DemandManagement_main_GET is called");
        
        String nextPage = "/A2/DemandManagement_main";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;        
        HttpSession session = request.getSession();
        
        ManufacturingFacilityEntity mf = (ManufacturingFacilityEntity) session.getAttribute("DemandManagement_mf");
        List<MasterProductionScheduleEntity> mpsList = new ArrayList<>();
        if(dmBean.generateMasterProductionSchedules(mf.getId())){
            System.out.println("Master Production Schedule is generated.");
            mpsList = dmBean.getMasterProductionSchedules(mf.getId());            
            System.out.println("mpsList.size(): " + mpsList.size());
        }
        request.setAttribute("mpsList", mpsList);
        try {
            MonthScheduleEntity schedule = dmBean.getLastSchedule();
            request.setAttribute("schedule", schedule);
        } catch (Exception ex) {
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
