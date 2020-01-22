/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A2_servlets;

import EntityManager.SaleForecastEntity;
import MRP.SalesForecast.SalesForecastBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Baoyu
 */
public class SalesForecast_ajax_servlet extends HttpServlet {
    @EJB
    private SalesForecastBeanLocal sfBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("SalesForecast_ajax_servlet is called.");

        String target = request.getPathInfo();
        HttpSession session = request.getSession();
        Long storeId = (long) session.getAttribute("sf_storeId");
        Long schedulelId = (long) session.getAttribute("scheduleId");
        Long productGroupId = Long.parseLong(request.getParameter("productGroupId"));
        
        switch (target) {
            case "/regression":
                try (PrintWriter out = response.getWriter()) {
                    SaleForecastEntity saleForecast = sfBean.getSalesForecastLinearRegression(storeId, productGroupId, schedulelId);
                    out.write(";"+saleForecast.getQuantity()+";");                    
                }
                break;

            case "/average":
                try (PrintWriter out = response.getWriter()) {
                    SaleForecastEntity saleForecast = sfBean.getSalesForecastMovingAverage(storeId, productGroupId, schedulelId);
                    out.write(";"+saleForecast.getQuantity()+";");                    
                }
                break;
                
            case "/multiple":
                try (PrintWriter out = response.getWriter()) {
                    SaleForecastEntity saleForecast = sfBean.getSalesForecastMultipleLinearRegression(storeId, productGroupId, schedulelId);
                    out.write(";"+saleForecast.getQuantity()+";");                    
                }
                break;
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
