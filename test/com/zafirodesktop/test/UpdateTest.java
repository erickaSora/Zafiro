/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.test;

import com.zafirodesktop.entity.Product;
import com.zafirodesktop.model.AbstractFacade;
import com.zafirodesktop.model.SessionBD;
import java.util.Collection;

/**
 *
 * @author Digitall
 */
public class UpdateTest {
    public static void main(String[] args) throws Exception {
        SessionBD.persistenceCreate();
        AbstractFacade abs = new AbstractFacade();
        Collection<Product> prods = abs.findAll("Product");
        for (Product prod : prods) {
            prod.setProductReference(prod.getProductReference()+" "+prod.getPrice());
            abs.updateFinal(prod);
        }
    }
}
