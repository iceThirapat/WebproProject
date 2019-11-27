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
@Table(name = "QUESTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q")
    , @NamedQuery(name = "Question.findByQuestion", query = "SELECT q FROM Question q WHERE q.question = :question")
    , @NamedQuery(name = "Question.findByQuestionno", query = "SELECT q FROM Question q WHERE q.questionno = :questionno")})
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "QUESTION")
    private String question;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "QUESTIONNO")
    private String questionno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionno")
    private Collection<Answer> answerCollection;
    @JoinColumn(name = "SUBJECTNO", referencedColumnName = "SUBJECTNO")
    @ManyToOne(optional = false)
    private Subjects subjectno;

    public Question() {
    }

    public Question(String questionno) {
        this.questionno = questionno;
    }

    public Question(String questionno, String question) {
        this.questionno = questionno;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionno() {
        return questionno;
    }

    public void setQuestionno(String questionno) {
        this.questionno = questionno;
    }

    @XmlTransient
    public Collection<Answer> getAnswerCollection() {
        return answerCollection;
    }

    public void setAnswerCollection(Collection<Answer> answerCollection) {
        this.answerCollection = answerCollection;
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
        hash += (questionno != null ? questionno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.questionno == null && other.questionno != null) || (this.questionno != null && !this.questionno.equals(other.questionno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Question[ questionno=" + questionno + " ]";
    }
    
}
