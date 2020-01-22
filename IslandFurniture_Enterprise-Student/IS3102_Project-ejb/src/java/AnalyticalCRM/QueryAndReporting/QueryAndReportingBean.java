/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AnalyticalCRM.QueryAndReporting;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class QueryAndReportingBean implements QueryAndReportingBeanLocal {
    @PersistenceContext
    private EntityManager em;
    
}
