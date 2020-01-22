package OperationalCRM.LoyaltyAndRewards;

import EntityManager.LineItemEntity;
import EntityManager.LoyaltyTierEntity;
import EntityManager.MemberEntity;
import HelperClasses.ReturnHelper;
import java.util.List;
import javax.ejb.Local;

@Local
public interface LoyaltyAndRewardsBeanLocal {
    public MemberEntity getMemberViaEmail(String email);
    public MemberEntity getMemberViaCard(String memberCard);
    public Integer getMemberLoyaltyPointsAmount(String email);
    public LoyaltyTierEntity getLowestLevelTier();
    public LoyaltyTierEntity getMemberLoyaltyTier(String email);
    public ReturnHelper updateMemberLoyaltyPointsAndTier(String email,Integer loyaltyPointsDeducted, Double amountDueInCurrentTransaction, Long storeID);
    public ReturnHelper createLoyaltyTier(String tier, Double amtRequiredPerAnnum);
    public ReturnHelper updateLoyaltyTier(Long tierID, Double amtRequiredPerAnnum);
    public ReturnHelper deleteLoyaltyTier(Long tierID);
    public LoyaltyTierEntity getLoyaltyTierByName(String name);
    public List<LoyaltyTierEntity> getAllLoyaltyTiers();
    public Boolean createSyncWithPhoneRequest(String qrCode);
    public String getSyncWithPhoneStatus(String qrCode);
    public Boolean tieMemberToSyncRequest(String email, String qrCode);
    
    public LoyaltyTierEntity getMemberNextTier(Long memberID);
    public MemberEntity checkMemberHasCard(String email);
    public MemberEntity tieCardToMember(String email, String loyaltyCardId);
}
