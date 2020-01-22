/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MaterialRequirementEntity;
import EntityManager.MonthScheduleEntity;
import MRP.DemandManagement.DemandManagementBeanLocal;
import MRP.ManufacturingRequirementPlanning.ManufacturingRequirementPlanningBeanLocal;
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
public class MRP_main_GET extends HttpServlet {

    @EJB
    private ManufacturingRequirementPlanningBeanLocal mrBean;
    @EJB
    private DemandManagementBeanLocal dmBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("servlet MRP_main_GET is called");

        String nextPage = "/A2/MRP_main";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        HttpSession session = request.getSession();

        ManufacturingFacilityEntity mf = (ManufacturingFacilityEntity) session.getAttribute("MRP_mf");
        List<MaterialRequirementEntity> mrList = new ArrayList<>();
        if (mrBean.generateMaterialRequirementPlan(mf.getId())) {
            System.out.println("Material Requirement Plan is generated.");
            mrList = mrBean.getMaterialRequirementEntityList(mf.getId());
        }        
        request.setAttribute("mrList", mrList);
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
