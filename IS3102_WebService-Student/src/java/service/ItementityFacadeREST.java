package service;

import Entity.Furniture;
import Entity.ItemCountryentity;
import Entity.Itementity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Stateless
@Path("entity.itementity")
public class ItementityFacadeREST extends AbstractFacade<Itementity> {

    @PersistenceContext(unitName = "WebService")
    private EntityManager em;

    public ItementityFacadeREST() {
        super(Itementity.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Itementity entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Itementity entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Itementity find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Path("items")
    @Produces({"application/json"})
    public List<Itementity> listAllItemsByCountry(@QueryParam("country") String country) {
        Query q = em.createQuery("Select c from ItemCountryentity c,Countryentity co where c.countryId.id=co.id and c.countryId.name=:country and c.isdeleted=false");
        q.setParameter("country", country);
        List<ItemCountryentity> list = q.getResultList();
        List<Itementity> itemList = new ArrayList();
        for (ItemCountryentity itemCountry : list) {
            Itementity item = itemCountry.getItemId();
            if (!item.getIsdeleted()) {
                em.detach(item);
                item.setFurnitureentity(null);
                item.setItemCountryentityList(null);
                item.setLineitementityList(null);
                item.setRetailproductentity(null);
                item.setWarehousesId(null);
                item.setWishlistentityList(null);
                itemList.add(item);
            }
        }
        return itemList;
    }

    @GET
    @Path("itemname")
    @Produces({"application/json"})
    public String getItemNameBySKU(@QueryParam("SKU") String SKU) {
        try {
            Query q = em.createQuery("Select i from Itementity i where i.sku=:SKU and i.isdeleted=false");
            q.setParameter("SKU", SKU);
            Itementity item = (Itementity) q.getSingleResult();
            return item.getName();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    //Retrieve an item based on SKU
    //this function is not used
    @GET
    @Path("item")
    @Produces({"application/json"})
    public Response getItemBySKU(@QueryParam("SKU") String SKU) {
        try {
            Query q = em.createQuery("Select i from Itementity i where i.sku=:SKU and i.isdeleted=false");
            q.setParameter("SKU", SKU);
            Itementity item = (Itementity) q.getSingleResult();
            return Response
                    .status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Max-Age", "1209600")
                    .entity(item)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("furniture")
    @Produces({"application/json"})
    public List<Furniture> listAllFurnitureByCountry(@QueryParam("country") String country) {
        Query q = em.createQuery("Select c from ItemCountryentity c,Countryentity co where c.countryId.id=co.id and co.name=:country and c.isdeleted=false");

        q.setParameter("country", country);
        List<ItemCountryentity> list = q.getResultList();
        List<Furniture> furnitureList = new ArrayList();
        System.out.println("Country is " + country);
        System.out.println("Number of furniture retreived: " + list.size());
        for (ItemCountryentity itemCountry : list) {
            Itementity item = itemCountry.getItemId();
            if (!item.getIsdeleted() && item.getType().equals("Furniture")) {
                Furniture furniture = new Furniture();
                furniture.setId(item.getId());
                furniture.setName(item.getName());
                furniture.setDescription(item.getDescription());
                furniture.setImageUrl(item.getFurnitureentity().getImageurl());
                furniture.setSKU(item.getSku());
                furniture.setType(item.getType());
                furniture.setLength(item.getLength());
                furniture.setWidth(item.getWidth());
                furniture.setHeight(item.getHeight());
                furnitureList.add(furniture);
            }
        }
        return furnitureList;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
