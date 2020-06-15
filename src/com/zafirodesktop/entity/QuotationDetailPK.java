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
public class QuotationDetailPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_QUOTATION_FK")
    private int idQuotationFk;
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_FK")
    private Integer idProductFk;

    public QuotationDetailPK() {
    }

    public QuotationDetailPK(int idQuotationFk, Integer idProductFk) {
        this.idQuotationFk = idQuotationFk;
        this.idProductFk = idProductFk;
    }

    public int getIdQuotationFk() {
        return idQuotationFk;
    }

    public void setIdQuotationFk(int idQuotationFk) {
        this.idQuotationFk = idQuotationFk;
    }

    public Integer getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(Integer idProductFk) {
        this.idProductFk = idProductFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idQuotationFk;
        hash += (idProductFk != null ? idProductFk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationDetailPK)) {
            return false;
        }
        QuotationDetailPK other = (QuotationDetailPK) object;
        if (this.idQuotationFk != other.idQuotationFk) {
            return false;
        }
        if ((this.idProductFk == null && other.idProductFk != null) || (this.idProductFk != null && !this.idProductFk.equals(other.idProductFk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.QuotationDetailPK[ idQuotationFk=" + idQuotationFk + ", idProductFk=" + idProductFk + " ]";
    }
    
}
