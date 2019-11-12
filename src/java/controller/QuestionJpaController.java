/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Subjects;
import model.Answer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import model.Question;

/**
 *
 * @author ICE
 */
public class QuestionJpaController implements Serializable {

    public QuestionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Question question) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (question.getAnswerCollection() == null) {
            question.setAnswerCollection(new ArrayList<Answer>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Subjects subjectno = question.getSubjectno();
            if (subjectno != null) {
                subjectno = em.getReference(subjectno.getClass(), subjectno.getSubjectno());
                question.setSubjectno(subjectno);
            }
            Collection<Answer> attachedAnswerCollection = new ArrayList<Answer>();
            for (Answer answerCollectionAnswerToAttach : question.getAnswerCollection()) {
                answerCollectionAnswerToAttach = em.getReference(answerCollectionAnswerToAttach.getClass(), answerCollectionAnswerToAttach.getAnswerno());
                attachedAnswerCollection.add(answerCollectionAnswerToAttach);
            }
            question.setAnswerCollection(attachedAnswerCollection);
            em.persist(question);
            if (subjectno != null) {
                subjectno.getQuestionCollection().add(question);
                subjectno = em.merge(subjectno);
            }
            for (Answer answerCollectionAnswer : question.getAnswerCollection()) {
                Question oldQuestionnoOfAnswerCollectionAnswer = answerCollectionAnswer.getQuestionno();
                answerCollectionAnswer.setQuestionno(question);
                answerCollectionAnswer = em.merge(answerCollectionAnswer);
                if (oldQuestionnoOfAnswerCollectionAnswer != null) {
                    oldQuestionnoOfAnswerCollectionAnswer.getAnswerCollection().remove(answerCollectionAnswer);
                    oldQuestionnoOfAnswerCollectionAnswer = em.merge(oldQuestionnoOfAnswerCollectionAnswer);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findQuestion(question.getQuestionno()) != null) {
                throw new PreexistingEntityException("Question " + question + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Question question) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Question persistentQuestion = em.find(Question.class, question.getQuestionno());
            Subjects subjectnoOld = persistentQuestion.getSubjectno();
            Subjects subjectnoNew = question.getSubjectno();
            Collection<Answer> answerCollectionOld = persistentQuestion.getAnswerCollection();
            Collection<Answer> answerCollectionNew = question.getAnswerCollection();
            List<String> illegalOrphanMessages = null;
            for (Answer answerCollectionOldAnswer : answerCollectionOld) {
                if (!answerCollectionNew.contains(answerCollectionOldAnswer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Answer " + answerCollectionOldAnswer + " since its questionno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (subjectnoNew != null) {
                subjectnoNew = em.getReference(subjectnoNew.getClass(), subjectnoNew.getSubjectno());
                question.setSubjectno(subjectnoNew);
            }
            Collection<Answer> attachedAnswerCollectionNew = new ArrayList<Answer>();
            for (Answer answerCollectionNewAnswerToAttach : answerCollectionNew) {
                answerCollectionNewAnswerToAttach = em.getReference(answerCollectionNewAnswerToAttach.getClass(), answerCollectionNewAnswerToAttach.getAnswerno());
                attachedAnswerCollectionNew.add(answerCollectionNewAnswerToAttach);
            }
            answerCollectionNew = attachedAnswerCollectionNew;
            question.setAnswerCollection(answerCollectionNew);
            question = em.merge(question);
            if (subjectnoOld != null && !subjectnoOld.equals(subjectnoNew)) {
                subjectnoOld.getQuestionCollection().remove(question);
                subjectnoOld = em.merge(subjectnoOld);
            }
            if (subjectnoNew != null && !subjectnoNew.equals(subjectnoOld)) {
                subjectnoNew.getQuestionCollection().add(question);
                subjectnoNew = em.merge(subjectnoNew);
            }
            for (Answer answerCollectionNewAnswer : answerCollectionNew) {
                if (!answerCollectionOld.contains(answerCollectionNewAnswer)) {
                    Question oldQuestionnoOfAnswerCollectionNewAnswer = answerCollectionNewAnswer.getQuestionno();
                    answerCollectionNewAnswer.setQuestionno(question);
                    answerCollectionNewAnswer = em.merge(answerCollectionNewAnswer);
                    if (oldQuestionnoOfAnswerCollectionNewAnswer != null && !oldQuestionnoOfAnswerCollectionNewAnswer.equals(question)) {
                        oldQuestionnoOfAnswerCollectionNewAnswer.getAnswerCollection().remove(answerCollectionNewAnswer);
                        oldQuestionnoOfAnswerCollectionNewAnswer = em.merge(oldQuestionnoOfAnswerCollectionNewAnswer);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = question.getQuestionno();
                if (findQuestion(id) == null) {
                    throw new NonexistentEntityException("The question with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Question question;
            try {
                question = em.getReference(Question.class, id);
                question.getQuestionno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The question with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Answer> answerCollectionOrphanCheck = question.getAnswerCollection();
            for (Answer answerCollectionOrphanCheckAnswer : answerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Question (" + question + ") cannot be destroyed since the Answer " + answerCollectionOrphanCheckAnswer + " in its answerCollection field has a non-nullable questionno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Subjects subjectno = question.getSubjectno();
            if (subjectno != null) {
                subjectno.getQuestionCollection().remove(question);
                subjectno = em.merge(subjectno);
            }
            em.remove(question);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Question> findQuestionEntities() {
        return findQuestionEntities(true, -1, -1);
    }

    public List<Question> findQuestionEntities(int maxResults, int firstResult) {
        return findQuestionEntities(false, maxResults, firstResult);
    }

    private List<Question> findQuestionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Question.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Question findQuestion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Question.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuestionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Question> rt = cq.from(Question.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
