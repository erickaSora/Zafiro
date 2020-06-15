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
public class ProductCategoryPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_FK")
    private Integer idProductFk;
    @Basic(optional = false)
    @Column(name = "ID_ITEM_CATEGORY_FK")
    private int idItemCategoryFk;

    public ProductCategoryPK() {
    }

    public ProductCategoryPK(Integer idProductFk, int idItemCategoryFk) {
        this.idProductFk = idProductFk;
        this.idItemCategoryFk = idItemCategoryFk;
    }

    public Integer getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(Integer idProductFk) {
        this.idProductFk = idProductFk;
    }

    public int getIdItemCategoryFk() {
        return idItemCategoryFk;
    }

    public void setIdItemCategoryFk(int idItemCategoryFk) {
        this.idItemCategoryFk = idItemCategoryFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductFk != null ? idProductFk.hashCode() : 0);
        hash += (int) idItemCategoryFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductCategoryPK)) {
            return false;
        }
        ProductCategoryPK other = (ProductCategoryPK) object;
        if ((this.idProductFk == null && other.idProductFk != null) || (this.idProductFk != null && !this.idProductFk.equals(other.idProductFk))) {
            return false;
        }
        if (this.idItemCategoryFk != other.idItemCategoryFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.ProductCategoryPK[ idProductFk=" + idProductFk + ", idItemCategoryFk=" + idItemCategoryFk + " ]";
    }
    
}
