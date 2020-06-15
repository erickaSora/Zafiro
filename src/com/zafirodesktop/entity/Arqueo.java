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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "ARQUEO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arqueo.findAll", query = "SELECT a FROM Arqueo a"),
    @NamedQuery(name = "Arqueo.findByIdArqueoPk", query = "SELECT a FROM Arqueo a WHERE a.idArqueoPk = :idArqueoPk"),
    @NamedQuery(name = "Arqueo.findByFechaArqueo", query = "SELECT a FROM Arqueo a WHERE a.fechaArqueo = :fechaArqueo")})
public class Arqueo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_ARQUEO_PK")
    private Integer idArqueoPk;
    @Basic(optional = false)
    @Column(name = "FECHA_ARQUEO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaArqueo;

    public Arqueo() {
    }

    public Arqueo(Integer idArqueoPk) {
        this.idArqueoPk = idArqueoPk;
    }

    public Arqueo(Integer idArqueoPk, Date fechaArqueo) {
        this.idArqueoPk = idArqueoPk;
        this.fechaArqueo = fechaArqueo;
    }

    public Integer getIdArqueoPk() {
        return idArqueoPk;
    }

    public void setIdArqueoPk(Integer idArqueoPk) {
        this.idArqueoPk = idArqueoPk;
    }

    public Date getFechaArqueo() {
        return fechaArqueo;
    }

    public void setFechaArqueo(Date fechaArqueo) {
        this.fechaArqueo = fechaArqueo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArqueoPk != null ? idArqueoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arqueo)) {
            return false;
        }
        Arqueo other = (Arqueo) object;
        if ((this.idArqueoPk == null && other.idArqueoPk != null) || (this.idArqueoPk != null && !this.idArqueoPk.equals(other.idArqueoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Arqueo[ idArqueoPk=" + idArqueoPk + " ]";
    }
    
}
