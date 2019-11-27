/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ICE
 */
@Entity
@Table(name = "EXAMHISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Examhistory.findAll", query = "SELECT e FROM Examhistory e")
    , @NamedQuery(name = "Examhistory.findByHistoryno", query = "SELECT e FROM Examhistory e WHERE e.historyno = :historyno")
    , @NamedQuery(name = "Examhistory.findByScore", query = "SELECT e FROM Examhistory e WHERE e.score = :score")})
public class Examhistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "HISTORYNO")
    private Integer historyno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "SCORE")
    private String score;
    @JoinColumn(name = "USERNO", referencedColumnName = "USERNO")
    @ManyToOne(optional = false)
    private Members userno;
    @JoinColumn(name = "SUBJECTNO", referencedColumnName = "SUBJECTNO")
    @ManyToOne(optional = false)
    private Subjects subjectno;

    public Examhistory() {
    }

    public Examhistory(Integer historyno) {
        this.historyno = historyno;
    }

    public Examhistory(Integer historyno, String score) {
        this.historyno = historyno;
        this.score = score;
    }

    public Integer getHistoryno() {
        return historyno;
    }

    public void setHistoryno(Integer historyno) {
        this.historyno = historyno;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Members getUserno() {
        return userno;
    }

    public void setUserno(Members userno) {
        this.userno = userno;
    }

    public Subjects getSubjectno() {
        return subjectno;
    }

    public void setSubjectno(Subjects subjectno) {
        this.subjectno = subjectno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyno != null ? historyno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Examhistory)) {
            return false;
        }
        Examhistory other = (Examhistory) object;
        if ((this.historyno == null && other.historyno != null) || (this.historyno != null && !this.historyno.equals(other.historyno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Examhistory[ historyno=" + historyno + " ]";
    }
    
}
