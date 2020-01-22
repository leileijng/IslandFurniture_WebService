/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MRP.ProductionPlanDistribution;

import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MonthScheduleEntity;
import EntityManager.ProductGroupLineItemEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.SaleAndOperationPlanEntity;
import EntityManager.SalesFigureLineItemEntity;
import EntityManager.ShippingOrderEntity;
import EntityManager.StoreEntity;
import MRP.SalesForecast.SalesForecastBeanLocal;
import SCM.InboundAndOutboundLogistics.InboundAndOutboundLogisticsBeanLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Lin Baoyu
 */
@Stateless
public class ProductionPlanDistributionBean implements ProductionPlanDistributionBeanLocal {

    @EJB
    private SalesForecastBeanLocal sfBean;
    @EJB
    private InboundAndOutboundLogisticsBeanLocal ioBean;
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public List<StoreEntity> getStoreListByRegionalOffice(Long regionalOfficeId) {
        try {
            Query q = em.createQuery("select s from StoreEntity s where s.regionalOffice.id = ?1 and s.isDeleted = false").setParameter(1, regionalOfficeId);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<ManufacturingFacilityEntity> getManufacturingFacilityListByRegionalOffice(Long regionalOfficeId) {
        try {
            Query q = em.createQuery("select mf from ManufacturingFacilityEntity mf where mf.regionalOffice.id = ?1 and mf.isDeleted = false").setParameter(1, regionalOfficeId);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean addStore_ManufacturingFacilityConnection(Long storeId, Long manufacturingFacilityId) {
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            ManufacturingFacilityEntity manufacturingFacility = em.find(ManufacturingFacilityEntity.class, manufacturingFacilityId);
            store.getManufacturingFacilityList().add(manufacturingFacility);
            manufacturingFacility.getStoreList().add(store);
            em.merge(store);
            em.merge(manufacturingFacility);
            em.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Boolean removeStore_ManufacturingFacilityConnection(Long storeId, Long mfId) {
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            ManufacturingFacilityEntity manufacturingFacility = em.find(ManufacturingFacilityEntity.class, mfId);
            store.getManufacturingFacilityList().remove(manufacturingFacility);
            manufacturingFacility.getStoreList().remove(store);
            em.merge(store);
            em.merge(manufacturingFacility);
            em.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void remove() {
        System.out.println("Production Plan Distribution Plan has been removed.");
    }

    @Override
    public Boolean distributeProductionPlan(Long regionalOfficeId) {
        try {
            Query q1 = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q1.getResultList();
            if (!scheduleList.isEmpty()) {
                MonthScheduleEntity lastSchedule = scheduleList.get(scheduleList.size() - 1);

                // clear former distribution
                try {
                    List<ManufacturingFacilityEntity> mfList = this.getManufacturingFacilityListByRegionalOffice(regionalOfficeId);
                    Collections.sort(mfList, new CustomeComparator_MF());
                    for (ManufacturingFacilityEntity mf : mfList) {
                        List<StoreEntity> storeList = mf.getStoreList();
                        Collections.sort(storeList, new CustomeComparator_Store());
                        for (StoreEntity store : storeList) {
                            Query q = em.createQuery("select sop from SaleAndOperationPlanEntity sop where sop.store.id = ?1 and sop.schedule.year = ?2 and sop.schedule.month= ?3")
                                    .setParameter(1, store.getId())
                                    .setParameter(2, lastSchedule.getYear())
                                    .setParameter(3, lastSchedule.getMonth());
                            List<SaleAndOperationPlanEntity> sopList = q.getResultList();
                            for (SaleAndOperationPlanEntity sop : sopList) {
                                sop.setManufacturingFacility(null);
                                em.merge(sop);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // create new distribution plan
                List<ManufacturingFacilityEntity> manufacturingFacilityList = this.getManufacturingFacilityListByRegionalOffice(regionalOfficeId);
                Collections.sort(manufacturingFacilityList, new CustomeComparator_MF());
                for (ManufacturingFacilityEntity mf : manufacturingFacilityList) {
                    Integer residueCapacity = mf.getCapacity();
                    List<StoreEntity> storeList = mf.getStoreList();
                    Collections.sort(storeList, new CustomeComparator_Store());
                    for (StoreEntity store : storeList) {
                        Query q = em.createQuery("select sop from SaleAndOperationPlanEntity sop where sop.store.id = ?1 and sop.schedule.year = ?2 and sop.schedule.month= ?3 and sop.productGroup.type = ?4")
                                .setParameter(1, store.getId())
                                .setParameter(2, lastSchedule.getYear())
                                .setParameter(3, lastSchedule.getMonth())
                                .setParameter(4, "Furniture");
                        List<SaleAndOperationPlanEntity> sopList = q.getResultList();
                        for (SaleAndOperationPlanEntity sop : sopList) {
                            if (sop.getManufacturingFacility() == null) {
                                if (residueCapacity >= (sop.getProductGroup().getWorkHours() * sop.getProductionPlan())) {
                                    residueCapacity -= (sop.getProductGroup().getWorkHours() * sop.getProductionPlan());
                                    sop.setManufacturingFacility(mf);
                                    em.merge(sop);
                                    System.out.println(mf.getName() + " - residueCapacity: " + residueCapacity + " after taking in " + sop.getProductGroup().getName() + " with quantity " + sop.getProductionPlan());
                                }
                            }
                        }
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private class CustomeComparator_MF implements Comparator<ManufacturingFacilityEntity> {

        @Override
        public int compare(ManufacturingFacilityEntity mf1, ManufacturingFacilityEntity mf2) {
            if (mf1.getStoreList().size() > mf2.getStoreList().size()) {
                return 1;
            } else if (mf1.getStoreList().size() == mf2.getStoreList().size()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    private class CustomeComparator_Store implements Comparator<StoreEntity> {

        @Override
        public int compare(StoreEntity s1, StoreEntity s2) {
            if (s1.getManufacturingFacilityList().size() > s2.getManufacturingFacilityList().size()) {
                return 1;
            } else if (s1.getManufacturingFacilityList().size() == s2.getManufacturingFacilityList().size()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    @Override
    public List<SaleAndOperationPlanEntity> getDistributedSOPList(Long regionalOfficeId) {
        try {
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            if (!scheduleList.isEmpty()) {
                MonthScheduleEntity lastSchedule = scheduleList.get(scheduleList.size() - 1);
                Query q2 = em.createQuery("select sop from SaleAndOperationPlanEntity sop where sop.store.regionalOffice.id = ?1 and sop.schedule.id = ?2 and sop.manufacturingFacility is not null")
                        .setParameter(1, regionalOfficeId)
                        .setParameter(2, lastSchedule.getId());
                return q2.getResultList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean generateShippingOrder(Long regionalOfficeId, Long scheduleId) {
        try {
            RegionalOfficeEntity ro = em.find(RegionalOfficeEntity.class, regionalOfficeId);
            MonthScheduleEntity schedule = em.find(MonthScheduleEntity.class, scheduleId);

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, schedule.getYear());
            calendar.set(Calendar.MONTH, schedule.getMonth() - 1);

            Query q2 = em.createQuery("select sop from SaleAndOperationPlanEntity sop where sop.schedule.id = ?1 and sop.manufacturingFacility.regionalOffice.id = ?2")
                    .setParameter(1, scheduleId)
                    .setParameter(2, regionalOfficeId);

            List<SaleAndOperationPlanEntity> sopList = (List<SaleAndOperationPlanEntity>) q2.getResultList();
            System.out.println("sopList.size(): " + sopList.size());

            for (SaleAndOperationPlanEntity sop : sopList) {
                calendar.set(Calendar.WEEK_OF_MONTH, 1);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                ShippingOrderEntity shippingOrder_week1 = new ShippingOrderEntity();
                if (schedule.getWorkDays_firstWeek() != 0) {
                    shippingOrder_week1 = ioBean.createShippingOrderBasicInfo(calendar.getTime(), sop.getManufacturingFacility().getWarehouse().getId(), sop.getStore().getWarehouse().getId());
                }

                calendar.set(Calendar.WEEK_OF_MONTH, 2);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                ShippingOrderEntity shippingOrder_week2 = ioBean.createShippingOrderBasicInfo(calendar.getTime(), sop.getManufacturingFacility().getWarehouse().getId(), sop.getStore().getWarehouse().getId());

                calendar.set(Calendar.WEEK_OF_MONTH, 3);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                ShippingOrderEntity shippingOrder_week3 = ioBean.createShippingOrderBasicInfo(calendar.getTime(), sop.getManufacturingFacility().getWarehouse().getId(), sop.getStore().getWarehouse().getId());

                calendar.set(Calendar.WEEK_OF_MONTH, 4);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                ShippingOrderEntity shippingOrder_week4 = ioBean.createShippingOrderBasicInfo(calendar.getTime(), sop.getManufacturingFacility().getWarehouse().getId(), sop.getStore().getWarehouse().getId());

                calendar.set(Calendar.WEEK_OF_MONTH, 5);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                ShippingOrderEntity shippingOrder_week5 = ioBean.createShippingOrderBasicInfo(calendar.getTime(), sop.getManufacturingFacility().getWarehouse().getId(), sop.getStore().getWarehouse().getId());

                int residualMonthlyProductAmount = sop.getProductionPlan();
                List<ProductGroupLineItemEntity> lineItemList = sop.getProductGroup().getLineItemList();
                for (ProductGroupLineItemEntity lineitem : lineItemList) {
                    System.out.println("sop.getProductGroup().getId(): " + sop.getProductGroup().getId());
                    System.out.println("schedule.getId(): " + schedule.getId());
                    System.out.println("sop.getStore().getId():" + sop.getStore().getId());
                    System.out.println("lineitem.getItem().getSKU(): " + lineitem.getItem().getSKU());
                    Query qe = em.createQuery("select l from SalesFigureLineItemEntity l where l.saleFigure.productGroup.id = ?1 and l.saleFigure.schedule.id=?2 and l.saleFigure.store.id =?3 and l.SKU = ?4")
                            .setParameter(1, sop.getProductGroup().getId())
                            .setParameter(2, sfBean.getTheBeforeOne(schedule).getId())
                            .setParameter(3, sop.getStore().getId())
                            .setParameter(4, lineitem.getItem().getSKU());

                    if (!qe.getResultList().isEmpty()) {
                        System.out.println("qe.getResultList() is not Empty()");

                        SalesFigureLineItemEntity salesFigureLineItem = (SalesFigureLineItemEntity) qe.getResultList().get(0);

                        // total work days in the month
                        int days_month = schedule.getWorkDays_firstWeek() + schedule.getWorkDays_secondWeek() + schedule.getWorkDays_thirdWeek()
                                + schedule.getWorkDays_forthWeek() + schedule.getWorkDays_fifthWeek();

                        int amount = 0;
                        if (!lineitem.getId().equals(lineItemList.get(lineItemList.size() - 1).getId())) {
                            amount = (int) Math.round(sop.getProductionPlan() * (1.0 * salesFigureLineItem.getQuantity() / salesFigureLineItem.getSaleFigure().getQuantity()));
                            residualMonthlyProductAmount -= amount;
                        } else {
                            amount = residualMonthlyProductAmount;
                        }
                        if (amount > 0) {
                            int amount_week1 = (int) Math.round(1.0 * amount * schedule.getWorkDays_firstWeek() / days_month);
                            if (schedule.getWorkDays_firstWeek() != 0) {
                                ioBean.addLineItemToShippingOrder(shippingOrder_week1.getId(), lineitem.getItem().getSKU(), amount_week1);
                            }
                            int amount_week2 = (int) Math.round(1.0 * amount * schedule.getWorkDays_secondWeek() / days_month);
                            ioBean.addLineItemToShippingOrder(shippingOrder_week2.getId(), lineitem.getItem().getSKU(), amount_week2);

                            int amount_week3 = (int) Math.round(1.0 * amount * schedule.getWorkDays_thirdWeek() / days_month);
                            ioBean.addLineItemToShippingOrder(shippingOrder_week3.getId(), lineitem.getItem().getSKU(), amount_week3);

                            int amount_week4 = (int) Math.round(1.0 * amount * schedule.getWorkDays_forthWeek() / days_month);
                            ioBean.addLineItemToShippingOrder(shippingOrder_week4.getId(), lineitem.getItem().getSKU(), amount_week4);

                            if (schedule.getWorkDays_fifthWeek() != 0) {
                                int amount_week5 = amount - amount_week1 - amount_week2 - amount_week3 - amount_week4;
                                ioBean.addLineItemToShippingOrder(shippingOrder_week5.getId(), lineitem.getItem().getSKU(), amount_week5);
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
}
