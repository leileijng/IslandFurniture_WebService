/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanesssa Jiang Lei
 */
public class StoreentityDB {

    public StoreentityDB() {
    }
        
     public String getStoreInfo(Long storeID) throws SQLException{
         try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345");
            String stmt = "SELECT * FROM storeentity s WHERE s.id=?";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.setLong(1, storeID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String info = "Store Name: ";
                info += rs.getString("NAME");
                info += "; Address: ";
                info += rs.getString("ADDRESS");
                info += " (";
                info += rs.getString("POSTALCODE");
                info += ") ";
                conn.close();
                return info;
            }else{
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
     
     }
    
}
