package CommonInfrastructure.AccountManagement;

import CommonInfrastructure.SystemSecurity.SystemSecurityBeanLocal;
import Config.Config;
import EntityManager.AccessRightEntity;
import EntityManager.CountryEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.MemberEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.RoleEntity;
import EntityManager.SalesRecordEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import EntityManager.WarehouseEntity;
import OperationalCRM.LoyaltyAndRewards.LoyaltyAndRewardsBeanLocal;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AccountManagementBean implements AccountManagementBeanLocal, AccountManagementBeanRemote {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    @EJB
    private SystemSecurityBeanLocal systemSecurityBean;

    @EJB
    private LoyaltyAndRewardsBeanLocal loyaltyAndRewardsBean;

    public AccountManagementBean() {
        System.out.println("\nCommonInfrastructure Server (EJB) created.");
    }

    @Override
    public List<MemberEntity> listAllMember() {
        System.out.println("listAllMember() called.");
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.isDeleted=false");
            List<MemberEntity> memberEntities = q.getResultList();
            return memberEntities;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all member:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean checkMemberEmailExists(String email) {
        System.out.println("checkMemberEmailExists() called with:" + email);
        Query q = em.createQuery("SELECT t FROM MemberEntity t WHERE t.email=:email");
        q.setParameter("email", email);
        try {
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean registerMember(String name, String address, Date DOB, String email, String phone, CountryEntity country, String city, String zipCode, String password) {
        System.out.println("registerMember() called with email:" + email);
        if (checkMemberEmailExists(email)) {
            return false;
        }
        Long memberID;
        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.create(name, address, DOB, email, phone, country, city, zipCode, passwordHash, passwordSalt);
            Date date = new Date();
            memberEntity.setJoinDate(date);
            memberEntity.setLoyaltyTier(loyaltyAndRewardsBean.getLowestLevelTier());
            em.persist(memberEntity);
            memberID = memberEntity.getMemberID();
            System.out.println("Email \"" + email + "\" registered successfully as id:" + memberID);
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to register member:\n" + ex);
            return false;
        }
    }

    @Override
    public MemberEntity getMemberByEmail(String email) {
        System.out.println("getMemberByEmail() called.");
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.email=:email");
            q.setParameter("email", email);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            return memberEntity;
        } catch (NoResultException ex) {
            System.out.println("Failed to find member.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getMemberByEmail:\n" + ex);
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean editMember(Long memberID, Date DOB, String name, String address, String email, String phone, CountryEntity country, String city, String zipCode, String password, Integer securityQuestion, String securityAnswer) {
        System.out.println("editMember() called with memberID:" + memberID);

        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.id=:id");
            q.setParameter("id", memberID);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            if (memberEntity.getId() == memberID) {
                memberEntity.setDOB(DOB);
                memberEntity.setName(name);
                memberEntity.setAddress(address);
                memberEntity.setPhone(phone);
                memberEntity.setCountry(country);
                memberEntity.setCity(city);
                memberEntity.setZipCode(zipCode);
                memberEntity.setSecurityQuestion(securityQuestion);
                memberEntity.setSecurityAnswer(securityAnswer);
                if (!password.isEmpty()) {
                    memberEntity.setPasswordSalt(passwordSalt);
                }
                if (!password.isEmpty()) {
                    memberEntity.setPasswordHash(passwordHash);
                }
                em.merge(memberEntity);
                System.out.println("Server edited member details successfully.");
                return true;
            }

        } catch (NoResultException ex) {
            System.out.println("Failed to find member to edit.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to retrieve activationCode:\n" + ex);
            return false;
        }

        return true;
    }

    @Override
    public Boolean editMemberAgeAndIncome(Long memberID, Integer age, Integer income) {
        System.out.println("editMemberAgeAndIncome() called with memberID:" + memberID);
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.id=:id");
            q.setParameter("id", memberID);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            if (memberEntity.getId() == memberID) {
                memberEntity.setAge(age);
                memberEntity.setIncome(income);
                em.merge(memberEntity);
                System.out.println("Server edited member details successfully.");
                return true;
            }

        } catch (NoResultException ex) {
            System.out.println("Failed to find member to edit.");
            return false;
        } catch (Exception ex) {
            System.out.println("editMemberAgeAndIncome(): Failed" + ex);
            return false;
        }
        return true;
    }

    @Override
    public MemberEntity loginMember(String email, String password) {
        System.out.println("loginMember() called with email:" + email);
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.email=:email");
            q.setParameter("email", email);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            String passwordSalt = memberEntity.getPasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (passwordHash.equals(memberEntity.getPasswordHash())) {
                System.out.println("Member with email:" + email + " logged in successfully.");
                if (memberEntity.getAccountActivationStatus() == false || memberEntity.getAccountLockStatus()) {
                    System.out.println("Member not activated or account locked.");
                    return null;
                }
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                out.println(new Date().toString() + ";" + memberEntity.getId() + ";loginMember();");
                out.close();
                return memberEntity;
            } else {
                System.out.println("Login credentials provided were incorrect, password wrong.");
                return null;
            }
        } catch (NoResultException ex) {//cannot find staff with that email
            System.out.println("Login credentials provided were incorrect, no such email found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to login member:\n" + ex);
            return null;
        }
    }

    /*public boolean checkStaffUsernameExists(String username) {
     System.out.println("checkStaffUsernameExists() called with:" + username);
     Query q = em.createQuery("SELECT t FROM StaffEntity t WHERE t.username=:username");
     q.setParameter("username", username);
     try {
     StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
     } catch (NoResultException ex) {
     return false;
     }
     return true;
     }*/
    @Override
    public boolean checkStaffEmailExists(String email) {
        System.out.println("checkStaffEmailExists() called with:" + email);
        Query q = em.createQuery("SELECT t FROM StaffEntity t WHERE t.email=:email");
        q.setParameter("email", email);
        try {
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public StaffEntity registerStaff(String callerStaffID, String identificationNo, String name, String phone, String email, String address, String password) {
        System.out.println("registerStaff() called with name:" + name);
        Long staffID;
        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            StaffEntity staffEntity = new StaffEntity();
            staffEntity.create(identificationNo, name, phone, email, address, passwordSalt, passwordHash);
            staffEntity.setRoles(new ArrayList());
            em.persist(staffEntity);
            em.flush();
            staffID = staffEntity.getId();
            System.out.println("Staff \"" + name + "\" registered successfully as id:" + staffID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";registerStaff();" + staffID + ";");
            out.close();
            return staffEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to register staff:\n" + ex);
            return null;
        }
    }

    @Override
    public StaffEntity getStaffByEmail(String email) {
        System.out.println("getStaffByEmail() called.");
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.email=:email");
            q.setParameter("email", email);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            return staffEntity;
        } catch (NoResultException ex) {
            System.out.println("Failed to find staff.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to getStaffByEmail:\n" + ex);
            ex.printStackTrace();
            return null;
        }

    }

    //For administrator to edit staff account.
    @Override
    public boolean editStaff(String callerStaffID, Long staffID, String identificationNo, String name, String phone, String password, String address, Integer securityQuestion,String securityAnswer) {
        System.out.println("editStaff() called with staffID:" + staffID);

        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t WHERE t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();

            staffEntity.setIdentificationNo(identificationNo);
            staffEntity.setName(name);
            staffEntity.setAddress(address);
            staffEntity.setPhone(phone);
            staffEntity.setSecurityQuestion(securityQuestion);
            staffEntity.setSecurityAnswer(securityAnswer);
            if (!password.isEmpty()) {
                staffEntity.setPasswordSalt(passwordSalt);
            }
            if (!password.isEmpty()) {
                staffEntity.setPasswordHash(passwordHash);
            }
            //staffEntity.setEmail(email);
            em.merge(staffEntity);
            System.out.println("\nServer edited staff succeessfully");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editStaff();" + staffID + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to edit staff:\n" + ex);
            return false;
        }
    }

    //For staff to edit their own staff account.
    @Override
    public boolean editStaff(String callerStaffID, Long staffID, String phone, String password, String address) {
        System.out.println("editStaff() called with staffID:" + staffID);

        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t WHERE t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();

            staffEntity.setAddress(address);
            staffEntity.setPhone(phone);
            if (!password.isEmpty()) {
                staffEntity.setPasswordSalt(passwordSalt);
            }
            if (!password.isEmpty()) {
                staffEntity.setPasswordHash(passwordHash);
            }
            em.merge(staffEntity);
            System.out.println("\nServer edited staff succeessfully");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editStaff();" + staffID + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to edit staff:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean removeStaff(String callerStaffID, Long staffID) {
        System.out.println("removeStaff() called with staffID:" + staffID);

        try {
            em.remove(em.getReference(StaffEntity.class, staffID));
            System.out.println("Staff removed succesfully");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";removeStaff();" + staffID + ";");
            out.close();
            return true;
        } catch (EntityNotFoundException ex) {
            System.out.println("Failed to remove staff, staff not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to staff:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean removeMember(String callerStaffID, Long memberID) {
        System.out.println("removeMember() called with memberID:" + memberID);
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t where t.id=:id");
            q.setParameter("id", memberID);
            MemberEntity memberEntity = (MemberEntity) q.getSingleResult();
            memberEntity.setIsDeleted(true);
            em.merge(memberEntity);
            em.flush();
            System.out.println("\nServer removed memberID:\n" + memberID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";removeMember();" + memberID + ";");
            out.close();
            return true;
        } catch (NoResultException ex) {
            System.out.println("Member not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to member:\n" + ex);
            return false;
        }
    }

    @Override
    public StaffEntity loginStaff(String email, String password) {
        System.out.println("loginStaff() called with email:" + email);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.email=:email");
            q.setParameter("email", email);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();

            System.out.println("Staff activation status" + staffEntity.getAccountActivationStatus());
            if (staffEntity.getAccountActivationStatus() == false || staffEntity.getAccountLockStatus() == true) {
                System.out.println("Account not yet activated or locked.");
                return null;
            }
            String passwordSalt = staffEntity.getPasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (passwordHash.equals(staffEntity.getPasswordHash())) {
                System.out.println("Staff with email:" + email + " logged in successfully.");
                staffEntity.setInvalidLoginAttempt(0);
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                System.out.println(Config.logFilePath + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                out.println(new Date().toString() + ";" + staffEntity.getId() + ";loginStaff();");
                out.close();
                return staffEntity;
            } else {
                System.out.println("Login credentials provided were incorrect, password wrong.");
                Integer numOfInvalidAttempts = staffEntity.getInvalidLoginAttempt();
                System.out.println("numOfInvalidAttemmpts : " + numOfInvalidAttempts);
                if (numOfInvalidAttempts == 9) {
                    staffEntity.setAccountLockStatus(true);
                    systemSecurityBean.sendPasswordResetEmailForStaff(email);
                    System.out.println("Account locked and email sent");
                } else {
                    staffEntity.setInvalidLoginAttempt(numOfInvalidAttempts + 1);
                }
                return null;
            }
        } catch (NoResultException ex) {//cannot find staff with that email
            System.out.println("Login credentials provided were incorrect, no such email found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to login staff:\n" + ex);
            return null;
        }
    }

    @Override
    public List<StaffEntity> listAllStaff() {
        System.out.println("listAllStaff() called.");
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t");
            List<StaffEntity> staffEntities = q.getResultList();
            return staffEntities;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all staff:\n" + ex);
            return null;
        }
    }

    @Override
    public RoleEntity createRole(String callerStaffID, String name, String accessLevel) {
        try {
            System.out.println("createRole() called with name: " + name);
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.create(name, accessLevel);
            em.persist(roleEntity);
            System.out.println("Role created.");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";createRole();" + roleEntity.getId() + ";");
            out.close();
            return roleEntity;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean updateRole(String callerStaffID, Long roleID, String accessLevel) {
        System.out.println("updateRole() called with roleID:" + roleID);
        RoleEntity roleEntity;
        try {
            Query q = em.createQuery("SELECT t FROM RoleEntity t where t.id=:id");
            q.setParameter("id", roleID);
            roleEntity = (RoleEntity) q.getSingleResult();
            //roleEntity.setName(name);
            roleEntity.setAccessLevel(accessLevel);
            em.merge(roleEntity);
            System.out.println("Roles updated successfully.");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";updateRole();" + roleID + ";");
            out.close();
        } catch (NoResultException ex) {
            System.out.println("No roles found to be updated.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer error while updating a role.\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteRole(String callerStaffID, Long roleID) {
        System.out.println("deleteRole() called with roleID:" + roleID);
        RoleEntity roleEntity;
        try {
            Query q = em.createQuery("SELECT t FROM RoleEntity t where t.id=:id");
            q.setParameter("id", roleID);
            roleEntity = (RoleEntity) q.getSingleResult();
            em.remove(roleEntity);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";deleteRole();" + roleID + ";");
            out.close();
        } catch (NoResultException ex) {
            System.out.println("No roles found to be deleted.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer error while deleting a role.\n" + ex);
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkIfRoleExists(String name) {
        System.out.println("checkRoleExists() called with:" + name);
        Query q = em.createQuery("SELECT t FROM RoleEntity t WHERE t.name=:name");
        q.setParameter("name", name);
        try {
            RoleEntity roleEntity = (RoleEntity) q.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean roleHasMembersAssigned(Long roleID) {
        System.out.println("roleHasMembersAssigned() called with roleID:" + roleID);
        RoleEntity roleEntity;
        try {
            Query q = em.createQuery("SELECT t FROM RoleEntity t where t.id=:id");
            roleEntity = (RoleEntity) q.getSingleResult();
            if (roleEntity.getStaffs() == null || roleEntity.getStaffs().isEmpty()) {
                System.out.println("Role is unused.");
                return false;
            } else {
                System.out.println("Role still contain members.");
                return true;
            }
        } catch (NoResultException ex) {
            System.out.println("No such roles found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer error while checking if role has members.\n" + ex);
            return false;
        }
    }

    @Override
    public List<RoleEntity> listAllRoles() {
        System.out.println("listAllRoles() called.");
        List<RoleEntity> roleEntities = new ArrayList();
        int result = 0;
        try {
            Query q = em.createQuery("SELECT t FROM RoleEntity t");
            roleEntities = q.getResultList();
            for (RoleEntity roleEntity : roleEntities) {
                em.refresh(roleEntity);
                result++;
            }
            System.out.println("Returned " + result + " roles.");
            return roleEntities;
        } catch (NoResultException ex) {
            System.out.println("No roles found to be returned.");
            roleEntities.clear();
            return roleEntities;
        } catch (Exception ex) {
            System.out.println("\nServer error while listing all roles.\n" + ex);
            return null;
        }
    }

    @Override
    public List<RoleEntity> listRolesHeldByStaff(Long staffID) {
        System.out.println("listRolesHeldByStaff() called.");
        List<RoleEntity> roleEntities = new ArrayList();
        int result = 0;
        try {
            StaffEntity staffEntity = em.getReference(StaffEntity.class, staffID);
            roleEntities = staffEntity.getRoles();
            for (RoleEntity roleEntity : roleEntities) {
                result++;
            }
            System.out.println("Returned " + result + " roles.");
            return roleEntities;
        } catch (NoResultException ex) {
            System.out.println("No roles found to be returned.");
            roleEntities.clear();
            return roleEntities;
        } catch (Exception ex) {
            System.out.println("\nServer error while listing all roles.\n" + ex);
            return null;
        }
    }

    @Override
    public RoleEntity searchRole(String name, String accessLevel) {
        System.out.println("searchRole() called with name:" + name);
        try {
            Query q = em.createQuery("SELECT t FROM RoleEntity t where t.name=:name AND t.accessLevel=:accessLevel");
            q.setParameter("name", name);
            q.setParameter("accessLevel", accessLevel);
            RoleEntity roleEntity = (RoleEntity) q.getSingleResult();
            System.out.println("ID:" + roleEntity.getId() + " Role:" + name + " ,Access level:" + accessLevel + " found.");
            return roleEntity;
        } catch (NoResultException ex) {
            System.out.println("Role:" + name + " ,Access level:" + accessLevel + " not found.");
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to search for role:\n" + ex);
            return null;
        }
    }

    @Override
    public RoleEntity searchRole(String name) {
        System.out.println("searchRole() called with name:" + name);
        try {
            Query q = em.createQuery("SELECT t FROM RoleEntity t where t.name=:name");
            q.setParameter("name", name);
            RoleEntity roleEntity = (RoleEntity) q.getSingleResult();
            return roleEntity;
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            System.out.println("\nServer failed to search for role:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean checkIfStaffHasRole(Long staffID, Long roleID) {
        System.out.println("checkIfStaffHasRole() called with staffID:" + staffID + ", roleID:" + roleID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:staffID");
            q.setParameter("staffID", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            Collection<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity currentRole : roles) {
                if (currentRole.getId().equals(staffID)) {
                    System.out.println("Staff has the role.");
                    return true;
                }
            }
            System.out.println("Staff do not have the role.");
            return false;
        } catch (NoResultException ex) {
            System.out.println("Staff not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to check if staff has a role:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean addStaffRole(String callerStaffID, Long staffID, Long roleID) {
        System.out.println("addStaffRole() called with staffID:" + staffID + ", roleID:" + roleID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:id");
            q.setParameter("id", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            q = em.createQuery("SELECT t FROM RoleEntity where t.id=:id");
            q.setParameter("id", roleID);
            RoleEntity roleEntity = (RoleEntity) q.getSingleResult();
            List<RoleEntity> roles = staffEntity.getRoles();
            roles.add(roleEntity);
            List<StaffEntity> staffs = roleEntity.getStaffs();
            staffs.add(staffEntity);
            em.merge(roleEntity);
            staffEntity.setRoles(roles);
            em.merge(staffEntity);
            System.out.println("Role:" + roleEntity.getName()
                    + " .Access level:" + roleEntity.getAccessLevel() + " added successfully to staff id:" + staffID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";addStaffRole();" + roleID + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove role for staff:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean removeStaffRole(String callerStaffID, Long staffID, Long roleID) {
        System.out.println("removeStaffRole() called with staffID:" + staffID);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.id=:id");
            q.setParameter("id", staffID);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            q = em.createQuery("SELECT t FROM RoleEntity where t.id=:id");
            q.setParameter("id", roleID);
            // Remove from roles side
            RoleEntity roleEntity = (RoleEntity) q.getSingleResult();
            List<StaffEntity> staffs = roleEntity.getStaffs();
            for (StaffEntity currentStaff : staffs) {
                if (currentStaff == staffEntity) {
                    staffs.remove(currentStaff);
                    roleEntity.setStaffs(staffs);
                    em.merge(roleEntity);
                    break;
                }
            }
            //Remove from staff side
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity currentRole : roles) {
                if (currentRole == roleEntity) {
                    roles.remove(currentRole);
                    staffEntity.setRoles(roles);
                    em.merge(staffEntity);
                    System.out.println("Role:" + currentRole.getName()
                            + " .Access level:" + currentRole.getAccessLevel() + " removed successfully from staff id:" + staffID);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                    out.println(new Date().toString() + ";" + callerStaffID + ";removeStaffRole();" + roleID + ";");
                    out.close();
                    return true; //Found the role & removed it
                }
            }
            return false; //Could not find the role to remove
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove role for staff:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean editStaffRole(String callerStaffID, Long staffID, List<Long> roleIDs) {
        System.out.println("editStaffRole() called with staffID:" + staffID);
        try {

            StaffEntity staffEntity = em.find(StaffEntity.class, staffID);
            List<RoleEntity> rolesToBeRemoved = staffEntity.getRoles();
            List<RoleEntity> newRoles = new ArrayList<RoleEntity>();
            String rolesIdList = "";
            System.out.println(roleIDs.size());
            for (int i = 0; i < roleIDs.size(); i++) {
                RoleEntity currRole = em.getReference(RoleEntity.class, roleIDs.get(i));
                System.out.println(currRole.getName());
                newRoles.add(currRole);
                rolesIdList.concat(roleIDs.get(i).toString() + ",");
                //Find all the roles that is removed
                System.out.println(rolesToBeRemoved.size() + "sdddd");

                for (int j = 0; j < rolesToBeRemoved.size(); j++) {
                    if (rolesToBeRemoved.get(j).getId() == currRole.getId()) {
                        rolesToBeRemoved.remove(j);//exist in both the new & old one, so is not removed
                        break;
                    }
                }
            }
            System.out.println(rolesToBeRemoved.size() + "ssssss");
            //Loop all the removed roles and remove the access right
            for (int i = 0; i < rolesToBeRemoved.size(); i++) {
                System.out.println("aaa");
                List<AccessRightEntity> accessRights = rolesToBeRemoved.get(i).getAccessRightList();
                for (int j = 0; j < accessRights.size(); j++) {
                    System.out.println(accessRights.get(j).getStaff().getId() + "+++" + staffID);
                    if (Objects.equals(accessRights.get(j).getStaff().getId(), staffID)) {
                        System.out.println("delete");
                        em.remove(accessRights.get(j));
                    }
                }
            }
            staffEntity.setRoles(new ArrayList());//blank their roles
            em.merge(staffEntity);
            em.flush();

            staffEntity.setRoles(newRoles);
            System.out.println(newRoles.size() + "sssssss");
            em.merge(staffEntity);
            em.flush();

            System.out.println(newRoles.size() + "aaaa");
            for (RoleEntity role : newRoles) {
                em.merge(role);
            }

            System.out.println("Roles successfully updated for staff id:" + staffID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editStaffRole();" + rolesIdList + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to update roles for staff:\n" + ex);
            return false;
        }
    }

    @Override
    public CountryEntity getCountry(String countryName) {
        System.out.println("getCountry() called with:" + countryName);
        Query q = em.createQuery("SELECT t FROM CountryEntity t WHERE t.name=:name");
        q.setParameter("name", countryName);
        try {
            CountryEntity countryEntity = (CountryEntity) q.getSingleResult();
            System.out.println("Match found with countryID:" + countryEntity.getId());
            return countryEntity;
        } catch (NoResultException ex) {
            System.out.println("No matching country found.");
            return null;
        }
    }

    // Generate a random salt for use in hashing the password;
    public String generatePasswordSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("\nServer failed to generate password salt.\n" + ex);
        }
        return Arrays.toString(salt);
    }

    public boolean resetStaffPassword(String email, String password) {
        System.out.println("resetStaffPassword() called with:" + email);
        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t");
            for (Object o : q.getResultList()) {
                StaffEntity i = (StaffEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    i.setPasswordHash(passwordHash);
                    i.setPasswordSalt(passwordSalt);
                    i.setAccountLockStatus(false);
                    i.setInvalidLoginAttempt(0);
                    em.merge(i);
                    System.out.println("Staff \"" + email + "\" changed password successful as id:");
                    return true;
                }
            }
            System.out.println("Staff \"" + email + "\" failed to change password as id:");
            return false;
        } catch (Exception ex) {
            System.out.println("Staff \"" + email + "\" failed to change password as id:");
            ex.printStackTrace();
            return false;
        }
    }

    public boolean resetMemberPassword(String email, String password) {
        System.out.println("resetMemberPassword() called with:" + email);
        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(passwordSalt, password);
        try {
            Query q = em.createQuery("SELECT t FROM MemberEntity t");
            for (Object o : q.getResultList()) {
                MemberEntity i = (MemberEntity) o;
                if (i.getEmail().equalsIgnoreCase(email)) {
                    i.setPasswordHash(passwordHash);
                    i.setPasswordSalt(passwordSalt);
                    i.setAccountLockStatus(false);
                    em.merge(i);
                    System.out.println("Member \"" + email + "\" changed password successful as id:");
                    return true;
                }
            }
            System.out.println("MemberEntity \"" + email + "\" failed to change password as id:");
            return false;
        } catch (Exception ex) {
            System.out.println("MemberEntity \"" + email + "\" failed to change password as id:");
            ex.printStackTrace();
            return false;
        }
    }

    // Uses supplied salt and password to generate a hashed password
    public String generatePasswordHash(String salt, String password) {
        String passwordHash = null;
        try {
            password = salt + password;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordHash = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("\nServer failed to hash password.\n" + ex);
        }
        return passwordHash;
    }

    @Override
    public StaffEntity getStaffById(Long id) {
        try {
            return em.find(StaffEntity.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public RoleEntity getRoleById(Long id) {
        try {
            return em.find(RoleEntity.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public AccessRightEntity createAccessRight(String callerStaffID, Long staffId, Long roleId, Long regionalOfficeId, Long storeId, Long warehouseId, Long mfId) {
        try {
            System.out.println("staffId: " + staffId + " roleId: " + roleId + " regionalOfficeId: " + regionalOfficeId + " storeId: " + storeId + " warehouseId: " + warehouseId + " mfId: " + mfId);

            StaffEntity staff = em.find(StaffEntity.class, staffId);
            RoleEntity role = em.find(RoleEntity.class, roleId);

            AccessRightEntity a = new AccessRightEntity();

            a.setStaff(staff);
            a.setRole(role);

            if (regionalOfficeId != -1) {
                RegionalOfficeEntity ro = em.find(RegionalOfficeEntity.class, regionalOfficeId);
                a.setRegionalOffice(ro);
            }
            if (storeId != -1) {
                StoreEntity s = em.find(StoreEntity.class, storeId);
                a.setStore(s);
            }
            if (warehouseId != -1) {
                WarehouseEntity warehouse = em.find(WarehouseEntity.class, warehouseId);
                a.setWarehouse(warehouse);
            }
            if (mfId != -1) {
                ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, mfId);
                a.setManufacturingFacility(mf);
            }
            em.persist(a);
            System.out.println("createAccessRight() success");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";createAccessRight();" + a.getId() + ";");
            out.close();
            return a;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean editAccessRight(String callerStaffID, Long staffId, Long roleId, Long regionalOfficeId, Long storeId, Long warehouseId, Long mfId) {
        try {
            System.out.println("staffId: " + staffId + " roleId: " + roleId + " regionalOfficeId: " + regionalOfficeId + " storeId: " + storeId + " warehouseId: " + warehouseId + " mfId: " + mfId);

            StaffEntity staff = em.find(StaffEntity.class, staffId);
            RoleEntity role = em.find(RoleEntity.class, roleId);

            Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                    .setParameter(1, staffId)
                    .setParameter(2, roleId);

            AccessRightEntity a = (AccessRightEntity) q.getSingleResult();

            a.setStaff(staff);
            a.setRole(role);

            if (regionalOfficeId != -1) {
                RegionalOfficeEntity ro = em.find(RegionalOfficeEntity.class, regionalOfficeId);
                a.setRegionalOffice(ro);
            }
            if (storeId != -1) {
                StoreEntity s = em.find(StoreEntity.class, storeId);
                a.setStore(s);
            }
            if (warehouseId != -1) {
                WarehouseEntity warehouse = em.find(WarehouseEntity.class, warehouseId);
                a.setWarehouse(warehouse);
            }
            if (mfId != -1) {
                ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, mfId);
                a.setManufacturingFacility(mf);
            }
            em.merge(a);
            System.out.println("em.persist();");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editAccessRight();" + a.getId() + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public AccessRightEntity isAccessRightExist(Long staffId, Long roleId) {
        try {
            System.out.println("staffId: " + staffId);
            System.out.println("roleId: " + roleId);
            Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                    .setParameter(1, staffId)
                    .setParameter(2, roleId);
            if (!q.getResultList().isEmpty()) {
                return (AccessRightEntity) q.getResultList().get(0);
            }
            System.out.println("access right not exist");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Integer checkStaffInvalidLoginAttempts(String email) {
        try {
            Query q = em.createQuery("SELECT t FROM StaffEntity t where t.email=:email");

            q.setParameter("email", email);
            StaffEntity staffEntity = (StaffEntity) q.getSingleResult();
            if (staffEntity != null) {
                return staffEntity.getInvalidLoginAttempt();
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }

    @Override
    public Boolean canStaffAccessToTheRegionalOffice(Long StaffId, Long RegionalOfficeId) {
        try {
            StaffEntity staff = em.find(StaffEntity.class, StaffId);
            List<RoleEntity> roles = staff.getRoles();
            RoleEntity admin = this.searchRole("Administrator", "System");
            RoleEntity globalManager = this.searchRole("Global Manager", "Organization");
            RoleEntity regionalManager = this.searchRole("Regional Manager", "Region");
            RoleEntity purchasingManager = this.searchRole("Purchasing Manager", "Region");
            if (roles.contains(admin) || roles.contains(globalManager)) {
                return true;
            } else if (roles.contains(regionalManager)) {
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, regionalManager.getId());
                AccessRightEntity access = (AccessRightEntity) q.getSingleResult();
                if (access.getRegionalOffice().getId().equals(RegionalOfficeId)) {
                    return true;
                }
            } else if (roles.contains(purchasingManager)) {
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, purchasingManager.getId());
                AccessRightEntity access = (AccessRightEntity) q.getSingleResult();
                if (access.getRegionalOffice().getId().equals(RegionalOfficeId)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean canStaffAccessToTheStore(Long StaffId, Long StoreId) {
        try {
            StaffEntity staff = em.find(StaffEntity.class, StaffId);
            StoreEntity store = em.find(StoreEntity.class, StoreId);
            List<RoleEntity> roles = staff.getRoles();
            RoleEntity admin = this.searchRole("Administrator", "System");
            RoleEntity globalManager = this.searchRole("Global Manager", "Organization");
            RoleEntity regionalManager = this.searchRole("Regional Manager", "Region");
            RoleEntity storeManager = this.searchRole("Store Manager", "Facility");

            if (roles.contains(admin) || roles.contains(globalManager)) {
                System.out.println("current user in role: " + admin.getName());
                return true;
            } else if (roles.contains(regionalManager)) {
                System.out.println("current user in role: " + regionalManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, regionalManager.getId());
                AccessRightEntity access = (AccessRightEntity) q.getSingleResult();
                if (access.getRegionalOffice().getId().equals(store.getRegionalOffice().getId())) {
                    return true;
                }
            } else if (roles.contains(storeManager)) {
                System.out.println("current user in role: " + storeManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, storeManager.getId());
                AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                if (accessRight.getStore().getId().equals(StoreId)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean canStaffAccessToTheManufacturingFacility(Long StaffId, Long MfId) {
        try {
            StaffEntity staff = em.find(StaffEntity.class, StaffId);
            ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, MfId);
            List<RoleEntity> roles = staff.getRoles();
            RoleEntity admin = this.searchRole("Administrator", "System");
            RoleEntity globalManager = this.searchRole("Global Manager", "Organization");
            RoleEntity regionalManager = this.searchRole("Regional Manager", "Region");
            RoleEntity mfManager = this.searchRole("Manufacturing Facility Manager", "Facility");
            if (roles.contains(admin) || roles.contains(globalManager)) {
                System.out.println("current user in role: " + admin.getName());
                return true;
            } else if (roles.contains(regionalManager)) {
                System.out.println("current user in role: " + regionalManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, regionalManager.getId());
                AccessRightEntity access = (AccessRightEntity) q.getSingleResult();
                if (access.getRegionalOffice().getId().equals(mf.getRegionalOffice().getId())) {
                    return true;
                }
            } else if (roles.contains(mfManager)) {
                System.out.println("current user in role: " + mfManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, mfManager.getId());
                AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                if (accessRight.getManufacturingFacility().getId().equals(MfId)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean canStaffAccessToTheWarehouse(Long StaffId, Long warehouseId) {
        try {
            StaffEntity staff = em.find(StaffEntity.class, StaffId);
            List<RoleEntity> roles = staff.getRoles();

            WarehouseEntity warehouse = em.find(WarehouseEntity.class, warehouseId);
            RegionalOfficeEntity RO = new RegionalOfficeEntity();
            ManufacturingFacilityEntity mf = new ManufacturingFacilityEntity();
            StoreEntity store = new StoreEntity();
            Boolean isWarehouseAttachToStore = true;
            if (warehouse.getStore() != null) {
                store = warehouse.getStore();
                RO = store.getRegionalOffice();
                isWarehouseAttachToStore = true;
            } else if (warehouse.getManufaturingFacility() != null) {
                mf = warehouse.getManufaturingFacility();
                RO = store.getRegionalOffice();
                isWarehouseAttachToStore = false;
            }

            RoleEntity admin = this.searchRole("Administrator", "System");
            RoleEntity globalManager = this.searchRole("Global Manager", "Organization");
            RoleEntity regionalManager = this.searchRole("Regional Manager", "Region");
            RoleEntity purchasingManager = this.searchRole("Purchasing Manager", "Region");
            RoleEntity mfManager = this.searchRole("Manufacturing Facility Manager", "Facility");
            RoleEntity storeManager = this.searchRole("Store Manager", "Facility");
            RoleEntity warehouseManager = this.searchRole("Warehouse Manager", "Facility");
            if (roles.contains(admin) || roles.contains(globalManager)) {
                System.out.println("current user in role: " + admin.getName());
                return true;
            } else if (roles.contains(regionalManager)) {
                System.out.println("current user in role: " + regionalManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, regionalManager.getId());
                AccessRightEntity access = (AccessRightEntity) q.getSingleResult();
                if (access.getRegionalOffice().getId().equals(RO.getId())) {
                    return true;
                }
            } else if (roles.contains(purchasingManager)) {
                System.out.println("current user in role: " + purchasingManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, purchasingManager.getId());
                AccessRightEntity access = (AccessRightEntity) q.getSingleResult();
                if (access.getRegionalOffice().getId().equals(RO.getId())) {
                    return true;
                }
            } else if (roles.contains(mfManager) && !isWarehouseAttachToStore) {
                System.out.println("current user in role: " + mfManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, mfManager.getId());
                AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                if (accessRight.getManufacturingFacility().getId().equals(mf.getId())) {
                    return true;
                }
            } else if (roles.contains(storeManager) && isWarehouseAttachToStore) {
                System.out.println("current user in role: " + storeManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, storeManager.getId());
                AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                if (accessRight.getStore().getId().equals(store.getId())) {
                    return true;
                }
            } else if (roles.contains(warehouseManager)) {
                System.out.println("current user in role: " + warehouseManager.getName());
                Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                        .setParameter(1, StaffId)
                        .setParameter(2, warehouseManager.getId());
                AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                if (accessRight.getWarehouse().equals(warehouseId)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Long getRegionalOfficeIdBasedOnStaffRole(Long staffId) {
        System.out.println("getRegionalOfficeIDbasedOnStaffRole() called:" + staffId);
        RoleEntity admin = this.searchRole("Administrator", "System");
        RoleEntity globalManager = this.searchRole("Global Manager", "Organization");
        RoleEntity regionalManager = this.searchRole("Regional Manager", "Region");
        RoleEntity purchasingManager = this.searchRole("Purchasing Manager", "Region");
        RoleEntity mfManager = this.searchRole("Manufacturing Facility Manager", "Facility");
        RoleEntity storeManager = this.searchRole("Store Manager", "Facility");
        RoleEntity warehouseManager = this.searchRole("Warehouse Manager", "Facility");
        try {
            System.out.println(regionalManager.getId() + "|" + staffId);
            StaffEntity staffEntity = em.getReference(StaffEntity.class, staffId);
            for (RoleEntity role : staffEntity.getRoles()) {
                if (role.getName().equals("Regional Manager")) {
                    Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                            .setParameter(1, staffId)
                            .setParameter(2, regionalManager.getId());
                    AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                    return accessRight.getRegionalOffice().getId();
                } else if (role.getName().equals("Purchasing Manager")) {
                    Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                            .setParameter(1, staffId)
                            .setParameter(2, purchasingManager.getId());
                    AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                    return accessRight.getRegionalOffice().getId();
                } else if (role.getName().equals("Manufacturing Facility Manager")) {
                    Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                            .setParameter(1, staffId)
                            .setParameter(2, mfManager.getId());
                    AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                    return accessRight.getManufacturingFacility().getRegionalOffice().getId();
                } else if (role.getName().equals("Store Manager")) {
                    Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                            .setParameter(1, staffId)
                            .setParameter(2, storeManager.getId());
                    AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                    return accessRight.getStore().getRegionalOffice().getId();
                } else if (role.getName().equals("Warehouse Manager")) {
                    Query q = em.createQuery("select a from AccessRightEntity a where a.staff.id = ?1 and a.role.id = ?2")
                            .setParameter(1, staffId)
                            .setParameter(2, warehouseManager.getId());
                    AccessRightEntity accessRight = (AccessRightEntity) q.getSingleResult();
                    return accessRight.getWarehouse().getRegionalOffice().getId();
                }
            }
            return null;
        } catch (Exception ex) {
            System.out.println("getRegionalOfficeIDbasedOnStaffRole():" + ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean checkIfStaffIsAdministrator(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Administrator")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsRegionalManager(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Regional Manager")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsManufacturingFacilityWarehouseManager(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Warehouse Manager")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsStoreManager(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Store Manager")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsMarketingDirector(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Marketing Director")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsProductDevelopmentEngineer(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Product Development Engineer")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsPurchasingManager(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Purchasing Manager")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsManufacturingFacilityManager(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Manufacturing Facility Manager")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsCashier(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Cashier")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsReceptionist(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Receptionist")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsPicker(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Picker")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean checkIfStaffIsGlobalManager(Long staffId) {
        try {
            StaffEntity staffEntity = (StaffEntity) em.getReference(StaffEntity.class, staffId);
            List<RoleEntity> roles = staffEntity.getRoles();
            for (RoleEntity role : roles) {
                if (role.getName().equals("Global Manager")) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public List<SalesRecordEntity> listAllSalesRecord() {
        System.out.println("listAllSalesRecord() called.");
        try {
            Query q = em.createQuery("SELECT t FROM SalesRecordEntity t");
            List<SalesRecordEntity> salesRecords = q.getResultList();
            return salesRecords;
        } catch (Exception ex) {
            System.out.println("\nServer failed to list all member:\n" + ex);
            return null;
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
