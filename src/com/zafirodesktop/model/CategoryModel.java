/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Category;
import com.zafirodesktop.entity.ItemCategory;
import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class CategoryModel extends AbstractFacade{
    public Collection<ItemCategory> findByIdCategory(Category cat){
        Query query = entityManager.createNamedQuery("ItemCategory.findByIdCat").setParameter("cat", cat);
        list = query.getResultList();
        return list;
    }
}
