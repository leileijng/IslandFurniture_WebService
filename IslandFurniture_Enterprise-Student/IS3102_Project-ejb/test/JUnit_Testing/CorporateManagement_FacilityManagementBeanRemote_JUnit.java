package JUnit_Testing;

import CorporateManagement.FacilityManagement.FacilityManagementBeanRemote;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.StoreEntity;
import EntityManager.WarehouseEntity;
import java.util.List;
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
public class CorporateManagement_FacilityManagementBeanRemote_JUnit {

    static Long manufacturingId = 52L;
    static Long manufacturingFacilityId = 1L; //do not change
    static Long storeId = 55L;
    static Long warehouseId = 56L;
    FacilityManagementBeanRemote facilityManagementBean = lookupFacilityManagementBeanRemote();

    public CorporateManagement_FacilityManagementBeanRemote_JUnit() {
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
    public void test01AddRegionalOffice() {
        System.out.println("testAddRegionalOffice");
        String testdata_callerStaffID = "12";
        String testdata_regionalOfficeName = "West Asian Regional Office";
        String testdata_address = "56 West View Drive";
        String testdata_telephone = "95432112";
        String testdata_email = "waro@if.com";
        Boolean result = facilityManagementBean.addRegionalOffice(testdata_callerStaffID, testdata_regionalOfficeName, testdata_address, testdata_telephone, testdata_email);
        assertTrue(result);
    }

    @Test
    public void test02EditRegionalOffice() {
        System.out.println("testAddRegionalOffice");
        String testdata_callerStaffID = "12";
        Long testdata_id = 1000L;
        String testdata_regionalOfficeName = "West Asian Regional Office";
        String testdata_address = "56 West View Drive";
        String testdata_telephone = "95432112";
        String testdata_email = "waro@if.com";
        Boolean result = facilityManagementBean.editRegionalOffice(testdata_callerStaffID, testdata_id, testdata_regionalOfficeName, testdata_address, testdata_telephone, testdata_email);
        assertFalse(result);
    }

    @Test
    public void test03RemoveRegionalOffice() {
        System.out.println("testRemoveRegionalOffice");
        String testdata_callerStaffID = "12";
        String testdata_regionalOfficeName = "East Asian Regional Office";
        Boolean result = facilityManagementBean.removeRegionalOffice(testdata_callerStaffID, testdata_regionalOfficeName);
        assertFalse(result);
    }

    @Test
    public void test04ViewRegionalOffice() {
        System.out.println("testViewRegionalOffice");
        String testdata_regionalOfficeName = "North Pacific Regional Office";
        RegionalOfficeEntity result = facilityManagementBean.viewRegionalOffice(testdata_regionalOfficeName);
        assertNull(result);
    }

    @Test
    public void test05ViewListOfRegionalOffice() {
        System.out.println("testViewListOfRegionalOffice");
        List result = facilityManagementBean.viewListOfRegionalOffice();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test06CheckNameExistsOfRegionalOffice() {
        System.out.println("testCheckNameExistsOfRegionalOffice");
        String testdata_regionalOfficeName = "Asia Pacific Regional Office";
        Boolean result = facilityManagementBean.checkNameExistsOfRegionalOffice(testdata_regionalOfficeName);
        assertTrue(result);
    }

    @Test
    public void test07CreateManufacturingFacility() {
        System.out.println("testCreateManufacturingFacility");
        String testdata_callerStaffID = "12";
        String testdata_manufacturingFacilityName = "Manufacturing Facility SG3";
        String testdata_address = "123 Teban Garden";
        String testdata_telephone = "67890123";
        String testdata_email = "mfsg3@if.com";
        Integer testdata_capacity = 1000;
        String testdata_latitude = "11";
        String testdata_longitude = "111";
        ManufacturingFacilityEntity result = facilityManagementBean.createManufacturingFacility(testdata_callerStaffID, testdata_manufacturingFacilityName, testdata_address, testdata_telephone, testdata_email, testdata_capacity, testdata_latitude, testdata_longitude);
        manufacturingFacilityId = result.getId();
        assertNotNull(result);
    }

    @Test
    public void test08EditManufacturingFacility() {
        System.out.println("testEditManufacturingFacility");
        String testdata_callerStaffID = "12";
        Long testdata_id = manufacturingFacilityId;
        String testdata_manufacturingFacilityName = "Manufacturing Facility SG3";
        String testdata_address = "123 Jurong Garden";
        String testdata_telephone = "61234567";
        String testdata_email = "mfsg3@if.com";
        Integer testdata_capacity = 1000;
        String testdata_latitude = "11";
        String testdata_longitude = "111";
        Boolean result = facilityManagementBean.editManufacturingFacility(testdata_callerStaffID, testdata_id, testdata_manufacturingFacilityName, testdata_address, testdata_telephone, testdata_email, testdata_capacity, testdata_latitude, testdata_longitude);
        assertTrue(result);
    }

    @Test
    public void test09RemoveManufacturingFacility() {
        System.out.println("testRemoveManufacturingFacility");
        String testdata_callerStaffID = "12";
        String testdata_manufacturingFacilityId = "1200";
        Boolean result = facilityManagementBean.removeManufacturingFacility(testdata_callerStaffID, testdata_manufacturingFacilityId);
        assertFalse(result);
    }

    @Test
    public void test10ViewManufacturingFacility() {
        System.out.println("testViewManufacturingFacility");
        Long testdata_manufacturingId = manufacturingFacilityId;
        ManufacturingFacilityEntity result = facilityManagementBean.viewManufacturingFacility(testdata_manufacturingId);
        assertNotNull(result);
    }

    @Test
    public void test11ViewListOfManufacturingFacility() {
        System.out.println("testViewListOfManufacturingFacility");
        List result = facilityManagementBean.viewListOfManufacturingFacility();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test12CheckNameExistsOfManufacturingFacility() {
        System.out.println("testCheckNameExistsOfManufacturingFacility");
        String testdata_name = "Middle East Manufacturing Facility";
        Boolean result = facilityManagementBean.checkNameExistsOfManufacturingFacility(testdata_name);
        assertFalse(result);
    }

    @Test
    public void test13GetManufacturingFacilityByName() {
        System.out.println("testGetManufacturingFacilityByName");
        String testdata_name = "Manufacturing Facility SG3";
        ManufacturingFacilityEntity result = facilityManagementBean.getManufacturingFacilityByName(testdata_name);
        assertEquals("Manufacturing Facility SG3", result.getName());
    }

    @Test
    public void test14AddManufacturingFacilityToRegionalOffice() {
        System.out.println("testAddManufacturingFacilityToRegionalOffice");
        String testdata_callerStaffID = "12";
        Long testdata_regionalOfficeId = 1000L;
        Long testdata_MFid = 48L;
        Boolean result = facilityManagementBean.addManufacturingFacilityToRegionalOffice(testdata_callerStaffID, testdata_regionalOfficeId, testdata_MFid);
        assertFalse(result);
    }

    @Test
    public void test15UpdateManufacturingFacilityToRegionalOffice() {
        System.out.println("testUpdateManufacturingFacilityToRegionalOffice");
        String testdata_callerStaffID = "12";
        Long testdata_regionalOfficeId = 46L;
        Long testdata_MFid = 48L;
        Boolean result = facilityManagementBean.updateManufacturingFacilityToRegionalOffice(testdata_callerStaffID, testdata_regionalOfficeId, testdata_MFid);
        assertFalse(result);
    }

//    @Test
//    public void test16EditStore() {
//        System.out.println("testEditStore");
//        String testdata_callerStaffID = "12";
//        Long testdata_id = storeId;
//        String testdata_storeName = "Bugis Home Store";
//        String testdata_address = "30 Bugis Street";
//        String testdata_telephone = "64756666";
//        String testdata_email = "bugisstore@if.com";
//        Long testdata_countryID = 19L;
//        Boolean result = facilityManagementBean.editStore(testdata_callerStaffID, testdata_id, testdata_storeName, testdata_address, testdata_telephone, testdata_email, testdata_countryID);
//        assertFalse(result == null);
//    }
    @Test
    public void test17ViewStoreEntity() {
        System.out.println("testViewStoreEntity");
        Long testdata_storeId = storeId;
        StoreEntity result = facilityManagementBean.viewStoreEntity(testdata_storeId);
        assertNotNull(result);
    }

    @Test
    public void test18ViewListOfStore() {
        System.out.println("testViewListOfStore");
        List result = facilityManagementBean.viewListOfStore();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test19CheckNameExistsOfStore() {
        System.out.println("testCheckNameExistsOfStore");
        String testdata_name = "Queenstown Store";
        Boolean result = facilityManagementBean.checkNameExistsOfStore(testdata_name);
        assertTrue(result);
    }

    @Test
    public void test20GetStoreByName() {
        System.out.println("testGetStoreByName");
        String testdata_name = "Tampines Store";
        StoreEntity result = facilityManagementBean.getStoreByName(testdata_name);
        assertEquals("Tampines Store", result.getName());
    }

    @Test
    public void test21GetStoreByID() {
        System.out.println("testGetStoreByID");
        Long testdata_id = storeId;
        StoreEntity result = facilityManagementBean.getStoreByID(testdata_id);
        assertNotNull(result);
    }

    @Test
    public void test22RemoveStore() {
        System.out.println("testRemoveStore");
        String testdata_callerStaffID = "12";
        Long testdata_storeId = 111L;
        Boolean result = facilityManagementBean.removeStore(testdata_callerStaffID, testdata_storeId);
        assertFalse(result);
    }

    @Test
    public void test23AddStoreToRegionalOffice() {
        System.out.println("testAddStoreToRegionalOffice");
        String testdata_callerStaffID = "12";
        Long testdata_regionalOfficeId = 123L;
        Long testdata_storeId = 901L;
        Boolean result = facilityManagementBean.addStoreToRegionalOffice(testdata_callerStaffID, testdata_regionalOfficeId, testdata_storeId);
        assertNotNull(result);
    }

    @Test
    public void test24UpdateStoreToRegionalOffice() {
        System.out.println("testUpdateStoreToRegionalOffice");
        String testdata_callerStaffID = "12";
        Long testdata_regionalOfficeId = 46L;
        Long testdata_storeId = 55L;
        Boolean result = facilityManagementBean.updateStoreToRegionalOffice(testdata_callerStaffID, testdata_regionalOfficeId, testdata_storeId);
        assertFalse(result);
    }

    @Test
    public void test25GetStoreListByRegionalOffice() {
        System.out.println("testGetStoreListByRegionalOffice");
        Long testdata_regionalOfficeId = 91L;
        List result = facilityManagementBean.getStoreListByRegionalOffice(testdata_regionalOfficeId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void test26CreateWarehouse() {
        System.out.println("testCreateWarehouse");
        String testdata_callerStaffID = "12";
        String testdata_warehouseName = "Bugis Store Warehouse";
        String testdata_address = "30 Bugis Street";
        String testdata_telephone = "6789123";
        String testdata_email = "bugiswarehouse@if.com";
        Long testdata_storeID = 55L;
        Long testdata_MFid = 48L;
        WarehouseEntity result = facilityManagementBean.createWarehouse(testdata_callerStaffID, testdata_warehouseName, testdata_address, testdata_telephone, testdata_email, testdata_storeID, testdata_MFid);
        assertNull(result);
    }

    @Test
    public void test27EditWarehouse() {
        System.out.println("testEditWarehouse");
        String testdata_callerStaffID = "12";
        String testdata_warehouseName = "Bugis Store Warehouse";
        String testdata_address = "30 Bugis Street";
        String testdata_telephone = "6789123";
        String testdata_email = "bugiswarehouse@if.com";
        Long testdata_id = warehouseId;
        Boolean result = facilityManagementBean.editWarehouse(testdata_callerStaffID, testdata_id, testdata_warehouseName, testdata_address, testdata_telephone, testdata_email);
        assertTrue(result);
    }

    @Test
    public void test28DeleteWarehouse() {
        System.out.println("testDeleteWarehouse");
        String testdata_callerStaffID = "12";
        Long testdata_id = warehouseId;
        Boolean result = facilityManagementBean.deleteWarehouse(testdata_callerStaffID, testdata_id);
        assertTrue(result);
    }

    @Test
    public void test29GetWarehouseByName() {
        System.out.println("testGetWarehouseByName");
        String testdata_name = "Bandai Store Warehouse";
        StoreEntity result = facilityManagementBean.getStoreByName(testdata_name);
        assertNull(result);
    }

    @Test
    public void test30CheckNameExistsOfWarehouse() {
        System.out.println("testCheckNameExistsOfWarehouse");
        String testdata_name = "Queenstown Store Warehouse";
        Boolean result = facilityManagementBean.checkNameExistsOfWarehouse(testdata_name);
        assertTrue(result);
    }

    @Test
    public void test31CheckIfWarehouseContainsItem() {
        System.out.println("testCheckIfWarehouseContainsItem");
        Long testdata_id = 51L;
        Boolean result = facilityManagementBean.checkIfWarehouseContainsItem(testdata_id);
        assertFalse(result);
    }

    @Test
    public void test32GetWarehouseById() {
        System.out.println("testGetWarehouseById");
        Long testdata_id = 49L;
        StoreEntity result = facilityManagementBean.getStoreByID(testdata_id);
        assertNull(result);
    }

    @Test
    public void test33GetWarehouseList() {
        System.out.println("testGetWarehouseList");
        List result = facilityManagementBean.getWarehouseList();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test34GetMFWarehouseList() {
        System.out.println("testGetMFWarehouseList");
        List result = facilityManagementBean.getMFWarehouseList();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test35GetStoreWarehouseList() {
        System.out.println("testGetStoreWarehouseList");
        List result = facilityManagementBean.getStoreWarehouseList();
        assertTrue(!result.isEmpty());
    }

    @Test
    public void test36GetListOfCountries() {
        System.out.println("testGetListOfCountries");
        List result = facilityManagementBean.getListOfCountries();
        assertTrue(!result.isEmpty());
    }

    private FacilityManagementBeanRemote lookupFacilityManagementBeanRemote() {
        try {
            Context c = new InitialContext();
            return (FacilityManagementBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/FacilityManagementBean!CorporateManagement.FacilityManagement.FacilityManagementBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
