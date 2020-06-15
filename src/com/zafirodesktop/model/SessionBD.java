/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author andresforero
 */
public class SessionBD {
    
    private static EntityManager entityManager;
    
    public static void persistenceCreate(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZafiroDesktopPU");
        entityManager = emf.createEntityManager();
        
    }
    
    public static EntityManager getEntityManager(){
        return entityManager;
    }
    
    public static void persistenceClose(){
        entityManager.close();
    }
    
}
