
package MRP.SalesForecast;

import EntityManager.ItemEntity;
import EntityManager.MonthScheduleEntity;
import EntityManager.SaleForecastEntity;
import EntityManager.SalesFigureEntity;
import EntityManager.StoreEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SalesForecastBeanLocal {           
    
        public SaleForecastEntity getSalesForecast(Long storeId, Long productGroupId, Long scheduleId);
    
        public SaleForecastEntity getSalesForecastMovingAverage(Long storeId, Long productGroupId, Long scheduleId);
    
        public List<SalesFigureEntity> getYearlySalesFigureList(Long StoreId, Long productGroupId, Integer year);
        
        public Boolean updateSalesFigureBySalesRecord(Long salesRecordId);
        
        public SaleForecastEntity getSalesForecastLinearRegression(Long storeId, Long productGroupId, Long scheduleId);
        
        public SaleForecastEntity getSalesForecastMultipleLinearRegression(Long storeId, Long productGroupId, Long scheduleId);
        
        public MonthScheduleEntity getTheBeforeOne(MonthScheduleEntity schedule);
        
        public Boolean editSaleForecast(Long saleForecastId, Integer quantity);
}
