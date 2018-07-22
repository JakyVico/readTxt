/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.leertxt;

import com.jcraft.jsch.ChannelSftp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaqueline
 */
public class ReadTxt {

    private static String ORIGEN = "/home/jaqueline/Desktop/archivos/origen/";
    private static String DESTINO = "/home/jaqueline/Desktop/archivos/destino/";
    private static int i = 0;
    private static String region, ciclo, fechaCorte, nombreCategoria, nombreReporte, categoria, importe, registros, tipo;
    private static ConexionBD conexionBD;
    
      int second=0;
    Timer timer= new Timer();
    TimerTask task =new TimerTask() {
        @Override
        public void run() {
            try {
                inicioArchivo();
            } catch (IOException ex) {
                Logger.getLogger(ReadTxt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    public void start(){
        timer.schedule(task,0 ,10000);
      
    }
    public static void main(String[] args)  {
        
      ReadTxt readTxt= new ReadTxt();
      readTxt.start();
              

    }
    
    public static void inicioArchivo() throws IOException{
          Files.walk(Paths.get(ORIGEN)).forEach(ruta -> {
            if (Files.isRegularFile(ruta)) {
                try {
                    Files.move(ruta, FileSystems.getDefault().getPath(DESTINO + ruta.toString().replace(ORIGEN, "")), StandardCopyOption.REPLACE_EXISTING);

                    i = i + 1;
                    System.out.println("Archivos a mover"+ruta);
                } catch (IOException ex) {
                    Logger.getLogger(ReadTxt.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Sin archivos");

            }

        });

        Files.walk(Paths.get(DESTINO)).forEach(ruta -> {
            if (Files.isRegularFile(ruta)) {
                try {

                    leerContenido(ruta.toString());

                } catch (IOException ex) {
                    Logger.getLogger(ReadTxt.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    private static void leerContenido(String pathDestino) throws FileNotFoundException, IOException {

        if (pathDestino.contains("-ANALIZADO")) {
            return;
        }
        BufferedReader br;

        br = new BufferedReader(new FileReader(pathDestino));

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
                importe = s1.substring(indiceF + 39, s1.length());
                if (importe.trim().startsWith("-")) {
                    tipo = "0";
                } else {
                    tipo = "1";
                }
                // System.err.println("|" + categoria + "|" + "\t\t" + "|" + nombreCategoria + "|" + "\t\t" + "|" + registros + "|" + "\t\t" + "|" + importe+"|"+tipo);
            } else if (s1.startsWith("*")) {
                nombreReporte = null;
                categoria = null;
                continue;
            } else {
                // System.out.println(s1 + i);
                continue;
            }
            if (nombreReporte != null && categoria != null && nombreCategoria != null && importe != null) {
                //System.out.println(region + " " + ciclo + " " + fechaCorte + " " + nombreReporte + " " + categoria + " " + nombreCategoria + " " + registros + " " + importe + "-----");
                //conexionBD(region, ciclo, fechaCorte, nombreCategoria,nombreReporte, categoria,importe, registros, tipo);
                conexionBD= new ConexionBD();
            conexionBD.insert(region, ciclo, fechaCorte, nombreCategoria, nombreReporte, categoria, importe, registros, tipo);
            }   
        }
        File file = new File(pathDestino);
        String nuevoNombre = pathDestino.replace(DESTINO, "").replace(".txt", "") + "-ANALIZADO";
        file.renameTo(new File(DESTINO + nuevoNombre + ".txt"));

    }

   
}
