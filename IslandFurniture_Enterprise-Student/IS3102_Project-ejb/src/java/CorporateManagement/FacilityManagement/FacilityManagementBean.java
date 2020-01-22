/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CorporateManagement.FacilityManagement;

import Config.Config;
import EntityManager.AccessRightEntity;
import EntityManager.CountryEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.StaffEntity;
import EntityManager.StoreEntity;
import EntityManager.WarehouseEntity;
import HelperClasses.ManufacturingFacilityHelper;
import HelperClasses.StoreHelper;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Loi Liang Yang
 */
@Stateless
public class FacilityManagementBean implements FacilityManagementBeanLocal, FacilityManagementBeanRemote {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public boolean addRegionalOffice(String callerStaffID, String regionalOfficeName, String address, String telephone, String email) {
        System.out.println("addRegionalOffice() called with name:" + regionalOfficeName);
        String name;
        Long regionalOfficeID;
        try {
            RegionalOfficeEntity regionalOfficeEntity = new RegionalOfficeEntity();
            regionalOfficeEntity.create(regionalOfficeName, address, telephone, email);
            em.persist(regionalOfficeEntity);
            name = regionalOfficeEntity.getName();
            regionalOfficeID = regionalOfficeEntity.getId();
            System.out.println("Regional Office Name \"" + name + "\" registered successfully as id:" + regionalOfficeID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";addRegionalOffice();" + regionalOfficeID + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to register regional office:\n" + ex);
            return false;
        }
    }

