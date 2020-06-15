/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.model;

import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.Settings;
import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class SettingsModel extends AbstractFacade {

    public Settings findLastInvoice(String lastInvoice) {
        Query query = entityManager.createNamedQuery("Settings.findByLastInvoice").setParameter("lastInvoice", lastInvoice);
        if (query.getSingleResult() != null) {
            Settings o = (Settings) query.getSingleResult();
            return o;
        } else {
            return null;
        }
    }

    public void backupBD(String path) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Query query = entityManager.createNativeQuery("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
            query.setParameter(1, path);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void importData(String path) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Query query = entityManager.createNativeQuery("CALL SYSCS_UTIL.SYSCS_IMPORT_DATA(NULL, 'PRODUCT', "
                    + "'SKU_PRODUCT,PRODUCT_REFERENCE,PRODUCT_DESCRIPTION,ACTUAL_PRICE,MINIMUN_STOCK,CANTIDAD_SELECCIONADA', "
                    + "NULL, ?, ';', '\\n', 'UTF-8',0)");
            query.setParameter(1, path);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public int maxProductId(){
        int resp=0;
        Query query = entityManager.createNamedQuery("Product.findMaxId");
        if(query.getSingleResult()!=null)
            resp = Integer.parseInt(query.getSingleResult().toString());
        return resp;
    }
    
    public Collection<Product> findAllImportedProducts(int lastId) {
        Query query = entityManager.createNamedQuery("Product.findAllImported").setParameter("id", lastId);
        list = query.getResultList();

        return list;
    }
}
