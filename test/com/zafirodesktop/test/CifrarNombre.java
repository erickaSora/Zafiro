/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Digitall
 */
public class CifrarNombre {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException {
        final String phrase = "Digitall TI";
        String marked = "DBB16-2E71C-8AD97-EEBA5";
        final String cifred = DigestUtils.sha1Hex(phrase.replaceAll("\\s+","").toLowerCase());
        String serial = cifred.substring(0, 20).toUpperCase();
        marked = marked.replace("-", "").toUpperCase();
        System.out.println(serial+"\n"+marked);
        if(serial.equals(marked))
            System.out.println("Si son iguales los seriales");
        else
            System.out.println("No son iguales, pirata");
        
        /*final String cifred2 = DigestUtils.md5Hex(phrase);
        String serial2 = cifred2.substring(0, 9).toUpperCase();
        String labeled[] = {serial2.substring(0,4),"-",serial2.substring(4,7),"-",serial2.substring(7,9)};
        System.out.println(cifred2+"\n"+serial2+"\n"+labeled[0]+labeled[1]+labeled[2]+labeled[3]+labeled[4]);*/
        
    }
}
