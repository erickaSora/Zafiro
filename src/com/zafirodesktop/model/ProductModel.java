/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.model;

import com.zafirodesktop.entity.Product;
import com.zafirodesktop.entity.ProductCategory;
import com.zafirodesktop.entity.ProductDiscount;
import com.zafirodesktop.entity.ProductTaxes;
import com.zafirodesktop.entity.Stock;
import java.util.Collection;
import javax.persistence.Query;

/**
 *
 * @author Digitall
 */
public class ProductModel extends AbstractFacade{
    
    public int findLast(){
        Query query = entityManager.createNamedQuery("Product.findLast");
        int prodId = (Integer)query.getSingleResult();
        return prodId;
    }
    
    public Collection<Object> findByIdProduct(String clase, int idProd){
        Query query = entityManager.createNamedQuery(clase+".findByIdProductFk").setParameter("idProductFk", idProd);
        list = query.getResultList();
        return list;
    }
    
    public Collection<Product> findByCategory(int idCategory){
        Query query = entityManager.createQuery("Select p from Product p, ProductCategory pc, ItemCategory ic "
                + "WHERE ic.idItemCategoryPk = :id AND p=pc.product AND ic=pc.itemCategory AND p.type=1").setParameter("id", idCategory);
        list = query.getResultList();
        return list;
    }
    
    public Collection<Product> findServiceByCategory(int idCategory){
        Query query = entityManager.createQuery("Select p from Product p, ProductCategory pc, ItemCategory ic "
                + "WHERE ic.idItemCategoryPk = :id AND p=pc.product AND ic=pc.itemCategory AND p.type=0").setParameter("id", idCategory);
        list = query.getResultList();
        return list;
    }
    
    public Collection<Product> findByDepartment(int idProdFk){
        Query query = entityManager.createNamedQuery("Product.findByDepartment").setParameter("id", idProdFk);
        list = query.getResultList();
        return list;
    }
    
    public Collection<ProductCategory> findByItemCategory(int idItemCat){
        Query query = entityManager.createNamedQuery("ProductCategory.findByIdItemCategoryFk").setParameter("id", idItemCat);
        list = query.getResultList();
        return list;
    }
    
    public Collection<ProductTaxes> findByTaxes(int idTax){
        Query query = entityManager.createNamedQuery("ProductTaxes.findByIdTaxFk").setParameter("idTaxFk", idTax);
        list = query.getResultList();
        return list;
    }
    
    public Collection<ProductDiscount> findByDiscount(int idDiscount){
        Query query = entityManager.createNamedQuery("ProductDiscount.findByIdDiscountFk").setParameter("idDiscountFk", idDiscount);
        list = query.getResultList();
        return list;
    }
    
    public void deleteFromStock(Integer id) throws Exception{
        try{
        if(!entityManager.getTransaction().isActive())
                entityManager.getTransaction().begin();
        Query query = entityManager.createNativeQuery("delete from stock where id_product_fk = ?");
        query.setParameter(1, id);
        query.executeUpdate();
        entityManager.flush();
        entityManager.clear();
        //entityManager.getTransaction().commit();
        }catch(Exception e){
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteTax(String id, Product actualProduct) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            ProductTaxes object = (ProductTaxes)findByIdString("ProductTaxes", id);
            entityManager.remove(object);
            entityManager.merge(actualProduct);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteDiscount(String id, Product actualProduct) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            ProductDiscount object = (ProductDiscount)findByIdString("ProductDiscount", id);
            entityManager.remove(object);
            entityManager.merge(actualProduct);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void deleteCategory(String id, Product actualProduct) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            ProductCategory object = (ProductCategory)findByIdString("ProductCategory", id);
            entityManager.remove(object);
            entityManager.merge(actualProduct);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void saveTax(ProductTaxes object, Product actualProduct)throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.merge(actualProduct);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void saveDiscount(ProductDiscount object, Product actualProduct)throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.merge(actualProduct);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void saveCategory(ProductCategory object, Product actualProduct)throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(object);
            entityManager.merge(actualProduct);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
    
    public void updateStock(Stock stock, Product product) throws Exception{
        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(stock);
            entityManager.merge(product);
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
}
