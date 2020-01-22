package OperationalCRM.LoyaltyAndRewards;

import CommonInfrastructure.AccountManagement.AccountManagementBeanLocal;
import EntityManager.LineItemEntity;
import EntityManager.LoyaltyTierEntity;
import EntityManager.MemberEntity;
import EntityManager.QRPhoneSyncEntity;
import EntityManager.StoreEntity;
import HelperClasses.ReturnHelper;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class LoyaltyAndRewardsBean implements LoyaltyAndRewardsBeanLocal {

    @EJB
    AccountManagementBeanLocal AccountManagementBeanLocal;
    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @Override
    public MemberEntity getMemberViaEmail(String email) {
        MemberEntity memberEntity = AccountManagementBeanLocal.getMemberByEmail(email);
        if (memberEntity == null) {
            return null;
        } else {
            return memberEntity;
        }
    }

    @Override
    public MemberEntity getMemberViaCard(String memberCard) {
        System.out.println("getMemberViaCard() called with memberCard:"+memberCard);
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.loyaltyCardId=:memberCard");
            q.setParameter("memberCard", memberCard);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            System.out.println("Returned member");
            return memberEntity;
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getMemberLoyaltyPointsAmount(String email) {
        System.out.println("getMemberLoyaltyPointsAmount() called");
        MemberEntity memberEntity = AccountManagementBeanLocal.getMemberByEmail(email);
        if (memberEntity == null) {
            return 0;
        } else {
            return memberEntity.getLoyaltyPoints();
        }
    }

    @Override
    public ReturnHelper createLoyaltyTier(String tier, Double amtRequiredPerAnnum) {
        try {
            System.out.println("createLoyaltyTier() called");
            if (getLoyaltyTierByName(tier) != null) {
                return new ReturnHelper(false, "Failed to create tier, the chosen name has been used before.");
            }
            LoyaltyTierEntity loyaltyTierEntity = new LoyaltyTierEntity(tier, amtRequiredPerAnnum);
            em.persist(loyaltyTierEntity);
            return new ReturnHelper(true, "Tier created successfully.");
        } catch (Exception ex) {
            System.out.println("createLoyaltyTier(): failed. " + ex);
            ex.printStackTrace();
            return new ReturnHelper(false, "Failed to create tier.");
        }
    }

    @Override
    public LoyaltyTierEntity getLowestLevelTier() {
        try {
            LoyaltyTierEntity lowestTier = new LoyaltyTierEntity();
            Query q = em.createQuery("select t from LoyaltyTierEntity t where t.isDeleted=false ORDER BY t.amtOfSpendingRequired ASC");
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            return (LoyaltyTierEntity) q.getResultList().get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public LoyaltyTierEntity getMemberLoyaltyTier(String email) {
        System.out.println("getMemberLoyaltyTier() called");
        try {
            Query q = em.createQuery("select m from MemberEntity m where m.email=:email and m.isDeleted=false");
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            return memberEntity.getLoyaltyTier();
        } catch (NoResultException ex) {
            System.out.println("getMemberLoyaltyTier(): Staff with specified email not found");
            return getLowestLevelTier();
        } catch (Exception ex) {
            System.out.println("getMemberLoyaltyTier() failed.");
            ex.printStackTrace();
            return getLowestLevelTier();
        }
    }

    @Override
    public LoyaltyTierEntity getMemberNextTier(Long memberID) {
        try {
            System.out.println("getMemberNextTier() called");
            MemberEntity memberEntity = em.getReference(MemberEntity.class, memberID);
            LoyaltyTierEntity memberCurrentTier = memberEntity.getLoyaltyTier();
            List<LoyaltyTierEntity> tiers = getAllLoyaltyTiers();
            //Already highest tier
            if (!tiers.isEmpty() && tiers.get(tiers.size() - 1).getAmtOfSpendingRequired().equals(memberCurrentTier.getAmtOfSpendingRequired())) {
                return null;
            }//list already sorted in asc order
            for (int i = 0; i < tiers.size() - 1; i++) {
                if (tiers.get(i).getAmtOfSpendingRequired().equals(memberCurrentTier.getAmtOfSpendingRequired())) {
                    return tiers.get(i + 1);//next tier
                }
            }
            return null;//shouldn't be able to reach here
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper updateMemberLoyaltyPointsAndTier(String email, Integer pointsUsed, Double amountPaid, Long storeID) {
        System.out.println("updateMemberLoyaltyPointsAndTier() called");
        ReturnHelper rh = new ReturnHelper(false, "");
        try {
            Query q = em.createQuery("select m from MemberEntity m where m.email=:email and m.isDeleted=false");
            q.setParameter("email", email);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            StoreEntity storeEntity = em.getReference(StoreEntity.class, storeID);
            memberEntity.setCumulativeSpending((memberEntity.getCumulativeSpending() + amountPaid/storeEntity.getCountry().getExchangeRate()));
            //Retrieve country for currency & exchange rate
            //Deduct his points if he used any
            memberEntity.setLoyaltyPoints(memberEntity.getLoyaltyPoints() - pointsUsed);
            em.merge(memberEntity);
            //Calculate points earned
            try {
                int loyaltyPointsEarned = (int) Math.round(amountPaid / storeEntity.getCountry().getExchangeRate());
                if (memberEntity.getLoyaltyTier().getTier().equals("Bronze")) {
                    loyaltyPointsEarned = loyaltyPointsEarned * 5;
                } else if (memberEntity.getLoyaltyTier().getTier().equals("Silver")) {
                    loyaltyPointsEarned = loyaltyPointsEarned * 10;
                } else if (memberEntity.getLoyaltyTier().getTier().equals("Gold")) {
                    loyaltyPointsEarned = loyaltyPointsEarned * 15;
                }
                int points = memberEntity.getLoyaltyPoints() + loyaltyPointsEarned;
                memberEntity.setLoyaltyPoints(points);
                em.merge(memberEntity);
            } catch (Exception ex) {
                System.out.println("createSalesRecord(): Error in retriving country");
                return new ReturnHelper(false, "System error in retriving country information.");
            }
            //Recalculates member tier
            List<LoyaltyTierEntity> tiers = getAllLoyaltyTiers();
            Double currentHighestTierSpending = 0.0;
            LoyaltyTierEntity highestTierThatFitsMember = null;
            // Find fitting tier
            for (LoyaltyTierEntity curr : tiers) {
                Double currAmtOfSpendingRequired = curr.getAmtOfSpendingRequired();
                if (memberEntity.getCumulativeSpending() >= currAmtOfSpendingRequired && currAmtOfSpendingRequired >= currentHighestTierSpending) {
                    currentHighestTierSpending = curr.getAmtOfSpendingRequired();
                    highestTierThatFitsMember = curr;
                }
            }
            // Update to new tier (if neccessary)
            if (!highestTierThatFitsMember.getId().equals(memberEntity.getLoyaltyTier().getId())) {
                memberEntity.setLoyaltyTier(highestTierThatFitsMember);
                rh = new ReturnHelper(true, "Your account tier have been updated to:" + highestTierThatFitsMember.getTier());
            }
            em.merge(memberEntity);
            return rh;
        } catch (NoResultException ex) {
            System.out.println("updateMemberLoyaltyPointsAndTier(): Staff with specified email not found");
            return new ReturnHelper(false, "Tier not updated, member account not found.");
        } catch (Exception ex) {
            System.out.println("updateMemberLoyaltyPointsAndTier(): failed.");
            ex.printStackTrace();
            return new ReturnHelper(false, "System error");
        }
    }

    @Override
    public ReturnHelper updateLoyaltyTier(Long tierID, Double amtRequiredPerAnnum) {
        System.out.println("updateLoyaltyTier() called");
        try {
            LoyaltyTierEntity loyaltyTierEntity = em.getReference(LoyaltyTierEntity.class, tierID);
            loyaltyTierEntity.setAmtOfSpendingRequired(amtRequiredPerAnnum);
            em.merge(loyaltyTierEntity);
            return new ReturnHelper(true, "Tier updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ReturnHelper(false, "Failed to update tier. TierID not found.");
        }
    }

    @Override
    public ReturnHelper deleteLoyaltyTier(Long tierID) {
        System.out.println("deleteLoyaltyTier() called");
        try {
            LoyaltyTierEntity loyaltyTierEntity = em.getReference(LoyaltyTierEntity.class, tierID);
            loyaltyTierEntity.setIsDeleted(true);
            em.merge(loyaltyTierEntity);
            return new ReturnHelper(true, "Tier deleted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ReturnHelper(false, "Failed to delete tier. TierID not found.");
        }
    }

    @Override
    public LoyaltyTierEntity getLoyaltyTierByName(String name) {
        System.out.println("getLoyaltyTierByName() called");
        try {
            Query q = em.createQuery("SELECT t FROM LoyaltyTierEntity t where t.tier=:name ORDER BY t.amtOfSpendingRequired ASC");
            q.setParameter("name", name);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            LoyaltyTierEntity loyaltyTierEntity = (LoyaltyTierEntity) q.getSingleResult();
            return loyaltyTierEntity;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LoyaltyTierEntity> getAllLoyaltyTiers() {
        System.out.println("getAllLoyaltyTiers() called");
        try {
            Query q = em.createQuery("SELECT t FROM LoyaltyTierEntity t where t.isDeleted=false order by t.amtOfSpendingRequired asc");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList();
        }
    }

    @Override
    public Boolean createSyncWithPhoneRequest(String qrCode) {
        System.out.println("createSyncWithPhoneRequest() called");
        try {
            QRPhoneSyncEntity phoneSyncEntity = new QRPhoneSyncEntity(qrCode);
            em.persist(phoneSyncEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("createSyncWithPhoneRequest(): Error");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String getSyncWithPhoneStatus(String qrCode) {
        System.out.println("getSyncWithPhoneStatus() called with qrCode:" + qrCode);
        try {
            Query q = em.createQuery("SELECT p from QRPhoneSyncEntity p where p.qrCode=:qrCode");
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            q.setParameter("qrCode", qrCode);
            QRPhoneSyncEntity phoneSyncEntity = (QRPhoneSyncEntity) q.getSingleResult();
            System.out.println("getSyncWithPhoneStatus(): "+phoneSyncEntity.getMemberEmail());
            return phoneSyncEntity.getMemberEmail();
        } catch (NoResultException nre) {
            System.out.println("getSyncWithPhoneStatus(): No result");
            return null;
        } catch (Exception ex) {
            System.out.println("getSyncWithPhoneStatus(): Error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean tieMemberToSyncRequest(String email, String qrCode) {
        System.out.println("tieMemberToSyncRequest() called");
        try {
            Query q = em.createQuery("SELECT p from QRPhoneSyncEntity p where p.qrCode=:qrCode");
            q.setParameter("qrCode", qrCode);
             q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            QRPhoneSyncEntity phoneSyncEntity = (QRPhoneSyncEntity) q.getSingleResult();
            if (phoneSyncEntity == null) {
                return false;
            } else {
                phoneSyncEntity.setMemberEmail(email);
                em.merge(phoneSyncEntity);
                em.flush();
                return true;
            }
        } catch (Exception ex) {
            System.out.println("tieMemberToSyncRequest(): Error");
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public MemberEntity checkMemberHasCard(String email) {
        System.out.println("checkMemberHasCard() called");
        try {
            Query q = em.createQuery("SELECT m from MemberEntity m where m.email=:email");
            q.setParameter("email", email);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            if (memberEntity.getLoyaltyCardId() == null || memberEntity.getLoyaltyCardId()=="") {
                return memberEntity;
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println("checkMemberHasCard(): Error");
            ex.printStackTrace();
            return null;
        }
    }
    
    @Override
    public MemberEntity tieCardToMember(String email, String loyaltyCardId) {
        System.out.println("tieCardToMember() called");
        try {
            Query q = em.createQuery("SELECT m from MemberEntity m where m.email=:email");
            q.setParameter("email", email);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            memberEntity.setLoyaltyCardId(loyaltyCardId);
            em.merge(memberEntity);
            return memberEntity;
        } catch (Exception ex) {
            System.out.println("tieCardToMember(): Error");
            ex.printStackTrace();
            return null;
        }
    }
}