    @Override
    public boolean checkNameExistsOfRegionalOffice(String name) {
        System.out.println("checkNameExistsOfRegionalOffice() called with name:" + name);
        try {
            Query q = em.createQuery("SELECT t FROM RegionalOfficeEntity t where t.isDeleted=false");
            for (Object o : q.getResultList()) {
                RegionalOfficeEntity i = (RegionalOfficeEntity) o;
                System.out.println(" i name is : " + i.getName());
                if (i.getName().equalsIgnoreCase(name)) {
                    System.out.println("Found existing name");
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to return regional office:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean editRegionalOffice(String callerStaffID, Long regionalOfficeID, String regionalOfficeName, String address, String telephone, String email) {
        System.out.println("editRegionalOffice() called with ID:" + regionalOfficeID + regionalOfficeName + address + telephone + email);
        try {
            RegionalOfficeEntity regionalOffice = em.find(RegionalOfficeEntity.class, regionalOfficeID);
            regionalOffice.setName(regionalOfficeName);
            regionalOffice.setAddress(address);
            regionalOffice.setTelephone(telephone);
            regionalOffice.setEmail(email);
            em.merge(regionalOffice);
            System.out.println("\nServer edited regional office: " + regionalOfficeID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editRegionalOffice();" + regionalOfficeID + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove regional office:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean removeRegionalOffice(String callerStaffID, String regionalOfficeID) {
        System.out.println("removeRegionalOffice() called with ID:" + regionalOfficeID);
        try {
            RegionalOfficeEntity regionalOfficeEntity = em.getReference(RegionalOfficeEntity.class, Long.valueOf(regionalOfficeID));
            if (regionalOfficeEntity.getManufacturingFacilityList().size() > 0 || regionalOfficeEntity.getStoreList().size() > 0) {
                return false; // Cannot remove if still got other facility using this.
            }
            regionalOfficeEntity.setIsDeleted(true);
            em.merge(regionalOfficeEntity);
            em.flush();
            System.out.println("Regional office removed succesfully");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";removeRegionalOffice();" + regionalOfficeID + ";");
            out.close();
            return true;
        } catch (EntityNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Failed to remove regional office, regional office not found.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove regional office:\n" + ex);
            return false;
        }

    }

    @Override
    public RegionalOfficeEntity viewRegionalOffice(String regionalOfficeId) {
        System.out.println("viewRegionalOffice() called with regionalOfficeName:" + regionalOfficeId);
        try {
            RegionalOfficeEntity regionalOffice = em.find(RegionalOfficeEntity.class, Long.valueOf(regionalOfficeId));

            System.out.println(" i id is :" + regionalOffice.getId());

            return regionalOffice;

        } catch (Exception ex) {
            System.out.println("\nServer failed to return regional office:\n" + ex);
            return null;
        }
    }

    @Override
    public List<RegionalOfficeEntity> viewListOfRegionalOffice() {
        System.out.println("viewListOfRegionalOffice() called.");
        List<RegionalOfficeEntity> listOfRegionalOffice = new ArrayList<RegionalOfficeEntity>();
        try {
            Query q = em.createQuery("SELECT t FROM RegionalOfficeEntity t where t.isDeleted=false");
            for (Object o : q.getResultList()) {
                RegionalOfficeEntity i = (RegionalOfficeEntity) o;
                listOfRegionalOffice.add(i);
            }
            return listOfRegionalOffice; //Could not find the role to remove
        } catch (Exception ex) {
            System.out.println("\nServer failed to run:\n" + ex);
            return null;
        }
    }

    @Override
    public ManufacturingFacilityEntity createManufacturingFacility(String callerStaffID, String manufacturingFacility, String address, String telephone, String email, Integer capacity, String latitude, String longitude) {
        System.out.println("createManufacturingFacility() called with name:" + manufacturingFacility);
        String name;
        Long manuafacturingFacilityID;
        try {
            ManufacturingFacilityEntity manufacturingFacilityEntity = new ManufacturingFacilityEntity();
            manufacturingFacilityEntity.create(manufacturingFacility, address, telephone, email, capacity, latitude, longitude);
            em.persist(manufacturingFacilityEntity);
            em.flush();
            name = manufacturingFacilityEntity.getName();
            manuafacturingFacilityID = manufacturingFacilityEntity.getId();
            System.out.println("Manufacturing Facility Name \"" + name + "\" registered successfully as id:" + manuafacturingFacilityID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";createManufacturingFacility();" + manuafacturingFacilityID + ";");
            out.close();
            em.detach(manufacturingFacilityEntity);
            return manufacturingFacilityEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to register manufacturing facility:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean checkNameExistsOfManufacturingFacility(String name) {
        System.out.println("checkNameExistsOfManufacturingFacility() called with name:" + name);
        try {
            Query q = em.createQuery("SELECT t FROM ManufacturingFacilityEntity t where t.isDeleted=false");
            for (Object o : q.getResultList()) {
                ManufacturingFacilityEntity i = (ManufacturingFacilityEntity) o;
                System.out.println(" i name is : " + i.getName());
                if (i.getName().equalsIgnoreCase(name)) {
                    System.out.println("Found existing name");
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to find manufacturing facility:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean editManufacturingFacility(String callerStaffID, Long manufacturingFacilityID, String manufacturingFacilityName, String address, String telephone, String email, Integer capacity, String latitude, String longitude) {
        System.out.println("editManufacturingFacility() called with ID:" + manufacturingFacilityID);
        try {
            ManufacturingFacilityEntity manufacturingFacility = em.find(ManufacturingFacilityEntity.class, manufacturingFacilityID);
            manufacturingFacility.setName(manufacturingFacilityName);
            manufacturingFacility.setAddress(address);
            manufacturingFacility.setTelephone(telephone);
            manufacturingFacility.setEmail(email);
            manufacturingFacility.setCapacity(capacity);
            manufacturingFacility.setLatitude(latitude);
            manufacturingFacility.setLongitude(longitude);
            em.merge(manufacturingFacility);
            System.out.println("\nServer edited manufacturing facility: " + manufacturingFacilityID);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editManufacturingFacility();" + manufacturingFacilityID + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("\nServer failed to edit manufacturing facility:\n" + ex);
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removeManufacturingFacility(String callerStaffID, String manufacturingFacilityID) {
        System.out.println("removeManufacturingFacility() called with ID:" + manufacturingFacilityID);
        try {
            Query q = em.createQuery("SELECT t FROM ManufacturingFacilityEntity t");

            for (Object o : q.getResultList()) {
                ManufacturingFacilityEntity i = (ManufacturingFacilityEntity) o;
                if (i.getWarehouse() != null) { //don't delete MF with warehouse
                    return false;
                }
                if (i.getId() == Long.valueOf(manufacturingFacilityID)) {
                    RegionalOfficeEntity regionalOffice = i.getRegionalOffice();
                    regionalOffice.getManufacturingFacilityList().remove(i);
                    em.merge(regionalOffice);
                    i.setIsDeleted(true);
                    em.merge(i);
                    em.flush();
                    System.out.println("\nServer removed manufacturing facility:\n" + manufacturingFacilityID);
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
                    out.println(new Date().toString() + ";" + callerStaffID + ";removeManufacturingFacility();" + manufacturingFacilityID + ";");
                    out.close();
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to remove manufacturing facility:\n" + ex);
            return false;
        }
    }

    @Override
    public ManufacturingFacilityEntity viewManufacturingFacility(Long manufacturingFacilityId) {
        System.out.println("viewManufacturingFacility() called with manufacturingFacility:" + manufacturingFacilityId);
        try {
            ManufacturingFacilityEntity manufacturingFacilityEntity = em.find(ManufacturingFacilityEntity.class, manufacturingFacilityId);
            return manufacturingFacilityEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to return manufacturing facility:\n" + ex);
            return null;
        }
    }

    @Override
    public List<ManufacturingFacilityEntity> viewListOfManufacturingFacility() {
        System.out.println("viewListOfRegionalOffice() called.");
        List<ManufacturingFacilityEntity> listOfManufacturingFacility = new ArrayList<>();
        try {
            em.flush();
            Query q = em.createQuery("SELECT t FROM ManufacturingFacilityEntity t where t.isDeleted=false");
            for (ManufacturingFacilityEntity mf : (List<ManufacturingFacilityEntity>) q.getResultList()) {
                em.refresh(mf);
                System.out.println("em.refresh(mf); mf.getStoreList().size(): " + mf.getStoreList().size());
                ManufacturingFacilityEntity i = (ManufacturingFacilityEntity) mf;
                listOfManufacturingFacility.add(i);
            }
            return listOfManufacturingFacility; //Could not find the role to remove
        } catch (Exception ex) {
            System.out.println("\nServer failed to run:\n" + ex);
            return null;
        }
    }

    @Override
    public Boolean addStoreToRegionalOffice(String callerStaffID, Long roID, Long storeId) {
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            RegionalOfficeEntity regionalOffice = em.find(RegionalOfficeEntity.class, roID);
            store.setRegionalOffice(regionalOffice);
            regionalOffice.getStoreList().add(store);
            em.merge(regionalOffice);
            em.merge(store);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";addStoreToRegionalOffice();" + roID + ";" + storeId + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public StoreEntity createStore(String callerStaffID, String storeName, String address, String telephone, String email, Long countryID, String postalCode, String imageURL, String latitude, String longitude) {
        System.out.println("createStore() called with name:" + storeName);
        String name;
        Long storeId;
        try {

            CountryEntity countryEntity = em.getReference(CountryEntity.class, countryID);
            StoreEntity storeEntity = new StoreEntity(storeName, address, telephone, email, countryEntity, postalCode, imageURL, latitude, longitude);
            em.persist(storeEntity);
            countryEntity.getStores().add(storeEntity);
            em.merge(countryEntity);
            em.flush();
            name = storeEntity.getName();
            storeId = storeEntity.getId();
            System.out.println("Store Name \"" + name + "\" registered successfully as id:" + storeId);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";createStore();" + storeId + ";");
            out.close();
            return storeEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to register manufacturing facility:\n" + ex);
            return null;
        }
    }

    @Override
    public boolean checkNameExistsOfStore(String name) {
        System.out.println("checkNameExistsOfStore() called with name:" + name);
        try {
            Query q = em.createQuery("SELECT t FROM StoreEntity t where t.isDeleted=false");
            for (Object o : q.getResultList()) {
                StoreEntity i = (StoreEntity) o;
                System.out.println(" i name is : " + i.getName());
                if (i.getName().equalsIgnoreCase(name)) {
                    System.out.println("Found existing name");
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to return store:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean editStore(String callerStaffID, Long storeId, String storeName, String address, String telephone, String email, Long countryID, String imageURL, String latitude, String longitude) {
        System.out.println("editStore() called with ID:" + storeId);
        try {
            StoreEntity storeEntity = em.find(StoreEntity.class, storeId);
            //remove from old country side
            storeEntity.getCountry().getStores().remove(storeEntity);
            //update store
            CountryEntity countryEntity = em.getReference(CountryEntity.class, countryID);
            storeEntity.setName(storeName);
            storeEntity.setAddress(address);
            storeEntity.setTelephone(telephone);
            storeEntity.setEmail(email);
            storeEntity.setStoreMapImageURL(imageURL);
            storeEntity.setCountry(countryEntity);
            storeEntity.setLatitude(latitude);
            storeEntity.setLongitude(longitude);
            em.merge(storeEntity);
            //add to new country side
            countryEntity.getStores().add(storeEntity);
            System.out.println("\nServer edited store:\n" + storeId);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editStore();" + storeId + ";");
            out.close();
            return true;

        } catch (Exception ex) {
            System.out.println("\nServer failed to edit store:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean removeStore(String callerStaffID, Long storeId) {
        System.out.println("removeStore() called.");
        try {
            Query q = em.createQuery("select s from StoreEntity s where s.id = ?1").setParameter(1, storeId);
            StoreEntity store = (StoreEntity) q.getSingleResult();
            store.getRegionalOffice().getStoreList().remove(store);
            store.setIsDeleted(true);
            em.merge(store);
            System.out.println("removeStore(): store removed");
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";removeStore();" + storeId + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            System.out.println("removeStore(): Failed to remove store:");
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public StoreEntity viewStoreEntity(Long storeId) {
        System.out.println("viewStoreEntity() called with storeEntity Id:" + storeId);
        try {
            StoreEntity storeEntity = em.find(StoreEntity.class, storeId);
            System.out.println("\nServer viewed store:\n" + storeId);
            return storeEntity;
        } catch (Exception ex) {
            System.out.println("\nServer failed to view store:\n" + ex);
            return null;
        }
    }

    @Override
    public List<StoreEntity> viewListOfStore() {
        System.out.println("viewListOfStorey() called.");
        List<StoreEntity> listOfStore = new ArrayList<StoreEntity>();
        try {
            Query q = em.createQuery("SELECT t FROM StoreEntity t where t.isDeleted=false");
            for (Object o : q.getResultList()) {
                StoreEntity i = (StoreEntity) o;
                listOfStore.add(i);
            }
            return listOfStore; //Could not find the role to remove
        } catch (Exception ex) {
            System.out.println("\nServer failed to run:\n" + ex);
            return null;
        }
    }

    @Override
    public WarehouseEntity createWarehouse(String callerStaffID, String warehouseName, String address, String telephone, String email, Long storeId, Long mfId) {
        System.out.println("createWarehouse called");
        try {
            Query q = em.createQuery("select w from WarehouseEntity w where w.warehouseName = ?1").setParameter(1, warehouseName);
            if (q.getResultList().isEmpty()) {
                WarehouseEntity warehouse = new WarehouseEntity(warehouseName, address, telephone, email);
                if (storeId != -1) {
                    StoreEntity store = em.find(StoreEntity.class, storeId);
                    if (store.getWarehouse() == null) {
                        warehouse.setStore(store);
                        warehouse.setRegionalOffice(store.getRegionalOffice());
                        warehouse.setCountry(store.getCountry());
                        store.setWarehouse(warehouse);
                        em.merge(store);
                        em.persist(warehouse);
                        return warehouse;
                    }
                } else if (mfId != -1) {
                    ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, mfId);
                    if (mf.getWarehouse() == null) {
                        warehouse.setManufaturingFacility(mf);
                        warehouse.setRegionalOffice(mf.getRegionalOffice());
                        mf.setWarehouse(warehouse);
                        em.merge(mf);
                        em.persist(warehouse);
                        return warehouse;
                    }
                }
            } else {
                System.out.println("warehouse name dupicated");
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();            
        }
        return null;
    }

    @Override
    public boolean checkNameExistsOfWarehouse(String name) {
        System.out.println("checkNameExistsOfWarehouse() called.");
        try {
            Query q = em.createQuery("Select i from WarehouseEntity i where i.warehouseName=:name and i.isDeleted=false");
            q.setParameter("name", name);
            q.getSingleResult();
            return true;
        } catch (NoResultException n) {
            System.out.println("\ncheckNameExistsOfWarehouse(): No warehouse of that name exist.");
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform name check of warehouse:\n" + ex);
            return false;
        }
    }

    @Override
    public Boolean editWarehouse(String callerStaffID, Long warehouseId, String warehouseName, String address, String telephone, String email) {
        try {
            WarehouseEntity warehouse = em.find(WarehouseEntity.class, warehouseId);
            warehouse.setWarehouseName(warehouseName);

            warehouse.setAddress(address);

            warehouse.setTelephone(telephone);

            warehouse.setEmail(email);

            em.merge(warehouse);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";editWarehouse();" + warehouseId + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteWarehouse(String callerStaffID, Long warehouseId) {
        try {
            WarehouseEntity warehouse = em.find(WarehouseEntity.class, warehouseId);
            ManufacturingFacilityEntity mf = warehouse.getManufaturingFacility();
            if (mf != null) {
                mf.setWarehouse(null);
                em.merge(mf);
            }
            StoreEntity store = warehouse.getStore();
            if (store != null) {
                store.setWarehouse(null);
                em.merge(store);
            }
            warehouse.setIsDeleted(true);
            em.merge(warehouse);
            em.flush();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";deleteWarehouse();" + warehouseId + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<WarehouseEntity> getWarehouseList() {
        try {
            Query q = em.createQuery("select w from WarehouseEntity w where w.isDeleted=false");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<WarehouseEntity>();
        }
    }

    @Override
    public WarehouseEntity getWarehouseEntityBasedOnStaffRole(Long staffId) {
        try {
            Query q = em.createQuery("select s from StaffEntity s where s.id=:staffId");
            q.setParameter("staffId", staffId);
            StaffEntity staff = (StaffEntity) q.getSingleResult();
            List<AccessRightEntity> accessRights = staff.getRoles().get(0).getAccessRightList();
            if (staff.getRoles().get(0).getName().equals("Manufacturing Facility Manager")) {
                for (AccessRightEntity accessRight : accessRights) {
                    if (accessRight.getStaff().getId().equals(staff.getId()) && accessRight.getManufacturingFacility() != null) {
                        return accessRight.getManufacturingFacility().getWarehouse();
                    }
                }
            } else if (staff.getRoles().get(0).getName().equals("Store Manager")) {
                for (AccessRightEntity accessRight1 : accessRights) {
                    if (accessRight1.getStaff().getId().equals(staff.getId()) && accessRight1.getStore() != null) {
                        return accessRight1.getStore().getWarehouse();
                    }
                }
            } else if (staff.getRoles().get(0).getName().equals("Warehouse Manager")) {
                for (AccessRightEntity accessRight1 : accessRights) {
                    if (accessRight1.getStaff().getId().equals(staff.getId()) && accessRight1.getWarehouse() != null) {
                        return accessRight1.getWarehouse();
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<WarehouseEntity> getWarehouseListByRegionalOffice(Long regionalOfficeId) {
        try {
            Query q = em.createQuery("select s from WarehouseEntity s where s.isDeleted=false and s.regionalOffice.id = ?1").setParameter(1, regionalOfficeId);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<WarehouseEntity> getMFWarehouseList() {
        try {
            Query q = em.createQuery("select w from WarehouseEntity w where w.isDeleted=false and w.manufaturingFacility is not null");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<WarehouseEntity>();
        }
    }

    @Override
    public List<WarehouseEntity> getStoreWarehouseList() {
        try {
            Query q = em.createQuery("select w from WarehouseEntity w where w.isDeleted=false and w.store IS NOT NULL");
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<WarehouseEntity>();
        }
    }

    @Override
    public WarehouseEntity getWarehouseByName(String warehouseName) {
        try {
            Query q = em.createQuery("select w from WarehouseEntity w where w.isDeleted=false and w.warehouseName = ?1").setParameter(1, warehouseName);
            return (WarehouseEntity) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public WarehouseEntity getWarehouseById(Long Id) {
        try {
            return em.find(WarehouseEntity.class, Id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<StoreEntity> getStoreListByRegionalOffice(Long regionalOfficeId) {
        try {
            Query q = em.createQuery("select s from StoreEntity s where s.isDeleted=false and s.regionalOffice.id = ?1").setParameter(1, regionalOfficeId);
            return q.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    @Remove
    public void remove() {
        System.out.println("Facility Management is removed");
    }

    @Override
    public StoreEntity getStoreByName(String storeName) {
        try {
            Query q = em.createQuery("select s from StoreEntity s where s.isDeleted=false and s.name = ?1").setParameter(1, storeName);
            return (StoreEntity) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public StoreEntity getStoreByID(Long storeID) {
        System.out.println("getStoreByID() called with ID:" + storeID);
        try {
            Query q = em.createQuery("select s from StoreEntity s where s.isDeleted=false and s.id = ?1").setParameter(1, storeID);
            StoreEntity storeEntity = (StoreEntity) q.getSingleResult();
            System.out.println("getStoreByID(): Store returned.");
            return storeEntity;
        } catch (NoResultException ex) {
            System.out.println("getStoreByID(): No such store found.");
            return null;
        } catch (Exception ex) {
            System.out.println("getStoreByID(): Failed to getStoreByID()");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public StoreHelper getStoreHelperClass(Long Id) {
        try {
            StoreEntity store = this.viewStoreEntity(Id);
            StoreHelper helper = new StoreHelper();
            helper.store = store;
            helper.regionalOffice = store.getRegionalOffice();
            helper.country = store.getCountry();
            System.out.println("return helper class");
            return helper;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StoreHelper> getStoreHelperList() {
        try {
            List<StoreEntity> storeList = this.viewListOfStore();
            List<StoreHelper> helperList = new ArrayList<StoreHelper>();
            for (StoreEntity s : storeList) {
                StoreHelper helper = new StoreHelper();
                helper.store = s;
                helper.regionalOffice = s.getRegionalOffice();
                helper.country = s.getCountry();
                helperList.add(helper);
            }
            return helperList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean updateStoreToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long storeId) {
        try {
            StoreEntity store = em.find(StoreEntity.class, storeId);
            RegionalOfficeEntity newRegionalOffice = em.find(RegionalOfficeEntity.class, regionalOfficeId);
            RegionalOfficeEntity oldRegionalOffice = store.getRegionalOffice();

            oldRegionalOffice.getStoreList().remove(store);
            store.setRegionalOffice(newRegionalOffice);
            newRegionalOffice.getStoreList().add(store);

            em.merge(oldRegionalOffice);
            em.merge(store);
            em.merge(newRegionalOffice);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";updateStoreToRegionalOffice();" + regionalOfficeId + ";" + storeId + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ManufacturingFacilityEntity getManufacturingFacilityByName(String name) {
        try {
            Query q = em.createQuery("select mf from ManufacturingFacilityEntity mf where mf.name = ?1").setParameter(1, name);
            return (ManufacturingFacilityEntity) q.getResultList().get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ManufacturingFacilityHelper getManufacturingFacilityHelper(Long manufacturingFacilityId) {
        try {
            ManufacturingFacilityHelper helper = new ManufacturingFacilityHelper();
            ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, manufacturingFacilityId);
            helper.manufacturingFacilityEntity = mf;
            helper.regionalOffice = mf.getRegionalOffice();
            return helper;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ManufacturingFacilityHelper> getManufacturingFacilityHelperList() {
        try {
            List<ManufacturingFacilityEntity> mfList = this.viewListOfManufacturingFacility();
            List<ManufacturingFacilityHelper> helperList = new ArrayList<ManufacturingFacilityHelper>();
            for (ManufacturingFacilityEntity mf : mfList) {
                ManufacturingFacilityHelper helper = new ManufacturingFacilityHelper();
                helper.manufacturingFacilityEntity = mf;
                helper.regionalOffice = mf.getRegionalOffice();
                helperList.add(helper);
            }
            return helperList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean addManufacturingFacilityToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long MFid) {
        try {
            ManufacturingFacilityEntity MF = em.find(ManufacturingFacilityEntity.class, MFid);
            RegionalOfficeEntity ro = em.find(RegionalOfficeEntity.class, regionalOfficeId);
            MF.setRegionalOffice(ro);
            ro.getManufacturingFacilityList().add(MF);
            em.merge(MF);
            em.merge(ro);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";addManufacturingFacilityToRegionalOffice();" + regionalOfficeId + ";" + MFid + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean updateManufacturingFacilityToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long MFid) {
        try {
            ManufacturingFacilityEntity MF = em.find(ManufacturingFacilityEntity.class, MFid);
            RegionalOfficeEntity newRO = em.find(RegionalOfficeEntity.class, regionalOfficeId);
            RegionalOfficeEntity oldRO = MF.getRegionalOffice();

            oldRO.getManufacturingFacilityList().remove(MF);
            MF.setRegionalOffice(newRO);
            newRO.getManufacturingFacilityList().add(MF);

            em.merge(newRO);
            em.merge(MF);
            em.merge(oldRO);
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";updateManufacturingFacilityToRegionalOffice();" + regionalOfficeId + ";" + MFid + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean removeManufacturingFacility(String callerStaffID, Long MFid) {
        try {
            ManufacturingFacilityEntity mf = em.find(ManufacturingFacilityEntity.class, MFid);
            if (mf.getWarehouse() != null) { //don't delete MF with warehouse
                return false;
            }
            RegionalOfficeEntity ro = mf.getRegionalOffice();
            ro.getManufacturingFacilityList().remove(mf);
            em.merge(ro);
            mf.setIsDeleted(true);
            em.merge(mf);
            em.flush();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Config.logFilePath, true)));
            out.println(new Date().toString() + ";" + callerStaffID + ";removeManufacturingFacility();" + MFid + ";");
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkIfWarehouseContainsItem(Long id) {
        try {
            WarehouseEntity we = em.find(WarehouseEntity.class, id);
            if (we.getStorageBins().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CountryEntity> getListOfCountries() {
        System.out.println("getListOfCountries() called.");
        try {
            Query q = em.createQuery("Select c from CountryEntity c");
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("\nServer failed to getListOfCountries:\n" + ex);
            return null;
        }
    }

    @Override
    public Long getCountryID(String countryName) {
        System.out.println("getCountryID() called.");
        try {
            Query q = em.createQuery("Select c from CountryEntity c where c.name=:countryName");
            q.setParameter("countryName", countryName);
            CountryEntity countryEntity = (CountryEntity) q.getSingleResult();
            return countryEntity.getId();
        } catch (Exception ex) {
            System.out.println("\nServer failed to getListOfCountries:\n" + ex);
            return null;
        }
    }

}
