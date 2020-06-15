/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "CATEGORY_DISCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoryDiscount.findAll", query = "SELECT c FROM CategoryDiscount c"),
    @NamedQuery(name = "CategoryDiscount.findByIdCategoryFk", query = "SELECT c FROM CategoryDiscount c WHERE c.categoryDiscountPK.idCategoryFk = :idCategoryFk"),
    @NamedQuery(name = "CategoryDiscount.findByIdDiscountFk", query = "SELECT c FROM CategoryDiscount c WHERE c.categoryDiscountPK.idDiscountFk = :idDiscountFk"),
    @NamedQuery(name = "CategoryDiscount.findByStartDate", query = "SELECT c FROM CategoryDiscount c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "CategoryDiscount.findByEndDate", query = "SELECT c FROM CategoryDiscount c WHERE c.endDate = :endDate")})
public class CategoryDiscount implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CategoryDiscountPK categoryDiscountPK;
    @Basic(optional = false)
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public CategoryDiscount() {
    }

    public CategoryDiscount(CategoryDiscountPK categoryDiscountPK) {
        this.categoryDiscountPK = categoryDiscountPK;
    }

    public CategoryDiscount(CategoryDiscountPK categoryDiscountPK, Date startDate, Date endDate) {
        this.categoryDiscountPK = categoryDiscountPK;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CategoryDiscount(int idCategoryFk, int idDiscountFk) {
        this.categoryDiscountPK = new CategoryDiscountPK(idCategoryFk, idDiscountFk);
    }

    public CategoryDiscountPK getCategoryDiscountPK() {
        return categoryDiscountPK;
    }

    public void setCategoryDiscountPK(CategoryDiscountPK categoryDiscountPK) {
        this.categoryDiscountPK = categoryDiscountPK;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryDiscountPK != null ? categoryDiscountPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoryDiscount)) {
            return false;
        }
        CategoryDiscount other = (CategoryDiscount) object;
        if ((this.categoryDiscountPK == null && other.categoryDiscountPK != null) || (this.categoryDiscountPK != null && !this.categoryDiscountPK.equals(other.categoryDiscountPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.CategoryDiscount[ categoryDiscountPK=" + categoryDiscountPK + " ]";
    }
    
}
