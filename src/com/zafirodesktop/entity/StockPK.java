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
public class StockPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_FK")
    private Integer idProductFk;
    @Basic(optional = false)
    @Column(name = "ID_WAREHOUSE_FK")
    private int idWarehouseFk;

    public StockPK() {
    }

    public StockPK(Integer idProductFk, int idWarehouseFk) {
        this.idProductFk = idProductFk;
        this.idWarehouseFk = idWarehouseFk;
    }

    public Integer getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(Integer idProductFk) {
        this.idProductFk = idProductFk;
    }

    public int getIdWarehouseFk() {
        return idWarehouseFk;
    }

    public void setIdWarehouseFk(int idWarehouseFk) {
        this.idWarehouseFk = idWarehouseFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductFk != null ? idProductFk.hashCode() : 0);
        hash += (int) idWarehouseFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockPK)) {
            return false;
        }
        StockPK other = (StockPK) object;
        if ((this.idProductFk == null && other.idProductFk != null) || (this.idProductFk != null && !this.idProductFk.equals(other.idProductFk))) {
            return false;
        }
        if (this.idWarehouseFk != other.idWarehouseFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.StockPK[ idProductFk=" + idProductFk + ", idWarehouseFk=" + idWarehouseFk + " ]";
    }
    
}
