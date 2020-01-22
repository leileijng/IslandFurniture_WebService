/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MRP.ManufacturingRequirementPlanning;

import EntityManager.MaterialRequirementEntity;
import java.util.List;
import javax.ejb.Local;


@Local
public interface ManufacturingRequirementPlanningBeanLocal {
    public Boolean generateMaterialRequirementPlan(Long MfId);
    public List<MaterialRequirementEntity> getMaterialRequirementEntityList(Long MfId);
}
