/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Deposit;
import com.zafirodesktop.entity.Remission;

/**
 *
 * @author Digitall
 */
public class DepositModel extends AbstractFacade{
    
    public void saveDeposit(Deposit object, Remission actualRemission) throws Exception{
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
}
