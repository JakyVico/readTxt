/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.leertxt;

import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author jaqueline
 */
public class ConexionBD {

    public boolean insert(String region1, String ciclo1, String fechaCorte1, String nombreCategoria1, String nombreReporte1, String categoria1, String importe1, String registros1, String tipo1) {

        System.out.println(region1 + ciclo1 + fechaCorte1 + nombreCategoria1 + nombreReporte1 + categoria1 + importe1 + registros1 + tipo1);
        Connection con = null;
        Statement stmt = null;
        String usr = "postpago";
        String pass = "postpago";

        region1 = region1.trim();
        ciclo1 = ciclo1.trim();
        String anio = fechaCorte1.substring(6, 8);
        fechaCorte1 = fechaCorte1.trim().substring(0, 6) + "20" + anio;
        nombreCategoria1 = nombreCategoria1.trim();
        nombreReporte1 = nombreReporte1.trim();
        categoria1 = categoria1.trim();
        importe1 = importe1.trim().replace(",", "");
        if (registros1.isEmpty()) {
            registros1 = "''";
        }
        registros1 = registros1.trim().replace(",", "");
        tipo1 = tipo1.trim();

        String query = "INSERT INTO METRICAS_AJUSTE\n"
                + "(REGION,CICLO,FECHA_CORTE,NOMBRE_REPORTE,CATEGORIA,NOMBRE_CAT,REGISTOS,IMPORTE,TIPO)\n"
                + "VALUES('R0" + region1 + "','" + ciclo1 + "','" + fechaCorte1 + "','" + nombreReporte1 + "'," + categoria1 + ",'" + nombreCategoria1 + "','" + registros1 + "','" + importe1 + "'," + tipo1 + ")";
        //System.out.println(query);

        /*try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@10.119.142.84:1521:datamart", usr, pass);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stmt = con.createStatement();
            /* ResultSet rs = stmt.executeQuery(query);
           while(rs.next())
                {
                    System.out.println("lal");
                }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return true;
    }

}
