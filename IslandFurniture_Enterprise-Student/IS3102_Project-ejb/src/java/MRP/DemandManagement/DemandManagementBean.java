/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MRP.DemandManagement;

import EntityManager.FurnitureEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MasterProductionScheduleEntity;
import EntityManager.MaterialRequirementEntity;
import EntityManager.MonthScheduleEntity;
import EntityManager.ProductGroupLineItemEntity;
import EntityManager.SaleAndOperationPlanEntity;
import EntityManager.SalesFigureLineItemEntity;
import EntityManager.WarehouseEntity;
import MRP.SalesForecast.SalesForecastBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DemandManagementBean implements DemandManagementBeanLocal {    
    
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public MonthScheduleEntity getLastSchedule() {
        try {
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            return scheduleList.get(scheduleList.size() - 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean generateMasterProductionSchedules(Long MfId) {
        System.out.println("generateMasterProductionSchedules is called.");
        try {
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            if (!scheduleList.isEmpty()) {
                ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, MfId);
                MonthScheduleEntity onPlanSchedule = scheduleList.get(scheduleList.size() - 1);
                MonthScheduleEntity lastSchedule = scheduleList.get(scheduleList.size() - 2);
                // clear former MPSs
                Query q0 = em.createQuery("select mr from MaterialRequirementEntity mr");
                for (MaterialRequirementEntity mr : (List<MaterialRequirementEntity>) q0.getResultList()) {
                    em.remove(mr);
                }
                em.flush();

                Query q1 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.mf.id = ?1 and mps.schedule.id = ?2")
                        .setParameter(1, MfId)
                        .setParameter(2, onPlanSchedule.getId());
                List<MasterProductionScheduleEntity> formerMPSs = (List<MasterProductionScheduleEntity>) q1.getResultList();
                for (MasterProductionScheduleEntity mps : formerMPSs) {
                    em.remove(mps);
                }
                em.flush();

                // generate MPSs
                Query q2 = em.createQuery("select sop from SaleAndOperationPlanEntity sop where sop.schedule.id = ?1 and sop.manufacturingFacility.id = ?2")
                        .setParameter(1, onPlanSchedule.getId())
                        .setParameter(2, MfId);
                List<SaleAndOperationPlanEntity> sopList = (List<SaleAndOperationPlanEntity>) q2.getResultList();
                System.out.println("sopList.size(): " + sopList.size());

                for (SaleAndOperationPlanEntity sop : sopList) {
                    int residualMonthlyProductAmount = sop.getProductionPlan();
                    List<ProductGroupLineItemEntity> lineItemList = sop.getProductGroup().getLineItemList();
                    
                    System.out.println("lineItemList.size(): " + lineItemList.size() );
                    
                    for (ProductGroupLineItemEntity lineitem : lineItemList) {

                        Query qe = em.createQuery("select l from SalesFigureLineItemEntity l where l.saleFigure.productGroup.id = ?1 and l.saleFigure.schedule.id=?2 and l.saleFigure.store.id =?3 and l.SKU = ?4")
                                .setParameter(1, sop.getProductGroup().getId())
                                .setParameter(2, lastSchedule.getId())
                                .setParameter(3, sop.getStore().getId())
                                .setParameter(4, lineitem.getItem().getSKU());

                        if (!qe.getResultList().isEmpty()) {
                            System.out.println("qe.getResultList() is not Empty()");

                            SalesFigureLineItemEntity salesFigureLineItem = (SalesFigureLineItemEntity) qe.getResultList().get(0);

                            Query q3 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.mf.id = ?1 and mps.schedule.id = ?2 and mps.furniture.SKU = ?3")
                                    .setParameter(1, MfId)
                                    .setParameter(2, onPlanSchedule.getId())
                                    .setParameter(3, lineitem.getItem().getSKU());

                            MasterProductionScheduleEntity mps;
                            Boolean mpsExits;
                            if (q3.getResultList().isEmpty()) {
                                mps = new MasterProductionScheduleEntity();
                                mps.setMf(mf);
                                mps.setSchedule(onPlanSchedule);
                                mps.setFurniture((FurnitureEntity) lineitem.getItem());
                                mpsExits = false;
                            } else {
                                mps = (MasterProductionScheduleEntity) q3.getResultList().get(0);
                                mpsExits = true;
                            }

                            // total work days in the month
                            int days_month = onPlanSchedule.getWorkDays_firstWeek() + onPlanSchedule.getWorkDays_secondWeek() + onPlanSchedule.getWorkDays_thirdWeek()
                                    + onPlanSchedule.getWorkDays_forthWeek() + onPlanSchedule.getWorkDays_fifthWeek();

                            int amount = 0;
                            if (!lineitem.getId().equals(lineItemList.get(lineItemList.size() - 1).getId())) {
                                amount = (int) Math.round(sop.getProductionPlan() * (1.0 * salesFigureLineItem.getQuantity() / salesFigureLineItem.getSaleFigure().getQuantity()));
                                residualMonthlyProductAmount -= amount;
                            } else {
                                amount = residualMonthlyProductAmount;
                            }

                            int amount_week1 = (int) Math.round(1.0 * amount * onPlanSchedule.getWorkDays_firstWeek() / days_month);
                            int amount_week2 = (int) Math.round(1.0 * amount * onPlanSchedule.getWorkDays_secondWeek() / days_month);
                            int amount_week3 = (int) Math.round(1.0 * amount * onPlanSchedule.getWorkDays_thirdWeek() / days_month);
                            int amount_week4 = (int) Math.round(1.0 * amount * onPlanSchedule.getWorkDays_forthWeek() / days_month);

                            mps.setAmount_month(mps.getAmount_month() + amount);
                            mps.setAmount_week1(mps.getAmount_week1() + amount_week1);
                            mps.setAmount_week2(mps.getAmount_week2() + amount_week2);
                            mps.setAmount_week3(mps.getAmount_week3() + amount_week3);
                            if (onPlanSchedule.getWorkDays_fifthWeek() == 0) {
                                mps.setAmount_week4(mps.getAmount_week4() + amount - amount_week1 - amount_week2 - amount_week3);
                            } else {
                                mps.setAmount_week4(mps.getAmount_week4() + amount_week4);
                                mps.setAmount_week5(mps.getAmount_week5() + amount - amount_week1 - amount_week2 - amount_week3 - amount_week4);
                            }
                            if (mpsExits) {
                                em.merge(mps);
                            } else {
                                em.persist(mps);
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<MasterProductionScheduleEntity> getMasterProductionSchedules(Long MfId) {
        try {
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            if (!scheduleList.isEmpty()) {
                ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, MfId);
                MonthScheduleEntity lastSchedule = scheduleList.get(scheduleList.size() - 1);
                Query q1 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.mf.id = ?1 and mps.schedule.id = ?2")
                        .setParameter(1, MfId)
                        .setParameter(2, lastSchedule.getId());
                return (List<MasterProductionScheduleEntity>) q1.getResultList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<MasterProductionScheduleEntity> getMPSList(Long warehouseId) {
        try {
            WarehouseEntity warehouse = em.find(WarehouseEntity.class, warehouseId);            
            Query q = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.mf.id = ?1 ")
                    .setParameter(1, warehouse.getManufaturingFacility().getId());            
            return (List<MasterProductionScheduleEntity>) q.getResultList();            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

}
