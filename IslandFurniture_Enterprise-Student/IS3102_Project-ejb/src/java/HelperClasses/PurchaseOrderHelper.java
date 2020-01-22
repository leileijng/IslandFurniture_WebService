/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperClasses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Baoyu
 */
public class PurchaseOrderHelper {
    
    public Long Id;
    public Date expectedReceivedDate;    
    public String warehouseName;    
    public String address;    
    public String email;    
    public String telephone;
    public List<lineItemHelper> lineItemHelpers = new ArrayList<>();
}
