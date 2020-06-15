/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Users;
import javax.persistence.Query;

/**
 *
 * @author andresforero
 */
public class UsersModel extends AbstractFacade{
    
    public Users findByCreatedOn(int id) {
        Query query = entityManager.createNamedQuery("Users.findByCreatedOn").setParameter("id", id);
        Users u = (Users)query.getSingleResult();

        return u;
    }
}
