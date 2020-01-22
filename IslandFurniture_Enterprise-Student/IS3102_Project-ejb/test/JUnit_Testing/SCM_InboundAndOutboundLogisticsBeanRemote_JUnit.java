package JUnit_Testing;

import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanRemote;
import EntityManager.ShippingOrderEntity;
import EntityManager.WarehouseEntity;
import java.util.Date;
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
public class SCM_InboundAndOutboundLogisticsBeanRemote_JUnit {

    InboundAndOutboundLogisticsBeanRemote inboundAndOutboundLogisticsBean = lookupInboundAndOutboundLogisticsBeanRemote();

    public SCM_InboundAndOutboundLogisticsBeanRemote_JUnit() {
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
    public void testCheckSKUExists() {
        System.out.println("testCheckSKUExists()");
        boolean result = inboundAndOutboundLogisticsBean.checkSKUExists("F_TD_01");
        assertTrue(result);
    }

    @Test
    public void testUpdateShippingOrder() {
        System.out.println("testUpdateShippingOrder");
        Long testdata_shippingOrderID = 352L;
        Long testdata_sourceWarehouseID = 24L;
        Long testdata_destinationWarehouseID = 54L;
        Date testdata_expectedReceivedDate = new Date();
        boolean result = inboundAndOutboundLogisticsBean.updateShippingOrder(testdata_shippingOrderID, testdata_sourceWarehouseID, testdata_destinationWarehouseID, testdata_expectedReceivedDate);
        assertFalse(result);
    }

    @Test
    public void testUpdateShippingOrderStatus() {
        System.out.println("testUpdateShippingOrderStatus");
        Long testdata_shippingOrderID = 352L;
        String testdata_status = "";
        String testdata_submittedBy = "";

        boolean result = inboundAndOutboundLogisticsBean.updateShippingOrderStatus(testdata_shippingOrderID, testdata_status, testdata_submittedBy);
        assertFalse(result);
    }

    @Test
    public void testAddLineItemToShippingOrder() {
        System.out.println("testAddLineItemToShippingOrder");
        Long testdata_shippingOrderID = 352L;
        String testdata_SKU = "";
        int testdata_qty = 1;

        boolean result = inboundAndOutboundLogisticsBean.addLineItemToShippingOrder(testdata_shippingOrderID, testdata_SKU, testdata_qty);
        assertNotNull(result);
    }

    @Test
    public void testRemoveLineItemFromShippingOrder() {
        System.out.println("testRemoveLineItemFromShippingOrder");
        Long testdata_shippingOrderID = 62L;
        Long testdata_lineItemID = 1000L;
        boolean result = inboundAndOutboundLogisticsBean.removeLineItemFromShippingOrder(testdata_shippingOrderID, testdata_lineItemID);
        assertFalse(result);
    }

    @Test
    public void testUpdateLineItemFromShippingOrder() {
        System.out.println("testUpdateLineItemFromShippingOrder");
        Long testdata_shippingOrderID = 432L;
        Long testdata_lineItemID = 1000L;
        String testdata_SKU = "";
        int testdata_qty = 1;
        boolean result = inboundAndOutboundLogisticsBean.updateLineItemFromShippingOrder(testdata_shippingOrderID, testdata_lineItemID, testdata_SKU, testdata_qty);
        assertFalse(result);
    }

    @Test
    public void test01GetShippingOrderList() {
        System.out.println("testGetShippingOrderList");
        WarehouseEntity testdata_orgin_warehouse = null; //???????
        Date testdata_shippedDate = new Date();
        List<ShippingOrderEntity> result = inboundAndOutboundLogisticsBean.getShippingOrderList(testdata_orgin_warehouse, testdata_shippedDate);
        assertNotNull(result);
    }

    @Test
    public void test02GetShippingOrderById() {
        System.out.println("testGetShippingOrderById");
        Long testdata_shippingOrderId = 563L;
        ShippingOrderEntity result = inboundAndOutboundLogisticsBean.getShippingOrderById(testdata_shippingOrderId);
        assertNull(result);
    }

    @Test
    public void testGetShippingOrderListByWarehouseId() {
        System.out.println("testGetShippingOrderListByWarehouseId");
        Long testdata_warehouseId = 64L;
        List<ShippingOrderEntity> result = inboundAndOutboundLogisticsBean.getShippingOrderListByWarehouseId(testdata_warehouseId);
        assertNotNull(result);
    }

    private InboundAndOutboundLogisticsBeanRemote lookupInboundAndOutboundLogisticsBeanRemote() {
        try {
            Context c = new InitialContext();
            return (InboundAndOutboundLogisticsBeanRemote) c.lookup("java:global/IS3102_Project/IS3102_Project-ejb/InboundAndOutboundLogisticsBean!SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
