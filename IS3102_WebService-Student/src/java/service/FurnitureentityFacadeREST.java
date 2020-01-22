package service;

import Entity.Furniture;
import Entity.Furnitureentity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

@Stateless
@Path("entity.furnitureentity")
public class FurnitureentityFacadeREST extends AbstractFacade<Furnitureentity> {

    @PersistenceContext(unitName = "WebService")
    private EntityManager em;

    public FurnitureentityFacadeREST() {
        super(Furnitureentity.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Furnitureentity entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Long id, Furnitureentity entity) {
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
    public Furnitureentity find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Furnitureentity> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Furnitureentity> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    //Display a list of all the furniture
    //this function is not used in the student's copy, can be found in the ECommerce_AllFurnituresServlet of Lecturer's copy
    @GET
    @Path("getFurnitureList")
    @Produces("application/json")
    public Response getFurnitureList(@QueryParam("countryID") Long countryID) {
        System.out.println("RESTful: getFurnitureList() called with countryID " + countryID);
        try {
            List<Furniture> list = new ArrayList<>();
            String stmt = "";
            PreparedStatement ps;
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");

            if (countryID == null) {
                stmt = "SELECT i.ID as id, i.NAME as name, f.IMAGEURL as imageURL, i.SKU as sku, i.DESCRIPTION as description, i.TYPE as type, i._LENGTH as length, i.WIDTH as width, i.HEIGHT as height, i.CATEGORY as category FROM itementity i, furnitureentity f where i.ID=f.ID and i.ISDELETED=FALSE;";
                ps = conn.prepareStatement(stmt);
            } else {
                stmt = "SELECT i.ID as id, i.NAME as name, f.IMAGEURL as imageURL, i.SKU as sku, i.DESCRIPTION as description, i.TYPE as type, i._LENGTH as length, i.WIDTH as width, i.HEIGHT as height, i.CATEGORY as category, ic.RETAILPRICE as price FROM itementity i, furnitureentity f, item_countryentity ic where i.ID=f.ID and i.ID=ic.ITEM_ID and i.ISDELETED=FALSE and ic.COUNTRY_ID=?;";
                ps = conn.prepareStatement(stmt);
                ps.setLong(1, countryID);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Furniture f = new Furniture();
                f.setId(rs.getLong("id"));
                f.setName(rs.getString("name"));
                f.setImageUrl(rs.getString("imageURL"));
                f.setSKU(rs.getString("sku"));
                f.setDescription(rs.getString("description"));
                f.setType(rs.getString("type"));
                f.setWidth(rs.getInt("width"));
                f.setHeight(rs.getInt("height"));
                f.setLength(rs.getInt("length"));
                f.setCategory(rs.getString("category"));
                if (countryID != null) {
                    f.setPrice(rs.getDouble("price"));
                }
                list.add(f);
            }
            GenericEntity<List<Furniture>> entity = new GenericEntity<List<Furniture>>(list) {
            };
            return Response
                    .status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Max-Age", "1209600")
                    .entity(entity)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    //#viewfurniturebycategorytask4a - retrieve a list of furniture by category
    //this function is used in the FurnitureCategoryServlet
    @GET
    @Path("getFurnitureListByCategory")
    @Produces("application/json")
    public Response getFurnitureListByCategory(@QueryParam("countryID") Long countryID, @QueryParam("category") String category) {
        System.out.println("RESTful: getFurnitureListByCategory() called with countryID " + countryID + " and category " + category);

        try {
            List<Furniture> list = new ArrayList<>();
            String stmt = "";
            PreparedStatement ps;
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");

            if (countryID == null) {
                stmt = "SELECT i.ID as id, i.NAME as name, f.IMAGEURL as imageURL, i.SKU as sku, i.DESCRIPTION as description, i.TYPE as type, i._LENGTH as length, i.WIDTH as width, i.HEIGHT as height, i.CATEGORY as category FROM itementity i, furnitureentity f where i.ID=f.ID and i.ISDELETED=FALSE and i.CATEGORY=?;";
                ps = conn.prepareStatement(stmt);
                ps.setString(1, category);
            } else {
                stmt = "SELECT i.ID as id, i.NAME as name, f.IMAGEURL as imageURL, i.SKU as sku, i.DESCRIPTION as description, i.TYPE as type, i._LENGTH as length, i.WIDTH as width, i.HEIGHT as height, i.CATEGORY as category, ic.RETAILPRICE as price FROM itementity i, furnitureentity f, item_countryentity ic where i.ID=f.ID and i.ID=ic.ITEM_ID and i.ISDELETED=FALSE and ic.COUNTRY_ID=? and i.CATEGORY=?;";
                ps = conn.prepareStatement(stmt);
                ps.setLong(1, countryID);
                ps.setString(2, category);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Furniture f = new Furniture();
                f.setId(rs.getLong("id"));
                f.setName(rs.getString("name"));
                f.setImageUrl(rs.getString("imageURL"));
                f.setSKU(rs.getString("sku"));
                f.setDescription(rs.getString("description"));
                f.setType(rs.getString("type"));
                f.setWidth(rs.getInt("width"));
                f.setHeight(rs.getInt("height"));
                f.setLength(rs.getInt("length"));
                f.setCategory(rs.getString("category"));
                if (countryID != null) {
                    f.setPrice(rs.getDouble("price"));
                }
                list.add(f);
            }
            GenericEntity<List<Furniture>> entity = new GenericEntity<List<Furniture>>(list) {
            };
            return Response
                    .status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Max-Age", "1209600")
                    .entity(entity)
                    .build();
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
