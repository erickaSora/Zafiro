/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Quotation;
import com.zafirodesktop.entity.QuotationDetail;
import com.zafirodesktop.entity.QuotationDetailPK;
import com.zafirodesktop.entity.TransactionDetail;
import com.zafirodesktop.entity.Tranzaction;
import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class TransactionDetailModel extends AbstractFacade{
    public Collection findAllById(String clase, int id){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery(clase+".findByIdTransactionFk").setParameter("id", id);
        list = query.getResultList();
        return list;
    }
    
    public void deleteAlls(int id) throws Exception{
        try{
        /*if(!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();*/
        Query query = entityManager.createNativeQuery("delete from transaction_detail where id_transaction_fk = ?");
        query.setParameter(1, id);
        query.executeUpdate();
        //entityManager.getTransaction().commit();
        }catch(Exception e){
            entityManager.getTransaction().rollback();
        }
    }
    
    public TransactionDetail findByTranzactionProduct(int idTranzaction, Integer idProduct){
        Query query = entityManager.createNamedQuery("TransactionDetail.findByProductTransaction").setParameter("id", idTranzaction)
                .setParameter("id2", idProduct);
        TransactionDetail td = (TransactionDetail)query.getSingleResult();
        return td;
    }
    
    public QuotationDetail findByQuotationProduct(QuotationDetailPK quotPK){
        Query query = entityManager.createNamedQuery("QuotationDetail.findById").setParameter("id", quotPK);
        QuotationDetail quotationDetail = (QuotationDetail)query.getSingleResult();
        return quotationDetail;
    }
    
    public void delete(int idTranzaction, Integer idProduct) throws Exception{
        try{
            if(!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
        Object object = findByTranzactionProduct(idTranzaction, idProduct);
        entityManager.remove(object);
        entityManager.flush();
        entityManager.clear();
        //entityManager.getTransaction().commit();
        }catch(Exception e){
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteTemp(int idTranzaction, Integer idProduct, Tranzaction trans) throws Exception{
        try{
            if(!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
        Object object = findByTranzactionProduct(idTranzaction, idProduct);
        entityManager.remove(object);
        entityManager.merge(trans);
        entityManager.flush();
        entityManager.clear();
        //entityManager.getTransaction().commit();
        }catch(Exception e){
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteQuotationDetail(QuotationDetailPK quotPK) throws Exception{
        try {
            if(!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
            Object object = findByQuotationProduct(quotPK);
            entityManager.remove(object);
            entityManager.flush();
            entityManager.clear();
            //entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    
    public void deleteQuotationDetails(QuotationDetailPK quotPK, Quotation actualQuotation) throws Exception{
        try {
            if(!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
            QuotationDetail quotationDetail = findByQuotationProduct(quotPK);
            entityManager.remove(quotationDetail);
            entityManager.merge(actualQuotation);
            entityManager.flush();
            entityManager.clear();
            //entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void saveQuotationDetail(QuotationDetail object, Quotation actualQuotation) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.merge(actualQuotation);
            entityManager.flush();
            entityManager.clear();
            //entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void saveTransactionDetail(TransactionDetail object, Tranzaction actualTranzaction) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.merge(actualTranzaction);
            entityManager.flush();
            entityManager.clear();
            //entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public TransactionDetail findStockBuy(Integer idProduct){
        TransactionDetail td;
        try{
            Query query = entityManager.createNamedQuery("TransactionDetail.findStockBuy").setParameter("idProduct", idProduct);
            td = (TransactionDetail)query.setMaxResults(1).getSingleResult();
        }catch(Exception e){
            td = null;
        }
        return td;
    }
}
