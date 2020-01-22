/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KitchenManagement.FoodDemandForecastingAndPlanning;

import EntityManager.LineItemEntity;
import EntityManager.MasterProductionScheduleEntity;
import EntityManager.MaterialRequirementEntity;
import EntityManager.MenuItemEntity;
import EntityManager.MonthScheduleEntity;
import EntityManager.PurchaseOrderEntity;
import EntityManager.RawIngredientEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.SalesFigureEntity;
import EntityManager.StoreEntity;
import EntityManager.Supplier_ItemEntity;
import SCM.ManufacturingInventoryControl.ManufacturingInventoryControlBeanLocal;
import SCM.RetailProductsAndRawMaterialsPurchasing.RetailProductsAndRawMaterialsPurchasingBeanLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author Administrator
 */
@Stateless
public class FoodDemandForecastingAndPlanningBean implements FoodDemandForecastingAndPlanningBeanLocal {

    @EJB
    private RetailProductsAndRawMaterialsPurchasingBeanLocal purchaseBean;
    @EJB
    private ManufacturingInventoryControlBeanLocal micBean;
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    public MonthScheduleEntity getTheBeforeOne(MonthScheduleEntity schedule) {
        try {
            if (schedule.getMonth() == 1) {
                Query q = em.createQuery("select s from MonthScheduleEntity s where s.year = ?1 and s.month = ?2")
                        .setParameter(1, schedule.getYear() - 1)
                        .setParameter(2, 12);
                return (MonthScheduleEntity) q.getResultList().get(0);
            } else {
                Query q = em.createQuery("select s from MonthScheduleEntity s where s.year = ?1 and s.month = ?2")
                        .setParameter(1, schedule.getYear())
                        .setParameter(2, schedule.getMonth() - 1);
                return (MonthScheduleEntity) q.getResultList().get(0);
            }
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public SaleForecastEntity getSalesForecast(Long storeId, Long menuItemId, Long scheduleId) {
        System.out.println("getSalesForecast is called");
        try {
            Query q = em.createQuery("select sf from SaleForecastEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                    .setParameter(1, menuItemId)
                    .setParameter(2, storeId)
                    .setParameter(3, scheduleId);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            if (!q.getResultList().isEmpty()) {
                return (SaleForecastEntity) q.getResultList().get(0);
            } else {
                return this.getSalesForecastLinearRegression(storeId, menuItemId, scheduleId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public SaleForecastEntity getSalesForecastMovingAverage(Long storeId, Long menuItemId, Long scheduleId) {
        try {
            Query q = em.createQuery("select sf from SaleForecastEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                    .setParameter(1, menuItemId)
                    .setParameter(2, storeId)
                    .setParameter(3, scheduleId);

            if (!q.getResultList().isEmpty()) {
                em.remove(q.getResultList().get(0));
                em.flush();
            }

            MonthScheduleEntity schedule = em.find(MonthScheduleEntity.class, scheduleId);
            StoreEntity store = em.find(StoreEntity.class, storeId);
            MenuItemEntity menuItem = em.find(MenuItemEntity.class, menuItemId);

            MonthScheduleEntity lastSchedule = schedule;

            try {
                int amount = 0;
                for (int i = 0; i < 3; i++) {

                    lastSchedule = this.getTheBeforeOne(lastSchedule);

                    Query q2 = em.createQuery("select sf from SalesFigureEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                            .setParameter(1, menuItemId)
                            .setParameter(2, storeId)
                            .setParameter(3, lastSchedule.getId());

                    if (!q2.getResultList().isEmpty()) {
                        System.out.println("!q2.getResultList().isEmpty()");

                        SalesFigureEntity salesFigureEntity = (SalesFigureEntity) q2.getResultList().get(0);
                        amount += salesFigureEntity.getQuantity();
                    }
                }

                SaleForecastEntity saleForecast = new SaleForecastEntity(store, menuItem, schedule, amount / 3);
                saleForecast.setMethod("A");
                em.persist(saleForecast);

                return saleForecast;
            } catch (Exception ex) {
                ex.printStackTrace();
                SaleForecastEntity saleForecast = new SaleForecastEntity(store, menuItem, schedule, 0);
                return saleForecast;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public SaleForecastEntity getSalesForecastLinearRegression(Long storeId, Long menuItemId, Long scheduleId) {
        System.out.println("debug......" + "getSalesForecastLinearRegression is called.");
        try {
            Query q = em.createQuery("select sf from SaleForecastEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                    .setParameter(1, menuItemId)
                    .setParameter(2, storeId)
                    .setParameter(3, scheduleId);

            if (!q.getResultList().isEmpty()) {
                em.remove(q.getResultList().get(0));
                em.flush();
            }

            // if not exist, then create it
            MonthScheduleEntity schedule = em.find(MonthScheduleEntity.class, scheduleId);
            StoreEntity store = em.find(StoreEntity.class, storeId);
            MenuItemEntity menuItem = em.find(MenuItemEntity.class, menuItemId);

            try {

                List<SalesFigureEntity> salesFigureList = new ArrayList<>();

                MonthScheduleEntity lastSchedule = schedule;

                Query q1 = em.createQuery("select sf from SalesFigureEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                        .setParameter(1, menuItemId)
                        .setParameter(2, storeId)
                        .setParameter(3, lastSchedule.getId());

                if (!q1.getResultList().isEmpty()) {
                    SalesFigureEntity salesFigureEntity = (SalesFigureEntity) q1.getResultList().get(0);
                    salesFigureList.add(salesFigureEntity);
                }

                while (this.getTheBeforeOne(lastSchedule) != null) {
                    lastSchedule = this.getTheBeforeOne(lastSchedule);

                    Query q2 = em.createQuery("select sf from SalesFigureEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                            .setParameter(1, menuItemId)
                            .setParameter(2, storeId)
                            .setParameter(3, lastSchedule.getId());

                    if (!q2.getResultList().isEmpty()) {
                        SalesFigureEntity salesFigureEntity = (SalesFigureEntity) q2.getResultList().get(0);
                        salesFigureList.add(salesFigureEntity);
                    }
                }

                SimpleRegression simpleRegression = new SimpleRegression();

                for (int i = 0; i < salesFigureList.size(); i++) {
                    simpleRegression.addData(salesFigureList.size() - i, salesFigureList.get(i).getQuantity());
                }

                double slope = simpleRegression.getSlope();
                double intercept = simpleRegression.getIntercept();
                double forecastQuantity = slope * (salesFigureList.size() + 1) + intercept;

                SaleForecastEntity saleForecast = new SaleForecastEntity(store, menuItem, schedule, Math.round((float) forecastQuantity));
                saleForecast.setMethod("R");
                em.persist(saleForecast);

                return saleForecast;

            } catch (Exception ex) {
                ex.printStackTrace();
                SaleForecastEntity saleForecast = new SaleForecastEntity(store, menuItem, schedule, 0);
                System.out.println("debug......" + " exception is catched");
                return saleForecast;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public SaleForecastEntity getSalesForecastMultipleLinearRegression(Long storeId, Long menuItemId, Long scheduleId) {
        System.out.println("debug......" + "getSalesForecastMultipleLinearRegression is called.");
        try {
            Query q = em.createQuery("select sf from SaleForecastEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                    .setParameter(1, menuItemId)
                    .setParameter(2, storeId)
                    .setParameter(3, scheduleId);

            if (!q.getResultList().isEmpty()) {
                em.remove(q.getResultList().get(0));
                em.flush();
            }

            // if not exist, then create it
            MonthScheduleEntity schedule = em.find(MonthScheduleEntity.class, scheduleId);
            StoreEntity store = em.find(StoreEntity.class, storeId);
            MenuItemEntity menuItem = em.find(MenuItemEntity.class, menuItemId);

            try {

                List<SalesFigureEntity> salesFigureList = new ArrayList<>();

                MonthScheduleEntity lastSchedule = schedule;

                Query q1 = em.createQuery("select sf from SalesFigureEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                        .setParameter(1, menuItemId)
                        .setParameter(2, storeId)
                        .setParameter(3, lastSchedule.getId());

                if (!q1.getResultList().isEmpty()) {
                    SalesFigureEntity salesFigureEntity = (SalesFigureEntity) q1.getResultList().get(0);
                    salesFigureList.add(salesFigureEntity);
                }

                while (this.getTheBeforeOne(lastSchedule) != null) {
                    lastSchedule = this.getTheBeforeOne(lastSchedule);

                    Query q2 = em.createQuery("select sf from SalesFigureEntity sf where sf.menuItem.id = ?1 AND sf.store.id = ?2 AND sf.schedule.id = ?3")
                            .setParameter(1, menuItemId)
                            .setParameter(2, storeId)
                            .setParameter(3, lastSchedule.getId());

                    if (!q2.getResultList().isEmpty()) {
                        SalesFigureEntity salesFigureEntity = (SalesFigureEntity) q2.getResultList().get(0);
                        salesFigureList.add(salesFigureEntity);
                    }
                }

                OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

                double[] y = new double[salesFigureList.size()];
                double[][] x = new double[salesFigureList.size()][];
                for (int i = 0; i < salesFigureList.size(); i++) {
                    y[i] = salesFigureList.get(i).getQuantity();

                    switch (salesFigureList.get(i).getSchedule().getMonth()) {
                        case 1:
                            x[i] = new double[]{i, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                            break;
                        case 2:
                            x[i] = new double[]{i, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                            break;
                        case 3:
                            x[i] = new double[]{i, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                            break;
                        case 4:
                            x[i] = new double[]{i, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0};
                            break;
                        case 5:
                            x[i] = new double[]{i, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0};
                            break;
                        case 6:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0};
                            break;
                        case 7:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
                            break;
                        case 8:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
                            break;
                        case 9:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
                            break;
                        case 10:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
                            break;
                        case 11:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
                            break;
                        case 12:
                            x[i] = new double[]{i, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
                            break;
                    }
                }
                regression.newSampleData(y, x);
                double[] coefficient = regression.estimateRegressionParameters();
                System.out.println("coefficient.length: " + coefficient.length);
                for (int i = 0; i < coefficient.length; i++) {
                    System.out.println("coefficient[i]: " + coefficient[i]);
                }
                double forecastQuantity = coefficient[0] - coefficient[1] + coefficient[schedule.getMonth() + 1];

                SaleForecastEntity saleForecast = new SaleForecastEntity(store, menuItem, schedule, Math.round((float) forecastQuantity));
                saleForecast.setMethod("M");
                em.persist(saleForecast);

                System.out.println("schedule.getId(): " + schedule.getId());

                return saleForecast;

            } catch (Exception ex) {
                ex.printStackTrace();
                SaleForecastEntity saleForecast = new SaleForecastEntity(store, menuItem, schedule, 0);
                System.out.println("debug......" + " exception is catched");
                return saleForecast;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SalesFigureEntity> getYearlySalesFigureList(Long StoreId, String menuItemSKU, Integer year) {
        try {
            Query q = em.createQuery("select s from SalesFigureEntity s where s.store.id = ?1 AND s.menuItem.SKU = ?2 AND s.schedule.year = ?3 ")
                    .setParameter(1, StoreId)
                    .setParameter(2, menuItemSKU)
                    .setParameter(3, year);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean generateMasterProductionSchedules(Long storeId) {
        System.out.println("generateMasterProductionSchedules is called.");
        try {
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            if (!scheduleList.isEmpty()) {
                StoreEntity store = em.find(StoreEntity.class, storeId);
                MonthScheduleEntity lastSchedule = scheduleList.get(scheduleList.size() - 1);

                Query q0 = em.createQuery("select mr from MaterialRequirementEntity mr");
                for (MaterialRequirementEntity mr : (List<MaterialRequirementEntity>) q0.getResultList()) {
                    em.remove(mr);
                }
                em.flush();

                // clear former MPSs
                Query q1 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.store.id = ?1 and mps.schedule.id = ?2")
                        .setParameter(1, storeId)
                        .setParameter(2, lastSchedule.getId());
                List<MasterProductionScheduleEntity> formerMPSs = (List<MasterProductionScheduleEntity>) q1.getResultList();
                for (MasterProductionScheduleEntity mps : formerMPSs) {
                    em.remove(mps);
                }
                em.flush();

                // generate MPSs
                Query q2 = em.createQuery("select sf from SaleForecastEntity sf where sf.schedule.id = ?1 and sf.store.id = ?2 and sf.menuItem is not null")
                        .setParameter(1, lastSchedule.getId())
                        .setParameter(2, storeId);
                List<SaleForecastEntity> sfList = (List<SaleForecastEntity>) q2.getResultList();
                System.out.println("sfList.size(): " + sfList.size());

                for (SaleForecastEntity sf : sfList) {

                    Query q3 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.store.id = ?1 and mps.schedule.id = ?2 and mps.menuItem.SKU = ?3")
                            .setParameter(1, storeId)
                            .setParameter(2, lastSchedule.getId())
                            .setParameter(3, sf.getMenuItem().getSKU());

                    MasterProductionScheduleEntity mps;
                    Boolean mpsExits;
                    if (q3.getResultList().isEmpty()) {
                        mps = new MasterProductionScheduleEntity();
                        mps.setStore(store);
                        mps.setSchedule(lastSchedule);
                        mps.setMenuItem(sf.getMenuItem());
                        mpsExits = false;
                    } else {
                        mps = (MasterProductionScheduleEntity) q3.getResultList().get(0);
                        mpsExits = true;
                    }
                    // total work days in the month
                    int days_month = lastSchedule.getWorkDays_firstWeek() + lastSchedule.getWorkDays_secondWeek() + lastSchedule.getWorkDays_thirdWeek()
                            + lastSchedule.getWorkDays_forthWeek() + lastSchedule.getWorkDays_fifthWeek();

                    int amount = sf.getQuantity();

                    int amount_week1 = (int) Math.round(1.0 * amount * lastSchedule.getWorkDays_firstWeek() / days_month);
                    int amount_week2 = (int) Math.round(1.0 * amount * lastSchedule.getWorkDays_secondWeek() / days_month);
                    int amount_week3 = (int) Math.round(1.0 * amount * lastSchedule.getWorkDays_thirdWeek() / days_month);
                    int amount_week4 = (int) Math.round(1.0 * amount * lastSchedule.getWorkDays_forthWeek() / days_month);

                    mps.setAmount_month(mps.getAmount_month() + amount);
                    mps.setAmount_week1(mps.getAmount_week1() + amount_week1);
                    mps.setAmount_week2(mps.getAmount_week2() + amount_week2);
                    mps.setAmount_week3(mps.getAmount_week3() + amount_week3);
                    if (lastSchedule.getWorkDays_fifthWeek() == 0) {
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
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<MasterProductionScheduleEntity> getMasterProductionSchedules(Long storeId) {
        try {
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            if (!scheduleList.isEmpty()) {
                StoreEntity store = em.find(StoreEntity.class, storeId);
                MonthScheduleEntity lastSchedule = scheduleList.get(scheduleList.size() - 1);
                Query q1 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.store.id = ?1 and mps.schedule.id = ?2")
                        .setParameter(1, storeId)
                        .setParameter(2, lastSchedule.getId());
                return (List<MasterProductionScheduleEntity>) q1.getResultList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean generateMaterialRequirementPlan(Long storeId) {
        System.out.println("generateMaterialRequirementPlan is called.");
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            MonthScheduleEntity schedule = scheduleList.get(scheduleList.size() - 1);
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, schedule.getYear());
            calendar.set(Calendar.MONTH, schedule.getMonth() - 1);

            Query q1 = em.createQuery("select mps from MasterProductionScheduleEntity mps where mps.store.id = ?1 and mps.schedule.id = ?2")
                    .setParameter(1, storeId)
                    .setParameter(2, schedule.getId());
            List<MasterProductionScheduleEntity> mpsList = (List<MasterProductionScheduleEntity>) q1.getResultList();

            for (MasterProductionScheduleEntity mps : mpsList) {
                if (mps.getMenuItem().getRecipe() != null) {
                    for (LineItemEntity lineItem : mps.getMenuItem().getRecipe().getListOfLineItems()) {
                        Query q2 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.rawIngredient.SKU = ?2 and mr.schedule.id = ?3 and mr.mps.id = ?4")
                                .setParameter(1, storeId)
                                .setParameter(2, lineItem.getItem().getSKU())
                                .setParameter(3, schedule.getId())
                                .setParameter(4, mps.getId());
                        List<MaterialRequirementEntity> mrList = (List<MaterialRequirementEntity>) q2.getResultList();
                        System.out.println("mrList.getSize(): " + mrList.size());
                        for (MaterialRequirementEntity mr : mrList) {
                            em.remove(mr);
                        }
                        em.flush();
                    }
                }
            }

            for (MasterProductionScheduleEntity mps : mpsList) {
                if (mps.getMenuItem().getRecipe() != null && mps.getAmount_month() != 0) {
                    for (LineItemEntity lineItem : mps.getMenuItem().getRecipe().getListOfLineItems()) {

                        Query query1 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.rawIngredient.SKU = ?2 and mr.schedule.id =?3 and mr.day = ?4 ")
                                .setParameter(1, storeId)
                                .setParameter(2, lineItem.getItem().getSKU())
                                .setParameter(3, schedule.getId())
                                .setParameter(4, 1);
                        if (mps.getAmount_week1() != 0) {
                            if (query1.getResultList().isEmpty()) {
                                MaterialRequirementEntity MR1 = new MaterialRequirementEntity();
                                MR1.setStore(store);
                                MR1.setMps(mps);
                                MR1.setRawIngredient((RawIngredientEntity) lineItem.getItem());
                                MR1.setQuantity(mps.getAmount_week1() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                                MR1.setSchedule(schedule);
                                MR1.setDay(1);
                                em.persist(MR1);
                            } else {
                                MaterialRequirementEntity MR1 = (MaterialRequirementEntity) query1.getResultList().get(0);
                                MR1.setQuantity(MR1.getQuantity() + mps.getAmount_week1() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                                em.merge(MR1);
                            }
                        }

                        calendar.set(Calendar.WEEK_OF_MONTH, 2);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        System.out.println("calendar.get(Calendar.DAY_OF_MONTH) week2 :" + calendar.get(Calendar.DAY_OF_MONTH));
                        Query query2 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.rawIngredient.SKU = ?2 and mr.schedule.id =?3 and mr.day = ?4 ")
                                .setParameter(1, storeId)
                                .setParameter(2, lineItem.getItem().getSKU())
                                .setParameter(3, schedule.getId())
                                .setParameter(4, calendar.get(Calendar.DAY_OF_MONTH));

                        if (query2.getResultList().isEmpty()) {
                            MaterialRequirementEntity MR2 = new MaterialRequirementEntity();
                            MR2.setStore(store);
                            MR2.setMps(mps);
                            MR2.setRawIngredient((RawIngredientEntity) lineItem.getItem());
                            System.out.println("persis-mps.getAmount_week2(): " + mps.getAmount_week2());
                            System.out.println("lineItem.getQuantity(): " + lineItem.getQuantity());
                            System.out.println("mps.getMenuItem().getRecipe().getBroadLotSize()" + mps.getMenuItem().getRecipe().getBroadLotSize());
                            MR2.setQuantity(mps.getAmount_week2() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                            MR2.setSchedule(schedule);
                            MR2.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                            em.persist(MR2);
                            System.out.println("persis-MR2.getQuantity(): " + MR2.getQuantity());
                        } else {
                            MaterialRequirementEntity MR2 = (MaterialRequirementEntity) query2.getResultList().get(0);
                            System.out.println("persis-mps.getAmount_week2(): " + mps.getAmount_week2());
                            System.out.println("lineItem.getQuantity(): " + lineItem.getQuantity());
                            System.out.println("mps.getMenuItem().getRecipe().getBroadLotSize()" + mps.getMenuItem().getRecipe().getBroadLotSize());

                            MR2.setQuantity(MR2.getQuantity() + mps.getAmount_week2() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);                            
                            em.merge(MR2);
                            System.out.println("merge-MR2.getQuantity(): " + MR2.getQuantity());
                        }

                        calendar.set(Calendar.WEEK_OF_MONTH, 3);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        System.out.println("calendar.get(Calendar.DAY_OF_MONTH) week3 :" + calendar.get(Calendar.DAY_OF_MONTH));
                        Query query3 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.rawIngredient.SKU = ?2 and mr.schedule.id =?3 and mr.day = ?4 ")
                                .setParameter(1, storeId)
                                .setParameter(2, lineItem.getItem().getSKU())
                                .setParameter(3, schedule.getId())
                                .setParameter(4, calendar.get(Calendar.DAY_OF_MONTH));
                        if (query3.getResultList().isEmpty()) {
                            MaterialRequirementEntity MR3 = new MaterialRequirementEntity();
                            MR3.setStore(store);
                            MR3.setMps(mps);
                            MR3.setRawIngredient((RawIngredientEntity) lineItem.getItem());
                            MR3.setQuantity(mps.getAmount_week3() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                            MR3.setSchedule(schedule);
                            MR3.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                            em.persist(MR3);
                        } else {
                            MaterialRequirementEntity MR3 = (MaterialRequirementEntity) query3.getResultList().get(0);
                            MR3.setQuantity(MR3.getQuantity() + mps.getAmount_week3() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                            em.merge(MR3);
                        }

                        calendar.set(Calendar.WEEK_OF_MONTH, 4);
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        System.out.println("calendar.get(Calendar.DAY_OF_MONTH) week4 :" + calendar.get(Calendar.DAY_OF_MONTH));
                        Query query4 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.rawIngredient.SKU = ?2 and mr.schedule.id =?3 and mr.day = ?4 ")
                                .setParameter(1, storeId)
                                .setParameter(2, lineItem.getItem().getSKU())
                                .setParameter(3, schedule.getId())
                                .setParameter(4, calendar.get(Calendar.DAY_OF_MONTH));
                        if (query4.getResultList().isEmpty()) {
                            MaterialRequirementEntity MR4 = new MaterialRequirementEntity();
                            MR4.setStore(store);
                            MR4.setMps(mps);
                            MR4.setRawIngredient((RawIngredientEntity) lineItem.getItem());
                            MR4.setQuantity(mps.getAmount_week4() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                            MR4.setSchedule(schedule);
                            MR4.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                            em.persist(MR4);
                        } else {
                            MaterialRequirementEntity MR4 = (MaterialRequirementEntity) query4.getResultList().get(0);
                            MR4.setQuantity(MR4.getQuantity() + mps.getAmount_week4() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                            em.merge(MR4);
                        }

                        if (mps.getAmount_week5() != 0) {
                            calendar.set(Calendar.WEEK_OF_MONTH, 5);
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            System.out.println("calendar.get(Calendar.DAY_OF_MONTH) week5 :" + calendar.get(Calendar.DAY_OF_MONTH));
                            Query query5 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.rawIngredient.SKU = ?2 and mr.schedule.id =?3 and mr.day = ?4 ")
                                    .setParameter(1, storeId)
                                    .setParameter(2, lineItem.getItem().getSKU())
                                    .setParameter(3, schedule.getId())
                                    .setParameter(4, calendar.get(Calendar.DAY_OF_MONTH));
                            if (query5.getResultList().isEmpty()) {
                                MaterialRequirementEntity MR5 = new MaterialRequirementEntity();
                                MR5.setStore(store);
                                MR5.setMps(mps);
                                MR5.setRawIngredient((RawIngredientEntity) lineItem.getItem());
                                MR5.setQuantity(mps.getAmount_week5() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                                MR5.setSchedule(schedule);
                                MR5.setDay(calendar.get(Calendar.DAY_OF_MONTH));
                                em.persist(MR5);
                            } else {
                                MaterialRequirementEntity MR5 = (MaterialRequirementEntity) query5.getResultList().get(0);
                                MR5.setQuantity(MR5.getQuantity() + mps.getAmount_week5() * lineItem.getQuantity() / mps.getMenuItem().getRecipe().getBroadLotSize() + 1);
                                em.merge(MR5);
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

    public List<MaterialRequirementEntity> getMaterialRequirementEntityList(Long storeId) {
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            MonthScheduleEntity schedule = scheduleList.get(scheduleList.size() - 1);

            Query q1 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.schedule.id = ?2")
                    .setParameter(1, storeId)
                    .setParameter(2, schedule.getId());
            return (List<MaterialRequirementEntity>) q1.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean generatePurchaseOrderFromMaterialRequirement(Long storeId) {
        System.out.println("generatePurchaseOrderFromMaterialRequirement is called.");
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            Query q = em.createQuery("select s from MonthScheduleEntity s");
            List<MonthScheduleEntity> scheduleList = q.getResultList();
            MonthScheduleEntity schedule = scheduleList.get(scheduleList.size() - 1);
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(Calendar.YEAR, schedule.getYear());
            calendar.set(Calendar.MONTH, schedule.getMonth()-1);

            Query q1 = em.createQuery("select rm from RawIngredientEntity rm");
            List<RawIngredientEntity> rmList = (List<RawIngredientEntity>) q1.getResultList();

            for (RawIngredientEntity rm : rmList) {
                Integer stockLevel = micBean.checkItemQty(store.getWarehouse().getId(), rm.getSKU());

                Query q2 = em.createQuery("select mr from MaterialRequirementEntity mr where mr.store.id = ?1 and mr.schedule.id = ?2 and mr.rawIngredient.SKU = ?3 order by mr.day")
                        .setParameter(1, storeId)
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
                                .setParameter(1, store.getRegionalOffice().getId())
                                .setParameter(2, rm.getSKU());
                        Supplier_ItemEntity supplier_ItemEntity = (Supplier_ItemEntity) q3.getSingleResult();

                        int lotsize = supplier_ItemEntity.getLotSize();
                        int purchaseQuantity = 0;
                        if (((mr.getQuantity() - stockLevel) % lotsize) != 0) {
                            purchaseQuantity = (((mr.getQuantity() - stockLevel) / lotsize) + 1) * lotsize;
                        } else {
                            purchaseQuantity = (((mr.getQuantity() - stockLevel) / lotsize)) * lotsize;
                        }
                        System.out.println("(mr.getQuantity() - stockLevel): " + (mr.getQuantity() - stockLevel) + "; lotsize: " + lotsize);
                        System.out.println("purchaseQuantity: " + purchaseQuantity);
                        calendar.set(Calendar.DAY_OF_MONTH, mr.getDay());

                        PurchaseOrderEntity purchaseOrder = purchaseBean.createPurchaseOrder(supplier_ItemEntity.getSupplier().getId(), store.getWarehouse().getId(), calendar.getTime());
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

    @Override
    public Boolean editSalesForecast(Long salesForecastId, Integer quantity) {
        try {
            SaleForecastEntity saleForecast = em.find(SaleForecastEntity.class, salesForecastId);
            saleForecast.setQuantity(quantity);
            saleForecast.setMethod("E");
            em.merge(saleForecast);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
