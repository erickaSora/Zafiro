/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.test;

import com.zafirodesktop.entity.Product;
import com.zafirodesktop.model.SessionBD;
import com.zafirodesktop.model.SettingsModel;
import java.util.Collection;

/**
 *
 * @author Digitall
 */
public class ImportTest {
    public static void main(String[] args) throws Exception {
        SessionBD.persistenceCreate();
        SettingsModel sm = new SettingsModel();
        int productID = sm.maxProductId();
        Collection<Product> col = sm.findAllImportedProducts(productID);
        System.out.println("id: "+productID);
        for (Product product : col) {
            System.out.println(product.getIdProductPk());
        }
        
        /*String path = "c:\\mybackups\\prueba.csv";
        sm.importData(path);*/
    }
}
