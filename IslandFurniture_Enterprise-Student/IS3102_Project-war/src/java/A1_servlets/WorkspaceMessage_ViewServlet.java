package A1_servlets;

import CommonInfrastructure.Workspace.WorkspaceBeanLocal;
import EntityManager.MessageInboxEntity;
import EntityManager.MessageOutboxEntity;
import EntityManager.StaffEntity;
import HelperClasses.MessageHelper;
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

public class WorkspaceMessage_ViewServlet extends HttpServlet {

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
            String messageID = request.getParameter("id");
            String view = request.getParameter("view");
            if (view == null) {
                response.sendRedirect("../WorkspaceMessage_Servlet");
            } else if (view.equals("inbox")) {
                MessageInboxEntity messageInboxEntity = workspaceBean.readInboxMessage(staffEntity.getId(), Long.parseLong(messageID));
                MessageHelper messageHelper = new MessageHelper();
                //Set data to message helper
                messageHelper.setMessageId(messageInboxEntity.getId());
                messageHelper.setSenderName(messageInboxEntity.getSender().getName());
                messageHelper.setMessage(messageInboxEntity.getMessage());
                messageHelper.setSentDate(messageInboxEntity.getSentDate());
                messageHelper.setMessageRead(messageInboxEntity.getMessageRead());
                List<String> receiversName = new ArrayList<>();
                List<String> receiversEmail = new ArrayList<>();

                for (int i = 0; i < messageInboxEntity.getReceivers().size(); i++) {
                    receiversName.add(messageInboxEntity.getReceivers().get(i).getName());
                    receiversEmail.add(messageInboxEntity.getReceivers().get(i).getEmail());
                }
                messageHelper.setReceiversName(receiversName);
                messageHelper.setReceiversEmail(receiversEmail);

                session.setAttribute("message", messageHelper);
                session.setAttribute("view", "inbox");
                response.sendRedirect("A1/workspace_messageInboxView.jsp");
            } else if (view.equals("sentMessages")) {
                MessageOutboxEntity messageOutboxEntity = workspaceBean.readSentMessage(staffEntity.getId(), Long.parseLong(messageID));
                MessageHelper messageHelper = new MessageHelper();
                //Set data to message helper
                messageHelper.setMessageId(messageOutboxEntity.getId());
                messageHelper.setSenderName(messageOutboxEntity.getSender().getName());
                messageHelper.setMessage(messageOutboxEntity.getMessage());
                messageHelper.setSentDate(messageOutboxEntity.getSentDate());
                messageHelper.setMessageRead(messageOutboxEntity.getMessageRead());
                List<String> receiversName = new ArrayList<>();
                List<String> receiversEmail = new ArrayList<>();

                for (int i = 0; i < messageOutboxEntity.getReceivers().size(); i++) {
                    receiversName.add(messageOutboxEntity.getReceivers().get(i).getName());
                    receiversEmail.add(messageOutboxEntity.getReceivers().get(i).getEmail());
                }
                messageHelper.setReceiversName(receiversName);
                messageHelper.setReceiversEmail(receiversEmail);

                session.setAttribute("message", messageHelper);
                session.setAttribute("view", "sentMessages");
                response.sendRedirect("A1/workspace_messageSentView.jsp");
            } else { // not in either inbox or outbox view
                response.sendRedirect("../WorkspaceMessage_Servlet");
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
