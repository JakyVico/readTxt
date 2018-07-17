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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class SftpController {

    private static final String USERNAME = "postpago";
    private static final String HOST = "10.119.142.84";
    private static final int PORT = 22;
    private static final String PASSWORD = "vs10e3e1";
    private static final String PATHORIGEN = "/postpago/ftpfile/MetricasAjustes";
    private static final String PATHDESTINO = "/postpago/procesos/Metricas/Ajustes";

    public static void main(String[] args) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String fileName) {
                return fileName.endsWith("txt");
            }
        };

        JSch jsch = new JSch();
        try {
            Session session = jsch.getSession(USERNAME,HOST,PORT);
            session.setConfig("PreferredAuthentications", "password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PASSWORD);
            session.connect();
            Channel channel = session.openChannel("sftp");
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.connect();
            System.out.println("Conectado en " + sftp.pwd());
            sftp.cd(PATHORIGEN);
            System.out.println(sftp.pwd()+"");
            //Listar archivos
            Vector filelist = sftp.ls("*.txt");
            System.out.println("Analizare "+ filelist.size()+" archivos");
            for(int i=0; i<filelist.size();i++){
                
                System.out.println("Archivo " +(i+1));
                System.out.println("--------------------------------------------------");
                
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) filelist.get(i);
                
                //BufferedReader br = new BufferedReader(new FileReader(entry.getFilename()));
                BufferedReader br = new BufferedReader(new InputStreamReader(sftp.get(entry.getFilename())));
                
               
                String s1 =null;
                while ((s1 = br.readLine()) != null) {
                 
                    List<String> registros2 =new ArrayList<String>();
                   // s1=s1.replaceAll(" ",""); //Quita espacios
                   s1=s1.trim();
                    if (s1.startsWith("R0")) {
                        s1=s1.replaceAll(" ","");
                        //System.out.println(s1+"\n");
                        int indice0=s1.indexOf("R0")+"R0".length();
                        int indiceI=s1.indexOf("CICLO-");
                        int indiceF=s1.indexOf("CICLO-")+"CICLO-".length();
                        int indice2I=s1.indexOf("FECHADECORTE:");
                        int indice2F=s1.indexOf("FECHADECORTE:")+"FECHADECORTE:".length();
                       
                        String region, ciclo, fecha;
                       
                        region = s1.substring(indice0, indiceI);
                        ciclo = s1.substring(indiceF ,indice2I);
                        fecha = s1.substring(indice2F,indice2F+8);
                        System.err.println(region + "\t" + ciclo + "\t" + fecha+"\n");

                        //escibeNuevoArch(s1);


                        //guardarBD();
                    } else if (s1.startsWith("REPORTE DE AJUSTES FACTURADOS")) {
                        s1=s1.replaceAll(" ","");
                        int indiceF=s1.indexOf("REPORTEDEAJUSTESFACTURADOS")+"REPORTEDEAJUSTESFACTURADOS".length();
                        int indiceC=s1.indexOf("PORCICLO");
                        String reporte=s1.substring(indiceF,indiceC);
                        System.err.println(reporte+"\n");
                        //escibeNuevoArch(s1);

                    } else if (s1.startsWith("TOTAL CATEGORIA:")) {
                         //System.out.println(s1+"\n");
                        s1=s1.trim();
                        int indiceF=s1.indexOf("TOTAL CATEGORIA:")+"TOTAL CATEGORIA:".length();
                        String totalregion, totalName, totalReg1, totalReg2;
                        totalregion = s1.substring(indiceF, indiceF+4);
                        totalName = s1.substring(indiceF+5, indiceF+24);
                        totalReg1 = s1.substring(indiceF+25,indiceF+38);
                        totalReg2 = s1.substring(indiceF+39, s1.length());
                        System.err.println("|"+totalregion+"|" + "\t\t" + "|"+totalName+"|" + "\t\t" +"|"+ totalReg1+"|" + "\t\t" +"|"+ totalReg2);
                        //escibeNuevoArch(s1);
                        


                    } else {
                        // System.out.println(s1 + i);
                    }

                    // System.out.println("registros"+row);
                }
            }

            //Copiar archivos a otra ubicacion
            /*System.out.println("Me movi a " + sftp.pwd());
            sftp.rename("prueba.txt",PATHDESTINO+"copiaPrueba2.txt");
            sftp.cd("../test2");
            System.out.println("Archivo copiado");
            sftp.pwd();*/
        } catch (JSchException e) {
            System.out.println("No se pudo realizar la conexión");
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
