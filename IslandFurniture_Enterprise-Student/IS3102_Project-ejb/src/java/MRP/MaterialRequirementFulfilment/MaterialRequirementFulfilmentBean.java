/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MRP.MaterialRequirementFulfilment;

import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MaterialRequirementEntity;
import EntityManager.MonthScheduleEntity;
import EntityManager.PurchaseOrderEntity;
import EntityManager.RawMaterialEntity;
import EntityManager.Supplier_ItemEntity;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import SCM.RetailProductsAndRawMaterialsPurchasing.RetailProductsAndRawMaterialsPurchasingBeanLocal;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MaterialRequirementFulfilmentBean implements MaterialRequirementFulfilmentBeanLocal {

    @EJB
    private RetailProductsAndRawMaterialsPurchasingBeanLocal purchaseBean;
    @EJB
    private ManufacturingInventoryControlBeanLocal micBean;

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public Boolean generatePurchaseOrderFromMaterialRequirement(Long MfId) {
        System.out.println("generatePurchaseOrderFromMaterialRequirement is called.");
        try {
            ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, MfId);
            System.out.println("mf.getWarehouse().getId(): "+mf.getWarehouse().getId());
         
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            MonthScheduleEntity schedule = scheduleList.get(scheduleList.size() - 1);
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, schedule.getYear());
            calendar.set(Calendar.MONTH, schedule.getMonth()-1);

            Query q1 = em.createQuery("select rm from RawMaterialEntity rm");
            List<RawMaterialEntity> rmList = (List<RawMaterialEntity>) q1.getResultList();

            for (RawMaterialEntity rm : rmList) {
                Integer stockLevel = micBean.checkItemQty(mf.getWarehouse().getId(), rm.getSKU());

                Query q2 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.mf.id = ?1 and mr.schedule.id = ?2 and mr.rawMaterial.SKU = ?3 order by mr.day")
                        .setParameter(1, MfId)
                        .setParameter(2, schedule.getId())
                        .setParameter(3, rm.getSKU());
                List<MaterialRequirementEntity> mrList = (List<MaterialRequirementEntity>) q2.getResultList();
                System.out.println("mrList.size()" + mrList.size());

                for (MaterialRequirementEntity mr : mrList) {
                    System.out.println("mr.getDay()" + mr.getDay());
                    if (stockLevel >= mr.getQuantity()) {
                        stockLevel -= mr.getQuantity();
                    } else {
                        Query q3 = em.createQuery("select si from Supplier_ItemEntity si where si.supplier.regionalOffice.id = ?1 and si.item.SKU = ?2")
                                .setParameter(1, mf.getRegionalOffice().getId())
                                .setParameter(2, rm.getSKU());
                        Supplier_ItemEntity supplier_ItemEntity = (Supplier_ItemEntity) q3.getSingleResult();

                        int lotsize = supplier_ItemEntity.getLotSize();
                        int purchaseQuantity = 0;
                        if (((mr.getQuantity() - stockLevel) % lotsize) != 0) {
                            purchaseQuantity = (((mr.getQuantity() - stockLevel) / lotsize) + 1) * lotsize;
                        } else {
                            purchaseQuantity = mr.getQuantity() - stockLevel;
                        }
                        System.out.println("(mr.getQuantity() - stockLevel): " + (mr.getQuantity() - stockLevel) + "; lotsize: " + lotsize);
                        System.out.println("purchaseQuantity: " + purchaseQuantity);
                        calendar.set(Calendar.DAY_OF_MONTH, mr.getDay());

                        PurchaseOrderEntity purchaseOrder = purchaseBean.createPurchaseOrder(supplier_ItemEntity.getSupplier().getId(), mf.getWarehouse().getId(), calendar.getTime());
                        purchaseBean.addLineItemToPurchaseOrder(purchaseOrder.getId(), rm.getSKU(), purchaseQuantity);

                        stockLevel = stockLevel + purchaseQuantity - mr.getQuantity();
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
