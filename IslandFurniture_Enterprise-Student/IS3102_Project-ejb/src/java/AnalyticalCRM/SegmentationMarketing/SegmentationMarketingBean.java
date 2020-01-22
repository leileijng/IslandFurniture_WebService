package AnalyticalCRM.SegmentationMarketing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class SegmentationMarketingBean implements SegmentationMarketingBeanLocal {
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;
    
    public void sendMonthlyNewsletter() {
        
    }
    
}
