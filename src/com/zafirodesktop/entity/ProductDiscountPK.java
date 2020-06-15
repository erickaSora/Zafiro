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
public class ProductDiscountPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_DISCOUNT_FK")
    private int idDiscountFk;
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_FK")
    private int idProductFk;

    public ProductDiscountPK() {
    }

    public ProductDiscountPK(int idDiscountFk, int idProductFk) {
        this.idDiscountFk = idDiscountFk;
        this.idProductFk = idProductFk;
    }

    public int getIdDiscountFk() {
        return idDiscountFk;
    }

    public void setIdDiscountFk(int idDiscountFk) {
        this.idDiscountFk = idDiscountFk;
    }

    public int getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(int idProductFk) {
        this.idProductFk = idProductFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDiscountFk;
        hash += (int) idProductFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductDiscountPK)) {
            return false;
        }
        ProductDiscountPK other = (ProductDiscountPK) object;
        if (this.idDiscountFk != other.idDiscountFk) {
            return false;
        }
        if (this.idProductFk != other.idProductFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.ProductDiscountPK[ idDiscountFk=" + idDiscountFk + ", idProductFk=" + idProductFk + " ]";
    }
    
}
