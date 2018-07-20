/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.leertxt;

/**
 *
 * @author jaqueline
 */
import com.jcraft.jsch.*;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class SftpController {

    private static final String USERNAME = "servici1";
    private static final String HOST = "159.203.7.113";
    private static final int PORT = 2223;
    private static final String PASSWORD = "VFZDGNB34oq4";
    private static final String PATHORIGEN = "public_html/test";
    private static final String PATHDESTINO = "/public_html/test2/";
    private static SimpleDateFormat miformato;
    private static String NombreFichero;
    //private static String region, ciclo, fecha, nombreReporte, categoria, nombreCategoria, totalRegistos, totalImporte;
    private static String region, ciclo, fechaCorte, nombreCategoria, nombreReporte, categoria, importe, registros, tipo;

    public static void main(String[] args) {
        miformato = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        java.util.Date Ahora = new java.util.Date();
        NombreFichero = miformato.format(Ahora);
        System.out.println("nombre ficharo"+NombreFichero);

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String fileName) {
                System.out.println(fileName.endsWith("txt") + "lalalalal");
                return fileName.endsWith("txt");
            }
        };

        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(USERNAME, HOST, PORT);
            session.setConfig("PreferredAuthentications", "password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PASSWORD);
            session.connect();
            Channel channel = session.openChannel("sftp");
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.connect();
            System.out.println("Conectado en " + sftp.pwd());
            sftp.cd(PATHORIGEN);
            //System.out.println(sftp.pwd() + "eSTOY EN:::::: ");
            //Listar archivos
            Vector filelist = sftp.ls("*.txt");
            System.out.println("Analizare " + filelist.size() + " archivos");
           

          

            System.out.println("Me movi a " + sftp.pwd());
            
            for (int i = 0; i < filelist.size(); i++) {

                System.out.println("Archivo " + (i + 1));
                System.out.println("--------------------------------------------------");
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) filelist.get(i);
                //BufferedReader br = new BufferedReader(new FileReader(entry.getFilename()));
                BufferedReader br = new BufferedReader(new InputStreamReader(sftp.get(entry.getFilename())));
                String nameArch=entry.getFilename();
                System.out.println("lalal"+entry.getFilename());
                String s1 = null;
                while ((s1 = br.readLine()) != null) {

                    List<String> registros2 = new ArrayList<String>();
                    // s1=s1.replaceAll(" ",""); //Quita espacios
                    s1 = s1.trim();
                    if (s1.startsWith("R0")) {
                        s1 = s1.replaceAll(" ", "");
                        int indice0 = s1.indexOf("R0") + "R0".length();
                        int indiceI = s1.indexOf("CICLO-");
                        int indiceF = s1.indexOf("CICLO-") + "CICLO-".length();
                        int indice2I = s1.indexOf("FECHADECORTE:");
                        int indice2F = s1.indexOf("FECHADECORTE:") + "FECHADECORTE:".length();
                        region = s1.substring(indice0, indiceI);
                        ciclo = s1.substring(indiceF, indice2I);
                        fechaCorte = s1.substring(indice2F, indice2F + 8);
                        //fechaCorte.substring(0, 6);
                       //System.err.println(region + "\t" + ciclo + "\t" + fechaCorte + "\n");

                    } else if (s1.startsWith("REPORTE DE AJUSTES FACTURADOS")) {
                        s1 = s1.replaceAll(" ", "");
                        int indiceF = s1.indexOf("REPORTEDEAJUSTESFACTURADOS") + "REPORTEDEAJUSTESFACTURADOS".length();
                        int indiceC = s1.indexOf("PORCICLO");
                        nombreReporte = s1.substring(indiceF, indiceC);
                    
                    } else if (s1.startsWith("TOTAL CATEGORIA:")) {
                        s1 = s1.trim();
                        int indiceF = s1.indexOf("TOTAL CATEGORIA:") + "TOTAL CATEGORIA:".length();
                        categoria = s1.substring(indiceF, indiceF + 4);
                        nombreCategoria = s1.substring(indiceF + 5, indiceF + 24);
                        registros = s1.substring(indiceF + 25, indiceF + 38);
                       importe= s1.substring(indiceF + 39, s1.length());                       
                        if(importe.trim().startsWith("-")){
                            tipo="0";
                        }else{
                            tipo="1";
                        }
                        // System.err.println("|" + categoria + "|" + "\t\t" + "|" + nombreCategoria + "|" + "\t\t" + "|" + registros + "|" + "\t\t" + "|" + importe+"|"+tipo);
                    }else if(s1.startsWith("*")){
                        nombreReporte=null;
                        categoria=null;
                    continue;
                }  else {
                        // System.out.println(s1 + i);
                      
                        continue;
                    }
                   if (nombreReporte!=null&&categoria!=null&&nombreCategoria!=null&&importe!=null) {
                        System.out.println(region + " " + ciclo + " " + fechaCorte + " " + nombreReporte + " " + categoria + " " + nombreCategoria + " " + registros + " " + importe + "-----");
                        //conexionBD(region, ciclo, fechaCorte, nombreCategoria,nombreReporte, categoria,importe, registros, tipo);
                    }
                }
            //Copiar archivos a otra ubicacion
           // System.out.println("Me movi a " + sftp.pwd());
           int count = 0;
                 
                System.out.println("kjkkjkj");
            
            sftp.rename(nameArch,PATHDESTINO+i+NombreFichero+".txt");
            System.out.println("Archivo copiado");
         //   sftp.pwd();
               
             continue;
             }

        } catch (JSchException e) {
            System.out.println("No se pudo realizar la conexión");
        } catch (SftpException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//String region, String ciclo, String fechaCorte, String nombreCategoria, String nombreReporte, double categoria, double importe, double registros, int tipo

    public static void conexionBD(String region1, String ciclo1, String fechaCorte1, String nombreCategoria1, String nombreReporte1, String categoria1, String importe1, String registros1, String tipo1) {

        //System.out.println(region1+ ciclo1+ fechaCorte1+ nombreCategoria1+ nombreReporte1+ categoria1+ importe1+registros1+ tipo1);
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

        try {
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
                }*/
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
        }

    }

}
//

