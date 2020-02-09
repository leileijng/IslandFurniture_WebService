package service;

import Entity.Member;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ECommerce
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    @POST
    @Path("createEcommerceTransactionRecord")
    @Consumes({"application/json"})
    public Response createEcommerceTransactionRecord(@QueryParam("memberID") Long memberID, @QueryParam("amountPaid") double amountPaid,
            @QueryParam("currency") String currency) {

        System.out.println("memberId: " + memberID);
        System.out.println("amount: " + amountPaid);
        System.out.println("currency:" + currency);
        System.out.println("createEcommerceTransactionRecord called");
        //Prepare data to insert
        //For create date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);

        //Insert the record to db
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");
            //syntax tried out in mysql, which successful
            //INSERT INTO salesrecordentity (AMOUNTDUE, AMOUNTPAID, AMOUNTPAIDUSINGPOINTS,CREATEDDATE,CURRENCY,LOYALTYPOINTSDEDUCTED,POSNAME,SERVEDBYSTAFF,MEMBER_ID,STORE_ID) VALUES (12,12, 0,'2020-01-11 12:08:01','SGD',0,' Counter 1','Cashier 1',23,59)
            String stmt = "INSERT INTO salesrecordentity (AMOUNTDUE, AMOUNTPAID, AMOUNTPAIDUSINGPOINTS,CREATEDDATE,CURRENCY,LOYALTYPOINTSDEDUCTED,POSNAME,SERVEDBYSTAFF,MEMBER_ID,STORE_ID) VALUES (?,?,0,?,?,0,' Counter 1','Cashier 1',?,59)";

            System.out.println(amountPaid + "," + currentDate + "," + currency + "," + memberID);
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, amountPaid);
            ps.setDouble(2, amountPaid);
            ps.setString(3, currentDate);
            ps.setString(4, currency);
            ps.setLong(5, memberID);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Result Set: " + rs.toString());
                int generatedKey = rs.getInt(1);
                String id = String.valueOf(generatedKey);
                System.out.println("inserted record id: " + id);
                conn.close();
                return Response.status(201).header("getSOnumber", id).build();
            } else {
                conn.close();
                return Response.status(404).build();
            }

        } catch (Exception ex) {
            System.out.println("ERROR:" + ex.getMessage());
            return Response.status(404).build();
        }
    }

    @POST
    @Path("createEcommerceLineItemRecord")
    @Consumes({"application/json"})
    public Response createEcommerceLineItemRecord(@QueryParam("itemEntityID") int itemID, @QueryParam("quantity") int quantity, @QueryParam("salesRecordID") Long salesId) {

        int newlineItemId;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            //insert new record to lineitem table
            //new outbound record
            String insertIntoLineItem = "insert into lineitementity(QUANTITY,ITEM_ID) values(?,?);";
            PreparedStatement ps = conn.prepareStatement(insertIntoLineItem, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, quantity);
            ps.setDouble(2, itemID);
            ps.executeUpdate();

            ResultSet rs1 = ps.getGeneratedKeys();
            if (rs1.next()) {
                newlineItemId = rs1.getInt(1);
                System.out.println("Generated Line item id is: " + newlineItemId);
                System.out.println("Insert into Line_item entity successfully");
            } else {
                conn.close();
                return Response.status(404).build();
            }

            //insert new record to storagebinlineitem database
            //link the lineitem info to storage bin (outbond)
            String insertStorageBinLineItem = "insert into storagebinentity_lineitementity values(21,?);";
            ps = conn.prepareStatement(insertStorageBinLineItem);
            ps.setInt(1, newlineItemId);
            int rs2 = ps.executeUpdate();

            if (rs2 > 0) {
                System.out.println("Insert into storagebinentity_lineitementity successfully");
            } else {
                conn.close();
                return Response.status(404).build();
            }

            //insert record to salesrecordentity_lineitementity table, link salesOrder and line item together 
            String insertSL = "insert into salesrecordentity_lineitementity values(?,?);";
            ps = conn.prepareStatement(insertSL);
            ps.setInt(1, salesId.intValue());
            ps.setInt(2, newlineItemId);
            int rsinsertSL = ps.executeUpdate();
            if (rsinsertSL > 0) {
                System.out.println("Insert into salesrecordentity_lineitementity successfully");
            } else {
                conn.close();
                return Response.status(404).build();
            }

            //update lineitem quantity (deduct the quantity)
            //first get the id of lineitem, that need to be updated
            List<Integer> lineItemsTobeUpdateIDs = new ArrayList<Integer>();
            int stock = 0;
            int lastrecordQuantityToDeduct = quantity;
            String getLineItemId = "SELECT l.* FROM "
                    + "storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i "
                    + "where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID "
                    + "and l.ITEM_ID=i.ID and s.ID=10001 and i.id=? order by l.quantity desc;";

            ps = conn.prepareStatement(getLineItemId);
            ps.setInt(1, itemID);
            ResultSet rsLineItemtoUpdate = ps.executeQuery();
            while (rsLineItemtoUpdate.next()) {
                lastrecordQuantityToDeduct = quantity - stock;
                stock += rsLineItemtoUpdate.getInt("QUANTITY");
                lineItemsTobeUpdateIDs.add(rsLineItemtoUpdate.getInt("ID"));
                //the first record can satisfy the deduction request
                if (quantity <= stock) {
                    break;
                }
            }

            //only need to update 1 record
            if (lineItemsTobeUpdateIDs.size() == 1) {
                //update line item quantity
                String updateLineItem = "Update lineitementity set lineitementity.QUANTITY = QUANTITY - ? where lineitementity.id = ?";
                ps = conn.prepareStatement(updateLineItem);
                ps.setInt(1, quantity);
                ps.setInt(2, lineItemsTobeUpdateIDs.get(0));
                int rsupdateLineItem = ps.executeUpdate();
                if (rsupdateLineItem > 0) {

                    System.out.println("Update lineItem quantity successfully");
                } else {
                    conn.close();
                    return Response.status(404).build();
                }
            } else if (lineItemsTobeUpdateIDs.size() > 1) {
                for (int i = 0; i < lineItemsTobeUpdateIDs.size() - 1; i++) {
                    String updateLineItem1 = "Update lineitementity set lineitementity.QUANTITY = 0 where lineitementity.id = ?";
                    ps = conn.prepareStatement(updateLineItem1);
                    ps.setInt(1, lineItemsTobeUpdateIDs.get(i));
                    int rsupdateLineItem1 = ps.executeUpdate();
                    if (rsupdateLineItem1 > 0) {
                        System.out.println("Update lineItem quantity successfully 1");
                    } else {
                        conn.close();
                        return Response.status(404).build();
                    }
                }

                String updateLineItem2 = "Update lineitementity set lineitementity.QUANTITY = QUANTITY - ? where lineitementity.id = ?";
                ps = conn.prepareStatement(updateLineItem2);
                ps.setInt(1, lastrecordQuantityToDeduct);
                ps.setInt(2, lineItemsTobeUpdateIDs.get(lineItemsTobeUpdateIDs.size() - 1));
                int rsupdateLineItem1 = ps.executeUpdate();
                if (rsupdateLineItem1 > 0) {
                    System.out.println("Update lineItem quantity successfully 2");
                } else {
                    conn.close();
                    return Response.status(404).build();
                }
            } else {
                conn.close();
                return Response.status(404).build();
            }

            
            //update storagebinenity table, (add free volume for warehouse)
            String upadtestoragebinentityAdd = "Update storagebinentity set storagebinentity.freeVolume = freeVolume + ? where id = 20;";
            ps = conn.prepareStatement(upadtestoragebinentityAdd);
            ps.setInt(1, quantity);
            int rsAddFreeVolume = ps.executeUpdate();

            if (rsAddFreeVolume > 0) {
                System.out.println("Update storagebinentity addd volume successfully");
            } else {
                conn.close();
                return Response.status(404).build();
            }
            
            //update storagebinentity table, (deduct free volume)
            String upadtestoragebinentity = "Update storagebinentity set storagebinentity.freeVolume = freeVolume - ? where id = 9;";
            ps = conn.prepareStatement(upadtestoragebinentity);
            ps.setInt(1, quantity);
            int rs3 = ps.executeUpdate();

            if (rs3 > 0) {
                conn.close();
                System.out.println("Update storagebinentity successfully");
                return Response.status(201).build();
            } else {
                conn.close();
                return Response.status(404).build();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
