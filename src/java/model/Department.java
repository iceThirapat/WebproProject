/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "DEPARTMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Department.findAll", query = "SELECT d FROM Department d")
    , @NamedQuery(name = "Department.findByDepartmentno", query = "SELECT d FROM Department d WHERE d.departmentno = :departmentno")
    , @NamedQuery(name = "Department.findByDepartmentname", query = "SELECT d FROM Department d WHERE d.departmentname = :departmentname")})
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "DEPARTMENTNO")
    private String departmentno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "DEPARTMENTNAME")
    private String departmentname;
    @JoinTable(name = "RELATIONDEPARTSUBJECT", joinColumns = {
        @JoinColumn(name = "DEPARTMENTNO", referencedColumnName = "DEPARTMENTNO")}, inverseJoinColumns = {
        @JoinColumn(name = "SUBJECTNO", referencedColumnName = "SUBJECTNO")})
    @ManyToMany
    private Collection<Subjects> subjectsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departmentno")
    private Collection<Members> membersCollection;

    public Department() {
    }

    public Department(String departmentno) {
        this.departmentno = departmentno;
    }

    public Department(String departmentno, String departmentname) {
        this.departmentno = departmentno;
        this.departmentname = departmentname;
    }

    public String getDepartmentno() {
        return departmentno;
    }

    public void setDepartmentno(String departmentno) {
        this.departmentno = departmentno;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    @XmlTransient
    public Collection<Subjects> getSubjectsCollection() {
        return subjectsCollection;
    }

    public void setSubjectsCollection(Collection<Subjects> subjectsCollection) {
        this.subjectsCollection = subjectsCollection;
    }

    @XmlTransient
    public Collection<Members> getMembersCollection() {
        return membersCollection;
    }

    public void setMembersCollection(Collection<Members> membersCollection) {
        this.membersCollection = membersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (departmentno != null ? departmentno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Department)) {
            return false;
        }
        Department other = (Department) object;
        if ((this.departmentno == null && other.departmentno != null) || (this.departmentno != null && !this.departmentno.equals(other.departmentno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Department[ departmentno=" + departmentno + " ]";
    }
    
}
