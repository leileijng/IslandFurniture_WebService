package SCM.RetailProductsAndRawMaterialsPurchasing;

import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.PurchaseOrderEntity;
import EntityManager.SupplierEntity;
import EntityManager.WarehouseEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RetailProductsAndRawMaterialsPurchasingBean implements RetailProductsAndRawMaterialsPurchasingBeanLocal, RetailProductsAndRawMaterialsPurchasingBeanRemote {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    public RetailProductsAndRawMaterialsPurchasingBean() {
    }

    @Override
    public PurchaseOrderEntity createPurchaseOrder(Long supplierID, Long receivingWarehouseID, Date expectedReceivedDate) {
        System.out.println("createPurchaseOrder() called");
        try {
            SupplierEntity supplierEntity = em.getReference(SupplierEntity.class, supplierID);
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, receivingWarehouseID);
            PurchaseOrderEntity purchaseOrder = new PurchaseOrderEntity(supplierEntity, warehouseEntity, expectedReceivedDate);
            em.persist(purchaseOrder);
            System.out.println("PurchaseOrder with id: " + purchaseOrder.getId() + " is created successfully");
            return purchaseOrder;
        } catch (EntityExistsException ex) {
            ex.printStackTrace();
            System.out.println("Failed to create purchase order.");
            return null;
        }
    }

    @Override
    public Boolean updatePurchaseOrder(Long purchaseOrderID, Long supplierID, Long receivingWarehouseID, Date expectedReceivedDate) {
        System.out.println("updatePurchaseOrder() called");
        try {
            PurchaseOrderEntity purchaseOrderEntity = em.getReference(PurchaseOrderEntity.class, purchaseOrderID);
            SupplierEntity supplierEntity = em.getReference(SupplierEntity.class, supplierID);
            WarehouseEntity warehouseEntity = em.getReference(WarehouseEntity.class, receivingWarehouseID);
            purchaseOrderEntity.setSupplier(supplierEntity);
            purchaseOrderEntity.setDestination(warehouseEntity);
            purchaseOrderEntity.setExpectedReceivedDate(expectedReceivedDate);
            System.out.println("PurchaseOrder updated successfully");
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Purchase order or supplier or warehouse not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to updatePurchaseOrder.");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addLineItemToPurchaseOrder(Long purchaseOrderID, String SKU, Integer qty) {
        System.out.println("addLineItemToPurchaseOrder() called");
        try {
            Query query = em.createQuery("select p from PurchaseOrderEntity p where p.id = ?1").setParameter(1, purchaseOrderID);
            PurchaseOrderEntity purchaseOrder = (PurchaseOrderEntity) query.getSingleResult();
            query = em.createQuery("select p from ItemEntity p where p.SKU = ?1").setParameter(1, SKU);
            ItemEntity itemEntity = (ItemEntity) query.getSingleResult();
            LineItemEntity lineItem = new LineItemEntity(itemEntity, qty, null);
            purchaseOrder.getLineItems().add(lineItem);
            em.merge(purchaseOrder);
            em.flush();
            System.out.println("Lineitem added.");
            return true;
        } catch (NoResultException ex) {
            System.out.println("Failed to addLineItemToPurchaseOrder(). Purchase order or SKU not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to addLineItemToPurchaseOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeLineItemFromPurchaseOrder(Long lineItemID, Long purchaseOrderID) {
        System.out.println("removeLineItemToPurchaseOrder() called");
        boolean lineItemRemoved = false;
        try {
            try {
                PurchaseOrderEntity purchaseOrder = em.getReference(PurchaseOrderEntity.class, purchaseOrderID);
                List<LineItemEntity> lineItems = purchaseOrder.getLineItems();
                for (int i = 0; i < lineItems.size(); i++) {
                    if (lineItems.get(i).getId().equals(lineItemID)) {
                        purchaseOrder.getLineItems().remove(i);
                        lineItemRemoved = true;
                        break;
                    }
                }
            } catch (EntityNotFoundException ex) {
                System.out.println("Purchase order not found.");
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
            System.out.println("Failed to addLineItemToPurchaseOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateLineItemFromPurchaseOrder(Long purchaseOrderID, Long lineItemID, String SKU, Integer qty) {
        System.out.println("updateLineItemFromPurchaseOrder() called");
        Boolean itemUpdated = false;
        try {
            PurchaseOrderEntity purchaseOrder = em.getReference(PurchaseOrderEntity.class, purchaseOrderID);
            List<LineItemEntity> lineItems = purchaseOrder.getLineItems();
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
            System.out.println("Purchase order not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to updateLineItemFromPurchaseOrder()");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public PurchaseOrderEntity getPurchaseOrderById(Long id) {
        try {
            return em.find(PurchaseOrderEntity.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean updatePurchaseOrderStatus(Long id, String status, String submittedBy) {
        try {
            PurchaseOrderEntity purchaseOrder = em.find(PurchaseOrderEntity.class, id);
            if(status.equals("Submitted")){
                purchaseOrder.setSubmittedBy(submittedBy);
                em.merge(purchaseOrder);
                em.flush();
            }
            if (!purchaseOrder.getStatus().equals("Completed")) {
                purchaseOrder.setStatus(status);
                em.merge(purchaseOrder);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PurchaseOrderEntity> getPurchaseOrderListByStatus(String status) {
        try {
            Query q = em.createQuery("select p from PurchaseOrderEntity p where p.status = ?1").setParameter(1, status);
            return q.getResultList();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<PurchaseOrderEntity> getPurchaseOrderList() {
        System.out.println("getPurchaseOrderList() called");
        try {
            Query q = em.createQuery("select p from PurchaseOrderEntity p ORDER BY p.createdDate desc");
            System.out.println("List returned.");
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("Failed to return list.");
            ex.printStackTrace();
            return new ArrayList<>();
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
    public List<PurchaseOrderEntity> getPurchaseOrderListByWarehouseId(Long warehouseId) {
        System.out.println("getPurchaseOrderListByWarehouseId() is called.");
        try {
            Query q;
            if (warehouseId == null) {
                q = em.createQuery("Select p from PurchaseOrderEntity p ORDER BY p.createdDate desc");
            } else {
                q = em.createQuery("Select p from PurchaseOrderEntity p where p.receivedWarehouse.id=:warehouseId ORDER BY p.createdDate DESC");
                q.setParameter("warehouseId", warehouseId);
            }
            List<PurchaseOrderEntity> listOfPurchaseOrders = q.getResultList();
            System.out.println("getPurchaseOrderListByWarehouseId() is successful.");
            return listOfPurchaseOrders;
        } catch (Exception ex) {
            System.out.println("Unable to getPurchaseOrderListByWarehouseId().");
            return null;
        }
    }

}
