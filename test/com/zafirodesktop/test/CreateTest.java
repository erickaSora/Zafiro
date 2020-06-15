/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.test;

import com.zafirodesktop.entity.Invoice;
import com.zafirodesktop.entity.Payment;
import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.Tranzaction;
import com.zafirodesktop.model.ProductModel;
import com.zafirodesktop.model.SessionBD;

/**
 *
 * @author Digitall
 */
public class CreateTest {
    public static void main(String[] args) throws Exception {
        SessionBD.persistenceCreate();
        ProductModel pdm = new ProductModel();
        /*ProductCategoryPK productCategoryPK = new ProductCategoryPK();
        productCategoryPK.setIdProductFk(11111);
        productCategoryPK.setIdItemCategoryFk(14);
        //ProductCategory productCategory = new ProductCategory("11111", 9);
        ProductCategory productCategory = new ProductCategory(productCategoryPK);
        productCategory.setItem("1");
        pdm.saveFinal(productCategory);*/
        short status = new Short("1");
        
        Invoice invoice = new Invoice("PB003");
        invoice.setStatus(status);
        invoice.setNoteHeader("fndsjkfnsdjkfnsdjk");
        Payment pay = (Payment) pdm.findByIdInt("Payment", 2);
        invoice.setIdPaymentFk(pay);
        Remission remission = new Remission();
        remission.setStatus(status);
        remission.setInvoiced(status);
        remission.setIdInvoiceFk(invoice);
        Tranzaction tranzaction = new Tranzaction();
        tranzaction.setIdRemissionFk(remission);
        Product product = (Product) pdm.findByName("Product", "4t0775nnc");
        product.setActualPrice(45555.0);
        Product product2 = (Product) pdm.findByName("Product", "mntprv01");
        product2.setActualPrice(2111111.0);
        TransactionDetail td = new TransactionDetail();
        td.setProduct(product);
        td.setTranzaction(tranzaction);
        pdm.updateIntermediate(product);
        pdm.updateIntermediate(product2);
        product.setCantidadSeleccionada(4);
        product2.setCantidadSeleccionada(2);
        pdm.updateIntermediate(product);
        pdm.updateIntermediate(product2);
        //pdm.saveIntermediate(remission);
        pdm.executeCommit();
        
        /*Collection<ProductCategory> lista = pdm.findByIdProduct("11111");
        for (ProductCategory productCategory : lista) {
            productCategory.getItem();
            System.out.println("Si hay por lo menos 1 elemento!!");
        }*/
    }
}
