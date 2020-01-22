package OperationalCRM.LoyaltyAndRewards;

import EntityManager.LineItemEntity;
import EntityManager.MemberEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@WebService(serviceName = "LoyaltyAndRewardsWebService")
@Stateless
public class LoyaltyAndRewardsWebService {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @EJB
    LoyaltyAndRewardsBeanLocal LoyaltyAndRewardsBeanLocal;

    @WebMethod
    public Integer getMemberLoyaltyPoints(@WebParam(name = "memberEmail") String memberEmail) {
        return LoyaltyAndRewardsBeanLocal.getMemberLoyaltyPointsAmount(memberEmail);
    }

    @WebMethod
    public MemberEntity getMemberViaEmail(@WebParam(name = "memberEmail") String memberEmail) {
        MemberEntity memberEntity = LoyaltyAndRewardsBeanLocal.getMemberViaEmail(memberEmail);
        em.detach(memberEntity);
        memberEntity.setShoppingList(null);
        memberEntity.setPurchases(null);
        memberEntity.setWishList(null);
        return memberEntity;
    }

    @WebMethod
    public MemberEntity getMemberViaCard(@WebParam(name = "memberCard") String memberCard) {
        MemberEntity member = LoyaltyAndRewardsBeanLocal.getMemberViaCard(memberCard);
        em.detach(member);
        member.setCountry(null);
        member.setPurchases(null);
        member.setWishList(null);
        //member.get
        //member.setShoppingList(null);
        return member;
    }

    @WebMethod
    public Boolean createSyncWithPhoneRequest(@WebParam(name = "qrCode") String qrCode) {
        return LoyaltyAndRewardsBeanLocal.createSyncWithPhoneRequest(qrCode);
    }

    @WebMethod
    public String getSyncWithPhoneStatus(@WebParam(name = "qrCode") String qrCode) {
        return LoyaltyAndRewardsBeanLocal.getSyncWithPhoneStatus(qrCode);
    }

    @WebMethod
    public Boolean tieMemberToSyncRequest(@WebParam(name = "email") String email, @WebParam(name = "qrCode") String qrCode) {
        return LoyaltyAndRewardsBeanLocal.tieMemberToSyncRequest(email, qrCode);
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
