/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "PRODUCT_CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductCategory.findAll", query = "SELECT p FROM ProductCategory p"),
    @NamedQuery(name = "ProductCategory.findByIdProductFk", query = "SELECT p FROM ProductCategory p WHERE p.productCategoryPK.idProductFk = :idProductFk"),
    @NamedQuery(name = "ProductCategory.findByIdItemCategoryFk", query = "SELECT p FROM ProductCategory p WHERE p.productCategoryPK.idItemCategoryFk = :id"),
    @NamedQuery(name = "ProductCategory.findById", query = "SELECT p FROM ProductCategory p WHERE p.item = :id")})
public class ProductCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductCategoryPK productCategoryPK;
    @Column(name = "ITEM")
    private String item;
    @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;
    @JoinColumn(name = "ID_ITEM_CATEGORY_FK", referencedColumnName = "ID_ITEM_CATEGORY_PK", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ItemCategory itemCategory;

    public ProductCategory() {
    }

    public ProductCategory(ProductCategoryPK productCategoryPK) {
        this.productCategoryPK = productCategoryPK;
    }

    public ProductCategory(Integer idProductFk, int idItemCategoryFk) {
        this.productCategoryPK = new ProductCategoryPK(idProductFk, idItemCategoryFk);
    }

    public ProductCategoryPK getProductCategoryPK() {
        return productCategoryPK;
    }

    public void setProductCategoryPK(ProductCategoryPK productCategoryPK) {
        this.productCategoryPK = productCategoryPK;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }
    
    public String getItemName(){
        return itemCategory.getIdCategoryFk().getCategoryName()+" - "+itemCategory.getItemCategoryName();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productCategoryPK != null ? productCategoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductCategory)) {
            return false;
        }
        ProductCategory other = (ProductCategory) object;
        if ((this.productCategoryPK == null && other.productCategoryPK != null) || (this.productCategoryPK != null && !this.productCategoryPK.equals(other.productCategoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.ProductCategory[ productCategoryPK=" + productCategoryPK + " ]";
    }
    
}
