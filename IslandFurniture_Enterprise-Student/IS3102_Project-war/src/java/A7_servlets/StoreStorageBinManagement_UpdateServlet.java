package A7_servlets;

import InventoryManagement.StoreAndKitchenInventoryManagement.StoreAndKitchenInventoryManagementBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StoreStorageBinManagement_UpdateServlet extends HttpServlet {

    @EJB
    private StoreAndKitchenInventoryManagementBeanLocal simbl;
    private String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            
            String storageBinId = request.getParameter("id");
            String name = request.getParameter("name");
            String length = request.getParameter("length");
            String width = request.getParameter("width");
            String height = request.getParameter("height");

            boolean canUpdate = simbl.updateStorageBin(Long.parseLong(storageBinId), name, Integer.parseInt(length), Integer.parseInt(width), Integer.parseInt(height));
            //out.println("<h1>" + canUpdate + "</h1>");
            if (!canUpdate) {
                result = "?errMsg=Please try again.";
                response.sendRedirect("A7/storageBinManagement_Update.jsp" + result);
            } else {
                result = "?errMsg=Storage bin updated successfully.";
                response.sendRedirect("StoreStorageBinManagement_Servlet" + result);
            }

        } catch (Exception ex) {
            out.println(ex);
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
