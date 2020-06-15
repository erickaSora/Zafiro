/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "ITEM_CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemCategory.findAll", query = "SELECT i FROM ItemCategory i"),
    @NamedQuery(name = "ItemCategory.findById", query = "SELECT i FROM ItemCategory i WHERE i.idItemCategoryPk = :id"),
    @NamedQuery(name = "ItemCategory.findByName", query = "SELECT i FROM ItemCategory i WHERE LOWER(i.itemCategoryName) = :name"),
    @NamedQuery(name = "ItemCategory.findByIdCat", query = "SELECT i FROM ItemCategory i WHERE i.idCategoryFk = :cat")})
public class ItemCategory implements Serializable {
    @OneToMany(mappedBy = "itemCategory")
    private Collection<ProductCategory> productCategoryCollection;
    @JoinTable(name = "PRODUCT_CATEGORY", joinColumns = {
        @JoinColumn(name = "ID_ITEM_CATEGORY_FK", referencedColumnName = "ID_ITEM_CATEGORY_PK")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_PRODUCT_FK", referencedColumnName = "ID_PRODUCT_PK")})
    @ManyToMany
    private Collection<Product> productCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ITEM_CATEGORY_PK")
    private Integer idItemCategoryPk;
    @Basic(optional = false)
    @Column(name = "ITEM_CATEGORY_NAME")
    private String itemCategoryName;
    @JoinColumn(name = "ID_CATEGORY_FK", referencedColumnName = "ID_CATEGORY_PK")
    @ManyToOne(optional = false)
    private Category idCategoryFk;

    public ItemCategory() {
    }

    public ItemCategory(Integer idItemCategoryPk) {
        this.idItemCategoryPk = idItemCategoryPk;
    }

    public ItemCategory(Integer idItemCategoryPk, String itemCategoryName) {
        this.idItemCategoryPk = idItemCategoryPk;
        this.itemCategoryName = itemCategoryName;
    }

    public Integer getIdItemCategoryPk() {
        return idItemCategoryPk;
    }

    public void setIdItemCategoryPk(Integer idItemCategoryPk) {
        this.idItemCategoryPk = idItemCategoryPk;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public Category getIdCategoryFk() {
        return idCategoryFk;
    }

    public void setIdCategoryFk(Category idCategoryFk) {
        this.idCategoryFk = idCategoryFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItemCategoryPk != null ? idItemCategoryPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemCategory)) {
            return false;
        }
        ItemCategory other = (ItemCategory) object;
        if ((this.idItemCategoryPk == null && other.idItemCategoryPk != null) || (this.idItemCategoryPk != null && !this.idItemCategoryPk.equals(other.idItemCategoryPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idCategoryFk.getCategoryName()+" - "+itemCategoryName;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }

    @XmlTransient
    public Collection<ProductCategory> getProductCategoryCollection() {
        return productCategoryCollection;
    }

    public void setProductCategoryCollection(Collection<ProductCategory> productCategoryCollection) {
        this.productCategoryCollection = productCategoryCollection;
    }
    
}
