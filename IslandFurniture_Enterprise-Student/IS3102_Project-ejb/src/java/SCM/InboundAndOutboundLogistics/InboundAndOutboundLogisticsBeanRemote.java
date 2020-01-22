
package SCM.InboundAndOutboundLogistics;

import EntityManager.ItemEntity;
import EntityManager.ShippingOrderEntity;
import EntityManager.WarehouseEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remove;

@Remote
public interface InboundAndOutboundLogisticsBeanRemote {

    // shipping tye can be by ship, by truck, by train etc
    public ShippingOrderEntity createShippingOrderBasicInfo(Date expectedReceivedDate, Long sourceWarehouseID, Long destinationWarehouseID);

    public boolean checkSKUExists(String SKU);

    public Boolean updateShippingOrder(Long shippingOrderID, Long sourceWarehouseID, Long destinationWarehouseID, Date expectedReceivedDate);

    public Boolean updateShippingOrderStatus(Long shippingOrderID, String status, String submittedBy);

    public Boolean addLineItemToShippingOrder(Long shippingOrderID, String SKU, Integer qty);

    public Boolean addLineItemToShippingOrder(Long shippingOrderID, String SKU, Integer qty, String packType);

    public Boolean removeLineItemFromShippingOrder(Long shippingOrderID, Long lineItemID);

    public Boolean updateLineItemFromShippingOrder(Long shippingOrderID, Long lineItemID, String SKU, Integer qty);

    public List<ShippingOrderEntity> getShippingOrderList(WarehouseEntity origin, Date shippedDate);

    public List<ShippingOrderEntity> getShippingOrderList(Date expectedReceivedDate, WarehouseEntity destination);

    public List<ShippingOrderEntity> getShippingOrderList();


    // return empty list when is no result or error

    public List<ShippingOrderEntity> getShippingOrderList(ItemEntity item, Date shippedDate, Date expectedReceivedDate, WarehouseEntity origin, WarehouseEntity destination);

    public ShippingOrderEntity getShippingOrderById(Long id);

        public List<ShippingOrderEntity> getShippingOrderListByWarehouseId(Long warehouseId);
    @Remove
    public void remove();
}
