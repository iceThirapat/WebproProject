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
@Table(name = "ANSWER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
    , @NamedQuery(name = "Answer.findByAnswer", query = "SELECT a FROM Answer a WHERE a.answer = :answer")
    , @NamedQuery(name = "Answer.findByIsright", query = "SELECT a FROM Answer a WHERE a.isright = :isright")
    , @NamedQuery(name = "Answer.findByAnswerno", query = "SELECT a FROM Answer a WHERE a.answerno = :answerno")})
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ANSWER")
    private String answer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISRIGHT")
    private Character isright;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "ANSWERNO")
    private String answerno;
    @JoinColumn(name = "QUESTIONNO", referencedColumnName = "QUESTIONNO")
    @ManyToOne(optional = false)
    private Question questionno;

    public Answer() {
    }

    public Answer(String answerno) {
        this.answerno = answerno;
    }

    public Answer(String answerno, String answer, Character isright) {
        this.answerno = answerno;
        this.answer = answer;
        this.isright = isright;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Character getIsright() {
        return isright;
    }

    public void setIsright(Character isright) {
        this.isright = isright;
    }

    public String getAnswerno() {
        return answerno;
    }

    public void setAnswerno(String answerno) {
        this.answerno = answerno;
    }

    public Question getQuestionno() {
        return questionno;
    }

    public void setQuestionno(Question questionno) {
        this.questionno = questionno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (answerno != null ? answerno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Answer)) {
            return false;
        }
        Answer other = (Answer) object;
        if ((this.answerno == null && other.answerno != null) || (this.answerno != null && !this.answerno.equals(other.answerno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Answer[ answerno=" + answerno + " ]";
    }
    
}
