package JUnit_Testing;

import CorporateManagement.FacilityManagement.FacilityManagementBeanRemote;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.RetailProductEntity;
import EntityManager.StorageBinEntity;
import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanRemote;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SCM_ManufacturingWarehouseManagementBeanRemote_JUnit {
    

    ManufacturingWarehouseManagementBeanRemote manufacturingWarehouseManagementBean = lookupManufacturingWarehouseManagementBeanRemote();
    FacilityManagementBeanRemote facilityManagementBean = lookupFacilityManagementBeanRemote();

    Long warehouseID = 56L;//Need to change to the warehouse created after the facility management JUnit test
    Long transferOrderID = 731L;
    //Automatically retrieved variable, no need to set
    Long storageBinID = 735L;
    String SKU = "DFNH";

    public SCM_ManufacturingWarehouseManagementBeanRemote_JUnit() {
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
    public void test01CreateStorageBin() {                
        Boolean result = manufacturingWarehouseManagementBean.createStorageBin(warehouseID, "bin1", "Pallet", 200, 200, 200);
        assertNotNull(result);
    }

    @Test
    public void test02ViewAllStorageBin() {
        List<StorageBinEntity> result = manufacturingWarehouseManagementBean.viewAllStorageBin(warehouseID);           
        assertNotNull(result.isEmpty());
    }

    @Test
    public void test03ViewStorageBin() {
        StorageBinEntity result = manufacturingWarehouseManagementBean.viewStorageBin(storageBinID);
        assertNotNull(result);
    }

    @Test
    public void test04UpdateStorageBin() {
        Boolean result = manufacturingWarehouseManagementBean.updateStorageBin(storageBinID,"bin1", 300, 300, 300);
        assertNotNull(result);
    }

    @Test
    public void testGetInboundStorageBin() {
        //Force create an inbound bin in the warehouse first
        manufacturingWarehouseManagementBean.createStorageBin(warehouseID,"bin3","Inbound", 200, 200, 200);
        StorageBinEntity result = manufacturingWarehouseManagementBean.getInboundStorageBin(warehouseID);
        assertNotNull(result);
    }

    @Test
    public void testGetOutboundStorageBin() {
        //Force create an outbound bin in the warehouse first
        manufacturingWarehouseManagementBean.createStorageBin(warehouseID, "bin2", "Outbound", 200, 200, 200);
        StorageBinEntity result = manufacturingWarehouseManagementBean.getInboundStorageBin(warehouseID);
        assertNotNull(result);
    }

    @Test
    public void test06MarkTransferOrderAsCompleted() {
        assertFalse(manufacturingWarehouseManagementBean.markTransferOrderAsCompleted(transferOrderID, "GHJK"));
    }

    @Test
    public void testCancelTransferOrder() {
        assertFalse(manufacturingWarehouseManagementBean.cancelTransferOrder(this.transferOrderID));
    }

    @Test
    public void testViewTransferOrder() {
        assertNull(manufacturingWarehouseManagementBean.viewTransferOrder(this.transferOrderID));        
    }

    @Test
    public void test07ViewAllTransferOrderByWarehouseId() {
        assertNotNull(manufacturingWarehouseManagementBean.viewAllTransferOrderByWarehouseId(warehouseID));
    }

    @Test
    public void testDeleteTransferOrder() {
        assertFalse(manufacturingWarehouseManagementBean.deleteTransferOrder(this.transferOrderID));
    }

    @Test
    public void testMarkTransferOrderAsUnfulfilled() {
        assertFalse(manufacturingWarehouseManagementBean.markTransferOrderAsUnfulfilled(this.transferOrderID));
    }

    @Test
    public void testSearchItemBySKU() {
        assertNull(manufacturingWarehouseManagementBean.searchItemBySKU(SKU));
    }

    private ManufacturingWarehouseManagementBeanRemote lookupManufacturingWarehouseManagementBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManufacturingWarehouseManagementBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/ManufacturingWarehouseManagementBean!SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
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
