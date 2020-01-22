package A1_servlets;

import CommonInfrastructure.Workspace.WorkspaceBeanLocal;
import EntityManager.AnnouncementEntity;
import EntityManager.StaffEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WorkspaceAnnouncement_UpdateServlet extends HttpServlet {

    @EJB
    private WorkspaceBeanLocal workspaceBeanLocal;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String result;
        try {
            HttpSession session = request.getSession();
            StaffEntity currentLoggedInStaffEntity = (StaffEntity) session.getAttribute("staffEntity");
            String currentLoggedInStaffID;
            if (currentLoggedInStaffEntity == null) {
                currentLoggedInStaffID = "System";
            } else {
                currentLoggedInStaffID = currentLoggedInStaffEntity.getId().toString();
            }
            String announcementId = request.getParameter("id");
            String message = request.getParameter("message");
            String expiryDate = request.getParameter("expiryDate");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(expiryDate);

            boolean canUpdate = workspaceBeanLocal.updateAnnouncement(currentLoggedInStaffID, Long.valueOf(announcementId), message, date);
            if (canUpdate) {
                result = "?id=" + announcementId + "&goodMsg=Announcement updated.";
                //update announcement list
                List<AnnouncementEntity> listOfAnnouncements = workspaceBeanLocal.getListOfAllNotExpiredAnnouncement();
                session.setAttribute("listOfAnnouncements", listOfAnnouncements);
                response.sendRedirect("A1/workspace_updateAnnouncement.jsp" + result);
            } else {
                result = "?id=" + announcementId + "&errMsg=Failed to update announcement.";
                response.sendRedirect("A1/workspace_updateAnnouncement.jsp" + result);
            }
        } catch (Exception ex) {
            out.println(ex);
            ex.printStackTrace();
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
