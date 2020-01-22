/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticalCRM.ValueAnalysis;

import EntityManager.FurnitureEntity;
import EntityManager.ItemEntity;
import EntityManager.LineItemEntity;
import EntityManager.MemberEntity;
import EntityManager.RetailProductEntity;
import EntityManager.SalesRecordEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerValueAnalysisBeanLocal {
    
    public List<LineItemEntity> sortBestSellingMenuItem1Year();
    
    public List<LineItemEntity> sortBestSellingRetailProduct1Year();
    
    public List<LineItemEntity> sortBestSellingFurniture1Year();
    
     public Integer getCustomerMonetaryValueMenuItem(Long memberId);
    
    public Integer getCustomerMonetaryValueRetailProduct(Long memberId);
    
    public Integer getCustomerFrequencyMenuItem(Long memberId);
    
    public Integer getCustomerFrequencyRetailProduct(Long memberId);
    
    public Integer getCustomerRecencyMenuItem(Long memberId);
    
    public Integer getCustomerRecencyRetailProduct(Long memberId);
    
    public Integer getAverageCustomerRecencyRetailProduct();
    
    public Integer getAverageCustomerMonetaryValueRetailProduct();
    
    public Integer getAverageCustomerFrequencyRetailProduct();
    
    public Integer getAverageCustomerFrequencyMenuItem();
    
     public Integer getAverageCustomerMonetaryValueMenuItem();
    
    public Integer getAverageCustomerRecencyMenuItem();
    
    public Date getItemLastPurchase(Long itemId);
    
    public LineItemEntity getSecondProductFromFirstMenuItem(String menuItem);
    
    public LineItemEntity getSecondProductFromFirstRP(String retailProduct);
    
    public LineItemEntity getSecondProductFromFirst(String furniture);
    
    public Integer getTotalMenuItemSoldInCountry(String country);
    
    public Integer getTotalRetailProductsSoldInCountry(String country);
    
    public Integer getTotalFurnitureSoldInCountry(String country);
    
    public Double getEstimatedCustomerLife();
    
    public Double getCustomerLifeTimeValue();
    
    public Integer numOfMembersInJoinDate(Integer year);
    
    public Integer getRevenueOfJoinDate(Integer year);
    
    public Integer numOfMembersInCountry(String country);

    public Integer totalCumulativeSpendingOfCountry(String country);
    
    public List<LineItemEntity> sortBestSellingRetailProducts();
    
    public Double averageOrderPriceForRetainedMembers();

    public Double averageOrdersPerRetainedMember();

    public Integer getTotalNumberOfSalesRecord();

    public Double getRetainedCustomerRetentionRate(List<MemberEntity> retainedMembers);

    public List<MemberEntity> getRetainedMembers();

    public Double averageOrderPriceInAcquiredYear();

    public Double getFurnitureTotalRevenue(Long furnitureId);

    public List<LineItemEntity> sortBestSellingFurniture();
    
    public List<LineItemEntity> sortBestSellingMenuItem();

    public Boolean sendMemberLoyaltyPoints(List<MemberEntity> members, Integer loyaltyPoints);

    public Double getStdErrorOfIncome();

    public Double getROfIncome();

    public Double getRSquaredOfIncome();

    public Double getStdErrorOfAge();

    public Double getROfAge();

    public Double getRSquaredOfAge();

    public Integer totalCumulativeSpendingOfJoinDate(Integer startDate, Integer endDate);

    public Integer numOfMembersInIncomeGroup(Integer startIncome, Integer endIncome);

    public Double averageOrderPrice();

    public Double averageOrdersPerAcquiredYear();

    public Double getCustomerRetentionRate();

    public Integer getAverageCustomerRecency();

    public Integer getAverageCustomerFrequency();

    public Integer getAverageCustomerMonetaryValue();

    public Integer getCustomerMonetaryValue(Long memberId);

    public Integer getCustomerFrequency(Long memberId);

    public Integer getCustomerRecency(Long memberId);

    public Integer numOfMembersInAgeGroup(Integer startAge, Integer endAge);

    public Integer averageCumulativeSpending();

    public Integer totalCumulativeSpendingOfAge(Integer startAge, Integer endAge);

    public Integer totalCumulativeSpendingOfIncome(Integer startIncome, Integer endIncome);

    public Double totalMemberRevenue();

    public Double totalNonMemberRevenue();

    public Integer customerLifetimeValueOfMember(Long memberId);

    public List<ItemEntity> viewSimilarProducts(Long itemId);

    public List<ItemEntity> viewUpsellProducts(Long itemId);

    public Integer viewMonthlyReport();

    public Integer viewSalesSummary();

    public List<FurnitureEntity> viewBestSellingFurniture();

    public List<RetailProductEntity> viewBestSellingRetailProducts();

    public List<SalesRecordEntity> viewMemberSalesRecord(Long memberId);
    
    public Double getSalesRecordAmountDueInUSD(Long salesRecordId);
}
