/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CorporateManagement.FacilityManagement;

import EntityManager.CountryEntity;
import EntityManager.ManufacturingFacilityEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.StoreEntity;
import EntityManager.WarehouseEntity;
import HelperClasses.ManufacturingFacilityHelper;
import HelperClasses.StoreHelper;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remove;

@Local
public interface FacilityManagementBeanLocal {

    public boolean addRegionalOffice(String callerStaffID, String regionalOfficeName, String address, String telephone, String email);

    public Boolean editRegionalOffice(String callerStaffID, Long id, String regionalOfficeName, String address, String telephone, String email);

    public Boolean removeRegionalOffice(String callerStaffID, String regionalOfficeName);

    public RegionalOfficeEntity viewRegionalOffice(String regionalOfficeName);

    public List<RegionalOfficeEntity> viewListOfRegionalOffice();

    public boolean checkNameExistsOfRegionalOffice(String name);

    public ManufacturingFacilityEntity createManufacturingFacility(String callerStaffID, String manufacturingFacilityName, String address, String telephone, String email, Integer capacity, String latitude, String longitude);

    public Boolean editManufacturingFacility(String callerStaffID, Long id, String manufacturingFacilityName, String address, String telephone, String email, Integer capacity, String latitude, String longitude);

    public Boolean removeManufacturingFacility(String callerStaffID, String manufacturingFacilityName);

    public ManufacturingFacilityEntity viewManufacturingFacility(Long manufacturingFacilityEntityId);

    public List<ManufacturingFacilityEntity> viewListOfManufacturingFacility();

    public boolean checkNameExistsOfManufacturingFacility(String name);

    public ManufacturingFacilityEntity getManufacturingFacilityByName(String name);

    public ManufacturingFacilityHelper getManufacturingFacilityHelper(Long manufacturingFacilityId);

    public List<ManufacturingFacilityHelper> getManufacturingFacilityHelperList();

    public Boolean addManufacturingFacilityToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long MFid);

    public Boolean updateManufacturingFacilityToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long MFid);

    public Boolean removeManufacturingFacility(String callerStaffID, Long Id);

    public StoreEntity createStore(String callerStaffID, String storeName, String address, String telephone, String email, Long countryID, String postalCode, String imageURL, String latitude, String longitude);

    public Boolean editStore(String callerStaffID, Long storeId, String storeName, String address, String telephone, String email, Long countryID, String imageURL, String latitude, String longitude);

    public StoreEntity viewStoreEntity(Long storeId);

    public List<StoreEntity> viewListOfStore();

    public boolean checkNameExistsOfStore(String name);

    public StoreEntity getStoreByName(String storeName);

    public StoreEntity getStoreByID(Long storeID);

    public Boolean removeStore(String callerStaffID, Long storeId);

    public Boolean addStoreToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long storeId);

    public Boolean updateStoreToRegionalOffice(String callerStaffID, Long regionalOfficeId, Long storeId);

    public StoreHelper getStoreHelperClass(Long Id);

    public List<StoreHelper> getStoreHelperList();

    public List<StoreEntity> getStoreListByRegionalOffice(Long regionalOfficeId);

    public WarehouseEntity createWarehouse(String callerStaffID, String warehouseName, String address, String telephone, String email, Long storeId, Long mfId);

    public Boolean editWarehouse(String callerStaffID, Long id, String warehouseName, String address, String telephone, String email);

    public Boolean deleteWarehouse(String callerStaffID, Long id);

    public WarehouseEntity getWarehouseByName(String warehouseName);

    public boolean checkNameExistsOfWarehouse(String name);

    public boolean checkIfWarehouseContainsItem(Long id);

    public List<WarehouseEntity> getWarehouseListByRegionalOffice(Long regionalOfficeId);

    public WarehouseEntity getWarehouseById(Long Id);

    public List<WarehouseEntity> getWarehouseList();

    public List<WarehouseEntity> getMFWarehouseList();

    public List<WarehouseEntity> getStoreWarehouseList();

    public List<CountryEntity> getListOfCountries();

    public Long getCountryID(String countryName);
    
    public WarehouseEntity getWarehouseEntityBasedOnStaffRole(Long staffId);


    @Remove
    public void remove();
}
