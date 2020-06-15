/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Remission;
import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class RemissionModel extends AbstractFacade{
    
    public Collection<Remission> findByIdSell(String idSell){
        Query query = entityManager.createNamedQuery("Remission.findByNoBuyReference").setParameter("noBuyReference", idSell);
        list = query.getResultList();
        return list;
    }
    
    public Remission findByIdCredit(String idCredit){
        Query query = entityManager.createNamedQuery("Remission.findByIdCredit").setParameter("noBuyReference", idCredit);
        Remission remission = (Remission) query.getSingleResult();
        return remission;
    }
    
    public Remission findInitializeCredit(int idPerson){
        Query query = entityManager.createNamedQuery("Remission.findInitializeCredit").setParameter("idPerson", idPerson);
        Remission remission = (Remission) query.getSingleResult();
        return remission;
    }
    
    public Remission findByIdInvoice(String idInvoice){
        Query query = entityManager.createNamedQuery("Remission.findByIdInvoice").setParameter("id", idInvoice);
        Remission remission = (Remission) query.getSingleResult();
        return remission;
    }
    
    public Collection<Remission> findAllCreditsByClient(int idClient){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("Remission.findAllCreditsByClient").setParameter("idClient", idClient);
        list = query.getResultList();
        return list;
    }
    
    public Collection<Remission> findAllCreditsBySupplier(int idSupplier){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("Remission.findAllCreditsBySupplier").setParameter("idClient", idSupplier);
        list = query.getResultList();
        return list;
    }
    
    public Collection<Remission> findAllCreditsActive(){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("Remission.findAllActiveCredit");
        list = query.getResultList();
        return list;
    }
    
    public Collection<Remission> findAllCreditsOrderActive(){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("Remission.findAllActiveCreditOrder");
        list = query.getResultList();
        return list;
    }
    
    public Collection<Remission> findCreditsActiveByClient(int idClient){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("Remission.findActiveCreditsByClient").setParameter("idClient", idClient);
        list = query.getResultList();
        return list;
    }
    
    public Collection findAllByIdRemission(String clase, int idRemission){
        Query query = entityManager.createNamedQuery(clase+".findByIdRemission").setParameter("id", idRemission);
        list = query.getResultList();
        return list;
    }
}
