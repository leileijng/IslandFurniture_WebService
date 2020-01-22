/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KitchenManagement.FoodDemandForecastingAndPlanning;

import EntityManager.MasterProductionScheduleEntity;
import EntityManager.MaterialRequirementEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.SalesFigureEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrator
 */
@Local
public interface FoodDemandForecastingAndPlanningBeanLocal {

    public SaleForecastEntity getSalesForecast(Long storeId, Long menuItemId, Long scheduleId);

    public Boolean editSalesForecast(Long salesForecastId, Integer quantity);

    public SaleForecastEntity getSalesForecastMovingAverage(Long storeId, Long menuItemId, Long scheduleId);

    public SaleForecastEntity getSalesForecastLinearRegression(Long storeId, Long menuItemId, Long scheduleId);

    public SaleForecastEntity getSalesForecastMultipleLinearRegression(Long storeId, Long menuItemId, Long scheduleId);

    public List<SalesFigureEntity> getYearlySalesFigureList(Long StoreId, String menuItemSKU, Integer year);

    public Boolean generateMasterProductionSchedules(Long storeId);

    public List<MasterProductionScheduleEntity> getMasterProductionSchedules(Long storeId);

    public Boolean generateMaterialRequirementPlan(Long storeId);

    public List<MaterialRequirementEntity> getMaterialRequirementEntityList(Long storeId);

    public Boolean generatePurchaseOrderFromMaterialRequirement(Long storeId);

}
