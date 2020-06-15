/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Remission;
import com.zafirodesktop.entity.Tranzaction;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class TranzactionModel extends AbstractFacade{
    
    public Tranzaction findByRemission(Integer remi){
        Query query = entityManager.createNamedQuery("Tranzaction.findByRemission").setParameter("id", remi);
        Tranzaction t = (Tranzaction)query.getSingleResult();
        return t;
    }
    
    public void saveTranzaction(Tranzaction object, Remission actualRemission) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.merge(actualRemission);
            entityManager.flush();
            entityManager.clear();
            //entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public List<Tranzaction> findAllOrdersByDate(Date startDate, Date endDate){
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery("Tranzaction.findAllOrdersByDate")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        List<Tranzaction> lista = query.getResultList();

        return lista;
    }
}
