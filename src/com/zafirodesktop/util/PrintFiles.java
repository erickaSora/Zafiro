/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.util;

import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Digitall
 */
public class PrintFiles {
    
    /*
        Método para imprimir archivos de tipo PDF de modo silencioso 
        @param: URL del archivo a imprimir (String url) 
    */
    public void silentPrintPDF(String fileName)
            throws IOException, PrinterException {
        Desktop desktop = Desktop.getDesktop();
        desktop.print(new File(fileName));
    }
    
}
