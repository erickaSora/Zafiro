/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Turn;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class TurnModel extends AbstractFacade{
    
    /*
        Método para buscar los turnos asociados a un usuario
        @param: id del usuario a buscar (int userId)
        @return: Array de turnos encontrados (Collection turn)
    */
    public Collection<Turn> findByUserId(int userId){
        Query query = entityManager.createNamedQuery("Turn.findByUserId")
                .setParameter("id", userId);
        Collection<Turn> t = query.getResultList();
        
        return t;
    }
    
    public Collection findByTurn(String clase, BigInteger turnId){
        Query query = entityManager.createNamedQuery(clase+".findByTurn")
                .setParameter("turn", turnId);
        Collection c = query.getResultList();
        
        return c;
    }
}
