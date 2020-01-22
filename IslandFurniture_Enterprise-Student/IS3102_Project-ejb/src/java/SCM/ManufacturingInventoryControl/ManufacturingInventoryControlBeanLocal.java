package SCM.ManufacturingInventoryControl;

import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.StorageBinEntity;
import HelperClasses.ItemStorageBinHelper;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ManufacturingInventoryControlBeanLocal {

    //Finds the list of suitable storage bin that can hold the item based on the kind of item passed in
    public List<StorageBinEntity> getEmptyStorageBins(Long warehouseID, ItemEntity itemEntity); //returns the appropirate type of empty storage bins  
    public Boolean moveInboundPurchaseOrderItemsToReceivingBin(Long purchaseOrderID);
    public Boolean moveInboundShippingOrderItemsToReceivingBin(Long shippingOrderID);
    public Boolean removeItemsFromOutboundBinForShipping(Long shippingOrderID);
    public boolean addItemToReceivingBin(Long warehouseID, String SKU);
    //if you want to move multiple items, please call this method many times
    public boolean moveSingleItemBetweenStorageBins(String SKU, StorageBinEntity source, StorageBinEntity destination);
    
    public Integer checkItemQty(Long warehouseId, String SKU);
    public List<StorageBinEntity> findStorageBinsThatContainsItem(Long warehouseId, String SKU);
    
    public Integer getTotalVolumeOfInboundStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfOutboundStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfShelfStorageBin(Long warehouseID);
    public Integer getTotalVolumeOfPalletStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfInboundStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfOutboundStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfShelfStorageBin(Long warehouseID);
    public Integer getTotalFreeVolumeOfPalletStorageBin(Long warehouseID);
    
    public List<ItemStorageBinHelper> getItemList(Long warehouseID);
    public List<ItemStorageBinHelper> getOutboundBinItemList(Long warehouseID);
    public List<ItemStorageBinHelper> getBinItemList(Long storageBinID);
    public Boolean emptyStorageBin(Long storageBin_ItemID, Long storageBinID);
    public LineItemEntity checkIfItemExistInsideStorageBin(Long storageBinID, String SKU);
    
    public Boolean checkIfStorageBinIsOfAppropriateItemType(Long storageBinId, String SKU);
    
    public Boolean addItemIntoBin(Long storageBinID, String SKU, Integer quantity);
}
