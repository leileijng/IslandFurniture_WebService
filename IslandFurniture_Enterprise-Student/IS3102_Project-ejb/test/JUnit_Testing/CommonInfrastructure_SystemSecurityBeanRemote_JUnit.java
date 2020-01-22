package JUnit_Testing;

import CommonInfrastructure.SystemSecurity.SystemSecurityBeanRemote;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommonInfrastructure_SystemSecurityBeanRemote_JUnit {

    SystemSecurityBeanRemote systemSecurityBean = lookupSystemSecurityBeanRemote();
    //No configuration needed

    public CommonInfrastructure_SystemSecurityBeanRemote_JUnit() {
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
    public void testSendActivationEmailForStaff() {
        System.out.println("testSendActivationEmailForStaff");
        String testdata_email = "admin@if.com";
        Boolean result = systemSecurityBean.sendActivationEmailForStaff(testdata_email);
        assertTrue(result);
    }

    @Test
    public void testSendPasswordResetEmailForStaff() {
        System.out.println("testSendPasswordResetEmailForStaff");
        String testdata_email = "admin@if.com";
        Boolean result = systemSecurityBean.sendPasswordResetEmailForStaff(testdata_email);
        assertTrue(result);
    }

    @Test
    public void testActivateStaffAccount() {
        System.out.println("testActivateStaffAccount");
        String testdata_email = "admin@if.com";
        String testdata_code = "123456787654321";
        Boolean result = systemSecurityBean.activateStaffAccount(testdata_email, testdata_code);
        assertFalse(result);
    }

    @Test
    public void testValidatePasswordResetForStaff() {
        System.out.println("testValidatePasswordResetForStaff");
        String testdata_email = "admin@if.com";
        String testdata_code = "123456787654321";
        Boolean result = systemSecurityBean.validatePasswordResetForStaff(testdata_email, testdata_code);
        assertFalse(result);
    }

    private SystemSecurityBeanRemote lookupSystemSecurityBeanRemote() {
        try {
            Context c = new InitialContext();
            return (SystemSecurityBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/SystemSecurityBean!CommonInfrastructure.SystemSecurity.SystemSecurityBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
