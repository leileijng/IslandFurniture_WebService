package CommonInfrastructure.AccountManagement;

import EntityManager.AccessRightEntity;
import EntityManager.CountryEntity;
import EntityManager.MemberEntity;
import EntityManager.RoleEntity;
import EntityManager.SalesRecordEntity;
import EntityManager.StaffEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface AccountManagementBeanLocal {

    public List<MemberEntity> listAllMember();

    public boolean checkMemberEmailExists(String email);

    public boolean registerMember(String name, String address, Date DOB, String email, String phone, CountryEntity country, String city, String zipCode, String password);

    public MemberEntity getMemberByEmail(String email);

    public boolean editMember(Long memberID, Date DOB, String name, String address, String email, String phone, CountryEntity country, String city, String zipCode, String password, Integer securityQuestion, String securityAnswer);
    
    public Boolean editMemberAgeAndIncome(Long memberID, Integer age, Integer income);

    public MemberEntity loginMember(String email, String password);

    public boolean checkStaffEmailExists(String email);

    public StaffEntity registerStaff(String callerStaffID, String identificationNo, String name, String phone, String email, String address, String password);

    public StaffEntity getStaffByEmail(String email);

    public StaffEntity getStaffById(Long id);

    public RoleEntity getRoleById(Long id);

    public boolean editStaff(String callerStaffID, Long staffID, String identificationNo, String name, String phone, String password, String address, Integer securityQuestion, String securityAnswer);

    public boolean editStaff(String callerStaffID, Long staffID, String phone, String password, String address);

    public boolean resetStaffPassword(String email, String password);

    public boolean resetMemberPassword(String email, String password);

    public boolean removeStaff(String callerStaffID, Long staffID);

    public boolean removeMember(String callerStaffID, Long memberID);

    public StaffEntity loginStaff(String email, String password);

    public List<StaffEntity> listAllStaff();

    //Creating the types of roles
    public RoleEntity createRole(String callerStaffID, String name, String accessLevel);

    public boolean updateRole(String callerStaffID, Long roleID, String accessLevel);

    public boolean deleteRole(String callerStaffID, Long roleID); //Returns true if role deleted successfully

    public Boolean checkIfRoleExists(String name);

    public boolean roleHasMembersAssigned(Long roleID);

    public List<RoleEntity> listAllRoles();

    public List<RoleEntity> listRolesHeldByStaff(Long staffID);

    public RoleEntity searchRole(String name, String accessLevel);

    public RoleEntity searchRole(String name);

    public boolean checkIfStaffHasRole(Long staffID, Long roleID);

    //Assign role to staffs. Returns true if operation is successful, false means either member have that role or role does not exist.
    public boolean addStaffRole(String callerStaffID, Long staffID, Long roleID);

    public boolean removeStaffRole(String callerStaffID, Long staffID, Long roleID);

    public boolean editStaffRole(String callerStaffID, Long staffID, List<Long> roleIDs);

    public CountryEntity getCountry(String countryName);

    public String generatePasswordSalt();

    public String generatePasswordHash(String salt, String password);

    public Integer checkStaffInvalidLoginAttempts(String email);

    public AccessRightEntity createAccessRight(String callerStaffID, Long staffId, Long roleId, Long regionalOfficeId, Long storeId, Long warehouseId, Long mfId);

    public AccessRightEntity isAccessRightExist(Long staffId, Long roleId);

    public Boolean canStaffAccessToTheRegionalOffice(Long StaffId, Long RegionalOfficeId);

    public Boolean canStaffAccessToTheStore(Long StaffId, Long StoreId);

    public Boolean canStaffAccessToTheManufacturingFacility(Long StaffId, Long MfId);

    public Boolean canStaffAccessToTheWarehouse(Long StaffId, Long warehouseId);

    public Boolean editAccessRight(String callerStaffID, Long staffId, Long roleId, Long regionalOfficeId, Long storeId, Long warehouseId, Long mfId);

    public Long getRegionalOfficeIdBasedOnStaffRole(Long staffId);

    public Boolean checkIfStaffIsAdministrator(Long staffId);

    public Boolean checkIfStaffIsRegionalManager(Long staffId);

    public Boolean checkIfStaffIsManufacturingFacilityWarehouseManager(Long staffId);

    public Boolean checkIfStaffIsStoreManager(Long staffId);

    public Boolean checkIfStaffIsMarketingDirector(Long staffId);

    public Boolean checkIfStaffIsProductDevelopmentEngineer(Long staffId);

    public Boolean checkIfStaffIsPurchasingManager(Long staffId);

    public Boolean checkIfStaffIsManufacturingFacilityManager(Long staffId);

    public Boolean checkIfStaffIsCashier(Long staffId);

    public Boolean checkIfStaffIsReceptionist(Long staffId);
    public Boolean checkIfStaffIsPicker(Long staffId);

    public Boolean checkIfStaffIsGlobalManager(Long staffId);
    
    public List<SalesRecordEntity> listAllSalesRecord();

}
