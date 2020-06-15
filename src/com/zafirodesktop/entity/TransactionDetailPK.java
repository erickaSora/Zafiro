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
public class TransactionDetailPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ID_PRODUCT_FK")
    private Integer idProductFk;
    @Basic(optional = false)
    @Column(name = "ID_TRANSACTION_FK")
    private int idTransactionFk;

    public TransactionDetailPK() {
    }

    public TransactionDetailPK(Integer idProductFk, int idTransactionFk) {
        this.idProductFk = idProductFk;
        this.idTransactionFk = idTransactionFk;
    }

    public Integer getIdProductFk() {
        return idProductFk;
    }

    public void setIdProductFk(Integer idProductFk) {
        this.idProductFk = idProductFk;
    }

    public int getIdTransactionFk() {
        return idTransactionFk;
    }

    public void setIdTransactionFk(int idTransactionFk) {
        this.idTransactionFk = idTransactionFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProductFk != null ? idProductFk.hashCode() : 0);
        hash += (int) idTransactionFk;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransactionDetailPK)) {
            return false;
        }
        TransactionDetailPK other = (TransactionDetailPK) object;
        if ((this.idProductFk == null && other.idProductFk != null) || (this.idProductFk != null && !this.idProductFk.equals(other.idProductFk))) {
            return false;
        }
        if (this.idTransactionFk != other.idTransactionFk) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.TransactionDetailPK[ idProductFk=" + idProductFk + ", idTransactionFk=" + idTransactionFk + " ]";
    }
    
}
