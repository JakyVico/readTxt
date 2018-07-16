/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.leertxt;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaqueline
 */
public class ReadTxt {

    /**
     * @param args the command line arguments
     */
    private static SimpleDateFormat miformato;
    private static String NombreFichero;
    private  static String nombreRep;

    public static void main(String[] args) throws FileNotFoundException, IOException {

        miformato = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date Ahora = new Date();
        NombreFichero = miformato.format(Ahora);
        

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith("txt");
            }
        };

        File f = new File("C:/Users/VS1XFI7/Desktop/JAQUE/TXTAnalisis/origen");
        String[] fileList = f.list(filter);
        for (int i = 0; i < fileList.length; i++) {

            String nombArch = fileList[i];
            nombreRep= fileList[i];
            System.out.println(nombArch + "nombre arfchivo");
            BufferedReader br = new BufferedReader(new FileReader("C:/Users/VS1XFI7/Desktop/JAQUE/TXTAnalisis/origen/" + nombArch));
            String s1 = br.readLine();
            //System.out.println(s1 + i);
           // List<String> row=generaLista();
            
            while ((s1 = br.readLine()) != null) {
                List<String> registros2=generaLista();
                if (s1.trim().startsWith("R0")) {
                    //System.out.println(s1 + "aaaaaaaaa");
                    String region,ciclo,fecha;
                    region=s1.substring(0, 3);
                    ciclo=s1.substring(17, 19);
                    fecha=s1.substring(43, 52);            
                    System.out.println(region+"\t"+ciclo+"\t"+fecha);
                    escibeNuevoArch(s1);
                   // row.add(region);
                    //row.add(ciclo);
                    //row.add(fecha);
                    
                    //guardarBD();
                }else if (s1.trim().startsWith("REPORTE DE AJUSTES")) {
                    //System.out.println(s1);
                    String reporte;
                    //este si se puede dividir por split con espacios .... 
                   reporte=s1.substring(43,48);
                    System.out.println(reporte);
                    escibeNuevoArch(s1);
                    //row.add(reporte);
                    
                } else if (s1.trim().startsWith("TOTAL CATEGORIA:")) {
                   // System.out.println(s1 + "aaaaaaaaa");
                   String totalregion,totalName,totalReg1,totalReg2;
                   totalregion=s1.substring(17, 23);
                    totalName=s1.substring(24, 40);
                    totalReg1=s1.substring(43, 52);  
                    totalReg2=s1.substring(43, 52);
                    System.out.println(totalregion+" "+totalName+" "+totalReg1+" "+totalReg2);
                    escibeNuevoArch(s1);
                    //row.add(totalregion);
                    //row.add(totalName);
                    //row.add(totalReg1);
                   // row.add(totalReg2);
                    
                }  else {
                   // System.out.println(s1 + i);
                }
                
         // System.out.println("registros"+row);
            }

        }

    }

    private static void escibeNuevoArch(String s1) {

        //System.out.println(s1 + "AAAA");
        PrintWriter pw = null;
        FileWriter fichero = null;
        try {
            fichero = new FileWriter("C:/Users/VS1XFI7/Desktop/JAQUE/TXTAnalisis/destino/" + NombreFichero + nombreRep.substring(0, 3)+".txt", true);
  
            pw = new PrintWriter(fichero);
            pw.println(s1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    private static void guardarBD() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
    
    public static List<String> generaLista() {
        return new ArrayList<String>();
    }

}
