/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import EntityManager.MonthScheduleEntity;
import EntityManager.RegionalOfficeEntity;
import MRP.DemandManagement.DemandManagementBeanLocal;
import MRP.ProductionPlanDistribution.ProductionPlanDistributionBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author Administrator
 */
public class MRP_main_POST extends HttpServlet {

    @EJB
    private ProductionPlanDistributionBeanLocal ppdBean;
    @EJB
    private DemandManagementBeanLocal dmBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nextPage = "/PPD_main_GET/*";
        ServletContext servletContext = getServletContext();
        RequestDispatcher dispatcher;
        HttpSession session = request.getSession();        
        String target = request.getPathInfo();

        switch (target) {

            case "/generateShippingOrderForFurniture":
                System.out.println("MRP_main_POST/generateShippingOrderForFurniture is called");
                MonthScheduleEntity schedule = dmBean.getLastSchedule();
                RegionalOfficeEntity regionalOffice = (RegionalOfficeEntity) session.getAttribute("PPD_regionalOffice");
                if (ppdBean.generateShippingOrder(regionalOffice.getId(), schedule.getId())) {
                    request.setAttribute("alertMessage", "Shipping Orders have been generated.");
                } else {
                    request.setAttribute("alertMessage", "Failed To generate Shipping Orders");
                }
                nextPage = "/PPD_main_GET/*";
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
