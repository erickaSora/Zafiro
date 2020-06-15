/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import com.itextpdf.text.DocumentException;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.util.PrintPDF;
import java.awt.Desktop;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.print.PrintException;

/**
 *
 * @author andresforero
 */
public class PrintTest {

    public static void main(String[] args) throws IOException, PrintException, PrinterException, ParseException, FileNotFoundException, DocumentException {

        /*InputStream inputStream = null;
         String path = "nn";
         try {
         path = System.getProperty("user.dir");
         path += "//invoice.pdf";
         inputStream = new BufferedInputStream(new FileInputStream(path));
         } catch (FileNotFoundException e) {
         e.printStackTrace();
         }
         if (inputStream == null) {
         return;
         }

         DocFlavor docFormat = DocFlavor.BYTE_ARRAY.AUTOSENSE;
         Doc document = new SimpleDoc(inputStream, docFormat, null);
         PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
         javax.print.PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
         if (defaultPrintService != null) {
         DocPrintJob printJob = defaultPrintService.createPrintJob();
         try {
         printJob.print(document, attributeSet);
         System.out.println(path);
         } catch (Exception e) {

         e.printStackTrace();
         }
         } else {
         System.err.println("No existen impresoras instaladas");
         }
         inputStream.close();*/

        /*Desktop desktop = Desktop.getDesktop();
         String path = System.getProperty("user.dir");
         path += "//invoice.pdf";
         File file = new File(path); 
         try {
         desktop.open(file);
         desktop.print(file);
         } catch (Exception e) {
         e.printStackTrace();
         }*/
        PrintPDF print = new PrintPDF();
        SimpleDateFormat sdfComplete = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String today = "21-11-2014 00:00:00";
        Date dayToReport = sdfComplete.parse(today);
        SessionBD.persistenceCreate();
        print.printCloseBoxReport(dayToReport, null);
    }
}
