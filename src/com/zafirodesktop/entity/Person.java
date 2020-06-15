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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "PERSON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.nit = :id"),
    @NamedQuery(name = "Person.findByRealId", query = "SELECT p FROM Person p WHERE p.idPersonPk = :id"),
    @NamedQuery(name = "Person.findByLike", query = "SELECT p FROM Person p WHERE LOWER(p.nit) LIKE :id "
            + "OR LOWER(CONCAT(p.personFirstname,' ',p.personLastname))" + "LIKE :txt2 OR p.personPhoneNo LIKE :txt3"),
    @NamedQuery(name = "Client.findByLike", query = "SELECT p FROM Person p WHERE p.personType = 'C' AND p IN "
            + "(SELECT p FROM Person p WHERE LOWER(p.nit) LIKE :id OR LOWER(CONCAT(p.personFirstname,' ',p.personLastname)) "
            + "LIKE :txt2 OR p.personPhoneNo LIKE :txt3)"),
    @NamedQuery(name = "Supplier.findByLike", query = "SELECT p FROM Person p WHERE p.personType = 'S' AND p IN "
            + "(SELECT p FROM Person p WHERE LOWER(p.nit) LIKE :id OR LOWER(CONCAT(p.personFirstname,' ',p.personLastname)) "
            + "LIKE :txt2 OR p.personPhoneNo LIKE :txt3)"),
    @NamedQuery(name = "Person.findByPersonFirstname", query = "SELECT p FROM Person p WHERE p.personFirstname = :personFirstname"),
    @NamedQuery(name = "Person.findByPersonLastname", query = "SELECT p FROM Person p WHERE p.personLastname = :personLastname"),
    @NamedQuery(name = "Person.findByPersonPhoneNo", query = "SELECT p FROM Person p WHERE p.personPhoneNo = :personPhoneNo"),
    @NamedQuery(name = "Person.findByPersonAddress", query = "SELECT p FROM Person p WHERE p.personAddress = :personAddress"),
    @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
    @NamedQuery(name = "Person.findByPersonType", query = "SELECT p FROM Person p WHERE p.personType = :personType"),
    @NamedQuery(name = "Person.findByPurchases", query = "SELECT p FROM Person p WHERE p.purchases = :purchases"),
    @NamedQuery(name = "Person.findByOrders", query = "SELECT p FROM Person p WHERE p.orders = :orders")})
public class Person implements Serializable {
    @OneToMany(mappedBy = "idClientFk")
    private Collection<Quotation> quotationCollection;
    @OneToMany(mappedBy = "idSupplierFk")
    private Collection<Quotation> quotationCollection1;
    @Basic(optional = false)
    @Column(name = "NIT")
    private String nit;
    @JoinTable(name = "OFFICE", joinColumns = {
        @JoinColumn(name = "ID_SUPPLIER_FK", referencedColumnName = "ID_PERSON_PK")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_PLACE_FK", referencedColumnName = "ID_PLACE_PK")})
    @ManyToMany
    private Collection<Place> placeCollection;
    @OneToMany(mappedBy = "idClientFk")
    private Collection<Remission> remissionCollection;
    @OneToMany(mappedBy = "idSupplierFk")
    private Collection<Remission> remissionCollection1;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PERSON_PK")
    private Integer idPersonPk;
    @Basic(optional = false)
    @Column(name = "PERSON_FIRSTNAME")
    private String personFirstname;
    @Column(name = "PERSON_LASTNAME")
    private String personLastname;
    @Column(name = "PERSON_PHONE_NO")
    private String personPhoneNo;
    @Column(name = "PERSON_ADDRESS")
    private String personAddress;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "BANK")
    private String bank;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Basic(optional = false)
    @Column(name = "PERSON_TYPE")
    private String personType = "C";
    @Column(name = "PURCHASES")
    private Integer purchases;
    @Column(name = "ORDERS")
    private Integer orders;
    @JoinColumn(name = "ID_PLACE_FK", referencedColumnName = "ID_PLACE_PK")
    @ManyToOne
    private Place idPlaceFk;

    public Person() {
    }

    public Person(Integer idPersonPk) {
        this.idPersonPk = idPersonPk;
    }

    public Person(Integer idPersonPk, String personFirstname, String personType) {
        this.idPersonPk = idPersonPk;
        this.personFirstname = personFirstname;
        this.personType = personType;
    }

    public Integer getIdPersonPk() {
        return idPersonPk;
    }

    public void setIdPersonPk(Integer idPersonPk) {
        this.idPersonPk = idPersonPk;
    }

    public String getPersonFirstname() {
        return personFirstname;
    }

    public void setPersonFirstname(String personFirstname) {
        this.personFirstname = personFirstname;
    }

    public String getPersonLastname() {
        return personLastname;
    }

    public void setPersonLastname(String personLastname) {
        this.personLastname = personLastname;
    }

    public String getPersonPhoneNo() {
        return personPhoneNo;
    }

    public void setPersonPhoneNo(String personPhoneNo) {
        this.personPhoneNo = personPhoneNo;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public Integer getPurchases() {
        return purchases;
    }

    public void setPurchases(Integer purchases) {
        this.purchases = purchases;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Place getIdPlaceFk() {
        return idPlaceFk;
    }

    public void setIdPlaceFk(Place idPlaceFk) {
        this.idPlaceFk = idPlaceFk;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getTotalName(){
        return personFirstname+" "+personLastname;
    }
    
    public String getType(){
        if(personType.equals("C"))
            return "Cliente";
        else           
            return "Proveedor";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersonPk != null ? idPersonPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.idPersonPk == null && other.idPersonPk != null) || (this.idPersonPk != null && !this.idPersonPk.equals(other.idPersonPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nit+" - "+personFirstname+" "+personLastname;
    }

    @XmlTransient
    public Collection<Remission> getRemissionCollection() {
        return remissionCollection;
    }

    public void setRemissionCollection(Collection<Remission> remissionCollection) {
        this.remissionCollection = remissionCollection;
    }

    @XmlTransient
    public Collection<Remission> getRemissionCollection1() {
        return remissionCollection1;
    }

    public void setRemissionCollection1(Collection<Remission> remissionCollection1) {
        this.remissionCollection1 = remissionCollection1;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    @XmlTransient
    public Collection<Place> getPlaceCollection() {
        return placeCollection;
    }

    public void setPlaceCollection(Collection<Place> placeCollection) {
        this.placeCollection = placeCollection;
    }

    @XmlTransient
    public Collection<Quotation> getQuotationCollection() {
        return quotationCollection;
    }

    public void setQuotationCollection(Collection<Quotation> quotationCollection) {
        this.quotationCollection = quotationCollection;
    }

    @XmlTransient
    public Collection<Quotation> getQuotationCollection1() {
        return quotationCollection1;
    }

    public void setQuotationCollection1(Collection<Quotation> quotationCollection1) {
        this.quotationCollection1 = quotationCollection1;
    }
    
}
