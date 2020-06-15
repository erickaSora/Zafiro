/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "OFFICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Office.findAll", query = "SELECT o FROM Office o"),
    @NamedQuery(name = "Office.findByIdPlaceFk", query = "SELECT o FROM Office o WHERE o.officePK.idPlaceFk = :idPlaceFk"),
    @NamedQuery(name = "Office.findByIdSupplierFk", query = "SELECT o FROM Office o WHERE o.officePK.idSupplierFk = :idSupplierFk")})
public class Office implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OfficePK officePK;

    public Office() {
    }

    public Office(OfficePK officePK) {
        this.officePK = officePK;
    }

    public Office(int idPlaceFk, Integer idSupplierFk) {
        this.officePK = new OfficePK(idPlaceFk, idSupplierFk);
    }

    public OfficePK getOfficePK() {
        return officePK;
    }

    public void setOfficePK(OfficePK officePK) {
        this.officePK = officePK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (officePK != null ? officePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Office)) {
            return false;
        }
        Office other = (Office) object;
        if ((this.officePK == null && other.officePK != null) || (this.officePK != null && !this.officePK.equals(other.officePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Office[ officePK=" + officePK + " ]";
    }
    
}
