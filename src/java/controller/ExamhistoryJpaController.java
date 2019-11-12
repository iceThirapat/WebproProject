/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import model.Examhistory;
import model.Members;
import model.Subjects;

/**
 *
 * @author ICE
 */
public class ExamhistoryJpaController implements Serializable {

    public ExamhistoryJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Examhistory examhistory) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Members userno = examhistory.getUserno();
            if (userno != null) {
                userno = em.getReference(userno.getClass(), userno.getUserno());
                examhistory.setUserno(userno);
            }
            Subjects subjectno = examhistory.getSubjectno();
            if (subjectno != null) {
                subjectno = em.getReference(subjectno.getClass(), subjectno.getSubjectno());
                examhistory.setSubjectno(subjectno);
            }
            em.persist(examhistory);
            if (userno != null) {
                userno.getExamhistoryCollection().add(examhistory);
                userno = em.merge(userno);
            }
            if (subjectno != null) {
                subjectno.getExamhistoryCollection().add(examhistory);
                subjectno = em.merge(subjectno);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findExamhistory(examhistory.getHistoryno()) != null) {
                throw new PreexistingEntityException("Examhistory " + examhistory + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Examhistory examhistory) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Examhistory persistentExamhistory = em.find(Examhistory.class, examhistory.getHistoryno());
            Members usernoOld = persistentExamhistory.getUserno();
            Members usernoNew = examhistory.getUserno();
            Subjects subjectnoOld = persistentExamhistory.getSubjectno();
            Subjects subjectnoNew = examhistory.getSubjectno();
            if (usernoNew != null) {
                usernoNew = em.getReference(usernoNew.getClass(), usernoNew.getUserno());
                examhistory.setUserno(usernoNew);
            }
            if (subjectnoNew != null) {
                subjectnoNew = em.getReference(subjectnoNew.getClass(), subjectnoNew.getSubjectno());
                examhistory.setSubjectno(subjectnoNew);
            }
            examhistory = em.merge(examhistory);
            if (usernoOld != null && !usernoOld.equals(usernoNew)) {
                usernoOld.getExamhistoryCollection().remove(examhistory);
                usernoOld = em.merge(usernoOld);
            }
            if (usernoNew != null && !usernoNew.equals(usernoOld)) {
                usernoNew.getExamhistoryCollection().add(examhistory);
                usernoNew = em.merge(usernoNew);
            }
            if (subjectnoOld != null && !subjectnoOld.equals(subjectnoNew)) {
                subjectnoOld.getExamhistoryCollection().remove(examhistory);
                subjectnoOld = em.merge(subjectnoOld);
            }
            if (subjectnoNew != null && !subjectnoNew.equals(subjectnoOld)) {
                subjectnoNew.getExamhistoryCollection().add(examhistory);
                subjectnoNew = em.merge(subjectnoNew);
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
                Integer id = examhistory.getHistoryno();
                if (findExamhistory(id) == null) {
                    throw new NonexistentEntityException("The examhistory with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Examhistory examhistory;
            try {
                examhistory = em.getReference(Examhistory.class, id);
                examhistory.getHistoryno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examhistory with id " + id + " no longer exists.", enfe);
            }
            Members userno = examhistory.getUserno();
            if (userno != null) {
                userno.getExamhistoryCollection().remove(examhistory);
                userno = em.merge(userno);
            }
            Subjects subjectno = examhistory.getSubjectno();
            if (subjectno != null) {
                subjectno.getExamhistoryCollection().remove(examhistory);
                subjectno = em.merge(subjectno);
            }
            em.remove(examhistory);
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

    public List<Examhistory> findExamhistoryEntities() {
        return findExamhistoryEntities(true, -1, -1);
    }

    public List<Examhistory> findExamhistoryEntities(int maxResults, int firstResult) {
        return findExamhistoryEntities(false, maxResults, firstResult);
    }

    private List<Examhistory> findExamhistoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Examhistory.class));
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

    public Examhistory findExamhistory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Examhistory.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamhistoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Examhistory> rt = cq.from(Examhistory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
