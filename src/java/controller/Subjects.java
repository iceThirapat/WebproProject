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
@Table(name = "SUBJECTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Subjects.findAll", query = "SELECT s FROM Subjects s")
    , @NamedQuery(name = "Subjects.findBySubjectname", query = "SELECT s FROM Subjects s WHERE s.subjectname = :subjectname")
    , @NamedQuery(name = "Subjects.findBySubjectdetail", query = "SELECT s FROM Subjects s WHERE s.subjectdetail = :subjectdetail")
    , @NamedQuery(name = "Subjects.findBySubjectno", query = "SELECT s FROM Subjects s WHERE s.subjectno = :subjectno")})
public class Subjects implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SUBJECTNAME")
    private String subjectname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "SUBJECTDETAIL")
    private String subjectdetail;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SUBJECTNO")
    private String subjectno;
    @ManyToMany(mappedBy = "subjectsCollection")
    private Collection<Department> departmentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectno")
    private Collection<Examhistory> examhistoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subjectno")
    private Collection<Question> questionCollection;

    public Subjects() {
    }

    public Subjects(String subjectno) {
        this.subjectno = subjectno;
    }

    public Subjects(String subjectno, String subjectname, String subjectdetail) {
        this.subjectno = subjectno;
        this.subjectname = subjectname;
        this.subjectdetail = subjectdetail;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getSubjectdetail() {
        return subjectdetail;
    }

    public void setSubjectdetail(String subjectdetail) {
        this.subjectdetail = subjectdetail;
    }

    public String getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(String subjectno) {
        this.subjectno = subjectno;
    }

    @XmlTransient
    public Collection<Department> getDepartmentCollection() {
        return departmentCollection;
    }

    public void setDepartmentCollection(Collection<Department> departmentCollection) {
        this.departmentCollection = departmentCollection;
    }

    @XmlTransient
    public Collection<Examhistory> getExamhistoryCollection() {
        return examhistoryCollection;
    }

    public void setExamhistoryCollection(Collection<Examhistory> examhistoryCollection) {
        this.examhistoryCollection = examhistoryCollection;
    }

    @XmlTransient
    public Collection<Question> getQuestionCollection() {
        return questionCollection;
    }

    public void setQuestionCollection(Collection<Question> questionCollection) {
        this.questionCollection = questionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subjectno != null ? subjectno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Subjects)) {
            return false;
        }
        Subjects other = (Subjects) object;
        if ((this.subjectno == null && other.subjectno != null) || (this.subjectno != null && !this.subjectno.equals(other.subjectno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Subjects[ subjectno=" + subjectno + " ]";
    }
    
}
