/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SCM.RetailProductsAndRawMaterialsPurchasing;

import EntityManager.LineItemEntity;
import EntityManager.PurchaseOrderEntity;
import EntityManager.SupplierEntity;
import HelperClasses.PurchaseOrderHelper;
import HelperClasses.lineItemHelper;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Baoyu
 */
@WebService(serviceName = "RetailProductsAndRawMaterialsPurchasingWebService")
@Stateless()
public class RetailProductsAndRawMaterialsPurchasingWebService {
    @EJB
    private RetailProductsAndRawMaterialsPurchasingBeanLocal PurchasingBean;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @WebMethod(operationName = "getPurchaseOrder")
    public List<PurchaseOrderHelper> getPurchaseOrder(@WebParam(name = "email") String email, @WebParam(name = "password") String password) {
        System.out.println("getPurchaseOrder is called.");
        try {
            Query q = em.createQuery("select s from SupplierEntity s where s.email = ?1 and s.supplierName = ?2 and s.isDeleted=false")
                    .setParameter(1, email)
                    .setParameter(2, password);

            if (!q.getResultList().isEmpty()) {
                SupplierEntity supplier = (SupplierEntity) q.getResultList().get(0);

                Query q1 = em.createQuery("select po from PurchaseOrderEntity po where po.supplier.id = ?1 and po.status = ?2 order by po.expectedReceivedDate")
                        .setParameter(1, supplier.getId())
                        .setParameter(2, "Submitted");

                List<PurchaseOrderEntity> POs = (List<PurchaseOrderEntity>) q1.getResultList();
                List<PurchaseOrderHelper> PurchaseOrderHelperList = new ArrayList<>();
                for (PurchaseOrderEntity po : POs) {
                    PurchaseOrderHelper POHelper = new PurchaseOrderHelper();
                    POHelper.Id = po.getId();
                    POHelper.address = po.getDestination().getAddress();
                    POHelper.email = po.getDestination().getEmail();
                    POHelper.expectedReceivedDate = po.getExpectedReceivedDate();
                    POHelper.telephone = po.getDestination().getTelephone();
                    POHelper.warehouseName = po.getDestination().getWarehouseName();

                    for (LineItemEntity lineItem : po.getLineItems()) {
                        lineItemHelper helper = new lineItemHelper();
                        helper.SKU = lineItem.getItem().getSKU();
                        helper.name = lineItem.getItem().getName();
                        helper.quantity = lineItem.getQuantity();
                        POHelper.lineItemHelpers.add(helper);
                    }
                    PurchaseOrderHelperList.add(POHelper);
                }
                System.out.println("PurchaseOrderHelperList.size(): " + PurchaseOrderHelperList.size());
                return PurchaseOrderHelperList;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @WebMethod(operationName = "rejectPurchaseOrder")
    public Boolean rejectPurchaseOrder(@WebParam(name = "email") String email, @WebParam(name = "password") String password, @WebParam(name = "purchaseOrderId") Long purchaseOrderId) {
        System.out.println("rejectPurchaseOrder is called.");

        try {
            Query q = em.createQuery("select s from SupplierEntity s where s.email = ?1 and s.supplierName = ?2 and s.isDeleted=false")
                    .setParameter(1, email)
                    .setParameter(2, password);

            if (!q.getResultList().isEmpty()) {
                PurchaseOrderEntity po = em.find(PurchaseOrderEntity.class, purchaseOrderId);
                PurchasingBean.updatePurchaseOrderStatus(purchaseOrderId, "Unfulfillable", po.getSubmittedBy());                
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    @WebMethod(operationName = "shipPurchaseOrder")
    public Boolean shipPurchaseOrder(@WebParam(name = "email") String email, @WebParam(name = "password") String password, @WebParam(name = "purchaseOrderId") Long purchaseOrderId) {
        System.out.println("shipPurchaseOrder is called.");

        try {
            Query q = em.createQuery("select s from SupplierEntity s where s.email = ?1 and s.supplierName = ?2 and s.isDeleted=false")
                    .setParameter(1, email)
                    .setParameter(2, password);

            if (!q.getResultList().isEmpty()) {
                PurchaseOrderEntity po = em.find(PurchaseOrderEntity.class, purchaseOrderId);
                PurchasingBean.updatePurchaseOrderStatus(purchaseOrderId, "Shipped", po.getSubmittedBy());                
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @WebMethod(operationName = "invoice")
    public Boolean invoice(@WebParam(name = "email") String email, @WebParam(name = "password") String password, @WebParam(name = "purchaseOrderId") Long purchaseOrderId) {
        System.out.println("getPurchaseOrder is called.");
        return false;
    }
}
