/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ANNOTATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Annotation.findAll", query = "SELECT a FROM Annotation a"),
    @NamedQuery(name = "Annotation.findById", query = "SELECT a FROM Annotation a WHERE a.idAnnotationPk = :id"),
    @NamedQuery(name = "Annotation.findByIdRemission", query = "SELECT a FROM Annotation a WHERE a.idRemissionFk.idRemissionPk = :id"),
    @NamedQuery(name = "Annotation.findByDescription", query = "SELECT a FROM Annotation a WHERE a.description = :description")})
public class Annotation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ANNOTATION_PK")
    private Integer idAnnotationPk;
    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "ID_REMISSION_FK", referencedColumnName = "ID_REMISSION_PK")
    @ManyToOne(optional = false)
    private Remission idRemissionFk;
    @Column(name = "ANNOTATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date annotationDate = getDefaultDate();

    public Annotation() {
    }

    public Annotation(Integer idAnnotationPk) {
        this.idAnnotationPk = idAnnotationPk;
    }

    public Annotation(Integer idAnnotationPk, String description) {
        this.idAnnotationPk = idAnnotationPk;
        this.description = description;
    }

    public Integer getIdAnnotationPk() {
        return idAnnotationPk;
    }

    public void setIdAnnotationPk(Integer idAnnotationPk) {
        this.idAnnotationPk = idAnnotationPk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Remission getIdRemissionFk() {
        return idRemissionFk;
    }

    public void setIdRemissionFk(Remission idRemissionFk) {
        this.idRemissionFk = idRemissionFk;
    }

    public Date getAnnotationDate() {
        return annotationDate;
    }

    public void setAnnotationDate(Date annotationDate) {
        this.annotationDate = annotationDate;
    }
    
    public static Date getDefaultDate() {
        return getDefaulTimezone(TimeZone.getDefault());
    }

    public static Date getDefaulTimezone(TimeZone timeZone) {
        return Calendar.getInstance(timeZone).getTime();
    }
    
    public String getDate() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(annotationDate);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnnotationPk != null ? idAnnotationPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Annotation)) {
            return false;
        }
        Annotation other = (Annotation) object;
        if ((this.idAnnotationPk == null && other.idAnnotationPk != null) || (this.idAnnotationPk != null && !this.idAnnotationPk.equals(other.idAnnotationPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Annotation[ idAnnotationPk=" + idAnnotationPk + " ]";
    }
    
}
