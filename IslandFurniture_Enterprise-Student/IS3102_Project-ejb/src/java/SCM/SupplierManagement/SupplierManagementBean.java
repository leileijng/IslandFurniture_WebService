package SCM.SupplierManagement;

import EntityManager.CountryEntity;
import EntityManager.RegionalOfficeEntity;
import EntityManager.SupplierEntity;
import EntityManager.Supplier_ItemEntity;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;

@Stateless
public class SupplierManagementBean implements SupplierManagementBeanLocal, SupplierManagementBeanRemote {

    @PersistenceContext(unitName = "IS3102_Project-ejbPU")
    private EntityManager em;
    private SupplierEntity supplier;
    private CountryEntity country;

    public SupplierEntity findASupplier(Long id) {
        return em.find(SupplierEntity.class, id);
    }

    public boolean checkSupplierExists(Long id) {
        return em.find(SupplierEntity.class, id) != null;
    }

    @Override
    public Boolean addSupplier(String supplierName, String contactNo, String email, String address, Long countryId, Long roID) {
        System.out.println("addSupplier() called.");
        try {
            RegionalOfficeEntity regionalOfficeEntity = em.getReference(RegionalOfficeEntity.class, roID);
            supplier = new SupplierEntity(supplierName, contactNo, email, address, regionalOfficeEntity);
            country = em.find(CountryEntity.class, countryId);
            supplier.setCountry(country);
            em.persist(supplier);
            em.flush();
            em.refresh(supplier);
            regionalOfficeEntity.getSuppliers().add(supplier);
            em.merge(regionalOfficeEntity);
            return true;
        } catch (Exception ex) {
            System.out.println("Failed to add supplier: " + ex);
            return false;
        }
    }

    @Override
    public boolean deleteSupplier(Long id) {
        System.out.println("deleteSupplier() called.");
        try {
            if (checkSupplierExists(id)) {
                supplier = em.getReference(SupplierEntity.class, id);
                supplier.setIsDeleted(true);
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to delete supplier: " + ex);
            return false;
        }
    }

    @Override
    public List<SupplierEntity> viewAllSupplierList() {
        System.out.println("viewAllSupplierList() called.");
        try {
            Query q = em.createQuery("Select s from SupplierEntity s where s.isDeleted=false");
            List<SupplierEntity> list = q.getResultList();
            return list;
        } catch (Exception ex) {
            System.out.println("Failed to viewAllSupplierList: " + ex);
            return null;
        }
    }

    @Override
    public SupplierEntity getSupplier(Long id) {
        System.out.println("getSupplier() called.");
        try {
            if (checkSupplierExists(id)) {
                return findASupplier(id);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Failed to viewInactiveSupplierList: " + ex);
            return null;
        }
    }

    @Override
    public boolean checkSupplierExists(String supplierName) {
        System.out.println("checkSupplierExists() called.");
        try {
            Query q = em.createQuery("Select s from SupplierEntity s where s.supplierName=:supplierName and s.isDeleted=false");
            q.setParameter("supplierName", supplierName);
            q.getSingleResult();
            return true;
        } catch (NoResultException n) {
            System.out.println("\nServer return no result:\n" + n);
            return false;
        } catch (Exception ex) {
            System.out.println("\nServer failed to perform checkSupplierExists:\n" + ex);
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
    public boolean editSupplier(Long supplierId, String name, String phone, String email, String address, Long countryId) {
        System.out.println("editSupplier() called.");
        try {
            if (checkSupplierExists(supplierId)) {
                supplier = em.getReference(SupplierEntity.class, supplierId);
                supplier.setSupplierName(name);
                supplier.setContactNo(phone);
                supplier.setEmail(email);
                supplier.setAddress(address);
                supplier.setCountry(em.getReference(CountryEntity.class, countryId));
                em.merge(supplier);
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.out.println("Failed to edit supplier: " + ex);
            return false;
        }
    }

    @Override
    public List<Supplier_ItemEntity> getSupplierItemList(Long supplierID) {
        System.out.println("getSupplierItemList() called");
        try {
            Query q = em.createQuery("Select s from Supplier_ItemEntity s where s.supplier.id =:supplierID and s.isDeleted=false ORDER BY s.item.SKU");
            q.setParameter("supplierID", supplierID);
            System.out.println("getSupplierItemList(): Successful");
            return q.getResultList();
//            List<Supplier_ItemEntity> supplier_ItemEntities = new ArrayList<>();
//            SupplierEntity supplierEntity = em.getReference(SupplierEntity.class, supplierID);

//            ItemSupplierHelper itemSupplierHelper = new ItemSupplierHelper();
//                List<ItemEntity> listOfItemEntities = getItemSuppliedBySupplier(supplierEntity.getId());
//                System.out.println("Retrieving line items of storage bin id " + supplierEntity.getId() + "...");
//                
//                if (listOfItemEntities != null && listOfItemEntities.size() > 0) {
//                    System.out.println("getItemList(): Size of listOfLineItemEntities inside the supplier id " + supplierEntity.getId() + ": " + listOfItemEntities.size());
//                    for (int i = 0; i < listOfItemEntities.size(); i++) {
//                        itemSupplierHelper = new ItemSupplierHelper();
//                        itemSupplierHelper.setItemID(listOfItemEntities.get(i).getId());
//                        itemSupplierHelper.setSKU(listOfItemEntities.get(i).getSKU());
//                        itemSupplierHelper.setItemName(listOfItemEntities.get(i).getName());
//                        itemSupplierHelper.setSupplierID(supplierEntity.getId());
//                        itemSupplierHelper.setItemPrice(listOfItemEntities.get(i).getCostPrice());
//                        itemSupplierHelper.setItemType(listOfItemEntities.get(i).getType());
//                        itemSupplierHelperList.add(itemSupplierHelper);
//                    }
//                }           
        } catch (EntityNotFoundException ex) {
            System.out.println("Supplier could not be found.");
            return null;
        } catch (Exception ex) {
            System.out.println("System failed to getSupplierItemList()");
            ex.printStackTrace();
            return null;
        }
    }

//    private List<ItemEntity> getItemSuppliedBySupplier(Long supplierID) {
//        System.out.println("getItemSuppliedBySupplier() called");
//        try {
//            em.flush();
//            SupplierEntity supplierEntity = em.getReference(SupplierEntity.class, supplierID);
//            List<ItemEntity> listOfItems = supplierEntity.getSupplyingItems();
//            if (listOfItems == null || listOfItems.size() == 0) {
//                System.out.println("No items");
//                return null;
//            } else {
//                System.out.println("Returned list of items");
//                return listOfItems;
//            }
//        } catch (Exception ex) {
//            System.out.println("Failed to getItemSuppliedBySupplier()");
//            ex.printStackTrace();
//            return null;
//        }
//    }
    public List<SupplierEntity> getSupplierListOfRO(Long roID) {
        System.out.println("getSupplierListByRO() called");
        try {
            Query q= em.createQuery("SELECT s from SupplierEntity s where s.regionalOffice.id=:roID and s.isDeleted=false");
            q.setParameter("roID", roID);
            return q.getResultList();
        } catch (Exception ex) {
            System.out.println("getSupplierListByRO(): Failed");
            ex.printStackTrace();
            return new ArrayList<SupplierEntity>();
        }
    }
}
