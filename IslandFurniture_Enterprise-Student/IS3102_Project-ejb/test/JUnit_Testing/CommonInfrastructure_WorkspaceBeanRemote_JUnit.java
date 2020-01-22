package JUnit_Testing;

import CommonInfrastructure.Workspace.WorkspaceBeanRemote;
import EntityManager.MessageInboxEntity;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommonInfrastructure_WorkspaceBeanRemote_JUnit {

    WorkspaceBeanRemote workspaceBean = lookupWorkspaceBeanRemote();
    static Long staffId = 12L; //admin id 12L after initial startup
    //todoId = 1L

    public CommonInfrastructure_WorkspaceBeanRemote_JUnit() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test01ListAllInboxMessages() {
        System.out.println("testListAllInboxMessages");
        Long testdata_staffID = staffId;
        List result = workspaceBean.listAllInboxMessages(testdata_staffID);
        assertTrue(result.isEmpty());
    }

    @Test
    public void test02ListAllUnreadInboxMessages() {
        System.out.println("testListAllUnreadInboxMessages");
        Long testdata_staffID = 1000L;
        List result = workspaceBean.listAllUnreadInboxMessages(testdata_staffID);
        assertNull(result);
    }

    @Test
    public void test03ListAllOutboxMessages() {
        System.out.println("testListAllOutboxMessages");
        Long testdata_staffID = 1000L;
        List result = workspaceBean.listAllOutboxMessages(testdata_staffID);
        assertNull(result);
    }

    @Test
    public void test04DeleteSingleInboxMessage() {
        System.out.println("testDeleteSingleInboxMessage");
        Long testdata_staffID = 1000L;
        Long testdata_messageID = 5L;
        Boolean result = workspaceBean.deleteSingleInboxMessage(testdata_staffID, testdata_messageID);
        assertFalse(result);
    }

    @Test
    public void test05DeleteSingleOutboxMessage() {
        System.out.println("testDeleteSingleOutboxMessage");
        Long testdata_staffID = 1000L;
        Long testdata_messageID = 251L;
        Boolean result = workspaceBean.deleteSingleOutboxMessage(testdata_staffID, testdata_messageID);
        assertFalse(result);
    }

    @Test
    public void test06DeleteAnnouncement() {
        System.out.println("testDeleteAnnouncement");
        String testdata_callerStaffID = "12";
        Long testdata_announcementId = 1000L;
        Boolean result = workspaceBean.deleteAnnouncement(testdata_callerStaffID, testdata_announcementId);
        assertFalse(result);
    }

    @Test
    public void test07UpdateAnnouncement() {
        System.out.println("testUpdateAnnouncement");
        String testdata_callerStaffID = "12";
        Long testdata_announcementId = 1000L;
        String testdata_message = "Please ensure that your account details are correct after registration";
        Date testdata_expiryDate = new Date();
        Boolean result = workspaceBean.updateAnnouncement(testdata_callerStaffID, testdata_announcementId, testdata_message, testdata_expiryDate);
        assertFalse(result);
    }

    @Test
    public void test08MakeAnnouncement() {
        System.out.println("testMakeAnnouncement");
        String testdata_callerStaffID = "12";
        String testdata_sender = "Administrator";
        String testdata_title = "Account registration";
        String testdata_message = "Please ensure that your account details are correct after registration";
        Date testdata_expiryDate = new Date();
        Boolean result = workspaceBean.makeAnnouncement(testdata_callerStaffID, testdata_sender, testdata_title, testdata_message, testdata_expiryDate);
        assertTrue(result);
    }

    @Test
    public void test09GetListOfAllNotExpiredAnnouncement() {
        System.out.println("testGetListOfAllNotExpiredAnnouncement");
        List result = workspaceBean.getListOfAllNotExpiredAnnouncement();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test10RemoveToDoList() {
        System.out.println("testRemoveToDoList");
        Long testdata_staffId = 1200L;
        Long testdata_toDoId = 3L;
        Boolean result = workspaceBean.removeToDoList(testdata_staffId, testdata_toDoId);
        assertFalse(result);
    }

    @Test
    public void test11AddToDoList() {
        System.out.println("testAddToDoList");
        Long testdata_staffId = staffId;
        String testdata_description = "Call Michael to buy parts from supplier for production.";
        Boolean result = workspaceBean.addToDoList(testdata_staffId, testdata_description);
        assertTrue(result);
    }

    @Test
    public void test12GetAllToDoListOfAStaff() {
        System.out.println("testGetAllToDoListOfAStaff");
        Long testdata_staffId = staffId;
        List result = workspaceBean.getAllToDoListOfAStaff(testdata_staffId);
        assertNotNull(result);
    }

    @Test
    public void test13MarkToDoListAsDone() {
        System.out.println("testMarkToDoListAsDone");
        Long testdata_id = 1000L;
        Boolean result = workspaceBean.markToDoListAsDone(testdata_id);
        assertFalse(result);
    }

    @Test
    public void test14MarkToDoListAsUndone() {
        System.out.println("testMarkToDoListAsDone");
        Long testdata_id = 1000L;
        Boolean result = workspaceBean.markToDoListAsDone(testdata_id);
        assertFalse(result);
    }

    @Test
    public void test15ToggleToDoListIsDone() {
        System.out.println("testToggleToDoListIsDone");
        Long testdata_id = 1000L;
        Boolean result = workspaceBean.toggleToDoListIsDone(testdata_id);
        assertFalse(result);
    }

    @Test
    public void test16GetStaffEmail() {
        System.out.println("testGetStaffEmail");
        Long testdata_staffID = staffId;
        String result = workspaceBean.getStaffEmail(testdata_staffID);
        assertEquals("admin@if.com", result);
    }

    @Test
    public void test17GetStaffName() {
        System.out.println("testGetStaffName");
        Long testdata_staffID = staffId;
        String result = workspaceBean.getStaffName(testdata_staffID);
        assertEquals("Administrator", result);
    }

    private WorkspaceBeanRemote lookupWorkspaceBeanRemote() {
        try {
            Context c = new InitialContext();
            return (WorkspaceBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/WorkspaceBean!CommonInfrastructure.Workspace.WorkspaceBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
