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
public class OfficePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PLACE_FK")
    private int idPlaceFk;
    @Basic(optional = false)
    @Column(name = "ID_SUPPLIER_FK")
    private Integer idSupplierFk;

    public OfficePK() {
    }

    public OfficePK(int idPlaceFk, Integer idSupplierFk) {
        this.idPlaceFk = idPlaceFk;
        this.idSupplierFk = idSupplierFk;
    }

    public int getIdPlaceFk() {
        return idPlaceFk;
    }

    public void setIdPlaceFk(int idPlaceFk) {
        this.idPlaceFk = idPlaceFk;
    }

    public Integer getIdSupplierFk() {
        return idSupplierFk;
    }

    public void setIdSupplierFk(Integer idSupplierFk) {
        this.idSupplierFk = idSupplierFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPlaceFk;
        hash += (idSupplierFk != null ? idSupplierFk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OfficePK)) {
            return false;
        }
        OfficePK other = (OfficePK) object;
        if (this.idPlaceFk != other.idPlaceFk) {
            return false;
        }
        if ((this.idSupplierFk == null && other.idSupplierFk != null) || (this.idSupplierFk != null && !this.idSupplierFk.equals(other.idSupplierFk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.OfficePK[ idPlaceFk=" + idPlaceFk + ", idSupplierFk=" + idSupplierFk + " ]";
    }
    
}
