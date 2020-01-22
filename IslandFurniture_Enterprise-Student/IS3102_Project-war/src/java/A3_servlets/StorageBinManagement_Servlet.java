package A3_servlets;

import EntityManager.MasterProductionScheduleEntity;
import EntityManager.StorageBinEntity;
import EntityManager.WarehouseEntity;
import MRP.DemandManagement.DemandManagementBeanLocal;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StorageBinManagement_Servlet extends HttpServlet {

    @EJB
    private DemandManagementBeanLocal demandManagementBean;

    @EJB
    private ManufacturingWarehouseManagementBeanLocal manufacturingWarehouseManagementBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session;
            session = request.getSession();
            String errMsg = request.getParameter("errMsg");
            WarehouseEntity warehouseEntity = (WarehouseEntity) (session.getAttribute("warehouseEntity"));
            if (warehouseEntity == null) {
                response.sendRedirect("A3/manufacturingWarehouseManagement_view.jsp");
            } else {
                List<StorageBinEntity> storageBins = manufacturingWarehouseManagementBean.viewAllStorageBin(warehouseEntity.getId());
                session.setAttribute("storageBins", storageBins);

                Calendar calendar = Calendar.getInstance();
                int week = calendar.get(Calendar.WEEK_OF_MONTH);
                List<MasterProductionScheduleEntity> listOfMPS = demandManagementBean.getMPSList(warehouseEntity.getId());
                System.out.println("listOfMPS.size()" + listOfMPS.size());
                System.out.println("week: " + week);
                session.setAttribute("listOfMPS", listOfMPS);
                session.setAttribute("week", week);
                if (errMsg == null || errMsg.equals("")) {
                    response.sendRedirect("A3/storageBinManagement.jsp");
                } else {
                    response.sendRedirect("A3/storageBinManagement.jsp?errMsg=" + errMsg);
                }
            }
        } catch (Exception ex) {
            out.println("\n\n " + ex.getMessage());
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
