package A1_servlets;

import CommonInfrastructure.Workspace.WorkspaceBeanLocal;
import EntityManager.StaffEntity;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WorkspaceMessage_AddServlet extends HttpServlet {

    @EJB
    private WorkspaceBeanLocal workspaceBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HttpSession session;
            session = request.getSession();
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            if (staffEntity == null) {
                response.sendRedirect("A1/staffLogin.jsp");
            } else {
                session.setAttribute("unreadMessages", workspaceBean.listAllUnreadInboxMessages(staffEntity.getId()));
                session.setAttribute("inboxMessages", workspaceBean.listAllInboxMessages(staffEntity.getId()));
                session.setAttribute("sentMessages", workspaceBean.listAllOutboxMessages(staffEntity.getId()));
            }
            String message = request.getParameter("message");
            if (message == null) {
                String[] selectArr = request.getParameterValues("select");
                String staffId = request.getParameter("id");
                List<Long> receiversID = new ArrayList<>();
                String receiverDisplayString = "";
                if (selectArr != null) {
                    for (int i = 0; i < selectArr.length; i++) {
                        receiverDisplayString += workspaceBean.getStaffName(Long.parseLong(selectArr[i]));
                        receiverDisplayString += " <" + workspaceBean.getStaffEmail(Long.parseLong(selectArr[i])) + ">;";
                        receiversID.add(Long.parseLong(selectArr[i]));
                    }
                } else {
                    receiverDisplayString += workspaceBean.getStaffName(Long.parseLong(staffId));
                    receiverDisplayString += " <" + workspaceBean.getStaffEmail(Long.parseLong(staffId)) + ">;";
                    receiversID.add(Long.parseLong(staffId));
                }
                session.setAttribute("receiversString", receiverDisplayString);
                session.setAttribute("receiversMemberID", receiversID);
                response.sendRedirect("A1/workspace_messageAdd.jsp");
            } else {
                List<Long> receiversStaffID = (List<Long>) session.getAttribute("receiversMemberID");
                Long senderStaffID = staffEntity.getId();
                workspaceBean.sendMessageToMultipleReceiver(senderStaffID, receiversStaffID, message);
                session.setAttribute("unreadMessages", workspaceBean.listAllUnreadInboxMessages(staffEntity.getId()));
                session.setAttribute("inboxMessages", workspaceBean.listAllInboxMessages(staffEntity.getId()));
                session.setAttribute("sentMessages", workspaceBean.listAllOutboxMessages(staffEntity.getId()));
                session.setAttribute("receiversString", null);
                session.setAttribute("receiversMemberID", null);
                response.sendRedirect("WorkspaceMessage_Servlet?goodMsg=Message sent.");
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
