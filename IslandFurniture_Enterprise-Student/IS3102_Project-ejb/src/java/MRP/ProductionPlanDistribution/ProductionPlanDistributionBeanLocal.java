/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MRP.ProductionPlanDistribution;

import EntityManager.ManufacturingFacilityEntity;
import EntityManager.SaleAndOperationPlanEntity;
import EntityManager.StoreEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remove;

/**
 *
 * @author Lin Baoyu
 */
@Local
public interface ProductionPlanDistributionBeanLocal {
    
    public List<StoreEntity> getStoreListByRegionalOffice(Long regionalOfficeId);
    
    public List<ManufacturingFacilityEntity> getManufacturingFacilityListByRegionalOffice(Long regionalOfficeId);       
    
    public Boolean addStore_ManufacturingFacilityConnection(Long storeId, Long manufacturingFacilityId);
    
    public Boolean removeStore_ManufacturingFacilityConnection(Long storeId, Long mfId);
        
    public Boolean distributeProductionPlan(Long regionalOfficeId);
    
    public List<SaleAndOperationPlanEntity> getDistributedSOPList(Long regionalOfficeId);
    
    public Boolean generateShippingOrder(Long regionalOfficeId, Long scheduleId);
    
    @Remove
    public void remove();
}
