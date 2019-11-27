/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ICE
 */
@Entity
@Table(name = "MEMBERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Members.findAll", query = "SELECT m FROM Members m")
    , @NamedQuery(name = "Members.findByUserno", query = "SELECT m FROM Members m WHERE m.userno = :userno")
    , @NamedQuery(name = "Members.findByUsername", query = "SELECT m FROM Members m WHERE m.username = :username")
    , @NamedQuery(name = "Members.findByPassword", query = "SELECT m FROM Members m WHERE m.password = :password")
    , @NamedQuery(name = "Members.findByFname", query = "SELECT m FROM Members m WHERE m.fname = :fname")
    , @NamedQuery(name = "Members.findByLname", query = "SELECT m FROM Members m WHERE m.lname = :lname")
    , @NamedQuery(name = "Members.findBySchool", query = "SELECT m FROM Members m WHERE m.school = :school")})
public class Members implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "USERNO")
    private Integer userno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "FNAME")
    private String fname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LNAME")
    private String lname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SCHOOL")
    private String school;
    @JoinColumn(name = "DEPARTMENTNO", referencedColumnName = "DEPARTMENTNO")
    @ManyToOne(optional = false)
    private Department departmentno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userno")
    private Collection<Examhistory> examhistoryCollection;

    public Members() {
    }

    public Members(Integer userno) {
        this.userno = userno;
    }

    public Members(Integer userno, String username, String password, String fname, String lname, String school) {
        this.userno = userno;
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.school = school;
    }

    public Integer getUserno() {
        return userno;
    }

    public void setUserno(Integer userno) {
        this.userno = userno;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Department getDepartmentno() {
        return departmentno;
    }

    public void setDepartmentno(Department departmentno) {
        this.departmentno = departmentno;
    }

    @XmlTransient
    public Collection<Examhistory> getExamhistoryCollection() {
        return examhistoryCollection;
    }

    public void setExamhistoryCollection(Collection<Examhistory> examhistoryCollection) {
        this.examhistoryCollection = examhistoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userno != null ? userno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Members)) {
            return false;
        }
        Members other = (Members) object;
        if ((this.userno == null && other.userno != null) || (this.userno != null && !this.userno.equals(other.userno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Members[ userno=" + userno + " ]";
    }
    
}
