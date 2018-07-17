/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.leertxt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VS1XFI7
 */
public class DAO {
    
    private static String PASS = "postpago";
    private static String USER  = "postpago";

    public static void conexionBD(){
            Connection con = null;
            Statement stmt = null;
            String usr = USER;
            String pass = PASS; 
        
            //String query = "" ;
            try
            {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
                con = DriverManager.getConnection("jdbc:db2://192.9.198.9:50000/DB2PROD", usr, pass);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }  
            try
            {
             stmt = con.createStatement();
            // ResultSet rs = stmt.executeQuery(query);
             List<ConexionBean>front2 = new ArrayList<ConexionBean>();
 
            }
            catch(SQLException e)
            {
            }
                      
        }  

}
