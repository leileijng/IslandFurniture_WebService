package JUnit_Testing;

import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.StorageBinEntity;
import HelperClasses.ItemStorageBinHelper;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanRemote;
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

public class SCM_ManufacturingInventoryControlBeanRemote_JUnit {

    ManufacturingInventoryControlBeanRemote manufacturingInventoryControlBean = lookupManufacturingInventoryControlBeanRemote();

    public SCM_ManufacturingInventoryControlBeanRemote_JUnit() {
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
    public void testGetEmptyStorageBins() {
        ItemEntity itemEntity = new FurnitureEntity();
        Long warehouseId = 1200L;
        List<StorageBinEntity> emptyStorageBins = manufacturingInventoryControlBean.getEmptyStorageBins(warehouseId, itemEntity);
        assertNull(emptyStorageBins);
    }

    @Test
    public void testMoveInboundPurchaseOrderItemsToReceivingBin() {
        Long purchaseOrderId = 5000L;
        Boolean result = manufacturingInventoryControlBean.moveInboundPurchaseOrderItemsToReceivingBin(purchaseOrderId);
        assertFalse(result);
    }

    @Test
    public void testMoveInboundShippingOrderItemsToReceivingBin() {
        Long shippingOrderID = 1L;
        Boolean result = manufacturingInventoryControlBean.moveInboundShippingOrderItemsToReceivingBin(shippingOrderID);
        assertFalse(result);
    }

    @Test
    public void testRemoveItemFromOutboundBinForShipping() {
        Long shippingOrderID = 1234L;
        Boolean result = manufacturingInventoryControlBean.removeItemsFromOutboundBinForShipping(shippingOrderID);
        assertFalse(result);
    }

    @Test
    public void testAddItemToReceivingBin() {
        Long shippingOrderID = 500L;
        String SKU = "F1";
        Boolean result = manufacturingInventoryControlBean.addItemToReceivingBin(shippingOrderID, SKU);
        assertFalse(result);
    }

    @Test
    public void testMoveSingleItemBetweenStorageBins() {
        StorageBinEntity source = new StorageBinEntity();
        StorageBinEntity destination = new StorageBinEntity();
        String SKU = "F1";
        Boolean result = manufacturingInventoryControlBean.moveSingleItemBetweenStorageBins(SKU, source, destination);
        assertFalse(result);
    }

    @Test
    public void testCheckItemQty() {
        Long warehouseId = 200L;
        String SKU = "F1";
        Integer result = manufacturingInventoryControlBean.checkItemQty(warehouseId, SKU);
        assertNotNull(result);
    }

    @Test
    public void testFindStorageBinsThatContainsItem() {
        Long warehouseId = 1200L;
        String SKU = "F1";
        List<StorageBinEntity> storageBins = manufacturingInventoryControlBean.findStorageBinsThatContainsItem(warehouseId, SKU);
        
        assertNull(storageBins);
    }

    @Test
    public void testGetTotalVolumeOfInboundStorageBin() {
        Long warehouseId = 1200L;
        Integer volume = manufacturingInventoryControlBean.getTotalVolumeOfInboundStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetTotalVolumeOfOutboundStorageBin() {
        Long warehouseId = 1200L;
        Integer volume = manufacturingInventoryControlBean.getTotalVolumeOfOutboundStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetTotalVolumeOfShelfStorageBin() {
        Long warehouseId = 1200L;
        Integer volume = manufacturingInventoryControlBean.getTotalVolumeOfShelfStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetTotalVolumeOfPalletStorageBin() {
        Long warehouseId = 1200L;
        Integer volume = manufacturingInventoryControlBean.getTotalVolumeOfPalletStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetTotalFreeVolumeOfInboundStorageBin() {
        Long warehouseId = 1200L;
        Integer volume = manufacturingInventoryControlBean.getTotalFreeVolumeOfInboundStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetTotalFreeVolumeOfOutboundStorageBin() {
        Long warehouseId = 600L;
        Integer volume = manufacturingInventoryControlBean.getTotalFreeVolumeOfOutboundStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetTotalFreeVolumeOfPalletStorageBin() {
        Long warehouseId = 600L;
        Integer volume = manufacturingInventoryControlBean.getTotalFreeVolumeOfPalletStorageBin(warehouseId);
        assertNotNull(volume);
    }

    @Test
    public void testGetItemList() {
        Long warehouseId = 600L;
        List<ItemStorageBinHelper> getItemList = manufacturingInventoryControlBean.getItemList(warehouseId);
        assertNull(getItemList);
    }

    @Test
    public void testGetOutboundBinItemList() {
        Long warehouseId = 600L;
        List<ItemStorageBinHelper> getItemList = manufacturingInventoryControlBean.getItemList(warehouseId);
        assertNull(getItemList);
    }

    @Test
    public void testGetBinItemList() {
        Long storageBinId = 200L;
        List<ItemStorageBinHelper> getItemList = manufacturingInventoryControlBean.getBinItemList(storageBinId);
        assertNotNull(getItemList);
    }

    @Test
    public void testEmptyStorageBin() {
        Long lineItemID = 200L;
        Long storageBinID = 1200L;
        Boolean result = manufacturingInventoryControlBean.emptyStorageBin(lineItemID, storageBinID);
        assertFalse(result);
    }

    @Test
    public void testCheckIfStorageBinIsOfAppropriateItemType() {
        Long storageBinId = 200L;
        String SKU = "F1";
        Boolean result = manufacturingInventoryControlBean.checkIfStorageBinIsOfAppropriateItemType(storageBinId, SKU);
        assertFalse(result);
    }

    private ManufacturingInventoryControlBeanRemote lookupManufacturingInventoryControlBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ManufacturingInventoryControlBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/ManufacturingInventoryControlBean!SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
