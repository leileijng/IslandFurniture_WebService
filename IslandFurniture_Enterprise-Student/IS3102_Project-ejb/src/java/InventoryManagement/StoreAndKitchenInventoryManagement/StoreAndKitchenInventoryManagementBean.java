package InventoryManagement.StoreAndKitchenInventoryManagement;

import Config.Config;
import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.PurchaseOrderEntity;
import EntityManager.RawMaterialEntity;
import EntityManager.RetailProductEntity;
import EntityManager.ShippingOrderEntity;
import EntityManager.StorageBinEntity;
import EntityManager.StoreEntity;
import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
import HelperClasses.ItemStorageBinHelper;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class StoreAndKitchenInventoryManagementBean implements StoreAndKitchenInventoryManagementBeanLocal {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;
    StorageBinEntity storageBin;
    WarehouseEntity warehouse;
    TransferOrderEntity transferOrder;
    List<LineItemEntity> lineItems;

    //Warehouse Management==================================================
    @Override
    public boolean createStorageBin(Long warehouseID, String name, String type, Integer _length, Integer width, Integer height) {
        try {
            switch (type.toLowerCase()) {
                case "inbound":
                    StorageBinEntity inbound = getInboundStorageBin(warehouseID);
                    if (inbound != null) {
                        return false;
                    }
                    break;
                case "outbound":
                    StorageBinEntity outbound = getOutboundStorageBin(warehouseID);
                    if (outbound != null) {
                        return false;
                    }
                    break;
                case "kitchen":
                    StorageBinEntity kitchenBin = getKitchenBin(warehouseID);
                    if (kitchenBin != null) {
                        return false;
                    }
                    break;
            }
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            storageBin = new StorageBinEntity(warehouseEntity, name, type, _length, width, height);
            em.persist(storageBin);
            warehouseEntity.getStorageBins().add(storageBin);
            em.merge(warehouseEntity);
            em.flush();
            System.out.println("Created storage bin successfully.");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to createStorageBin, cannot find warehouse.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to createStorageBin:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean updateStorageBin(Long storageBinId, String name, Integer length, Integer width, Integer height) {
        System.out.println("updateStorageBin() called.");
        System.out.println("length: " + length);
        System.out.println("width: " + width);
        System.out.println("height: " + height);

        try {
            storageBin = em.getReference(StorageBinEntity.class, storageBinId);
            System.out.println("Size of storage bin to be updated is " + storageBin.getLineItems().size());
            if (storageBin == null || !storageBin.getLineItems().isEmpty()) {
                System.out.println("Cannot find storage bin or storage bin contains items");
                return false;
            }
            storageBin.setName(name);
            storageBin.setHeight(height);
            storageBin.setLength(length);
            storageBin.setWidth(width);
            storageBin.setFreeVolume(storageBin.getVolume());
            em.merge(storageBin);
            System.out.println("updateStorageBin() updated successfully.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to updateStorageBin:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean deleteStorageBin(Long id) {
        System.out.println("deleteStorageBin() called.");
        try {
            storageBin = em.find(StorageBinEntity.class, id);
            if (storageBin == null || !storageBin.getLineItems().isEmpty()) {
                System.out.println("Unable to delete. Storage bin either not found or not empty.");
                return false;
            } else {
                Query q = em.createQuery("Select t from TransferOrderEntity t where t.origin.id=:oid or t.target.id=:tid");
                q.setParameter("oid", id);
                q.setParameter("tid", id);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                List<TransferOrderEntity> listOfTransferOrders = q.getResultList();
                for (TransferOrderEntity t : listOfTransferOrders) {
                    em.remove(t);
                }
                em.merge(storageBin);
                em.remove(storageBin);
                System.out.println("deleteStorageBin() bin removed");
                return true;
            }
        } catch (Exception ex) {
            System.out.println("\nServer failed to deleteStorageBin:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public StorageBinEntity viewStorageBin(Long id) {
        try {
            if (em.getReference(StorageBinEntity.class, id) == null) {

                return null;
            }

            return em.getReference(StorageBinEntity.class, id);
        } catch (Exception ex) {
            System.out.println("\nServer failed to viewStorageBin:\n" + ex);
            return null;
        }
    }

    @Override
    public List<StorageBinEntity> viewAllStorageBin(Long warehouseID) {
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            return storageBins;
        } catch (Exception ex) {
            System.out.println("\nServer failed to viewAllStorageBin:\n" + ex);
            return null;
        }
    }

    @Override
    public TransferOrderEntity viewTransferOrder(Long id) {
        try {
            if (em.getReference(TransferOrderEntity.class, id) == null) {

                return null;
            }

            return em.getReference(TransferOrderEntity.class, id);
        } catch (Exception ex) {
            System.out.println("\nServer failed to viewTransferOrder:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean deleteTransferOrder(Long id) {
        try {
            if (em.getReference(TransferOrderEntity.class, id) == null) {
                return false;
            }
            TransferOrderEntity transferOrderEntity = em.getReference(TransferOrderEntity.class, id);
            transferOrderEntity.setIsDeleted(true);
            em.merge(transferOrderEntity);
            em.flush();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to deleteTransferOrder:\n" + ex);
            return false;
        }
    }

    @Override
    public StorageBinEntity getInboundStorageBin(Long warehouseID) {
        System.out.println("getInboundStorageBin() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.type='Inbound' AND sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            storageBin = (StorageBinEntity) q.getSingleResult();
            return storageBin;
        } catch (NoResultException ex) {
            System.out.println("No inbound storage found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getInboundStorageBin:\n" + ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public StorageBinEntity getOutboundStorageBin(Long warehouseID) {
        System.out.println("getOutboundStorageBin() called");
        try {
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            Long id = warehouseEntity.getId();
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.type='Outbound' AND sb.warehouse.id=:id");
            q.setParameter("id", id);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            storageBin = (StorageBinEntity) q.getSingleResult();
            return storageBin;
        } catch (NoResultException ex) {
            System.out.println("No outbound storage found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getOutboundStorageBin:\n" + ex);
            return null;
        }
    }

    @Override
    public List<StorageBinEntity> getRetailOutletBins(Long warehouseID) {
        System.out.println("getRetailOutletBin() called");
        try {
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            Long id = warehouseEntity.getId();
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.type='Retail Outlet' AND sb.warehouse.id=:id");
            q.setParameter("id", id);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("No retail outlet storage found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getRetailOutletBin:\n" + ex);
            return null;
        }
    }

    @Override
    public List<StorageBinEntity> getFurnitureMarketplaceBins(Long warehouseID) {
        System.out.println("getFurnitureMarketplaceBin() called");
        try {
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            Long id = warehouseEntity.getId();
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.type='Furniture Marketplace' AND sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            return q.getResultList();
        } catch (NoResultException ex) {
            System.out.println("No furnituire marketplace storage found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getFurnitureMarketplaceBin:\n" + ex);
            return null;
        }
    }

    @Override
    public StorageBinEntity getKitchenBin(Long warehouseID) {
        System.out.println("getFurnitureMarketplaceBin() called");
        try {
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            Long id = warehouseEntity.getId();
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.type='Kitchen' AND sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            return (StorageBinEntity) q.getSingleResult();
        } catch (NoResultException ex) {
            System.out.println("No furnituire marketplace storage found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getFurnitureMarketplaceBin:\n" + ex);
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean markTransferOrderAsCompleted(Long transferOrderId, String submittedBy) {
        System.out.println("markTransferOrderAsCompleted() for store called");
        try {
            transferOrder = em.getReference(TransferOrderEntity.class, transferOrderId);
            Integer quantityToMove = transferOrder.getLineItem().getQuantity();
            System.out.println("The number of quantity to move is " + quantityToMove);

            LineItemEntity lineItem = transferOrder.getLineItem();
            em.merge(lineItem);
            int itemVolume = lineItem.getItem().getVolume();
            int totalVolume = itemVolume * quantityToMove;
            StorageBinEntity targetBin = transferOrder.getTarget();
            int targetFreeVolume = targetBin.getFreeVolume();
            if (totalVolume > targetFreeVolume) {
                throw new Exception();
            }

            for (int i = 0; i < quantityToMove; i++) {
                String SKU = transferOrder.getLineItem().getItem().getSKU();
                StorageBinEntity originBin = transferOrder.getOrigin();
                targetBin = transferOrder.getTarget();
                System.out.println("Moving in progress....");
                System.out.println("SKU number is " + SKU);
                System.out.println("originBin: " + originBin.getId() + " moving to targetBin: " + targetBin.getId());

                boolean isPass = moveSingleItemBetweenStorageBins(SKU, originBin, targetBin);
                if (!isPass) {
                    System.out.println("markTransferOrderAsCompleted() incompleted resulted in roll back. Item was not found in source bin or volume of destination bin is insufficient.");
                    throw new Exception();
                }
            }
            transferOrder.setSubmittedBy(submittedBy);
            transferOrder.setStatus("Completed");
            transferOrder.setDateTransferred(new Date());
            em.merge(transferOrder);
            em.flush();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to markTransferOrderAsCompleted for store:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelTransferOrder(Long transferOrderId) {
        try {
            transferOrder = em.getReference(TransferOrderEntity.class, transferOrderId);
            transferOrder.setStatus("Cancelled");

            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to cancelTransferOrder:\n" + ex);
            return false;
        }
    }

    @Override
    public List<TransferOrderEntity> viewAllTransferOrderByWarehouseId(Long warehouseId) {
        System.out.println("viewAllTransferOrderByWarehouseId() called.");
        try {
            Query q = em.createQuery("Select t from TransferOrderEntity t where t.warehouse.id=:id and t.isDeleted=false");
            q.setParameter("id", warehouseId);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            System.out.println("viewAllTransferOrderByWarehouseId() is successful.");
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("\nServer failed to viewAllTransferOrderByWarehouseId():\n" + ex);
            return null;
        }
    }

    @Override
    public boolean markTransferOrderAsUnfulfilled(Long transferOrderId) {
        System.out.println("markTransferOrderAsUnfulfilled() called.");
        try {
            transferOrder = em.getReference(TransferOrderEntity.class, transferOrderId);
            transferOrder.setStatus(
                    "Unfulfillable");

            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to markTransferOrderAsUnfulfilled:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean createTransferOrder(Long warehouseID, Long originStorageBinID, Long targetStorageBinID, LineItemEntity lineItem) {
        System.out.println("createTransferOrder() called.");
        try {
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, warehouseID);
            StorageBinEntity originStorageBin = em.getReference(StorageBinEntity.class, originStorageBinID);

            StorageBinEntity target = em.getReference(StorageBinEntity.class, targetStorageBinID);
            transferOrder = new TransferOrderEntity(warehouseEntity, lineItem, originStorageBin, target);
            em.persist(transferOrder);
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to createTransferOrder:\n" + ex);
            return false;
        }
    }

    @Override
    public ItemEntity searchItemBySKU(String SKU) {
        try {
            Query q = em.createQuery("SELECT t FROM ItemEntity t where t.SKU=:SKU");
            q.setParameter("SKU", SKU);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            ItemEntity itemEntity = (ItemEntity) q.getSingleResult();
            return itemEntity;
        } catch (EntityNotFoundException ex) {
            return null;
        }
    }

    @Override
    public Boolean addLineItemToTransferOrder(Long transferOrderID, String SKU, Integer quantity) {
        System.out.println("addLineItemToTransferOrder() called.");
        try {
            ItemEntity itemEntity = searchItemBySKU(SKU);
            if (itemEntity == null) {
                return false;//cannot find item
            }
            LineItemEntity lineItem = new LineItemEntity(itemEntity, quantity, "");
            TransferOrderEntity transferOrderEntity = em.getReference(TransferOrderEntity.class, transferOrderID);
            transferOrderEntity.setLineItem(lineItem);
            em.merge(transferOrderEntity);
            System.out.println("Item added to transfer order.");
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to addLineItemToTransferOrder:\n" + ex);
            ex.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean removeLineItemFromTransferOrder(Long transferOrderID) {
        System.out.println("removeLineItemFromTransferOrder() called.");
        try {
            TransferOrderEntity transferOrderEntity = em.getReference(TransferOrderEntity.class, transferOrderID);
            Long lineItemID = transferOrderEntity.getLineItem().getId();
            transferOrderEntity.setLineItem(null);
            em.merge(transferOrderEntity);
            LineItemEntity lineItemEntity = em.getReference(LineItemEntity.class, lineItemID);
            em.remove(lineItemEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to removeLineItemFromTransferOrder:\n" + ex);
            return false;
        }
    }

    //Inventory Control==================================================
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
    public Boolean removeItemFromOutboundBinForShipping(Long shippingOrderID) {
        System.out.println("removeOutboundBinToShipAShippingOrder called()");
        try {
            ShippingOrderEntity shippingOrderEntity = em.getReference(ShippingOrderEntity.class, shippingOrderID);
            WarehouseEntity warehouse = shippingOrderEntity.getOrigin();
            StorageBinEntity outbound = getOutboundStorageBin(warehouse.getId());
            if (outbound == null) {
                System.out.println("removeItemFromOutboundBinForShipping(): Outbound bin does not exist.");
                return false;
            }
            em.merge(outbound);
            List<LineItemEntity> itemsInShippingOrder = shippingOrderEntity.getLineItems();
            //For each item in shipping order
            for (LineItemEntity lineItemEntity : itemsInShippingOrder) {
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
    public LineItemEntity checkIfItemExistInsideStorageBin(Long storageBinID, String SKU
    ) {
        System.out.println("checkIfItemExistInsideStorageBin() called");
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

        System.out.println("addItemToReceivingBin() called with SKU:" + SKU + " & wahouseID:" + warehouseID);
        StorageBinEntity inboundBin = getInboundStorageBin(warehouseID);
        em.merge(inboundBin);
        if (inboundBin == null) {
            System.out.println("Failed to add item to receiving bin, receiving bin not found.");
            return false;
        }
        try {
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
                    LineItemEntity l = currentBin.getLineItems().get(i);
                    em.refresh(l);
                    if (itemEntity.getSKU().equals(SKU)) {
                        //exisiting quantity + those in the bin
                        qty = qty + l.getQuantity();
                    }
                }
            }
            System.out.println("Returned qty:" + qty);
            return qty;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed checkItemQty, warehouse or item not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to checkItemQty:\n" + ex);
            return null;
        }
    }

    @Override
    public List<StorageBinEntity> findStorageBinsThatContainsItem(Long warehouseId, String SKU) {
        System.out.println("findStorageBinsThatContainsItem() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseId);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            q.setParameter("id", warehouseId);
            List<StorageBinEntity> resultStorageBins = q.getResultList();
            List<StorageBinEntity> storageBins = new ArrayList<>();
            for (int i = 0; i < resultStorageBins.size(); i++) {
                StorageBinEntity storageBinEntity = resultStorageBins.get(i);
                List<LineItemEntity> lineItems = storageBinEntity.getLineItems();
                for (int j = 0; j < lineItems.size(); j++) {
                    if (lineItems.get(j).getItem().getSKU().equals(SKU)) {
                        System.out.println("findRetailStorageBinsThatContainsItem(): Found");
                        storageBins.add(storageBinEntity);
                    }
                }
            }
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
    public List<StorageBinEntity> findRetailStorageBinsThatContainsItem(Long warehouseId, String SKU) {
        System.out.println("findRetailStorageBinsThatContainsItem() called");
        try {
            //Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id and sb.lineItems.item.SKU=:SKU and (sb.type='Shelf' OR sb.type='Pallet')");
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id and (sb.type='Shelf' OR sb.type='Pallet')");
            q.setParameter("id", warehouseId);
            List<StorageBinEntity> resultStorageBins = q.getResultList();
            List<StorageBinEntity> storageBins = new ArrayList<>();
            for (int i = 0; i < resultStorageBins.size(); i++) {
                StorageBinEntity storageBinEntity = resultStorageBins.get(i);
                List<LineItemEntity> lineItems = storageBinEntity.getLineItems();
                for (int j = 0; j < lineItems.size(); j++) {
                    if (lineItems.get(j).getItem().getSKU().equals(SKU)) {
                        System.out.println("findRetailStorageBinsThatContainsItem(): Found");
                        storageBins.add(storageBinEntity);
                    }
                }
            }
            return storageBins;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed findRetailStorageBinsThatContainsItem, warehouse or item not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to findRetailStorageBinsThatContainsItem:\n" + ex);
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
    public Integer getTotalVolumeOfRetailOutlet(Long warehouseID
    ) {
        System.out.println("getTotalVolumeOfRetailOutlet() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Retail Outlet")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfRetailOutlet(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfRetailOutlet, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfRetailOutlet:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalVolumeOfFurnitureMarketplace(Long warehouseID
    ) {
        System.out.println("getTotalVolumeOfFurnitureMarketplace() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Furniture Marketplace")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfFurnitureMarketplace(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfFurnitureMarketplace, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfFurnitureMarketplace:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalVolumeOfKitchen(Long warehouseID) {
        System.out.println("getTotalVolumeOfKitchen() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Kitchen")) {
                    volume += storageBinEntity.getVolume();
                }
            }
            System.out.println("getTotalVolumeOfKitchen(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalVolumeOfKitchen, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalVolumeOfKitchen:\n" + ex);
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

    @Override
    public Integer getTotalFreeVolumeOfRetailOutlet(Long warehouseID
    ) {
        System.out.println("getTotalFreeVolumeOfRetailOutlet() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Retail Outlet")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfRetailOutlet(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfRetailOutlet, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfRetailOutlet:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalFreeVolumeOfFurnitureMarketplace(Long warehouseID
    ) {
        System.out.println("getTotalFreeVolumeOfFurnitureMarketplace() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Furniture Marketplace")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfFurnitureMarketplace(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfFurnitureMarketplace, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfFurnitureMarketplace:\n" + ex);
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer getTotalFreeVolumeOfKitchen(Long warehouseID) {
        System.out.println("getTotalFreeVolumeOfKitchen() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id");
            q.setParameter("id", warehouseID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<StorageBinEntity> storageBins = q.getResultList();
            Integer volume = 0;
            for (StorageBinEntity storageBinEntity : storageBins) {
                if (storageBinEntity.getType().equals("Kitchen")) {
                    volume += storageBinEntity.getFreeVolume();
                }
            }
            System.out.println("getTotalFreeVolumeOfKitchen(): Returned volume.");
            return volume;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed getTotalFreeVolumeOfKitchen, warehouse not found.");
            return 0;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getTotalFreeVolumeOfKitchen:\n" + ex);
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
                        System.out.println("quantity inside storage bins: " + itemStorageBinHelper.getItemQty());
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
            StorageBinEntity storageBinEntity = getOutboundStorageBin(warehouseID);
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
            } else if (storageBin.getType().equals("Kitchen")) {
                if (!itemEntity.getType().equals("Raw Ingredient") && !itemEntity.getType().equals("Menu Item")) {
                    System.out.println("Incorrect bin.");
                    return false;
                }
            } else if (storageBin.getType().equals("Furniture Marketplace")) {
                if (!itemEntity.getType().equals("Furniture")) {
                    System.out.println("Incorrect bin.");
                    return false;
                }
            } else if (storageBin.getType().equals("Retail Outlet")) {
                if (!itemEntity.getType().equals("Retail Product")) {
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean removeItemFromFurnitureMarketplace(Long storeID, String SKU, Integer qty) {
        System.out.println("removeItemFromFurnitureMarketplace called()");
        int qtyRemainingToBeRemoved = qty;

        try {
            WarehouseEntity warehouse = em.getReference(StoreEntity.class, storeID).getWarehouse();
            List<StorageBinEntity> furnitureMarketplace = getFurnitureMarketplaceBins(warehouse.getId());
            if (furnitureMarketplace == null) {
                System.out.println("removeItemFromFurnitureMarketplace(): bin does not exist.");
            } else {
                //Loop thru all the bins and check
                for (int i = 0; i < furnitureMarketplace.size(); i++) {
                    //Check if item's in  bin
                    LineItemEntity storageBinLineItem = checkIfItemExistInsideStorageBin(furnitureMarketplace.get(i).getId(), SKU);
                    //Line item does not exist 
                    if (storageBinLineItem == null) {
                        // do nothing and go to next bin
                    } else {            //line item exist
                        for (; qtyRemainingToBeRemoved > 0; qtyRemainingToBeRemoved--) {
                            //if it is the last item
                            if (storageBinLineItem.getQuantity() == 1) {
                                furnitureMarketplace.get(i).getLineItems().remove(storageBinLineItem);
                                em.remove(storageBinLineItem);
                                em.flush();
                                break;
                            } else {
                                storageBinLineItem.setQuantity(storageBinLineItem.getQuantity() - 1);
                                em.flush();
                            }
                            em.merge(furnitureMarketplace.get(i));
                            furnitureMarketplace.get(i).setFreeVolume(furnitureMarketplace.get(i).getFreeVolume() + storageBinLineItem.getItem().getVolume());
                            em.flush();
                        }
                        if (qtyRemainingToBeRemoved == 0) {
                            break;
                        }
                    }
                }
            }
            if (qtyRemainingToBeRemoved > 0) { //If still got remaining item to remove even after looping thru everything, throw exception to rollback
                System.out.println("removeItemFromFurnitureMarketplace(): Store has insufficient quantity to be removed.");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromFurnitureMarketplace(); Furniture marketplace has insufficient quantity of " + SKU + " to be removed");
                out.close();
                throw new Exception();
            }

            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeItemFromFurnitureMarketplace(): Could not find either store or furniture bin!");
            return false;
        } catch (Exception ex) {
            System.out.println("removeItemFromFurnitureMarketplace(): Failed to removeItemFromFurnitureMarketplace()");
            ex.printStackTrace();
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromFurnitureMarketplace(); failed to remove " + SKU + " from furniture marketplace");
                out.close();
            } catch (Exception ex2) {
            }
            return false;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean removeItemFromRetailOutlet(Long storeID, String SKU, Integer qty) {
        System.out.println("removeItemFromRetailOutlet called()");
        int qtyRemainingToBeRemoved = qty;

        try {
            WarehouseEntity warehouse = em.getReference(StoreEntity.class, storeID).getWarehouse();
            List<StorageBinEntity> retailOutlet = getRetailOutletBins(warehouse.getId());
            if (retailOutlet == null) {
                System.out.println("removeItemFromRetailOutlet(): bin does not exist.");
            } else {
                //Loop thru all the bins and check
                for (int i = 0; i < retailOutlet.size(); i++) {
                    //Check if item's in  bin
                    LineItemEntity storageBinLineItem = checkIfItemExistInsideStorageBin(retailOutlet.get(i).getId(), SKU);
                    //Line item does not exist 
                    if (storageBinLineItem == null) {
                        // do nothing and go to next bin
                    } else {            //line item exist
                        for (; qtyRemainingToBeRemoved > 0; qtyRemainingToBeRemoved--) {
                            //if it is the last item
                            if (storageBinLineItem.getQuantity() == 1) {
                                retailOutlet.get(i).getLineItems().remove(storageBinLineItem);
                                em.remove(storageBinLineItem);
                                em.flush();
                                break;
                            } else {
                                storageBinLineItem.setQuantity(storageBinLineItem.getQuantity() - 1);
                                em.flush();
                            }
                            em.merge(retailOutlet.get(i));
                            retailOutlet.get(i).setFreeVolume(retailOutlet.get(i).getFreeVolume() + storageBinLineItem.getItem().getVolume());
                            em.flush();
                        }

                    }
                    if (qtyRemainingToBeRemoved == 0) {
                        break;
                    }
                }
            }
            if (qtyRemainingToBeRemoved > 0) { //If still got remaining item to remove even after looping thru everything, throw exception to rollback
                System.out.println("removeItemFromRetailOutlet(): Store has insufficient quantity to be removed.");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromRetailOutlet(); Retail outlet has insufficient quantity of " + SKU + " to be removed");
                out.close();
                throw new Exception();
            }

            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeItemFromRetailOutlet(): Could not find either store or furniture bin!");
            return false;
        } catch (Exception ex) {
            System.out.println("removeItemFromRetailOutlet(): Failed to removeItemFromRetailOutlet()");
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromRetailOutlet(); failed to remove " + SKU + " from retail outlet");
                out.close();
            } catch (Exception ex2) {
            }
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean removeItemFromKitchen(Long storeID, String SKU, Integer qty) {
        System.out.println("removeItemFromKitchen called()");

        try {
            WarehouseEntity warehouse = em.getReference(StoreEntity.class, storeID).getWarehouse();
            StorageBinEntity kitchen = getKitchenBin(warehouse.getId());
            if (kitchen == null) {
                System.out.println("removeItemFromKitchen(): bin does not exist.");
                return false;
            }
            //Check if item's in  bin
            LineItemEntity storageBinLineItem = checkIfItemExistInsideStorageBin(kitchen.getId(), SKU);

            //Line item does not exist (cause only 1 kitchen bin)
            if (storageBinLineItem == null) {
                System.out.println("removeItemFromKitchen(): Bin does not have sufficient quantity to fufill the order.");
                throw new Exception();
            } else {            //line item exist
                for (int curr = qty; curr > 0; curr--) {
                    //if it is the last item
                    if (storageBinLineItem.getQuantity() == 1) {
                        kitchen.getLineItems().remove(storageBinLineItem);
                        em.remove(storageBinLineItem);
                        em.flush();
                    } else {
                        if (storageBinLineItem.getQuantity() == 0) {
                            System.out.println("removeItemFromKitchen(): Outbound bin has insufficient quantity to be removed.");
                            throw new Exception();
                        }
                        storageBinLineItem.setQuantity(storageBinLineItem.getQuantity() - 1);
                        em.flush();
                    }
                    em.merge(kitchen);
                    kitchen.setFreeVolume(kitchen.getFreeVolume() + storageBinLineItem.getItem().getVolume());
                    em.flush();
                }

            }

            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeItemFromKitchen(): Could not find either store or kitchen bin!");
            return false;
        } catch (Exception ex) {
            System.out.println("removeItemFromKitchen(): Failed to removeItemFromKitchen()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean removeItemFromPallets(Long storeID, String SKU, Integer qty) {
        System.out.println("removeItemFromPallets called()");
        int qtyRemainingToBeRemoved = qty;

        try {
            WarehouseEntity warehouse = em.getReference(StoreEntity.class, storeID).getWarehouse();
            List<StorageBinEntity> pallets = findPalletsThatContainsItem(warehouse.getId(), SKU);
            if (pallets
                    == null) {
                System.out.println("removeItemFromPallets(): bin with item does not exist.");
                return false;
            } else {
                //Loop thru all the bins and check
                for (int i = 0; i < pallets.size(); i++) {
                    //Check if item's in  bin
                    LineItemEntity storageBinLineItem = checkIfItemExistInsideStorageBin(pallets.get(i).getId(), SKU);
                    //Line item does not exist 
                    if (storageBinLineItem == null) {
                        // do nothing and go to next bin
                    } else {            //line item exist
                        for (; qtyRemainingToBeRemoved > 0; qtyRemainingToBeRemoved--) {
                            //if it is the last item
                            if (storageBinLineItem.getQuantity() == 1) {
                                pallets.get(i).getLineItems().remove(storageBinLineItem);
                                em.remove(storageBinLineItem);
                                em.flush();
                                break;
                            } else {
                                storageBinLineItem.setQuantity(storageBinLineItem.getQuantity() - 1);
                                em.flush();
                            }
                            em.merge(pallets.get(i));
                            pallets.get(i).setFreeVolume(pallets.get(i).getFreeVolume() + storageBinLineItem.getItem().getVolume());
                            em.flush();
                        }
                    }
                    if (qtyRemainingToBeRemoved == 0) {
                        break;
                    }
                }
            }
            if (qtyRemainingToBeRemoved
                    > 0) { //If still got remaining item to remove even after looping thru everything, throw exception to rollback
                System.out.println("removeItemFromPallets(): Store has insufficient quantity to be removed.");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromPallets(); Store has insufficient quantity of " + SKU + " to be removed");
                out.close();
                throw new Exception();
            }

            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeItemFromPallets(): Could not find either store or furniture bin!");
            return false;
        } catch (Exception ex) {
            System.out.println("removeItemFromPallets(): Failed to removeItemFromRetailOutlet()");
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromPallets(); failed to remove " + SKU + " from pallets");
                out.close();
            } catch (Exception ex2) {
            }
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean removeItemFromShelfs(Long storeID, String SKU, Integer qty) {
        System.out.println("removeItemFromShelfs called()");
        int qtyRemainingToBeRemoved = qty;

        try {
            WarehouseEntity warehouse = em.getReference(StoreEntity.class, storeID).getWarehouse();
            List<StorageBinEntity> shelfs = findShelfsThatContainsItem(warehouse.getId(), SKU);
            if (shelfs
                    == null) {
                System.out.println("removeItemFromShelfs(): bin with item does not exist.");
                return false;
            } else {
                //Loop thru all the bins and check
                for (int i = 0; i < shelfs.size(); i++) {
                    //Check if item's in  bin
                    LineItemEntity storageBinLineItem = checkIfItemExistInsideStorageBin(shelfs.get(i).getId(), SKU);
                    //Line item does not exist 
                    if (storageBinLineItem == null) {
                        // do nothing and go to next bin
                    } else {            //line item exist
                        for (; qtyRemainingToBeRemoved > 0; qtyRemainingToBeRemoved--) {
                            //if it is the last item
                            if (storageBinLineItem.getQuantity() == 1) {
                                shelfs.get(i).getLineItems().remove(storageBinLineItem);
                                em.remove(storageBinLineItem);
                                em.flush();
                                break;
                            } else {
                                storageBinLineItem.setQuantity(storageBinLineItem.getQuantity() - 1);
                                em.flush();
                            }
                            em.merge(shelfs.get(i));
                            shelfs.get(i).setFreeVolume(shelfs.get(i).getFreeVolume() + storageBinLineItem.getItem().getVolume());
                            em.flush();
                        }
                    }
                    if (qtyRemainingToBeRemoved == 0) {
                        break;
                    }
                }
            }
            if (qtyRemainingToBeRemoved
                    > 0) { //If still got remaining item to remove even after looping thru everything, throw exception to rollback
                System.out.println("removeItemFromShelfs(): Store has insufficient quantity to be removed.");
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromShelfs(); Store has insufficient quantity of " + SKU + " to be removed");
                out.close();
                throw new Exception();
            }

            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("removeItemFromShelfs(): Could not find either store or furniture bin!");
            return false;
        } catch (Exception ex) {
            System.out.println("removeItemFromShelfs(): Failed to removeItemFromRetailOutlet()");
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + storeID + ";removeItemFromShelfs(); failed to remove " + SKU + " from pallets");
                out.close();
            } catch (Exception ex2) {
            }
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeItemsFromInventory(Long storeID, String SKU, Integer qty, Boolean picker) {
        System.out.println("removeItemsFromInventory() called");
        try {
            ItemEntity itemEntity = getItemBySKU(SKU);
            //If needs self collection, don't remove first
            if (!picker && itemEntity.getVolume() > Config.minVolumeForCollectionAreaItems) {
                return false;
            } else if (picker && itemEntity.getVolume() < Config.minVolumeForCollectionAreaItems) {
                return false;
            }
            Boolean removeStatus = false;
            if (picker) { //Picker
                switch (itemEntity.getType()) {
                    case "Furniture":
                        removeStatus = removeItemFromPallets(storeID, SKU, qty);
                        break;
                    case "Retail Product":
                        removeStatus = removeItemFromShelfs(storeID, SKU, qty);
                        break;
                    default:
                        // no need to remove for all other items
                        removeStatus = false;
                }
            } else { //No picker
                switch (itemEntity.getType()) {
                    case "Furniture":
                        removeStatus = removeItemFromFurnitureMarketplace(storeID, SKU, qty);
                        break;
                    case "Retail Product":
                        removeStatus = removeItemFromRetailOutlet(storeID, SKU, qty);
                        break;
                    default:
                        // no need to remove for all other items
                        removeStatus = false;
                }
            }
            return removeStatus;
        } catch (Exception ex) {
            System.out.println("removeItemsFromInventory(): Error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TransferOrderEntity> viewLatestCompletedTransferOrders(Long warehouseId) {
        System.out.println("viewLatestCompletedTransferOrders() called. with warehouseId:" + warehouseId);
        try {
            Query q = em.createQuery("Select t from TransferOrderEntity t where t.warehouse.id=:id and t.status='Completed' and t.isDeleted=false ORDER BY t.dateTransferred desc");
            q.setMaxResults(3);
            q.setParameter("id", warehouseId);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nServer failed to viewLatestCompletedTransferOrders():\n" + ex);
            return null;
        }
    }

    public ItemEntity getItemBySKU(String SKU) {
        System.out.println("getItemBySKU() called with SKU: " + SKU);
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU and i.isDeleted=false");
            q.setParameter("SKU", SKU);
            ItemEntity item = (ItemEntity) q.getSingleResult();
            System.out.println("getItemBySKU() is successful.");
            return item;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getItemBySKU():\n" + ex);
            return null;
        }
    }

    @Override
    public List<StorageBinEntity> findShelfsThatContainsItem(Long warehouseId, String SKU) {
        System.out.println("findShelfsThatContainsItem() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id and sb.type='Shelf'");
            q.setParameter("id", warehouseId);
            q.setParameter("id", warehouseId);
            List<StorageBinEntity> resultStorageBins = q.getResultList();
            List<StorageBinEntity> storageBins = new ArrayList<>();
            for (int i = 0; i < resultStorageBins.size(); i++) {
                StorageBinEntity storageBinEntity = resultStorageBins.get(i);
                List<LineItemEntity> lineItems = storageBinEntity.getLineItems();
                for (int j = 0; j < lineItems.size(); j++) {
                    if (lineItems.get(j).getItem().getSKU().equals(SKU)) {
                        System.out.println("findRetailStorageBinsThatContainsItem(): Found");
                        storageBins.add(storageBinEntity);
                    }
                }
            }
            return storageBins;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed findShelfsThatContainsItem, warehouse or item not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to findStorageBinThatContainsItem:\n" + ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<StorageBinEntity> findPalletsThatContainsItem(Long warehouseId, String SKU) {
        System.out.println("findPalletsThatContainsItem() called");
        try {
            Query q = em.createQuery("Select sb from StorageBinEntity sb where sb.warehouse.id=:id and sb.type='Pallet'");
            q.setParameter("id", warehouseId);
            q.setParameter("id", warehouseId);
            List<StorageBinEntity> resultStorageBins = q.getResultList();
            List<StorageBinEntity> storageBins = new ArrayList<>();
            for (int i = 0; i < resultStorageBins.size(); i++) {
                StorageBinEntity storageBinEntity = resultStorageBins.get(i);
                List<LineItemEntity> lineItems = storageBinEntity.getLineItems();
                for (int j = 0; j < lineItems.size(); j++) {
                    if (lineItems.get(j).getItem().getSKU().equals(SKU)) {
                        System.out.println("findRetailStorageBinsThatContainsItem(): Found");
                        storageBins.add(storageBinEntity);
                    }
                }
            }
            return storageBins;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed findPalletsThatContainsItem, warehouse or item not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to findPalletsThatContainsItem:\n" + ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean removeItemFromBin(Long storageBinID, String SKU) {
        System.out.println("removeItemFromBin() called");

        try {
            StorageBinEntity storageBinEntity = em.getReference(StorageBinEntity.class, storageBinID);
            LineItemEntity lineItem = checkIfItemExistInsideStorageBin(storageBinEntity.getId(), SKU);

            em.merge(lineItem);

            //if it is the last item
            if (lineItem.getQuantity()
                    == 1) {
                storageBinEntity.getLineItems().remove(lineItem);
                em.remove(lineItem);
            } else {
                lineItem.setQuantity(lineItem.getQuantity() - 1);
            }

            em.flush();

            storageBinEntity.setFreeVolume(storageBinEntity.getFreeVolume() + lineItem.getItem().getVolume());
            em.flush();

            return true;
        } catch (Exception ex) {
            System.out.println("removeItemFromBin(): Error");
            ex.printStackTrace();
            return false;
        }
    }
}
