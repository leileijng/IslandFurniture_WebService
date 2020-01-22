/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import CorporateManagement.FacilityManagement.FacilityManagementBeanLocal;
import EntityManager.ManufacturingFacilityEntity;
import MRP.ProductionPlanDistribution.ProductionPlanDistributionBeanLocal;
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
public class store_MF_connectionManagement_POST extends HttpServlet {
    
    @EJB
    private FacilityManagementBeanLocal fmBean;
    @EJB
    private ProductionPlanDistributionBeanLocal ppdBean;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String nextPage = "/store_MF_connectionManagement_GET/*?regionalOffice=";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        HttpSession session = request.getSession();
        String target = request.getPathInfo();
        
        switch (target) {
            
            case "/addConnection":
                try {
                    
                    Long mfId = Long.parseLong(request.getParameter("mfId"));
                    Long storeId = Long.parseLong(request.getParameter("storeId"));
                    Long regionalOfficeId = Long.parseLong(request.getParameter("regionalOfficeId"));
                    
                    nextPage = "/store_MF_connectionManagement_GET/*?regionalOffice=" + regionalOfficeId;
                    
                    ppdBean.addStore_ManufacturingFacilityConnection(storeId, mfId); 
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            
            case "/removeConnection":
                try {
                    Long mfId = Long.parseLong(request.getParameter("mfId"));
                    Long storeId = Long.parseLong(request.getParameter("storeId"));
                    Long regionalOfficeId = Long.parseLong(request.getParameter("regionalOfficeId"));
                    
                    nextPage = "/store_MF_connectionManagement_GET/*?regionalOffice=" + regionalOfficeId;
                    
                    ppdBean.removeStore_ManufacturingFacilityConnection(storeId, mfId);
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            
        }
        System.out.println("nextpage: " + nextPage);
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
