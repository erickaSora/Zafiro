/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.util;

import com.google.gson.Gson;
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
public class LogActions {
    private Gson gson;
    private JSONErrorsLog currentError;
    private final String path = System.getProperty("user.dir")+"\\Scirebox_log.log";
    File logFile;

    public LogActions() {
        logFile = new File(path);
    }
    
    /*
        Método para registrar una nueva entrada al log
        @param: código del error (String code), método en el que ocurrió(String currentOperation),
        mensaje del error(String errorMessage), causa del error(String errorCause), usuario a quien le ocurrió(String currentUser)
    */
    public void createEntry(String code, String currentOperation, String errorMessage, String errorCause, String currentUser){
        currentError = new JSONErrorsLog(code, currentOperation, errorMessage, errorCause, currentUser);
        try{
            BufferedWriter bw;
            gson = new Gson();
            if(logFile.exists()){
                bw = new BufferedWriter(new FileWriter(logFile, true));
                bw.newLine();
                bw.write(gson.toJson(currentError));
            }else{
                bw = new BufferedWriter(new FileWriter(logFile));
                bw.write(gson.toJson(currentError));
            }
            bw.close();
        }catch(IOException ex){
            Logger.getLogger(LogActions.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    /*
        Método para capturar las líneas asociadas al log
    */
    public List<JSONErrorsLog> readEntries(){
        List<JSONErrorsLog> errorsList = new ArrayList<>();
        String text;
        try{
            FileReader fr = new FileReader(logFile);
            BufferedReader br = new BufferedReader(fr);
            while((text=br.readLine())!=null){
                gson = new Gson();
                currentError = gson.fromJson(text, JSONErrorsLog.class);
                errorsList.add(currentError);
            }
        }catch (FileNotFoundException ex) {
            Logger.getLogger(LogActions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogActions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return errorsList;
    }
    
}
