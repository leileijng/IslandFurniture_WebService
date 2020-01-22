/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author Administrator
 */
@Entity
public class MonthScheduleEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar date;
    private Integer year;
    private Integer month;
    private Integer workDays_firstWeek;
    private Integer workDays_secondWeek;
    private Integer workDays_thirdWeek;
    private Integer workDays_forthWeek;
    private Integer workDays_fifthWeek;
    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy = "schedule")
    private List<SaleAndOperationPlanEntity> sopList;

    public MonthScheduleEntity() {
        this.sopList = new ArrayList<>();
    }    

    public MonthScheduleEntity(Calendar date) {
        this.date = date;
        this.year = date.get(Calendar.YEAR);
        this.month = date.get(Calendar.MONTH);
        this.sopList = new ArrayList<>();
    }

    public MonthScheduleEntity(Integer year, Integer month, Integer workDays_firstWeek, Integer workDays_secondWeek, Integer workDays_thirdWeek, Integer workDays_forthWeek, Integer workDays_fifthWeek) {
        this.year = year;
        this.month = month;
        this.workDays_firstWeek = workDays_firstWeek;
        this.workDays_secondWeek = workDays_secondWeek;
        this.workDays_thirdWeek = workDays_thirdWeek;
        this.workDays_forthWeek = workDays_forthWeek;
        this.workDays_fifthWeek = workDays_fifthWeek;
        this.sopList = new ArrayList<>();
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public List<SaleAndOperationPlanEntity> getSopList() {
        return sopList;
    }

    public void setSopList(List<SaleAndOperationPlanEntity> sopList) {
        this.sopList = sopList;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getWorkDays_firstWeek() {
        return workDays_firstWeek;
    }

    public void setWorkDays_firstWeek(Integer workDays_firstWeek) {
        this.workDays_firstWeek = workDays_firstWeek;
    }

    public Integer getWorkDays_secondWeek() {
        return workDays_secondWeek;
    }

    public void setWorkDays_secondWeek(Integer workDays_secondWeek) {
        this.workDays_secondWeek = workDays_secondWeek;
    }

    public Integer getWorkDays_thirdWeek() {
        return workDays_thirdWeek;
    }

    public void setWorkDays_thirdWeek(Integer workDays_thirdWeek) {
        this.workDays_thirdWeek = workDays_thirdWeek;
    }

    public Integer getWorkDays_forthWeek() {
        return workDays_forthWeek;
    }

    public void setWorkDays_forthWeek(Integer workDays_forthWeek) {
        this.workDays_forthWeek = workDays_forthWeek;
    }

    public Integer getWorkDays_fifthWeek() {
        return workDays_fifthWeek;
    }

    public void setWorkDays_fifthWeek(Integer workDays_fifthWeek) {
        this.workDays_fifthWeek = workDays_fifthWeek;
    }        
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MonthScheduleEntity)) {
            return false;
        }
        MonthScheduleEntity other = (MonthScheduleEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntityManager.MonthScheduleEntity[ id=" + id + " ]";
    }

}
