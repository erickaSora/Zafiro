/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByName", query = "SELECT u FROM Users u WHERE u.username = :name"),
    @NamedQuery(name = "Users.findByCreatedOn", query = "SELECT u FROM Users u WHERE u.createdOn = :id"),
    @NamedQuery(name = "Users.findByUserPass", query = "SELECT u FROM Users u WHERE u.username = :id AND u.password = :passwd"),
    @NamedQuery(name = "Users.findByLike", query = "SELECT u FROM Users u WHERE LOWER(u.username) LIKE :id OR LOWER(u.firstName) LIKE :txt2 "
            + "OR LOWER(u.lastName) LIKE :txt3")})
public class Users implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUserFk")
    private Collection<Turn> turnCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "SALT")
    private String salt;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ACTIVATION_CODE")
    private String activationCode;
    @Column(name = "FORGOTTEN_PASSWORD_CODE")
    private String forgottenPasswordCode;
    @Column(name = "FORGOTTEN_PASSWORD_TIME")
    private Integer forgottenPasswordTime;
    @Column(name = "REMEMBER_CODE")
    private String rememberCode;
    @Column(name = "CREATED_ON")
    private Integer createdOn;
    @Column(name = "LAST_LOGIN")
    private Integer lastLogin;
    @Basic(optional = false)
    @Column(name = "ACTIVE")
    private short active;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "COMPANY")
    private String company;
    @Column(name = "PHONE")
    private String phone;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(String username, String password, short active) {
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getForgottenPasswordCode() {
        return forgottenPasswordCode;
    }

    public void setForgottenPasswordCode(String forgottenPasswordCode) {
        this.forgottenPasswordCode = forgottenPasswordCode;
    }

    public Integer getForgottenPasswordTime() {
        return forgottenPasswordTime;
    }

    public void setForgottenPasswordTime(Integer forgottenPasswordTime) {
        this.forgottenPasswordTime = forgottenPasswordTime;
    }

    public String getRememberCode() {
        return rememberCode;
    }

    public void setRememberCode(String rememberCode) {
        this.rememberCode = rememberCode;
    }

    public Integer getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Integer createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Integer lastLogin) {
        this.lastLogin = lastLogin;
    }

    public short getActive() {
        return active;
    }

    public void setActive(short active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getType(){
        if(createdOn.equals(1))
            return "Administrador";
        else
            return "Vendedor";
    }
    
    public String getTotalName(){
        if(lastName != null)
            return firstName+" "+lastName;
        else
            return firstName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName;
    }

    @XmlTransient
    public Collection<Turn> getTurnCollection() {
        return turnCollection;
    }

    public void setTurnCollection(Collection<Turn> turnCollection) {
        this.turnCollection = turnCollection;
    }
    
}
