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
public class ProductTaxesPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_FK")
    private Integer idProductFk;
    @Basic(optional = false)
    @Column(name = "ID_TAX_FK")
    private int idTaxFk;

    public ProductTaxesPK() {
    }

    public ProductTaxesPK(Integer idProductFk, int idTaxFk) {
        this.idProductFk = idProductFk;
        this.idTaxFk = idTaxFk;
    }

    public Integer getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(Integer idProductFk) {
        this.idProductFk = idProductFk;
    }

    public int getIdTaxFk() {
        return idTaxFk;
    }

    public void setIdTaxFk(int idTaxFk) {
        this.idTaxFk = idTaxFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductFk != null ? idProductFk.hashCode() : 0);
        hash += (int) idTaxFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductTaxesPK)) {
            return false;
        }
        ProductTaxesPK other = (ProductTaxesPK) object;
        if ((this.idProductFk == null && other.idProductFk != null) || (this.idProductFk != null && !this.idProductFk.equals(other.idProductFk))) {
            return false;
        }
        if (this.idTaxFk != other.idTaxFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.ProductTaxesPK[ idProductFk=" + idProductFk + ", idTaxFk=" + idTaxFk + " ]";
    }
    
}
