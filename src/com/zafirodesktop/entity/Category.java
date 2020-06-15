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
@Table(name = "CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findById", query = "SELECT c FROM Category c WHERE c.idCategoryPk = :id"),
    @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE LOWER(c.categoryName) = :name"),
    @NamedQuery(name = "Category.findByLikeInt", query = "SELECT c FROM Category c WHERE c.idCategoryPk = :id OR LOWER(c.categoryName) LIKE :txt2 "
            + "OR LOWER(c.categoryName) LIKE :txt3")})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CATEGORY_PK")
    private Integer idCategoryPk;
    @Basic(optional = false)
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @OneToMany(mappedBy = "idCategoryFk")
    private Collection<ItemCategory> itemCategoryCollection;

    public Category() {
    }

    public Category(Integer idCategoryPk) {
        this.idCategoryPk = idCategoryPk;
    }

    public Category(Integer idCategoryPk, String categoryName) {
        this.idCategoryPk = idCategoryPk;
        this.categoryName = categoryName;
    }

    public Integer getIdCategoryPk() {
        return idCategoryPk;
    }

    public void setIdCategoryPk(Integer idCategoryPk) {
        this.idCategoryPk = idCategoryPk;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @XmlTransient
    public Collection<ItemCategory> getItemCategoryCollection() {
        return itemCategoryCollection;
    }

    public void setItemCategoryCollection(Collection<ItemCategory> itemCategoryCollection) {
        this.itemCategoryCollection = itemCategoryCollection;
    }
    
    public String getId(){
        return idCategoryPk.toString();
    }
    
    public String getAsociatedItems(){
        return String.valueOf(itemCategoryCollection.size());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCategoryPk != null ? idCategoryPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.idCategoryPk == null && other.idCategoryPk != null) || (this.idCategoryPk != null && !this.idCategoryPk.equals(other.idCategoryPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Category[ idCategoryPk=" + idCategoryPk + " ]";
    }
    
}
