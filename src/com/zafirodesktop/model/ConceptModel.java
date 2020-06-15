/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class ConceptModel extends AbstractFacade{
    
    public Collection findAllByType(String type){
        Query query = entityManager.createNamedQuery("Concept.findByConceptType").setParameter("conceptType", type);
        list = query.getResultList();
        
        return list;
    }
}
