package A4_servlets;

import EntityManager.MemberEntity;
import OperationalCRM.LoyaltyAndRewards.LoyaltyAndRewardsBeanLocal;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoyaltyCardManagement_Servlet extends HttpServlet {

    @EJB
    private LoyaltyAndRewardsBeanLocal loyaltyAndRewardsBeanLocal;
    String result;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String memberEmail = request.getParameter("memberEmail");
            String memberEmail2 = request.getParameter("memberEmail2");
            String loyaltyCardID = request.getParameter("loyaltyCardID");
            System.out.println(memberEmail);
            System.out.println(loyaltyCardID);
            if (loyaltyCardID == null) {
                MemberEntity memberEntity = loyaltyAndRewardsBeanLocal.checkMemberHasCard(memberEmail);
                if (memberEntity != null) { //No card tied before
                    result = "?goodMsg=Member retrieved. Scan the loyalty card before issuing to the member.&memberEmail="+memberEmail;
                    response.sendRedirect("A4/loyaltyCardMgt.jsp?" + result);
                } else {//Card already tied
                    result = "?errMsg=Member has already previously collected a loyalty card.";
                    response.sendRedirect("A4/loyaltyCardMgt.jsp" + result);
                }
            } else {
                MemberEntity memberEntity = loyaltyAndRewardsBeanLocal.tieCardToMember(memberEmail2, loyaltyCardID);
                result = "?goodMsg=Card linked to member.";
                response.sendRedirect("A4/loyaltyCardMgt.jsp" + result);
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
