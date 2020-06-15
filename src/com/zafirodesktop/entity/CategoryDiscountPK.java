/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Digitall
 */
@Embeddable
public class CategoryDiscountPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_CATEGORY_FK")
    private int idCategoryFk;
    @Basic(optional = false)
    @Column(name = "ID_DISCOUNT_FK")
    private int idDiscountFk;

    public CategoryDiscountPK() {
    }

    public CategoryDiscountPK(int idCategoryFk, int idDiscountFk) {
        this.idCategoryFk = idCategoryFk;
        this.idDiscountFk = idDiscountFk;
    }

    public int getIdCategoryFk() {
        return idCategoryFk;
    }

    public void setIdCategoryFk(int idCategoryFk) {
        this.idCategoryFk = idCategoryFk;
    }

    public int getIdDiscountFk() {
        return idDiscountFk;
    }

    public void setIdDiscountFk(int idDiscountFk) {
        this.idDiscountFk = idDiscountFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCategoryFk;
        hash += (int) idDiscountFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoryDiscountPK)) {
            return false;
        }
        CategoryDiscountPK other = (CategoryDiscountPK) object;
        if (this.idCategoryFk != other.idCategoryFk) {
            return false;
        }
        if (this.idDiscountFk != other.idDiscountFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.CategoryDiscountPK[ idCategoryFk=" + idCategoryFk + ", idDiscountFk=" + idDiscountFk + " ]";
    }
    
}
