package InventoryManagement.StoreAndKitchenInventoryManagement;

import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.StorageBinEntity;
import EntityManager.TransferOrderEntity;
import HelperClasses.ItemStorageBinHelper;
import HelperClasses.ReturnHelper;
import java.util.List;
import javax.ejb.Local;

@Local
public interface StoreAndKitchenInventoryManagementBeanLocal {
    //Warehouse Management
    public boolean createStorageBin(Long warehouseID, String name, String type, Integer _length, Integer width, Integer height); //types are inbound, outbound, shelf, pallet
    public boolean updateStorageBin(Long storageBinId, String name, Integer length, Integer width, Integer height);
    public boolean deleteStorageBin(Long id);
    public StorageBinEntity viewStorageBin(Long storageBinID);
    public List<StorageBinEntity> viewAllStorageBin(Long warehouseID);
    public StorageBinEntity getInboundStorageBin(Long warehouseID); //look for the inbound storagebin
    public StorageBinEntity getOutboundStorageBin(Long warehouseID); //look for the inbound storagebin
    public List<StorageBinEntity> getRetailOutletBins(Long warehouseID);
    public List<StorageBinEntity> getFurnitureMarketplaceBins(Long warehouseID);
    public StorageBinEntity getKitchenBin(Long warehouseID);
    public boolean createTransferOrder(Long warehouseID, Long originStorageBinId, Long targetStorageBinID, LineItemEntity lineItem);
    public Boolean addLineItemToTransferOrder(Long transferOrderID, String SKU, Integer quantity);
    public Boolean removeLineItemFromTransferOrder(Long transferOrderID);
    public boolean markTransferOrderAsCompleted(Long transferOrderId, String submittedBy);
    public boolean cancelTransferOrder(Long transferOrderId);
    public TransferOrderEntity viewTransferOrder(Long transferOrderId);
    public List<TransferOrderEntity> viewAllTransferOrderByWarehouseId(Long warehouseId);
    public boolean deleteTransferOrder(Long id);
    public boolean markTransferOrderAsUnfulfilled(Long transferOrderId);
    public ItemEntity searchItemBySKU(String SKU);
    
    //Inventory Control
    public List<StorageBinEntity> getEmptyStorageBins(Long warehouseID, ItemEntity itemEntity); //returns the appropirate type of empty storage bins  
    public Boolean moveInboundPurchaseOrderItemsToReceivingBin(Long purchaseOrderID);
    public Boolean moveInboundShippingOrderItemsToReceivingBin(Long shippingOrderID);
    public Boolean removeItemFromOutboundBinForShipping(Long shippingOrderID);
    public boolean addItemToReceivingBin(Long warehouseID, String SKU);
    //if you want to move multiple items, please call this method many times
    public boolean moveSingleItemBetweenStorageBins(String SKU, StorageBinEntity source, StorageBinEntity destination);
    public Integer checkItemQty(Long warehouseId, String SKU);
    public List<StorageBinEntity> findStorageBinsThatContainsItem(Long warehouseId, String SKU);
    public List<StorageBinEntity> findRetailStorageBinsThatContainsItem(Long warehouseId, String SKU);
    public Integer getTotalVolumeOfInboundStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfOutboundStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfShelfStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfPalletStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfRetailOutlet(Long warehouseID);
    public Integer getTotalVolumeOfFurnitureMarketplace(Long warehouseID);
    public Integer getTotalVolumeOfKitchen(Long warehouseID);
    public Integer getTotalFreeVolumeOfInboundStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfOutboundStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfShelfStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfPalletStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfRetailOutlet(Long warehouseID);
    public Integer getTotalFreeVolumeOfFurnitureMarketplace(Long warehouseID);
    public Integer getTotalFreeVolumeOfKitchen(Long warehouseID);
    
    public List<ItemStorageBinHelper> getItemList(Long warehouseID);
    public List<ItemStorageBinHelper> getOutboundBinItemList(Long warehouseID);
    public List<ItemStorageBinHelper> getBinItemList(Long storageBinID);
    public Boolean emptyStorageBin(Long storageBin_ItemID, Long storageBinID);
    public LineItemEntity checkIfItemExistInsideStorageBin(Long storageBinID, String SKU);
    
    public Boolean checkIfStorageBinIsOfAppropriateItemType(Long storageBinId, String SKU);
    
    public Boolean removeItemsFromInventory(Long storeID, String SKU, Integer qty, Boolean picker);
    public Boolean removeItemFromFurnitureMarketplace(Long storeID, String SKU, Integer qty);
    public Boolean removeItemFromRetailOutlet(Long storeID, String SKU, Integer qty);
    public Boolean removeItemFromKitchen(Long storeID, String SKU, Integer qty);
    public Boolean removeItemFromPallets(Long storeID, String SKU, Integer qty);
    public Boolean removeItemFromShelfs(Long storeID, String SKU, Integer qty);
    public List<StorageBinEntity> findShelfsThatContainsItem(Long warehouseId, String SKU);
    public List<StorageBinEntity> findPalletsThatContainsItem(Long warehouseId, String SKU);
    public Boolean removeItemFromBin(Long storageBinID, String SKU);
    
    public List<TransferOrderEntity> viewLatestCompletedTransferOrders(Long warehouseId);
}
