package CommonInfrastructure.Workspace;

import EntityManager.AnnouncementEntity;
import EntityManager.MessageEntity;
import EntityManager.MessageInboxEntity;
import EntityManager.MessageOutboxEntity;
import EntityManager.ToDoEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface WorkspaceBeanLocal {
    //Check staff exists via AccountManagementBean first before doing anything in this bean

    public boolean sendMessage(Long senderStaffID, Long receiverStaffID, String message);

    public boolean sendMessageToMultipleReceiver(Long senderStaffID, List<Long> staffIDs, String message);

    public List<MessageInboxEntity> listAllInboxMessages(Long staffID);

    public List<MessageInboxEntity> listAllUnreadInboxMessages(Long staffID);

    public List<MessageOutboxEntity> listAllOutboxMessages(Long staffID);

    public MessageInboxEntity readInboxMessage(Long staffID, Long messageID); // returns null if staff or message not found

    public MessageOutboxEntity readSentMessage(Long staffID, Long messageID); // returns null if staff or message not found
    //Following returns true if operation suceeds

    public boolean deleteSingleInboxMessage(Long staffID, Long messageID);

    public boolean deleteSingleOutboxMessage(Long staffID, Long messageID);

    public boolean makeAnnouncement(String callerStaffID, String sender, String title, String message, Date expiryDate); //annoucement is just a message, added to all the staffEntity, with the annoucement flag set

    public List<AnnouncementEntity> getListOfAllNotExpiredAnnouncement();

    public boolean updateAnnouncement(String callerStaffID, Long announcementId, String message, Date expiryDate);

    public boolean deleteAnnouncement(String callerStaffID, Long announcementId);

    public boolean addToDoList(Long staffId, String description);

    public boolean removeToDoList(Long staffId, Long toDoId);

    public boolean editToDoList(Long id, String description);

    public List<ToDoEntity> getAllToDoListOfAStaff(Long staffId);

    public boolean markToDoListAsDone(Long id);

    public boolean markToDoListAsUndone(Long id);

    public boolean toggleToDoListIsDone(Long id);

    public String getStaffEmail(Long staffID);

    public String getStaffName(Long staffID);
}
