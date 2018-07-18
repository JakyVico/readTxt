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
             String query = "INSERT INTO METRICAS_AJUSTE\n"+
                    "(REGION,CICLO,FECHA_CORTE,NOMBRE_CAT,NOMBRE_REPORTE,CATEGORIA,IMPORTE,REGISTOS,TIPO)\n"+
                    "VALUES('R11','11','11/11/1111','prueba1','prueba1',12,1114,1145,0);" ;
            
            try
            {
                Class.forName("com.ibm.db2.jcc.DB2Driver");
               con = DriverManager.getConnection("jdbc:oracle:thin:@10.119.142.84:1521:datamart",usr, pass);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }  
            try
            {
             stmt = con.createStatement();
             //ResultSet rs = stmt.executeQuery(query);
 
            }
            catch(SQLException e)
            {
            }
                      
        }  

}
