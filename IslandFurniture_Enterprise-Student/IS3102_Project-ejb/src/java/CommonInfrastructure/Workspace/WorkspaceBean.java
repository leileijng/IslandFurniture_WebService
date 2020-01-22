package CommonInfrastructure.Workspace;

import Config.Config;
import EntityManager.AnnouncementEntity;
import EntityManager.MessageInboxEntity;
import EntityManager.MessageOutboxEntity;
import EntityManager.StaffEntity;
import EntityManager.ToDoEntity;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@Stateless
public class WorkspaceBean implements WorkspaceBeanLocal, WorkspaceBeanRemote {
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean sendMessage(Long senderStaffID, Long receiverStaffID, String message) {
        System.out.println("sendMessage() called with senderStaffID:" + senderStaffID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:senderStaffID");
            q.setParameter("senderStaffID", senderStaffID);
            StaffEntity senderStaffEntity = (StaffEntity) q.getSingleResult();
            q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:receiverStaffID");
            q.setParameter("receiverStaffID", receiverStaffID);
            StaffEntity receiverStaffEntity = (StaffEntity) q.getSingleResult();

            //sender side
            MessageOutboxEntity messageOutboxEntity = new MessageOutboxEntity();
            messageOutboxEntity.createOneToOneMessage(senderStaffEntity, receiverStaffEntity, message);
            messageOutboxEntity.setSentDate(new Date());
            messageOutboxEntity.setMessageRead(true);
            em.persist(messageOutboxEntity);

            List<MessageOutboxEntity> senderSentMessages = senderStaffEntity.getSentMessages();
            senderSentMessages.add(messageOutboxEntity);
            senderStaffEntity.setSentMessages(senderSentMessages);
            em.merge(senderStaffEntity);

            //receiver side
            MessageInboxEntity messageInboxEntity = new MessageInboxEntity();
            messageInboxEntity.createOneToOneMessage(senderStaffEntity, receiverStaffEntity, message);
            messageInboxEntity.setSentDate(new Date());
            messageInboxEntity.setMessageRead(false);
            em.persist(messageInboxEntity);

            List<MessageInboxEntity> receiverInboxMessages = receiverStaffEntity.getInboxMessages();
            receiverInboxMessages.add(messageInboxEntity);
            receiverStaffEntity.setInboxMessages(receiverInboxMessages);
            em.merge(receiverStaffEntity);

            System.out.println("Message sent.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to send a message:\n" + ex);
            return false;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean sendMessageToMultipleReceiver(Long senderStaffID, List<Long> receiverStaffID, String message) {
        System.out.println("sendMessageToMultipleReceiver() called with senderStaffID:" + senderStaffID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:senderStaffID");
            q.setParameter("senderStaffID", senderStaffID);
            StaffEntity senderStaffEntity = (StaffEntity) q.getSingleResult();

            //Get all the entity of the receiver
            List<StaffEntity> receivers = new ArrayList();
            for (Long currentStaffID : receiverStaffID) {
                q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:receiverStaffID");
                q.setParameter("receiverStaffID", currentStaffID);
                StaffEntity currentStaff = (StaffEntity) q.getSingleResult();
                receivers.add(currentStaff);
            }

            //Add the message to all the receiver
            for (StaffEntity currentStaff : receivers) {
                MessageInboxEntity messageInboxEntity = new MessageInboxEntity();
                messageInboxEntity.createOneToManyMessage(senderStaffEntity, receivers, message);
                messageInboxEntity.setSentDate(new Date());
                messageInboxEntity.setMessageRead(false); //receiver inbox
                em.persist(messageInboxEntity);
                List<MessageInboxEntity> receiverInboxMessages = currentStaff.getInboxMessages();
                receiverInboxMessages.add(messageInboxEntity);
                currentStaff.setInboxMessages(receiverInboxMessages);
                em.merge(currentStaff);
            }

            //Add message to sender sent items
            MessageOutboxEntity messageOutboxEntity = new MessageOutboxEntity();
            messageOutboxEntity.createOneToManyMessage(senderStaffEntity, receivers, message);
            messageOutboxEntity.setSentDate(new Date());
            messageOutboxEntity.setMessageRead(true); //sender sent items
            em.persist(messageOutboxEntity);

            List<MessageOutboxEntity> senderSentMessages = senderStaffEntity.getSentMessages();
            senderSentMessages.add(messageOutboxEntity);
            senderStaffEntity.setSentMessages(senderSentMessages);
            em.merge(senderStaffEntity);

            System.out.println("Message sent.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to send a message:\n" + ex);
            return false;
        }
    }

    public List<MessageInboxEntity> listAllInboxMessages(Long staffID) {
        System.out.println("listAllInboxMessages() called with staffID:" + staffID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageInboxEntity> inboxMessages = staffEntity.getInboxMessages();

            ArrayList<MessageInboxEntity> arrList = new ArrayList<>();
            for (int i = inboxMessages.size() - 1; i >= 0; i--) {
                arrList.add(inboxMessages.get(i));
            }

            System.out.println("Message list returned.");
            return arrList;
        } catch (Exception ex) {
            System.out.println("\nServer error in listing all inbox messages:\n" + ex);
            return null;
        }
    }

    public List<MessageInboxEntity> listAllUnreadInboxMessages(Long staffID) {
        System.out.println("listAllUnreadInboxMessages() called with staffID:" + staffID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageInboxEntity> inboxMessages = staffEntity.getInboxMessages();
            List<MessageInboxEntity> unreadInboxMessages = new ArrayList<>();
            for (MessageInboxEntity currentMessage : inboxMessages) {
                if (!currentMessage.getMessageRead()) {
                    unreadInboxMessages.add(currentMessage);
                }
            }
            System.out.println("Unread message list returned.");
            return unreadInboxMessages;
        } catch (Exception ex) {
            System.out.println("\nServer error in listing all unread inbox messages:\n" + ex);
            return null;
        }
    }

    public List<MessageOutboxEntity> listAllOutboxMessages(Long staffID) {
        System.out.println("listAllSentMessages() called with staffID:" + staffID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageOutboxEntity> sentMessages = staffEntity.getSentMessages();
            System.out.println("Message list returned.");

            ArrayList<MessageOutboxEntity> arrList = new ArrayList<>();
            for (int i = sentMessages.size() - 1; i >= 0; i--) {
                arrList.add(sentMessages.get(i));
            }
            return arrList;
        } catch (Exception ex) {
            System.out.println("\nServer error in listing all sent messages:\n" + ex);
            return null;
        }
    }

    @Override
    public MessageInboxEntity readInboxMessage(Long staffID, Long messageID) {
        System.out.println("readInboxMessage() called with staffID:" + staffID + " & messageID: " + messageID);
        MessageInboxEntity message = null;
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageInboxEntity> inboxMessages = staffEntity.getInboxMessages();
            for (MessageInboxEntity currentMessage : inboxMessages) {
                if (currentMessage.getId().equals(messageID)) {
                    currentMessage.setMessageRead(true);
                    message = currentMessage;
                    break;
                }
            }
            staffEntity.setInboxMessages(inboxMessages);
            em.merge(staffEntity);
            System.out.println("Message returned.");
            return message;
        } catch (Exception ex) {
            System.out.println("\nServer error in reading a message:\n" + ex);
            return null;
        }
    }

    @Override
    public MessageOutboxEntity readSentMessage(Long staffID, Long messageID) {
        System.out.println("readSentMessage() called with staffID:" + staffID + " & messageID: " + messageID);
        MessageOutboxEntity message = null;
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageOutboxEntity> sentMessages = staffEntity.getSentMessages();
            for (MessageOutboxEntity currentMessage : sentMessages) {
                if (currentMessage.getId().equals(messageID)) {
                    currentMessage.setMessageRead(true);
                    message = currentMessage;
                    break;
                }
            }
            staffEntity.setSentMessages(sentMessages);
            em.merge(staffEntity);
            System.out.println("Message returned.");
            return message;
        } catch (Exception ex) {
            System.out.println("\nServer error in reading a message:\n" + ex);
            return null;
        }
    }

