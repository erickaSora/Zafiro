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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PLACE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Place.findAll", query = "SELECT p FROM Place p"),
    @NamedQuery(name = "Place.findById", query = "SELECT p FROM Place p WHERE p.idPlacePk = :id"),
    @NamedQuery(name = "Place.findByLike1", query = "SELECT p FROM Place p WHERE p.placeType = 'M' AND LOWER(p.placeName) LIKE :id"),
    @NamedQuery(name = "Place.findByName", query = "SELECT p FROM Place p WHERE p.placeName = :name"),
    @NamedQuery(name = "Place.findByPlaceType", query = "SELECT p FROM Place p WHERE p.placeType = :placeType")})
public class Place implements Serializable {
    @OneToMany(mappedBy = "idPlaceFk")
    private Collection<Person> personCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PLACE_PK")
    private Integer idPlacePk;
    @Basic(optional = false)
    @Column(name = "PLACE_NAME")
    private String placeName;
    @Basic(optional = false)
    @Column(name = "PLACE_TYPE")
    private String placeType;
    @OneToMany(mappedBy = "idPlaceFk")
    private Collection<Place> placeCollection;
    @JoinColumn(name = "ID_PLACE_FK", referencedColumnName = "ID_PLACE_PK")
    @ManyToOne
    private Place idPlaceFk;

    public Place() {
    }

    public Place(Integer idPlacePk) {
        this.idPlacePk = idPlacePk;
    }

    public Place(Integer idPlacePk, String placeName, String placeType) {
        this.idPlacePk = idPlacePk;
        this.placeName = placeName;
        this.placeType = placeType;
    }

    public Integer getIdPlacePk() {
        return idPlacePk;
    }

    public void setIdPlacePk(Integer idPlacePk) {
        this.idPlacePk = idPlacePk;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    @XmlTransient
    public Collection<Place> getPlaceCollection() {
        return placeCollection;
    }

    public void setPlaceCollection(Collection<Place> placeCollection) {
        this.placeCollection = placeCollection;
    }

    public Place getIdPlaceFk() {
        return idPlaceFk;
    }

    public void setIdPlaceFk(Place idPlaceFk) {
        this.idPlaceFk = idPlaceFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlacePk != null ? idPlacePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Place)) {
            return false;
        }
        Place other = (Place) object;
        if ((this.idPlacePk == null && other.idPlacePk != null) || (this.idPlacePk != null && !this.idPlacePk.equals(other.idPlacePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return placeName+" "+idPlaceFk.getPlaceName();
    }

    @XmlTransient
    public Collection<Person> getPersonCollection() {
        return personCollection;
    }

    public void setPersonCollection(Collection<Person> personCollection) {
        this.personCollection = personCollection;
    }
    
}
