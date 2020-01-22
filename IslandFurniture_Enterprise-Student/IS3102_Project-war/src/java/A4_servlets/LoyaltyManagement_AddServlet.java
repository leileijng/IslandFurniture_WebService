package A4_servlets;

import OperationalCRM.LoyaltyAndRewards.LoyaltyAndRewardsBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoyaltyManagement_AddServlet extends HttpServlet {

    @EJB
    private LoyaltyAndRewardsBeanLocal loyaltyAndRewardsBeanLocal;
    String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String tier = request.getParameter("tier");
            String requiredAmount = request.getParameter("requiredAmount");
            String source = request.getParameter("source");

            if (loyaltyAndRewardsBeanLocal.getLoyaltyTierByName(tier) == null) {
                loyaltyAndRewardsBeanLocal.createLoyaltyTier(tier, Double.parseDouble(requiredAmount));

                result = "?goodMsg=Tier: " + tier + " has been created successfully.";
                response.sendRedirect("LoyaltyManagement_Servlet" + result);
            } else {
                result = "?errMsg=Failed to add tier, Tier: " + tier + " already exist.";
                response.sendRedirect(source + result);
            }
        } catch (Exception ex) {
            out.println(ex);
        } finally {
            out.close();
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
