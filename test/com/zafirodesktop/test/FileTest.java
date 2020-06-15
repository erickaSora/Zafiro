/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import com.google.gson.Gson;
import com.zafirodesktop.util.JSONErrorsLog;
import com.zafirodesktop.util.LogActions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Digitall
 */
public class FileTest {
    
    public static void main(String[] args) {
        LogActions logs = new LogActions();
        /*List<JSONErrorsLog> errorList = logs.readEntries();
        for(JSONErrorsLog error : errorList){
            System.out.println(error.toString());
        }*/
        logs.createEntry("500", "login", "No se pudo acceder a la base de datos", "connection refused", "");
    }
    
    public void createEntry(){
        String path = System.getProperty("user.dir")+"\\Scirebox_log.log";
        File logFile = new File(path);
        BufferedWriter bw;
        try {
            if(logFile.exists()){
                bw = new BufferedWriter(new FileWriter(logFile, true));
                bw.newLine();
                //Se crea el objeto con lo especificado
                JSONErrorsLog currentError = new JSONErrorsLog("4000", "Registrar usuario", "No se pudo registrar porque ya existe", "BD Exception, llave duplicada", "Administrador");
                Gson gson = new Gson();
                bw.write(gson.toJson(currentError));
                System.out.println("Todo funcionó perfecto, compruebe en el archivo la linea agregada");
            }else{
                bw = new BufferedWriter(new FileWriter(logFile));
                //Se crea el objeto con lo especificado
                JSONErrorsLog currentError = new JSONErrorsLog("1000", "Error de conexión", "No se encontró la BD zafiro", "Connection refused zafiro", "");
                Gson gson = new Gson();
                bw.write(gson.toJson(currentError));
                System.out.println("Todo funcionó perfecto, compruebe en la ruta el archivo creado");
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readEntry(){
        String path = System.getProperty("user.dir")+"\\Scirebox_log.log";
        File logFile = new File(path);
        String text;
        try {
            FileReader fr = new FileReader(logFile);
            BufferedReader br = new BufferedReader(fr);
            while((text=br.readLine())!=null){
                Gson gson = new Gson();
                JSONErrorsLog currentError = gson.fromJson(text, JSONErrorsLog.class);
                System.out.println(currentError.toString());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
