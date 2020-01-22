
package SCM.SupplierManagement;

import EntityManager.CountryEntity;
import EntityManager.SupplierEntity;
import EntityManager.Supplier_ItemEntity;
import java.util.List;
import javax.ejb.Remote;


@Remote
public interface SupplierManagementBeanRemote {
    public Boolean addSupplier(String supplierName, String contactNo, String email, String address, Long countryId, Long roID);
    public boolean deleteSupplier(Long id);//if supplier exists call em.remove(supplier) else returns false
    public boolean editSupplier(Long supplierId, String name, String phone, String email, String address, Long countryId);//merge the SupplierEntity if exists else returns false
    public SupplierEntity getSupplier(Long id);//returns a SupplierEntity else returns null
    public List<SupplierEntity> viewAllSupplierList();
    public boolean checkSupplierExists(String supplierName);
    public List<CountryEntity> getListOfCountries();
    public List<Supplier_ItemEntity> getSupplierItemList(Long supplierID);
    public List<SupplierEntity> getSupplierListOfRO(Long roID);
}
