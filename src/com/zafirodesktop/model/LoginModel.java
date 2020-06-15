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
 * @author Digitall
 */
public class LoginModel extends AbstractFacade{
    
    public Users findByUserPasswd(String user, String passwd){
        Query query = entityManager.createNamedQuery("Users.findByUserPass")
                .setParameter("id", user)
                .setParameter("passwd", passwd);
        Users o = (Users)query.getSingleResult();
        
        return o;
    }
    
}
