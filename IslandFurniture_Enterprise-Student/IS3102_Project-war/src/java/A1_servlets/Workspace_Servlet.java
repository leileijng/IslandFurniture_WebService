/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package A1_servlets;

import CommonInfrastructure.Workspace.WorkspaceBeanLocal;
import EntityManager.StaffEntity;
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
 * @author -VeRyLuNaTiC
 */
public class Workspace_Servlet extends HttpServlet {

    @EJB
    private WorkspaceBeanLocal workspaceBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session;
            session = request.getSession();
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            if (staffEntity == null) {
                response.sendRedirect("A1/staffLogin.jsp");
            } else {
                session.setAttribute("staffEntity", staffEntity);
                session.setAttribute("listOfAnnouncements", workspaceBean.getListOfAllNotExpiredAnnouncement());
                session.setAttribute("unreadMessages", workspaceBean.listAllUnreadInboxMessages(staffEntity.getId()));
                session.setAttribute("inboxMessages", workspaceBean.listAllInboxMessages(staffEntity.getId()));
                session.setAttribute("sentMessages", workspaceBean.listAllOutboxMessages(staffEntity.getId()));
                session.setAttribute("toDoList", workspaceBean.getAllToDoListOfAStaff(staffEntity.getId()));
                response.sendRedirect("A1/workspace.jsp");
            }
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
