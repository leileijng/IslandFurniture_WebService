package CommonInfrastructure.SystemSecurity;

import javax.ejb.Local;


@Local
public interface SystemSecurityBeanLocal {
    public Boolean discountMemberLoyaltyPoints(String memberId, Integer loyaltyPoints);
    
    public Boolean sendActivationEmailForStaff(String email);
    public Boolean sendActivationEmailForMember(String email);
    
    public Boolean sendPasswordResetEmailForStaff(String email);
    public Boolean sendPasswordResetEmailForMember(String email);
    
    public Boolean activateStaffAccount(String email, String code);
    public Boolean activateMemberAccount(String email, String code);
    
    public Boolean validatePasswordResetForStaff(String email, String code);
    public Boolean validatePasswordResetForMember(String email, String code);
    
}
