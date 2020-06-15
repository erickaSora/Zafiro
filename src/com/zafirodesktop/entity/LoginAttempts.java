/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zafirodesktop.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Digitall
 */
@Entity
@Table(name = "LOGIN_ATTEMPTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoginAttempts.findAll", query = "SELECT l FROM LoginAttempts l"),
    @NamedQuery(name = "LoginAttempts.findById", query = "SELECT l FROM LoginAttempts l WHERE l.id = :id"),
    @NamedQuery(name = "LoginAttempts.findByIpAddress", query = "SELECT l FROM LoginAttempts l WHERE l.ipAddress = :ipAddress"),
    @NamedQuery(name = "LoginAttempts.findByLogin", query = "SELECT l FROM LoginAttempts l WHERE l.login = :login"),
    @NamedQuery(name = "LoginAttempts.findByTime", query = "SELECT l FROM LoginAttempts l WHERE l.time = :time")})
public class LoginAttempts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "TIME")
    private Integer time;

    public LoginAttempts() {
    }

    public LoginAttempts(Integer id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
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
        if (!(object instanceof LoginAttempts)) {
            return false;
        }
        LoginAttempts other = (LoginAttempts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zafirodesktop.entity.LoginAttempts[ id=" + id + " ]";
    }
    
}
