package SCM.RetailProductsAndRawMaterialsPurchasing;

import EntityManager.PurchaseOrderEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface RetailProductsAndRawMaterialsPurchasingBeanRemote { 
   public PurchaseOrderEntity createPurchaseOrder(Long supplierID, Long recivingWarehouseID, Date expectedReceivedDate);
   public Boolean updatePurchaseOrder(Long purchaseOrderID, Long supplierID, Long receivingWarehouseID, Date expectedReceivedDate);
   public Boolean updatePurchaseOrderStatus(Long id, String status, String submittedBy);
   public Boolean addLineItemToPurchaseOrder(Long id, String SKU, Integer qty);
   public Boolean removeLineItemFromPurchaseOrder(Long lineItemID, Long purchaseOrderID);
   public Boolean updateLineItemFromPurchaseOrder(Long purchaseOrderID, Long lineItemID, String SKU, Integer qty);
   public PurchaseOrderEntity getPurchaseOrderById(Long id);
   public List<PurchaseOrderEntity> getPurchaseOrderListByStatus(String status);
   public List<PurchaseOrderEntity> getPurchaseOrderList();
   public boolean checkSKUExists(String SKU);
       public List<PurchaseOrderEntity> getPurchaseOrderListByWarehouseId(Long warehouseId);

}