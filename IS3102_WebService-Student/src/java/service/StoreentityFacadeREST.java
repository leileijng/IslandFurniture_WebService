package service;

import Entity.Storeentity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("entity.storeentity")
public class StoreentityFacadeREST extends AbstractFacade<Storeentity> {

    @PersistenceContext(unitName = "WebService")
    private EntityManager em;

    public StoreentityFacadeREST() {
        super(Storeentity.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Storeentity entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Storeentity entity) {
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
    public Storeentity find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Path("stores")
    @Produces({"application/json"})
    public List<Storeentity> listAllStores() {
        Query q = em.createQuery("Select s from Storeentity s where s.isdeleted=FALSE and s.countryId.name='Singapore'");
        List<Storeentity> list = q.getResultList();
        for (Storeentity s : list) {
            em.detach(s);
            s.setCountryId(null);
            s.setRegionalofficeId(null);
            s.setWarehouseId(null);
        }
        List<Storeentity> list2 = new ArrayList();
        list2.add(list.get(0));
        return list;
    }

    //get the item quantity based on the storeID
    //this function is used by ECommerce_StockAvailability servlet
    @GET
    @Path("getQuantity")
    @Produces({"application/json"})
    public Response getItemQuantityOfStore(@QueryParam("storeID") Long storeID, @QueryParam("SKU") String SKU) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "SELECT sum(l.QUANTITY) as sum FROM storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID and l.ITEM_ID=i.ID and s.ID=? and i.SKU=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, storeID);
            ps.setString(2, SKU);
            ResultSet rs = ps.executeQuery();
            int qty = 0;
            if (rs.next()) {
                qty = rs.getInt("sum");
            }

            return Response.ok(qty + "", MediaType.APPLICATION_JSON).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
