/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MRP.DemandManagement;

import EntityManager.MasterProductionScheduleEntity;
import EntityManager.MonthScheduleEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Jason
 */
@Local
public interface DemandManagementBeanLocal {
    public MonthScheduleEntity getLastSchedule();
    public List<MasterProductionScheduleEntity> getMasterProductionSchedules(Long MfId);
    public Boolean generateMasterProductionSchedules(Long MfId);
    public List<MasterProductionScheduleEntity> getMPSList(Long warehouseId);
}
