/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MRP.MaterialRequirementFulfilment;

import javax.ejb.Local;

@Local
public interface MaterialRequirementFulfilmentBeanLocal {
    public Boolean generatePurchaseOrderFromMaterialRequirement(Long MfId);
}
