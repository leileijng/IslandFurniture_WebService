/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanesssa Jiang Lei
 */
public class Salesrecordentity_lineitementityDB {

    public Salesrecordentity_lineitementityDB() {
    }

    public boolean insertNewRecord(Long salesId, int newlineItemId) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");

            String insertSL = "insert into salesrecordentity_lineitementity values(?,?);";
            PreparedStatement ps = conn.prepareStatement(insertSL);
            ps.setInt(1, salesId.intValue());
            ps.setInt(2, newlineItemId);
            int rsinsertSL = ps.executeUpdate();
            if (rsinsertSL > 0) {
                System.out.println("Insert into salesrecordentity_lineitementity successfully");
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Salesrecordentity_lineitementityDB" + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
}
