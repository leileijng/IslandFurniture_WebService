package SCM.InboundAndOutboundLogistics;

import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.ShippingOrderEntity;
import EntityManager.StorageBinEntity;
import EntityManager.WarehouseEntity;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import SCM.ManufacturingWarehouseManagement.ManufacturingWarehouseManagementBeanLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@Stateless
public class InboundAndOutboundLogisticsBean implements InboundAndOutboundLogisticsBeanLocal, InboundAndOutboundLogisticsBeanRemote {

    @EJB
    private ManufacturingWarehouseManagementBeanLocal manufacturingWarehouseManagementBean;

    @EJB
    private ManufacturingInventoryControlBeanLocal manufacturingInventoryControlBean;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public ShippingOrderEntity createShippingOrderBasicInfo(Date expectedReceivedDate, Long sourceWarehouseID, Long destinationWarehouseID) {

        try {
            WarehouseEntity sourceWarehouse = em.getReference(WarehouseEntity.class, sourceWarehouseID);
            WarehouseEntity destinationWarehouse = em.getReference(WarehouseEntity.class, destinationWarehouseID);
            ShippingOrderEntity shippingOrder = new ShippingOrderEntity(expectedReceivedDate, sourceWarehouse, destinationWarehouse);

            em.persist(shippingOrder);
            sourceWarehouse.getOutbound().add(shippingOrder);
            em.merge(sourceWarehouse);
            destinationWarehouse.getInbound().add(shippingOrder);
            em.merge(destinationWarehouse);
            System.out.println("ShippingOrder with id: " + shippingOrder.getId() + " is created successfully");
            return shippingOrder;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean addLineItemToShippingOrder(Long shippingOrderID, String SKU, Integer qty) {
        System.out.println("addLineItemToShippingOrder() called");
        try {
            Query query = em.createQuery("select s from ShippingOrderEntity s where s.id = ?1").setParameter(1, shippingOrderID);
            ShippingOrderEntity shippingOrder = (ShippingOrderEntity) query.getSingleResult();
            query = em.createQuery("select p from ItemEntity p where p.SKU = ?1").setParameter(1, SKU);
            ItemEntity itemEntity = (ItemEntity) query.getSingleResult();
            LineItemEntity lineItem = new LineItemEntity(itemEntity, qty, null);
            shippingOrder.getLineItems().add(lineItem);
            em.merge(shippingOrder);
            em.flush();
            System.out.println("Lineitem added.");
            return true;
        } catch (NoResultException ex) {
            System.out.println("Failed to addLineItemToShippingOrder(). Shipping order or SKU not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Fail to addLineItemToShippingOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addLineItemToShippingOrder(Long shippingOrderID, String SKU, Integer qty, String packType) {
        System.out.println("addLineItemToShippingOrder() called");
        try {
            Query query = em.createQuery("select s from ShippingOrderEntity s where s.id = ?1").setParameter(1, shippingOrderID);
            ShippingOrderEntity shippingOrder = (ShippingOrderEntity) query.getSingleResult();
            query = em.createQuery("select p from ItemEntity p where p.SKU = ?1").setParameter(1, SKU);
            ItemEntity itemEntity = (ItemEntity) query.getSingleResult();
            LineItemEntity lineItem = new LineItemEntity(itemEntity, qty, packType);
            shippingOrder.getLineItems().add(lineItem);
            em.merge(shippingOrder);
            em.flush();
            System.out.println("Lineitem added.");
            return true;
        } catch (NoResultException ex) {
            System.out.println("Failed to addLineItemToShippingOrder(). Shipping order or SKU not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Fail to addLineItemToShippingOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeLineItemFromShippingOrder(Long shippingOrderID, Long lineItemID) {
        System.out.println("removeLineItemFromShippingOrder() called");
        boolean lineItemRemoved = false;
        try {
            try {
                ShippingOrderEntity shippingOrderEntity = em.getReference(ShippingOrderEntity.class, shippingOrderID);
                List<LineItemEntity> lineItems = shippingOrderEntity.getLineItems();
                for (int i = 0; i < lineItems.size(); i++) {
                    if (lineItems.get(i).getId().equals(lineItemID)) {
                        shippingOrderEntity.getLineItems().remove(i);
                        lineItemRemoved = true;
                        break;
                    }
                }
            } catch (EntityNotFoundException ex) {
                System.out.println("Shipping order not found.");
                return false;
            }
            try {
                LineItemEntity lineItem = em.getReference(LineItemEntity.class, lineItemID);
                em.remove(lineItem);
                em.flush();
                return lineItemRemoved;
            } catch (EntityNotFoundException ex) {
                System.out.println("Line item not found.");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Failed to removeLineItemFromShippingOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateLineItemFromShippingOrder(Long shippingOrderID, Long lineItemID, String SKU, Integer qty) {
        System.out.println("updateLineItemFromShippingOrder() called");
        Boolean itemUpdated = false;
        try {
            ShippingOrderEntity shippingOrder = em.getReference(ShippingOrderEntity.class, shippingOrderID);
            List<LineItemEntity> lineItems = shippingOrder.getLineItems();
            for (int i = 0; i < lineItems.size(); i++) {
                if (lineItems.get(i).getId().equals(lineItemID)) {
                    Query query = em.createQuery("select p from ItemEntity p where p.SKU = ?1").setParameter(1, SKU);
                    ItemEntity itemEntity = (ItemEntity) query.getSingleResult();
                    LineItemEntity lineItem = new LineItemEntity(itemEntity, qty, null);
                    lineItems.set(i, lineItem);
                    itemUpdated = true;
                    break;
                }
            }
            return itemUpdated;
        } catch (NoResultException ex) {
            System.out.println("SKU not found.");
            return false;
        } catch (EntityNotFoundException ex) {
            System.out.println("Shipping order not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to updateLineItemFromShippingOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkSKUExists(String SKU) {
        try {
            Query q = em.createQuery("Select i from ItemEntity i where i.SKU=:SKU");
            q.setParameter("SKU", SKU);
            q.getSingleResult();
            return true;
        } catch (NoResultException n) {
            System.out.println("\nServer return no result:\n" + n);
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform checkSKUExists:\n" + ex);
            return false;
        }
    }

    @Override
    public ShippingOrderEntity getShippingOrderById(Long id) {
        try {
            return em.find(ShippingOrderEntity.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean updateShippingOrder(Long shippingOrderID, Long sourceWarehouseID, Long destinationWarehouseID, Date expectedReceivedDate) {
        System.out.println("updateShippingOrder() called");
        try {
            ShippingOrderEntity shippingOrderEntity = em.getReference(ShippingOrderEntity.class, shippingOrderID);
            WarehouseEntity sourceWarehouse = em.getReference(WarehouseEntity.class, sourceWarehouseID);
            WarehouseEntity destinationWarehouse = em.getReference(WarehouseEntity.class, destinationWarehouseID);
            shippingOrderEntity.setOrigin(sourceWarehouse);
            shippingOrderEntity.setDestination(destinationWarehouse);
            shippingOrderEntity.setExpectedReceivedDate(expectedReceivedDate);
            System.out.println("ShippingOrder updated successfully");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Shipping order or supplier or warehouse not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to updateShippingOrder.");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Boolean updateShippingOrderStatus(Long shippingOrderID, String status, String submittedBy) {
        System.out.println("updateShippingOrderStatus() called.");
        try {
            ShippingOrderEntity shippingOrder = em.find(ShippingOrderEntity.class, shippingOrderID);
            if (status.equals("Submitted")) {
                shippingOrder.setSubmittedBy(submittedBy);
                em.merge(shippingOrder);
                em.flush();
            }
            if (!shippingOrder.getStatus().equals("Completed")) {
                if (status.equals("Shipped")) {
                    List<LineItemEntity> listOfLineItems = shippingOrder.getLineItems();
                    //ensure that storage bin can meet shipping order quantity
                    for (LineItemEntity lineItemInShippingOrder : listOfLineItems) {
                        em.merge(lineItemInShippingOrder);
                        int quantityToMove = lineItemInShippingOrder.getQuantity();

                        WarehouseEntity warehouse = shippingOrder.getOrigin();
                        StorageBinEntity outbound = manufacturingWarehouseManagementBean.getOutboundStorageBin(warehouse.getId());
                        em.merge(outbound);
                        em.refresh(outbound);
                        List<LineItemEntity> listOfLineItemsInOutboundBin = outbound.getLineItems();
                        System.out.println("Size of listOfLineItemsInOutboundBin: " + listOfLineItemsInOutboundBin.size());
                        for (LineItemEntity lineItemInOutboundBin : listOfLineItemsInOutboundBin) {
                            if (lineItemInOutboundBin.getItem().getSKU().equals(lineItemInShippingOrder.getItem().getSKU())) {
                                int quantityInOutboundBin = lineItemInOutboundBin.getQuantity();
                                System.out.println("updateShippingOrderStatus(): quantityToMove: " + quantityToMove);
                                System.out.println("updateShippingOrderStatus(): quantityInOutboundBin: " + quantityInOutboundBin);
                                if (quantityToMove > quantityInOutboundBin) {
                                    System.out.println("Outbound bin does not have enough quantity to move.");
                                    throw new Exception();
                                }
                            }
                        }//end of "ensure that storage bin can meet shipping order quantity"
                    }
                    
                    //Perform the actual removal
                    Boolean isRemovedSuccessfully = manufacturingInventoryControlBean.removeItemsFromOutboundBinForShipping(shippingOrderID);
                    em.merge(shippingOrder);
                    em.flush();
                    em.refresh(shippingOrder);
                    if (!isRemovedSuccessfully) {
                        shippingOrder.setStatus("Submitted");
                        em.flush();
                        throw new Exception();
                    }

                    shippingOrder.setShippedDate(new Date());

                } else if (status.equals("Completed")) {
                    boolean isMovedToReceivingBin = manufacturingInventoryControlBean.moveInboundShippingOrderItemsToReceivingBin(shippingOrderID);
                    em.merge(shippingOrder);
                    em.refresh(shippingOrder);
                    System.out.println("Moving items to receiving bin...");
                    if (!isMovedToReceivingBin) {
                        shippingOrder.setStatus("Shipped");
                        return false;
                    }

                    shippingOrder.setReceivedDate(new Date());
                }
                shippingOrder.setStatus(status);
            }
            em.merge(shippingOrder);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ShippingOrderEntity> getShippingOrderList(WarehouseEntity origin, Date shippedDate) {
        try {
            Query query = em.createQuery("select s from ShippingOrderEntity s where s.origin = ?1 and s.shippedDate = ?2 ORDER BY s.createdDate desc")
                    .setParameter(1, origin).setParameter(2, shippedDate, TemporalType.DATE);

            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<ShippingOrderEntity>();
        }
    }

    @Override
    public List<ShippingOrderEntity> getShippingOrderList(Date expectedReceivedDate, WarehouseEntity destination) {
        try {
            Query query = em.createQuery("select s from ShippingOrderEntity s where s.destination = ?1 and s.expectedReceivedDate = ?2 ORDER BY s.expectedReceivedDate desc")
                    .setParameter(1, destination).setParameter(2, expectedReceivedDate, TemporalType.DATE);

            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<ShippingOrderEntity>();
        }
    }

    @Override
    public List<ShippingOrderEntity> getShippingOrderList(ItemEntity item, Date shippedDate, Date expectedReceivedDate, WarehouseEntity origin, WarehouseEntity destination) {
        try {
            // to do
            Query query = em.createQuery("from s in ShippingOrderEntity s where s.shippedDate.getDay() = :shippedDate").setParameter("shippedDate", shippedDate.getDay());

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<ShippingOrderEntity>();
        }
    }

    @Override
    public List<ShippingOrderEntity> getShippingOrderList() {
        System.out.println("getShippingOrderList() called");
        try {
            Query q = em.createQuery("select p from ShippingOrderEntity p ORDER BY p.createdDate DESC");
            System.out.println("List returned.");
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("Failed to return list.");
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Remove
    public void remove() {
        System.out.println("Inbound and outbound logistics bean has bean removed.");
    }

    @Override
    public List<ShippingOrderEntity> getShippingOrderListByWarehouseId(Long warehouseId) {
        System.out.println("getShippingOrderListByWarehouseId() is called.");
        try {
            Query q;
            if (warehouseId == null) {
                q = em.createQuery("Select s from ShippingOrderEntity s");
            } else {
                q = em.createQuery("Select s from ShippingOrderEntity s where s.origin.id=:warehouseId or s.destination.id=:wId");
                q.setParameter("warehouseId", warehouseId);
                q.setParameter("wId", warehouseId);
            }
            List<ShippingOrderEntity> listOfShippingOrders = q.getResultList();
            System.out.println("getShippingOrderListByWarehouseId() is successful.");
            return listOfShippingOrders;
        } catch (Exception ex) {
            System.out.println("Unable to getShippingOrderListByWarehouseId().");
            return null;
        }
    }

}
