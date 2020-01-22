package JUnit_Testing;

import EntityManager.CountryEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.SupplierEntity;
import EntityManager.Supplier_ItemEntity;
import SCM.SupplierManagement.SupplierManagementBeanRemote;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SCM_SupplierManagementBeanRemote_JUnit {
    SupplierManagementBeanRemote supplierManagementBean = lookupSupplierManagementBeanRemote();
    
    private Long supplierID = 62L;
    public SCM_SupplierManagementBeanRemote_JUnit() {
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
    public void addSupplier() {
        System.out.println("addSupplier()");
        String supplierName = "Impossible Supplier";
        String contactNo = "+6567661234";
        String email = "supplier@woodspteltd.com.sg";
        String address = "Singapore, NUS kent ridge";
        Long testdata_countryId = 1200L;
        Long testdata_roId = 1200L;
        Boolean result = supplierManagementBean.addSupplier(supplierName, contactNo, email, address, testdata_countryId, testdata_roId);
        assertFalse(result);
    }

    @Test
    public void deleteSupplier() {
        System.out.println("deleteSupplier()");
        Long testdata_supplierID = 1200L;
        Boolean result = supplierManagementBean.deleteSupplier(testdata_supplierID);
        assertFalse(result);
    }

    @Test
    public void editSupplier() {
        System.out.println("editSupplier()");
        String testdata_supplierName = "1";
        Long testdata_supplierID = 1000L;
        String testdata_address = "55 Tanglin Home, #04-12";
        String testdata_phone = "81234333";
        String testdata_password = "";
        String testdata_email = "woods@com.com";
        Long testdata_countryId = 1200L;
        Boolean result = supplierManagementBean.editSupplier(testdata_supplierID, testdata_supplierName, testdata_phone, testdata_email, testdata_address, testdata_countryId);
        assertFalse(result);
    }

    @Test
    public void getSupplier() {
        System.out.println("getSupplier()");
        Long testdata_supplierID = 62L;
        SupplierEntity result = supplierManagementBean.getSupplier(testdata_supplierID);
        assertNotNull(result);
    }

    @Test
    public void viewAllSupplierList() {
        System.out.println("viewAllSupplierList()");
        List<SupplierEntity> suppliers = supplierManagementBean.viewAllSupplierList();
        assertNotNull(suppliers);
    }

    @Test
    public void checkSupplierExists() {
        System.out.println("checkSupplierExists()");
        String testdata_supplierName = "1";
        Boolean result = supplierManagementBean.checkSupplierExists(testdata_supplierName);
        assertNotNull(result);
    }

    @Test
    public void getListOfCountries() {
        System.out.println("getListOfCountries()");
        List<CountryEntity> countries = supplierManagementBean.getListOfCountries();
        assertNotNull(countries);
    }

    @Test
    public void getSupplierItemList() {
        System.out.println("getSupplierItemList()");
        Long testdata_supplierID = 1000L;
        List<Supplier_ItemEntity> supplierItemList = supplierManagementBean.getSupplierItemList(testdata_supplierID);
        assertNotNull(supplierItemList);
    }

    @Test
    public void getSupplierListOfRO() {
        System.out.println("getSupplierListOfRO()");
        Long testdata_supplierID = 1000L;
        List<SupplierEntity> supplierListOfRO = supplierManagementBean.getSupplierListOfRO(testdata_supplierID);
        assertNotNull(supplierListOfRO);
    }

    private SupplierManagementBeanRemote lookupSupplierManagementBeanRemote() {
        try {
            Context c = new InitialContext();
            return (SupplierManagementBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/SupplierManagementBean!SCM.SupplierManagement.SupplierManagementBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
