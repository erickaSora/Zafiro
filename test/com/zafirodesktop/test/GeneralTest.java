/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import com.zafirodesktop.controller.MainController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Digitall
 */
public class GeneralTest {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        /*float res;
         float pct = 100; 
         int IVA = 16;
         float res = (IVA/pct)+1;*/
        /*String path = System.getProperty("user.dir");
        path += "\\web-files\\logo3.jpg";
        System.out.println(path);
        String srcLogo = "D:\\Digitall\\imagenes\\Fondos\\1.png";
        File newLogo = new File(srcLogo);
        if (newLogo.exists()) {
            File logoPath = new File(path);
            InputStream in = new FileInputStream(newLogo);
            OutputStream out = new FileOutputStream(logoPath);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } else {
            System.out.print("El logo no existe");
        }*/
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy");
        System.out.println(sdf.format(cal.getTime()));
    }
}
