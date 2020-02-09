/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanesssa Jiang Lei
 */
public class LineitementityDB {

    public LineitementityDB() {
    }
    public int insertNewRecord(int quantity, int itemID) throws SQLException{
         try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            //insert new record to lineitem table
            //new outbound record
            String insertIntoLineItem = "insert into lineitementity(QUANTITY,ITEM_ID) values(?,?);";
            PreparedStatement ps = conn.prepareStatement(insertIntoLineItem, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, quantity);
            ps.setInt(2, itemID);
            ps.executeUpdate();

            ResultSet rs1 = ps.getGeneratedKeys();
            if (rs1.next()) {
                int newlineItemId = rs1.getInt(1);
                System.out.println("Generated Line item id is: " + newlineItemId);
                System.out.println("Insert into Line_item entity successfully");
                return newlineItemId;
            } else {
                conn.close();
                return 0;
            }
        } catch (Exception ex) {
            System.out.println("LineitementityDB" + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
}
    public boolean decreaseQty(int quantity, int itemID) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            List<Integer> lineItemsTobeUpdateIDs = new ArrayList<Integer>();
            int stock = 0;
            int lastrecordQuantityToDeduct = quantity;
            String getLineItemId = "SELECT l.* FROM "
                    + "storeentity s, warehouseentity w, storagebinentity sb, storagebinentity_lineitementity sbli, lineitementity l, itementity i "
                    + "where s.WAREHOUSE_ID=w.ID and w.ID=sb.WAREHOUSE_ID and sb.ID=sbli.StorageBinEntity_ID and sbli.lineItems_ID=l.ID "
                    + "and l.ITEM_ID=i.ID and s.ID=10001 and i.id=? order by l.quantity desc;";

            PreparedStatement ps = conn.prepareStatement(getLineItemId);
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
                    return false;
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
                        return false;
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
                    return false;
                }
            } else {
                conn.close();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    return true;
}
   

}
