package service;

import DB.LineitementityDB;
import DB.SalesrecordentityDB;
import DB.Salesrecordentity_lineitementityDB;
import DB.StoragebinentityDB;
import DB.Storagebinentity_lineitementityDB;
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

        SalesrecordentityDB db = new SalesrecordentityDB();
        
        //Insert the record to db
        try {
            String id = db.insertNewRecord(memberID, amountPaid, currency, currentDate);
            if(id != null){
                return Response.status(201).header("getSOnumber", id).build();
            }
            else {
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

        //insert new record to lineitem table
        //new outbound record
        LineitementityDB lineitemDB = new LineitementityDB();
        int newlineItemId;
        try {
            newlineItemId = lineitemDB.insertNewRecord(quantity, itemID);
            if (newlineItemId == 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //insert new record to storagebinlineitem database
        //link the lineitem info to storage bin (outbond)
        Storagebinentity_lineitementityDB sliDB = new Storagebinentity_lineitementityDB();
        try {
            if(!sliDB.insertNewRecord(newlineItemId)){
                return Response.status(Response.Status.BAD_REQUEST).build();
            };
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //insert record to salesrecordentity_lineitementity table, link salesOrder and line item together 
        Salesrecordentity_lineitementityDB salesLineDB = new Salesrecordentity_lineitementityDB();
        try {
            if(!salesLineDB.insertNewRecord(salesId, newlineItemId)){
                return Response.status(Response.Status.BAD_REQUEST).build();
            };
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //update lineitem quantity (deduct the quantity)
        //first get the id of lineitem, that need to be updated
        try {
            if (!lineitemDB.decreaseQty(quantity, itemID)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        StoragebinentityDB sbDB = new StoragebinentityDB();
        //update storagebinenity table, (add free volume for warehouse)
        try {
            if (!sbDB.increaseFreeVolume(quantity)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        //update storagebinentity table, (deduct free volume)
        try {
            if (!sbDB.decreaseFreeVolume(quantity)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        return Response.status(Response.Status.OK).build();
    }
}
