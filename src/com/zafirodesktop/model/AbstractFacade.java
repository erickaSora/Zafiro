/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zafirodesktop.model;

import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class AbstractFacade {

    EntityManager entityManager = SessionBD.getEntityManager();
    public Collection list;

    public void saveFinal(Object object) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void saveIntermediate(Object object) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.flush();
            entityManager.clear();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void executeCommit(){
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.getMessage();
        }
    }
    
    public void executeRollback(){
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.getTransaction().rollback();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public Collection findAll(String clase) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery(clase + ".findAll");
        list = query.getResultList();

        return list;
    }

    public Collection findAllDate(String clase, Date startDate, Date endDate) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery(clase + ".findAllDate")
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate);
        list = query.getResultList();

        return list;
    }

    public Collection findByStatus(String clase, short status) {
        Query query = entityManager.createNamedQuery(clase + ".findByStatus")
                .setParameter("status", status);
        list = query.getResultList();

        return list;
    }

    public Object findByIdInt(String clase, int id) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery(clase + ".findById").setParameter("id", id);
        Object o = query.getSingleResult();

        return o;
    }

    public Object findByIdString(String clase, String id) {
        Query query = entityManager.createNamedQuery(clase + ".findById").setParameter("id", id.toLowerCase());
        Object o;
        try{
            o = query.getSingleResult();
        }catch(Exception e){
            o = null;
        }
        return o;
    }
    
    public Object findByRealId(String clase, int id) {
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Query query = entityManager.createNamedQuery(clase + ".findByRealId").setParameter("id", id);
        Object o = query.getSingleResult();

        return o;
    }

    public Object findByName(String clase, String name) {
        Query query = entityManager.createNamedQuery(clase + ".findByName").setParameter("name", name.toLowerCase());
        Object o = (Object) query.getSingleResult();
        return o;
    }

    public Collection findByLike(String clase, String id, String txt2, String txt3) {
        Query query = entityManager.createNamedQuery(clase + ".findByLike").setParameter("id", "%" + id.toLowerCase() + "%").setParameter("txt2", "%" + txt2.toLowerCase() + "%")
                .setParameter("txt3", "%" + txt3.toLowerCase() + "%");
        list = query.getResultList();

        return list;
    }

    public Collection findByLikeInt(String clase, int id, String txt2, String txt3) {
        Query query = entityManager.createNamedQuery(clase + ".findByLikeInt").setParameter("id", id).setParameter("txt2", "%" + txt2.toLowerCase() + "%")
                .setParameter("txt3", txt3);
        list = query.getResultList();

        return list;
    }

    public Collection findByLike1(String clase, String id) {
        Query query = entityManager.createNamedQuery(clase + ".findByLike1").setParameter("id", "%" + id + "%");
        list = query.getResultList();

        return list;
    }
    
    /*
        Método para buscar los que tienen la fecha especificada como nula
        @return: Array de objetos encontrados (Collection)
    */
    public Collection findByNonDate(String clase){
        Query query = entityManager.createNamedQuery(clase+".findByNonDate");
        list = query.getResultList();
        
        return list;
    }

    public void updateFinal(Object object) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(object);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void updateIntermediate(Object object) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(object);
            entityManager.flush();
            entityManager.clear();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    public void deleteIntFinal(String clase, int id) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Object object = findByIdInt(clase, id);
            entityManager.remove(object);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteIntIntermediate(String clase, int id) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Object object = findByIdInt(clase, id);
            entityManager.remove(object);
            entityManager.flush();
            entityManager.clear();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    public void deleteStringFinal(String clase, String id) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Object object = findByIdString(clase, id);
            entityManager.remove(object);
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteStringIntermediate(String clase, String id) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Object object = findByIdString(clase, id);
            entityManager.remove(object);
            entityManager.flush();
            entityManager.clear();
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
}
