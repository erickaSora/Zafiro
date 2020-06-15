/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "CONCEPT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Concept.findAll", query = "SELECT c FROM Concept c"),
    @NamedQuery(name = "Concept.findById", query = "SELECT c FROM Concept c WHERE c.idConceptPk = :id"),
    @NamedQuery(name = "Concept.findByConceptType", query = "SELECT c FROM Concept c WHERE c.conceptType = :conceptType"),
    @NamedQuery(name = "Concept.findByConceptName", query = "SELECT c FROM Concept c WHERE c.conceptName = :conceptName"),
    @NamedQuery(name = "Concept.findByConceptDescription", query = "SELECT c FROM Concept c WHERE c.conceptDescription = :conceptDescription")})
public class Concept implements Serializable {
    @OneToMany(mappedBy = "idConceptFk")
    private Collection<Remission> remissionCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CONCEPT_PK")
    private Integer idConceptPk;
    @Basic(optional = false)
    @Column(name = "CONCEPT_TYPE")
    private String conceptType;
    @Column(name = "CONCEPT_NAME")
    private String conceptName;
    @Column(name = "CONCEPT_DESCRIPTION")
    private String conceptDescription;

    public Concept() {
    }

    public Concept(Integer idConceptPk) {
        this.idConceptPk = idConceptPk;
    }

    public Concept(Integer idConceptPk, String conceptType) {
        this.idConceptPk = idConceptPk;
        this.conceptType = conceptType;
    }

    public Integer getIdConceptPk() {
        return idConceptPk;
    }

    public void setIdConceptPk(Integer idConceptPk) {
        this.idConceptPk = idConceptPk;
    }

    public String getConceptType() {
        return conceptType;
    }

    public void setConceptType(String conceptType) {
        this.conceptType = conceptType;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getConceptDescription() {
        return conceptDescription;
    }

    public void setConceptDescription(String conceptDescription) {
        this.conceptDescription = conceptDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConceptPk != null ? idConceptPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Concept)) {
            return false;
        }
        Concept other = (Concept) object;
        if ((this.idConceptPk == null && other.idConceptPk != null) || (this.idConceptPk != null && !this.idConceptPk.equals(other.idConceptPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.Concept[ idConceptPk=" + idConceptPk + " ]";
    }

    @XmlTransient
    public Collection<Remission> getRemissionCollection() {
        return remissionCollection;
    }

    public void setRemissionCollection(Collection<Remission> remissionCollection) {
        this.remissionCollection = remissionCollection;
    }
    
}
