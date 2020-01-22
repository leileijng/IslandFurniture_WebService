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

public class WorkspaceMessage_Servlet extends HttpServlet {

    @EJB
    private WorkspaceBeanLocal workspaceBean;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession();
            StaffEntity staffEntity = (StaffEntity) session.getAttribute("staffEntity");
            if (staffEntity == null) {
                response.sendRedirect("A1/staffLogin.jsp");
            } else {
                // Set session attributes for retriving message count
                session.setAttribute("unreadMessages", workspaceBean.listAllUnreadInboxMessages(staffEntity.getId()));
                session.setAttribute("inboxMessages", workspaceBean.listAllInboxMessages(staffEntity.getId()));
                session.setAttribute("sentMessages", workspaceBean.listAllOutboxMessages(staffEntity.getId()));
            }
            String errMsg = request.getParameter("errMsg");
            String goodMsg = request.getParameter("goodMsg");

            // Convert messages into easier to read helpers (for jsp)
            //inbox
            List<MessageInboxEntity> inboxMessageEntities = workspaceBean.listAllInboxMessages(staffEntity.getId());
            List<MessageHelper> inboxMessageHelpers = new ArrayList<>();
            for (MessageInboxEntity currentMessage : inboxMessageEntities) {
                MessageHelper messageHelper = new MessageHelper();
                messageHelper.setMessageId(currentMessage.getId());
                messageHelper.setSenderName(currentMessage.getSender().getName());
                messageHelper.setSenderEmail(currentMessage.getSender().getEmail());
                List<String> receiversName = new ArrayList<>();
                List<String> receiversEmail = new ArrayList<>();
                for (int i = 0; i < currentMessage.getReceivers().size(); i++) {
                    receiversName.add(currentMessage.getReceivers().get(i).getName());
                    receiversEmail.add(currentMessage.getReceivers().get(i).getEmail());
                }
                messageHelper.setReceiversName(receiversName);
                messageHelper.setReceiversEmail(receiversEmail);
                messageHelper.setMessage(currentMessage.getMessage());
                messageHelper.setSentDate(currentMessage.getSentDate());
                messageHelper.setMessageRead(currentMessage.getMessageRead());
                inboxMessageHelpers.add(messageHelper);
            }
            session.setAttribute("inboxMessagesHelpers", inboxMessageHelpers);
            //sent messages
            List<MessageOutboxEntity> sentMessageEntities = workspaceBean.listAllOutboxMessages(staffEntity.getId());
            List<MessageHelper> sentMessageHelpers = new ArrayList<>();
            for (MessageOutboxEntity currentMessage : sentMessageEntities) {
                MessageHelper messageHelper = new MessageHelper();
                messageHelper.setMessageId(currentMessage.getId());
                messageHelper.setSenderName(currentMessage.getSender().getName());
                messageHelper.setSenderEmail(currentMessage.getSender().getEmail());
                List<String> receiversName = new ArrayList<>();
                List<String> receiversEmail = new ArrayList<>();
                for (int i = 0; i < currentMessage.getReceivers().size(); i++) {
                    receiversName.add(currentMessage.getReceivers().get(i).getName());
                    receiversEmail.add(currentMessage.getReceivers().get(i).getEmail());
                }
                messageHelper.setReceiversName(receiversName);
                messageHelper.setReceiversEmail(receiversEmail);
                messageHelper.setMessage(currentMessage.getMessage());
                messageHelper.setSentDate(currentMessage.getSentDate());
                messageHelper.setMessageRead(currentMessage.getMessageRead());
                sentMessageHelpers.add(messageHelper);
            }
            session.setAttribute("sentMessagesHelpers", sentMessageHelpers);

            //View controller (between inbox & sent messages
            String view = request.getParameter("view");
            if (view == null) { //if never specify in param then get from session
                view = (String) session.getAttribute("view");
            }
            if (view == null || view.equals("inbox")) {
                session.setAttribute("view", "inbox");

                if (errMsg == null && goodMsg == null) {
                    response.sendRedirect("A1/workspace_messageInbox.jsp");
                } else if ((errMsg != null) && (goodMsg == null)) {
                    if (!errMsg.equals("")) {
                        response.sendRedirect("A1/workspace_messageInbox.jsp?errMsg=" + errMsg);
                    }
                } else if ((errMsg == null && goodMsg != null)) {
                    if (!goodMsg.equals("")) {
                        response.sendRedirect("A1/workspace_messageInbox.jsp?goodMsg=" + goodMsg);
                    }
                }

            } else if (view.equals("sentMessages")) {
                session.setAttribute("view", "sentMessages");

                if (errMsg == null && goodMsg == null) {
                    response.sendRedirect("A1/workspace_messageSent.jsp");
                } else if ((errMsg != null) && (goodMsg == null)) {
                    if (!errMsg.equals("")) {
                        response.sendRedirect("A1/workspace_messageSent.jsp?errMsg=" + errMsg);
                    }
                } else if ((errMsg == null && goodMsg != null)) {
                    if (!goodMsg.equals("")) {
                        response.sendRedirect("A1/workspace_messageSent.jsp?goodMsg=" + goodMsg);
                    }
                }
            } else {
                session.setAttribute("view", "inbox");

                if (errMsg == null && goodMsg == null) {
                    response.sendRedirect("A1/workspace_messageInbox.jsp");
                } else if ((errMsg != null) && (goodMsg == null)) {
                    if (!errMsg.equals("")) {
                        response.sendRedirect("A1/workspace_messageInbox.jsp?errMsg=" + errMsg);
                    }
                } else if ((errMsg == null && goodMsg != null)) {
                    if (!goodMsg.equals("")) {
                        response.sendRedirect("A1/workspace_messageInbox.jsp?goodMsg=" + goodMsg);
                    }
                }
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
