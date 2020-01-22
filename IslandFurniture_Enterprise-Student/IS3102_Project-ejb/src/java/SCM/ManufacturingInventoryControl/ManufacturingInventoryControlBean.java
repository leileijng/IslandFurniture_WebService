package SCM.ManufacturingInventoryControl;

import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.PurchaseOrderEntity;
import EntityManager.RawMaterialEntity;
import EntityManager.RetailProductEntity;
import EntityManager.ShippingOrderEntity;
import EntityManager.StorageBinEntity;
import EntityManager.WarehouseEntity;
import HelperClasses.ItemStorageBinHelper;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ManufacturingInventoryControlBean implements ManufacturingInventoryControlBeanLocal, ManufacturingInventoryControlBeanRemote {

    @EJB
    private ManufacturingWarehouseManagementBeanLocal manufacturingWarehouseManagementBean;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public List<StorageBinEntity> getEmptyStorageBins(Long warehouseID, ItemEntity itemEntity) {
        System.out.println("getEmptyStorageBins() called with ItemEntity:" + itemEntity);

        WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);

        List<StorageBinEntity> listOfAppropriateEmptyStorageBins = new ArrayList<>();
        String storageBinType = "";
        if (itemEntity instanceof FurnitureEntity) {
            storageBinType = "Pallet";
        }
        if (itemEntity instanceof RawMaterialEntity) {
            storageBinType = "Pallet";
        }
        if (itemEntity instanceof RetailProductEntity) {
            storageBinType = "Shelf";
        }
        try {
            List<StorageBinEntity> storageBinEntities = warehouseEntity.getStorageBins();
            for (StorageBinEntity binEntity : storageBinEntities) {
                if (binEntity.getFreeVolume().equals(binEntity.getVolume())) {
                    listOfAppropriateEmptyStorageBins.add(binEntity);
                }
            }
            System.out.println("Returned listOfAppropriateEmptyStorageBins.");
            return listOfAppropriateEmptyStorageBins;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to return listOfAppropriateEmptyStorageBins. Warehouse or bin not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed return listOfAppropriateEmptyStorageBins:\n" + ex);
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean removeItemsFromOutboundBinForShipping(Long shippingOrderID) {
        System.out.println("removeItemFromOutboundBinForShipping called()");
        try {
            ShippingOrderEntity shippingOrderEntity = em.getReference(ShippingOrderEntity.class, shippingOrderID);
            WarehouseEntity warehouse = shippingOrderEntity.getOrigin();
            StorageBinEntity outbound = manufacturingWarehouseManagementBean.getOutboundStorageBin(warehouse.getId());
            if (outbound == null) {
                System.out.println("removeItemFromOutboundBinForShipping(): Outbound bin does not exist.");
                return false;
            }
            em.merge(outbound);
            List<LineItemEntity> itemsInShippingOrder = shippingOrderEntity.getLineItems();
            //For each item in shipping order
            for (LineItemEntity lineItemEntity : itemsInShippingOrder) {
                int lineItemToRemoveQuantity = lineItemEntity.getQuantity();
                //Remove the amount of quantity requied for each line item in the shipping order
                for (int i = 0; i < lineItemToRemoveQuantity; i++) {
                    //Check if it's in outbound bin
                    em.merge(lineItemEntity);
                    LineItemEntity lineItemInOutboundBin = checkIfItemExistInsideStorageBin(outbound.getId(), lineItemEntity.getItem().getSKU());

                    //Line item does not exist 
                    if (lineItemInOutboundBin == null) {
                        System.out.println("Outbound bin does not have sufficient quantity to ship the order.");
                        throw new Exception();
                    } else {            //line item exist
                        em.merge(lineItemInOutboundBin);
                        //if it is the last item
                        if (lineItemInOutboundBin.getQuantity() == 1) {
                            outbound.getLineItems().remove(lineItemInOutboundBin);
                            em.remove(lineItemInOutboundBin);
                            em.flush();
                        } else {
                            if (lineItemInOutboundBin.getQuantity() == 0) {
                                System.out.println("Outbound bin has insufficient quantity to be removed. Please try again.");
                                throw new Exception();
                            }
                            lineItemInOutboundBin.setQuantity(lineItemInOutboundBin.getQuantity() - 1);
                            em.flush();
                        }
                        em.merge(outbound);
                        outbound.setFreeVolume(outbound.getFreeVolume() + lineItemInOutboundBin.getItem().getVolume());
                        System.out.println("Setting outbound volume...");
                        System.out.println("Outbound volume = outbound.getFreeVolume() + lineItemInOutboundBin.getItem().getVolume()" + outbound.getFreeVolume() + "+" + lineItemInOutboundBin.getItem().getVolume());
                        em.flush();
                    }
                }
            }
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Could not find either shipping order or outbound bin!");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to removeOutboundBinToShipAShippingOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean moveInboundShippingOrderItemsToReceivingBin(Long shippingOrderID
    ) {
        System.out.println("moveInboundShippingOrderItemsToReceivingBin called()");
        try {
            ShippingOrderEntity shippingOrderEntity = em.getReference(ShippingOrderEntity.class, shippingOrderID);
            Long warehouseID = shippingOrderEntity.getDestination().getId();
            List<LineItemEntity> lineItemsInPurchaseOrder = shippingOrderEntity.getLineItems();
            for (LineItemEntity lineItemEntity : lineItemsInPurchaseOrder) {
                em.merge(lineItemEntity);
                ItemEntity itemEntity = lineItemEntity.getItem();
                int quantity = lineItemEntity.getQuantity();
                for (int i = 0; i < quantity; i++) {
                    if (!addItemToReceivingBin(warehouseID, itemEntity.getSKU())) {
                        return false;
                    }
                }
            }
            em.merge(shippingOrderEntity);
            shippingOrderEntity.setStatus("Completed");
            em.flush();
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Could not find either shipping order or the warehouse.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to moveInboundShippingOrderItemsToReceivingBin()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public LineItemEntity checkIfItemExistInsideStorageBin(Long storageBinID, String SKU) {
        System.out.println("checkIfItemExistInsideStorageBin() called with:" + storageBinID + "," + SKU);
        StorageBinEntity storageBin = em.getReference(StorageBinEntity.class, storageBinID);
        List<LineItemEntity> listOfLineItems = storageBin.getLineItems();
        for (LineItemEntity lineItem : listOfLineItems) {
            if (lineItem.getItem().getSKU().equals(SKU)) {
                System.out.println("checkIfItemExistInsideStorageBin(): SKU found, returned line item.");
                return lineItem;
            }
        }
        System.out.println("checkIfItemExistInsideStorageBin(): SKU not found.");
        return null;
    }

    @Override
    public Boolean moveInboundPurchaseOrderItemsToReceivingBin(Long purchaseOrderID
    ) {
        System.out.println("moveInboundPurchaseOrderItemsToReceivingBin called()");
        try {
            PurchaseOrderEntity purchaseOrderEntity = em.getReference(PurchaseOrderEntity.class, purchaseOrderID);
            Long warehouseID = purchaseOrderEntity.getDestination().getId();
            List<LineItemEntity> lineItemsInPurchaseOrder = purchaseOrderEntity.getLineItems();
            for (LineItemEntity lineItemEntity : lineItemsInPurchaseOrder) {
                ItemEntity itemEntity = lineItemEntity.getItem();
                int quantity = lineItemEntity.getQuantity();
                for (int i = 0; i < quantity; i++) {
                    if (!addItemToReceivingBin(warehouseID, itemEntity.getSKU())) {
                        System.out.println("Failed to add into inbound bin: item SKU " + itemEntity.getSKU());
                        return false;
                    }
                }
            }
            purchaseOrderEntity.setStatus("Completed");
            em.merge(purchaseOrderEntity);
            System.out.println("All purchase order items moved into inbound bin successfully.");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Could not find either purchase order or the warehouse.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to moveInboundPurchaseOrderItemsToReceivingBin()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addItemToReceivingBin(Long warehouseID, String SKU
    ) {

        try {
            System.out.println("addItemToReceivingBin() called with SKU:" + SKU + " & wahouseID:" + warehouseID);
            StorageBinEntity inboundBin = manufacturingWarehouseManagementBean.getInboundStorageBin(warehouseID);
            em.merge(inboundBin);
            if (inboundBin == null) {
                System.out.println("Failed to add item to receiving bin, receiving bin not found.");
                return false;
            }
            Query q = em.createQuery("SELECT t FROM ItemEntity t WHERE t.SKU=:SKU");
            q.setParameter("SKU", SKU);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ItemEntity itemEntity = (ItemEntity) q.getSingleResult();

            //Check if storage bin have that type of item before
            LineItemEntity lineItem = checkIfItemExistInsideStorageBin(inboundBin.getId(), SKU);
            inboundBin.setFreeVolume(inboundBin.getFreeVolume() - itemEntity.getVolume());
            em.merge(inboundBin);
            if (lineItem != null) {
                em.merge(lineItem);
                lineItem.setQuantity(lineItem.getQuantity() + 1);
            } else {
                lineItem = new LineItemEntity(itemEntity, 1, "");
                em.persist(lineItem);
                inboundBin.getLineItems().add(lineItem);
                em.merge(inboundBin);
            }
            em.flush();
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to add item to receiving bin, item not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to move items between bins:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean moveSingleItemBetweenStorageBins(String SKU, StorageBinEntity source, StorageBinEntity destination
    ) {
        try {
            System.out.println("moveSingleItemBetweenStorageBins() called with SKU:" + SKU);
            em.merge(source);
            em.merge(destination);
            //remove one item from source
            if (checkIfStorageBinIsOfAppropriateItemType(destination.getId(), SKU)) {
                LineItemEntity lineItem = checkIfItemExistInsideStorageBin(source.getId(), SKU);
                em.merge(lineItem);
                //if it is the last item
                if (lineItem.getQuantity() == 1) {
                    source.getLineItems().remove(lineItem);
                    em.remove(lineItem);
                } else {
                    lineItem.setQuantity(lineItem.getQuantity() - 1);
                }
                em.flush();
                System.out.println("Setting free volume of source bin...");
                System.out.println("Free volume of source = source.getFreeVolume() + lineItem.getItem().getVolume(): " + source.getFreeVolume() + " + " + lineItem.getItem().getVolume());
                source.setFreeVolume(source.getFreeVolume() + lineItem.getItem().getVolume());
                em.flush();

                //add one item to destination
                lineItem = checkIfItemExistInsideStorageBin(destination.getId(), SKU);
                //if it does not exist
                if (lineItem == null) {
                    System.out.println("SKU item: " + SKU + " is not found in destination bin. Adding new line item.");
                    Query q = em.createQuery("SELECT t FROM ItemEntity t WHERE t.SKU=:SKU");
                    q.setParameter("SKU", SKU);
                    q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    ItemEntity itemEntity = (ItemEntity) q.getSingleResult();
                    LineItemEntity newLineItem = new LineItemEntity(itemEntity, 1, "");
                    em.persist(newLineItem);
                    em.flush();
                    em.refresh(newLineItem);
                    destination.getLineItems().add(newLineItem);
                    lineItem = newLineItem;
                    em.flush();
                } else {
                    em.merge(lineItem);
                    lineItem.setQuantity(lineItem.getQuantity() + 1);
                    em.flush();
                }
                System.out.println("Setting free volume of destination bin...");
                System.out.println("Free volume of destination = destination.getFreeVolume() - lineItem.getItem().getVolume(): " + destination.getFreeVolume() + " - " + lineItem.getItem().getVolume());

                destination.setFreeVolume(destination.getFreeVolume() - lineItem.getItem().getVolume());
                em.flush();
                if (destination.getFreeVolume() < 0) {
                    System.out.println("Destination bin ran out of storage space.");
                    throw new Exception();
                }
                return true;
            } else {
                System.out.println("Failed to move single item between storage bins!");
                return false;
            }
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to move the item between bins, item not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to move the item between bins:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer checkItemQty(Long warehouseId, String SKU) {
        System.out.println("checkItemQty() called with SKU:" + SKU);
        Integer qty = 0;
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseId);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            for (StorageBinEntity currentBin : storageBins) {
                for (int i = 0; i < currentBin.getLineItems().size(); i++) {
                    ItemEntity itemEntity = currentBin.getLineItems().get(i).getItem();
                    if (itemEntity.getSKU().equals(SKU)) {
                        //exisiting quantity + those in the bin
                        qty = qty + currentBin.getLineItems().get(i).getQuantity();
                    }
                }
            }
            System.out.println("Returned qty:" + qty);
            return qty;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed checkItemQty, warehouse or item not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to checkItemQty:\n" + ex);
            return 0;
        }
    }

    @Override
    public List<StorageBinEntity> findStorageBinsThatContainsItem(Long warehouseId, String SKU
    ) {
        System.out.println("findStorageBinsThatContainsItem() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id and sb.items.SKU=:SKU");
            q.setParameter("id", warehouseId);
            q.setParameter("SKU", SKU);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            return storageBins;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed findStorageBinThatContainsItem, warehouse or item not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to findStorageBinThatContainsItem:\n" + ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getTotalVolumeOfInboundStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalVolumeOfInboundStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Inbound")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfInboundStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfInboundStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfInboundStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalVolumeOfOutboundStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalVolumeOfOutboundStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Outbound")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfOutboundStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfOutboundStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfOutboundStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalVolumeOfShelfStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalVolumeOfShelfStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Shelf")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfShelfStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfShelfStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfShelfStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalVolumeOfPalletStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalVolumeOfPalletStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Pallet")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfPalletStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfPalletStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfPalletStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalFreeVolumeOfInboundStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalFreeVolumeOfInboundStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Inbound")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfInboundStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfInboundStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfInboundStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalFreeVolumeOfOutboundStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalFreeVolumeOfOutboundStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Outbound")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfOutboundStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfOutboundStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfOutboundStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalFreeVolumeOfShelfStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalFreeVolumeOfShelfStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Shelf")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfShelfStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfShelfStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfShelfStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalFreeVolumeOfPalletStorageBin(Long warehouseID
    ) {
        System.out.println("getTotalFreeVolumeOfPalletStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Pallet")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfPalletStorageBin(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfPalletStorageBin, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfPalletStorageBin:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    //Assumes one type of item is in each storage bin, so just return the first one
    private List<LineItemEntity> getItemInsideStorageBin(Long storageBinID) {
        System.out.println("getItemInsideStorageBin() called");

        try {
            em.flush();
            StorageBinEntity storageBinEntity = em.getReference(StorageBinEntity.class, storageBinID);
            List<LineItemEntity> listOfLineItems = storageBinEntity.getLineItems();
            for (int i = 0; i < listOfLineItems.size(); i++) {
                LineItemEntity l = listOfLineItems.get(i);
                em.refresh(l);
                listOfLineItems.set(i, l);
            }
            if (listOfLineItems == null || listOfLineItems.size() == 0) {
                System.out.println("No items");
                return null;
            } else {
                System.out.println("Returned list of items & their quantity");
                return listOfLineItems;
            }
        } catch (Exception ex) {
            System.out.println("Failed to getItemInsideStorageBin()");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ItemStorageBinHelper> getItemList(Long warehouseID) {
        System.out.println("getItemList() called");
        try {
            em.flush();
            List<ItemStorageBinHelper> itemStorageBinHelperList = new ArrayList<>();
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            List<StorageBinEntity> storageBins = warehouseEntity.getStorageBins();

            ItemStorageBinHelper itemStorageBinHelper = new ItemStorageBinHelper();
            System.out.println("Number of storage bins in warehouse id " + warehouseID + ": " + storageBins.size());
            //For each bin in the warehouse
            for (StorageBinEntity storageBin : storageBins) {
                //Get all their contents
                List<LineItemEntity> listOfLineItemEntities = getItemInsideStorageBin(storageBin.getId());
                System.out.println("Retrieving line items of storage bin id " + storageBin.getId() + "...");
                //If the bin is not empty
                if (listOfLineItemEntities != null && listOfLineItemEntities.size() > 0) {
                    //Add all the entries inside the bin to helper list
                    System.out.println("getItemList(): Size of listOfLineItemEntities inside the storage bin id " + storageBin.getId() + ": " + listOfLineItemEntities.size());
                    for (int i = 0; i < listOfLineItemEntities.size(); i++) {
                        itemStorageBinHelper = new ItemStorageBinHelper();
                        itemStorageBinHelper.setLineItemID(listOfLineItemEntities.get(i).getId());
                        itemStorageBinHelper.setSKU(listOfLineItemEntities.get(i).getItem().getSKU());
                        itemStorageBinHelper.setItemName(listOfLineItemEntities.get(i).getItem().getName());
                        itemStorageBinHelper.setStorageBinID(storageBin.getId());
                        itemStorageBinHelper.setStorageBinName(storageBin.getName());
                        itemStorageBinHelper.setStorageBinType(storageBin.getType());
                        itemStorageBinHelper.setItemQty(listOfLineItemEntities.get(i).getQuantity());
                        itemStorageBinHelper.setItemType(listOfLineItemEntities.get(i).getItem().getType());
                        itemStorageBinHelperList.add(itemStorageBinHelper);
                    }
                }
            }

            return itemStorageBinHelperList;
        } catch (EntityNotFoundException ex) {
            System.out.println("Warehouse could not be found.");
            return null;
        } catch (Exception ex) {
            System.out.println("System failed to getItemList()");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ItemStorageBinHelper> getOutboundBinItemList(Long warehouseID) {
        System.out.println("getOutboundBinItemList() called");
        try {
            em.flush();
            List<ItemStorageBinHelper> itemStorageBinHelperList = new ArrayList<>();
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            StorageBinEntity storageBinEntity = manufacturingWarehouseManagementBean.getOutboundStorageBin(warehouseID);
            List<StorageBinEntity> storageBins = new ArrayList();
            storageBins.add(storageBinEntity);

            ItemStorageBinHelper itemStorageBinHelper = new ItemStorageBinHelper();
            //For each bin in the warehouse
            for (StorageBinEntity storageBin : storageBins) {
                //Get all their contents
                List<LineItemEntity> listOfLineItemEntities = getItemInsideStorageBin(storageBin.getId());
                System.out.println("Retrieving line items of storage bin id " + storageBin.getId() + "...");
                //If the bin is not empty
                if (listOfLineItemEntities != null && listOfLineItemEntities.size() > 0) {
                    //Add all the entries inside the bin to helper list
                    System.out.println("getItemList(): Size of listOfLineItemEntities inside the storage bin id " + storageBin.getId() + ": " + listOfLineItemEntities.size());
                    for (int i = 0; i < listOfLineItemEntities.size(); i++) {
                        itemStorageBinHelper = new ItemStorageBinHelper();
                        itemStorageBinHelper.setLineItemID(listOfLineItemEntities.get(i).getId());
                        itemStorageBinHelper.setSKU(listOfLineItemEntities.get(i).getItem().getSKU());
                        itemStorageBinHelper.setItemName(listOfLineItemEntities.get(i).getItem().getName());
                        itemStorageBinHelper.setStorageBinID(storageBin.getId());
                        itemStorageBinHelper.setStorageBinType(storageBin.getType());
                        itemStorageBinHelper.setItemQty(listOfLineItemEntities.get(i).getQuantity());
                        itemStorageBinHelper.setItemType(listOfLineItemEntities.get(i).getItem().getType());
                        itemStorageBinHelperList.add(itemStorageBinHelper);
                    }
                }
            }

            return itemStorageBinHelperList;
        } catch (EntityNotFoundException ex) {
            System.out.println("Warehouse could not be found.");
            return null;
        } catch (Exception ex) {
            System.out.println("System failed to getOutboundBinItemList()");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ItemStorageBinHelper> getBinItemList(Long storageBinID) {
        System.out.println("getBinItemList() called");
        try {
            em.flush();
            List<ItemStorageBinHelper> itemStorageBinHelperList = new ArrayList<>();
            StorageBinEntity storageBinEntity = em.getReference(StorageBinEntity.class, storageBinID);
            List<StorageBinEntity> storageBins = new ArrayList();
            storageBins.add(storageBinEntity);

            ItemStorageBinHelper itemStorageBinHelper = new ItemStorageBinHelper();
            //For each bin in the warehouse
            for (StorageBinEntity storageBin : storageBins) {
                //Get all their contents
                List<LineItemEntity> listOfLineItemEntities = getItemInsideStorageBin(storageBin.getId());
                System.out.println("Retrieving line items of storage bin id " + storageBin.getId() + "...");
                //If the bin is not empty
                if (listOfLineItemEntities != null && listOfLineItemEntities.size() > 0) {
                    //Add all the entries inside the bin to helper list
                    System.out.println("getItemList(): Size of listOfLineItemEntities inside the storage bin id " + storageBin.getId() + ": " + listOfLineItemEntities.size());
                    for (int i = 0; i < listOfLineItemEntities.size(); i++) {
                        itemStorageBinHelper = new ItemStorageBinHelper();
                        itemStorageBinHelper.setLineItemID(listOfLineItemEntities.get(i).getId());
                        itemStorageBinHelper.setSKU(listOfLineItemEntities.get(i).getItem().getSKU());
                        itemStorageBinHelper.setItemName(listOfLineItemEntities.get(i).getItem().getName());
                        itemStorageBinHelper.setStorageBinID(storageBin.getId());
                        itemStorageBinHelper.setStorageBinType(storageBin.getType());
                        itemStorageBinHelper.setItemQty(listOfLineItemEntities.get(i).getQuantity());
                        itemStorageBinHelper.setItemType(listOfLineItemEntities.get(i).getItem().getType());
                        itemStorageBinHelperList.add(itemStorageBinHelper);
                    }
                }
            }

            return itemStorageBinHelperList;
        } catch (EntityNotFoundException ex) {
            System.out.println("Storage bin could not be found.");
            return null;
        } catch (Exception ex) {
            System.out.println("System failed to getBinItemList()");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean emptyStorageBin(Long lineItemID, Long storageBinID) {
        System.out.println("emptyStorageBin_ItemEntity() called");

        try {
            LineItemEntity lineItemEntity = em.getReference(LineItemEntity.class, lineItemID);

            StorageBinEntity storageBinEntity = em.getReference(StorageBinEntity.class, storageBinID);
            em.refresh(storageBinEntity);
            ItemEntity itemsDeleted = lineItemEntity.getItem();
            int totalVolumeDeleted = 0;
            totalVolumeDeleted += itemsDeleted.getVolume() * lineItemEntity.getQuantity();
            storageBinEntity.setFreeVolume(storageBinEntity.getFreeVolume() + totalVolumeDeleted);
            em.remove(lineItemEntity);
            storageBinEntity.getLineItems().remove(lineItemEntity);
            em.merge(storageBinEntity);
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Storage Bin could not be found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to emptyStorageBin_ItemEntity()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean checkIfStorageBinIsOfAppropriateItemType(Long storageBinId, String SKU) {
        System.out.println("checkIfStorageBinIsOfAppropriateItemType() called.");
        try {
            Query q = em.createQuery("SELECT t FROM ItemEntity t WHERE t.SKU=:SKU");
            q.setParameter("SKU", SKU);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ItemEntity itemEntity = (ItemEntity) q.getSingleResult();
            StorageBinEntity storageBin = em.getReference(StorageBinEntity.class, storageBinId);
            if (storageBin.getType().equals("Shelf")) {
                if (itemEntity.getType().equals("Furniture")) {
                    System.out.println("Incorrect bin.");
                    return false;
                }
            } else if (storageBin.getType().equals("Pallet")) {
                if (!itemEntity.getType().equals("Furniture")) {
                    System.out.println("Incorrect bin.");
                    return false;
                }
            }
            System.out.println("Correct bin.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform checkIfStorageBinIsOfAppropriateItemType().");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addItemIntoBin(Long storageBinID, String SKU, Integer quantity) {
        System.out.println("addItemIntoBin called");
        try {
            StorageBinEntity storageBinEntity = em.find(StorageBinEntity.class, storageBinID);
            Query q = em.createQuery("Select i from ItemEntity i where i.isDeleted=false and i.SKU=:SKU");
            q.setParameter("SKU", SKU);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ItemEntity item = (ItemEntity) q.getSingleResult();
            Integer totalVolume = item.getVolume() * quantity;
            if (storageBinEntity.getFreeVolume() >= totalVolume) {
                storageBinEntity.setFreeVolume(storageBinEntity.getFreeVolume() - totalVolume);
                for (LineItemEntity lineItem : storageBinEntity.getLineItems()) {
                    if (lineItem.getItem().getSKU().equals(item.getSKU())) {
                        lineItem.setQuantity(lineItem.getQuantity() + quantity);
                        return true;
                    }
                }
                LineItemEntity newLineItem = new LineItemEntity(item, quantity, "");
                em.persist(newLineItem);
                storageBinEntity.getLineItems().add(newLineItem);
                return true;
            } else {
                System.out.println("Not enough space to add item!");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Failed to add item to bin!");
            ex.printStackTrace();
            return false;
        }

    }
}
