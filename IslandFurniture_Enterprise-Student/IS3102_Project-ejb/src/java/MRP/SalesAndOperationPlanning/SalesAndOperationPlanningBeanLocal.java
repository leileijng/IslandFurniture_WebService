/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MRP.SalesAndOperationPlanning;

import EntityManager.MonthScheduleEntity;
import EntityManager.ProductGroupEntity;
import EntityManager.SaleAndOperationPlanEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.StoreEntity;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remove;

/**
 *
 * @author Lin Baoyu
 */
@Local
public interface SalesAndOperationPlanningBeanLocal {    

    public SaleAndOperationPlanEntity createSOP(SaleForecastEntity saleForcast, StoreEntity store, Calendar schedule, Integer productionPlan, Integer currentInventoryLevel, Integer targetInventoryLevel);
    
    public SaleAndOperationPlanEntity createSOP(Long storeId, Long scheduleId, Long productGroupId, Integer saleForcast, Integer productionPlan, Integer currentInventoryLevel, Integer targetInventoryLevel);
    
    public Boolean editSOP(Long Id, Integer productionPlan, Integer currentInventoryLevel, Integer targetInventoryLevel);
    
    public Boolean deleteSOP(Long Id);
    
    public List<SaleAndOperationPlanEntity> getSOPList();
    
    public List<SaleAndOperationPlanEntity> getSOPlistByYear(int year);
    
    public SaleAndOperationPlanEntity getSOPbyId(Long id);
    
    public List<MonthScheduleEntity> getScheduleList();
    
    public List<ProductGroupEntity> getUnplannedProductGroup(Long storeId, Long scheduleId);
    
    public List<SaleAndOperationPlanEntity> getSaleAndOperationPlanList(Long storeId, Long scheduleId);
    
    public List<SaleAndOperationPlanEntity> getSaleAndOperationPlanList_RetailGood(Long storeId, Long scheduleId);
    
    public List<SOP_Helper> getSOPHelperList(Long storeId, Long scheduleId);
    
    public MonthScheduleEntity createSchedule(Integer year, Integer month, Integer workDays_firstWeek, Integer workDays_secondWeek, Integer workDays_thirdWeek, Integer workDays_forthWeek, Integer workDays_fifthWeek);
    
    public MonthScheduleEntity getScheduleById(Long id);
    
    public Boolean deleteSchedule(Long id);
    
    public ProductGroupEntity getProductGroupBySOP(Long sopId);
    
    public Boolean generatePurchaseOrdersForRetailProduct(Long storeId, Long scheduleId);    
    
    public List<Integer> getPastTargetInventoryLevel(Long storeId, Long scheduleId, Long productGroupId);        
    
    @Remove
    public void remove();
}