    public boolean deleteSingleInboxMessage(Long staffID, Long messageID) {
        System.out.println("deleteInboxMessage() called with staffID:" + staffID + " & messageID: " + messageID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageInboxEntity> inboxMessages = staffEntity.getInboxMessages();

            for (MessageInboxEntity inboxMsg : inboxMessages) {
                if (inboxMsg.getId().equals(messageID)) {
                    inboxMessages.remove(inboxMsg);
                    break;
                }
            }
            staffEntity.setInboxMessages(inboxMessages);
            em.merge(staffEntity);
            System.out.println("Message deleted.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer error in deleting a message:\n" + ex);
            return false;
        }
    }

    public boolean deleteSingleOutboxMessage(Long staffID, Long messageID) {
        System.out.println("deleteSentMessage() called with staffID:" + staffID + " & messageID: " + messageID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            List<MessageOutboxEntity> sentMessages = staffEntity.getSentMessages();

            for (MessageOutboxEntity currentMessage : sentMessages) {
                if (currentMessage.getId().equals(messageID)) {
                    sentMessages.remove(currentMessage);
                    break;
                }
            }

            staffEntity.setSentMessages(sentMessages);
            em.merge(staffEntity);
            System.out.println("Message deleted.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer error in deleting a message:\n" + ex);
            return false;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public boolean makeAnnouncement(String callerStaffID, String sender, String title, String message, Date expiryDate) {
        System.out.println("makeAnnouncement() called with sender:" + sender);
        try {
            AnnouncementEntity announcement = new AnnouncementEntity(sender, title, message, expiryDate);
            em.persist(announcement);
            System.out.println("Announcement broadcasted.");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";makeAnnouncement();" + announcement.getId() + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to broadcast announcement:\n" + ex);
            return false;
        }
    }

    @Override
    public List<AnnouncementEntity> getListOfAllNotExpiredAnnouncement() {
        System.out.println("getListOfAllNotExpiredAnnouncement() called.");
        try {
            Query q = em.createQuery("select a from AnnouncementEntity a where a.expiryDate>=:today");
            q.setParameter("today", new Date(Calendar.getInstance().getTimeInMillis()), TemporalType.DATE);

            List<AnnouncementEntity> listOfAnnouncement = q.getResultList();
            System.out.println("getListOfAllNotExpiredAnnouncement() is successful");
            return listOfAnnouncement;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform getListOfAllNotExpiredAnnouncement():\n" + ex);
            return null;
        }
    }

    @Override
    public boolean updateAnnouncement(String callerStaffID, Long announcementId, String message, Date expiryDate) {
        System.out.println("updateAnnouncement() is called.");
        try {
            AnnouncementEntity announcementEntity = em.find(AnnouncementEntity.class, announcementId);
            announcementEntity.setMessage(message);
            announcementEntity.setExpiryDate(expiryDate);
            em.merge(announcementEntity);
            System.out.println("updateAnnouncement() is successful.");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";updateAnnouncement();" + announcementId + ";");
            out.close();
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("updateAnnouncement() EntityNotFoundException:\n" + ex);
            return false;
        } catch (Exception ex) {
            System.out.println("failed to perform updateAnnouncement():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean deleteAnnouncement(String callerStaffID, Long announcementId) {
        System.out.println("deleteAnnouncement() called.");
        try {
            AnnouncementEntity toDo = em.find(AnnouncementEntity.class, announcementId);
            em.remove(toDo);
            System.out.println("deleteAnnouncement() is successful.");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";deleteAnnouncement();" + announcementId + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("failed to perform deleteAnnouncement():\n" + ex);
            return false;
        }
    }

    @Override
    public String getStaffEmail(Long staffID) {
        System.out.println("getStaffEmail() called with staffID:" + staffID);
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffID);
            System.out.println("Staff email returned succesfully");
            return staffEntity.getEmail();
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to get staff, staff not found.\n" + ex);
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getStaffEmail:\n" + ex);
            return null;
        }
    }

    @Override
    public String getStaffName(Long staffID) {
        System.out.println("getStaffEmail() called with staffID:" + staffID);
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffID);
            System.out.println("Staff email returned succesfully");
            return staffEntity.getName();
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to get staff, staff not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getStaffEmail:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean addToDoList(Long staffId, String description) {
        System.out.println("addToDoList() called.");
        try {
            ToDoEntity toDo = new ToDoEntity(description, false);
            StaffEntity staffEntity = em.getReference(StaffEntity.class, staffId);
            staffEntity.getToDoList().add(toDo);
            em.merge(staffEntity);
            System.out.println("addToDoList() successful. Current size of ToDoList: " + staffEntity.getToDoList().size());
            return true;
        } catch (Exception ex) {
            System.out.println("failed to addToDoList():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean removeToDoList(Long staffId, Long toDoId) {
        System.out.println("removeToDoList() called.");
        try {
            StaffEntity staff = em.find(StaffEntity.class, staffId);
            ToDoEntity toDo = em.find(ToDoEntity.class, toDoId);
            staff.getToDoList().remove(toDo);
            em.remove(toDo);
            System.out.println("removeToDoList() is successful.");
            return true;
        } catch (Exception ex) {
            System.out.println("failed to perform removeToDoList():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean editToDoList(Long id, String description) {
        System.out.println("editToDoList() is called.");
        try {
            ToDoEntity toDo = em.find(ToDoEntity.class, id);
            toDo.setDescription(description);
            em.merge(toDo);
            System.out.println("editToDoList() is successful.");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("editToDoList() EntityNotFoundException:\n" + ex);
            return false;
        } catch (Exception ex) {
            System.out.println("failed to perform editToDoList():\n" + ex);
            return false;
        }
    }

    @Override
    public List<ToDoEntity> getAllToDoListOfAStaff(Long staffId) {
        System.out.println("getAllToDoList() is called.");
        try {
            Query q = em.createQuery("Select s from StaffEntity s where s.id=:staffId");
            q.setParameter("staffId", staffId);
            StaffEntity staff = (StaffEntity) q.getSingleResult();
            List<ToDoEntity> toDoList = staff.getToDoList();
            System.out.println("getAllToDoList() is successful.");
            return toDoList;
        } catch (Exception ex) {
            System.out.println("failed to getAllToDoList().\n" + ex);
            return null;
        }
    }

    @Override
    public boolean markToDoListAsDone(Long id) {
        System.out.println("markToDoListAsDone() is called.");
        try {
            ToDoEntity toDo = em.find(ToDoEntity.class, id);
            toDo.setIsDone(true);
            em.merge(toDo);
            System.out.println("markToDoListAsDone() is successful.");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("markToDoListAsDone() EntityNotFoundException:\n" + ex);
            return false;
        } catch (Exception ex) {
            System.out.println("failed to perform markToDoListAsDone():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean markToDoListAsUndone(Long id) {
        System.out.println("markToDoListAsUndone() is called.");
        try {
            ToDoEntity toDo = em.find(ToDoEntity.class, id);
            toDo.setIsDone(false);
            em.merge(toDo);
            System.out.println("markToDoListAsUndone() is successful.");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("markToDoListAsUndone() EntityNotFoundException:\n" + ex);
            return false;
        } catch (Exception ex) {
            System.out.println("failed to perform markToDoListAsUndone():\n" + ex);
            return false;
        }
    }

    @Override
    public boolean toggleToDoListIsDone(Long id) {
        System.out.println("toggleToDoListIsDone() is called.");
        try {
            ToDoEntity toDo = em.find(ToDoEntity.class, id);

            if (toDo.isDone()) {
                toDo.setIsDone(false);
            } else {
                toDo.setIsDone(true);
            }
            em.merge(toDo);
            System.out.println("toggleToDoListIsDone() is successful.");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("toggleToDoListIsDone() EntityNotFoundException:\n" + ex);
            return false;
        } catch (Exception ex) {
            System.out.println("failed to perform toggleToDoListIsDone():\n" + ex);
            return false;
        }
    }

}
