package SCM.ManufacturingWarehouseManagement;

import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.StorageBinEntity;
import EntityManager.TransferOrderEntity;
import EntityManager.WarehouseEntity;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
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
public class ManufacturingWarehouseManagementBean implements ManufacturingWarehouseManagementBeanLocal, ManufacturingWarehouseManagementBeanRemote {

    @EJB
    private ManufacturingInventoryControlBeanLocal micb;
    @Resource
    private EJBContext context;
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;
    StorageBinEntity storageBin;
    WarehouseEntity warehouse;
    TransferOrderEntity transferOrder;
    List<LineItemEntity> lineItems;

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
            return em.find(TransferOrderEntity.class, id);
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
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean markTransferOrderAsCompleted(Long transferOrderId, String submittedBy) {
        System.out.println("markTransferOrderAsCompleted() called");
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

                boolean isPass = micb.moveSingleItemBetweenStorageBins(SKU, originBin, targetBin);
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
            System.out.println("\nServer failed to markTransferOrderAsCompleted:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cancelTransferOrder(Long transferOrderId) {
        try {
            transferOrder = em.getReference(TransferOrderEntity.class, transferOrderId);
            transferOrder.setStatus( "Cancelled");

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
            transferOrder.setStatus("Unfulfillable");
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
        } catch (Exception ex) {
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
    
    @Override
    public List<TransferOrderEntity> viewLatestCompletedTransferOrders(Long warehouseId) {
        System.out.println("viewLatestCompletedTransferOrders() called. with warehouseId:"+warehouseId);
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
}
