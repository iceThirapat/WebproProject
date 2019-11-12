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
import model.Department;
import model.Examhistory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import model.Members;

/**
 *
 * @author ICE
 */
public class MembersJpaController implements Serializable {

    public MembersJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Members members) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (members.getExamhistoryCollection() == null) {
            members.setExamhistoryCollection(new ArrayList<Examhistory>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Department departmentno = members.getDepartmentno();
            if (departmentno != null) {
                departmentno = em.getReference(departmentno.getClass(), departmentno.getDepartmentno());
                members.setDepartmentno(departmentno);
            }
            Collection<Examhistory> attachedExamhistoryCollection = new ArrayList<Examhistory>();
            for (Examhistory examhistoryCollectionExamhistoryToAttach : members.getExamhistoryCollection()) {
                examhistoryCollectionExamhistoryToAttach = em.getReference(examhistoryCollectionExamhistoryToAttach.getClass(), examhistoryCollectionExamhistoryToAttach.getHistoryno());
                attachedExamhistoryCollection.add(examhistoryCollectionExamhistoryToAttach);
            }
            members.setExamhistoryCollection(attachedExamhistoryCollection);
            em.persist(members);
            if (departmentno != null) {
                departmentno.getMembersCollection().add(members);
                departmentno = em.merge(departmentno);
            }
            for (Examhistory examhistoryCollectionExamhistory : members.getExamhistoryCollection()) {
                Members oldUsernoOfExamhistoryCollectionExamhistory = examhistoryCollectionExamhistory.getUserno();
                examhistoryCollectionExamhistory.setUserno(members);
                examhistoryCollectionExamhistory = em.merge(examhistoryCollectionExamhistory);
                if (oldUsernoOfExamhistoryCollectionExamhistory != null) {
                    oldUsernoOfExamhistoryCollectionExamhistory.getExamhistoryCollection().remove(examhistoryCollectionExamhistory);
                    oldUsernoOfExamhistoryCollectionExamhistory = em.merge(oldUsernoOfExamhistoryCollectionExamhistory);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMembers(members.getUserno()) != null) {
                throw new PreexistingEntityException("Members " + members + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Members members) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Members persistentMembers = em.find(Members.class, members.getUserno());
            Department departmentnoOld = persistentMembers.getDepartmentno();
            Department departmentnoNew = members.getDepartmentno();
            Collection<Examhistory> examhistoryCollectionOld = persistentMembers.getExamhistoryCollection();
            Collection<Examhistory> examhistoryCollectionNew = members.getExamhistoryCollection();
            List<String> illegalOrphanMessages = null;
            for (Examhistory examhistoryCollectionOldExamhistory : examhistoryCollectionOld) {
                if (!examhistoryCollectionNew.contains(examhistoryCollectionOldExamhistory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examhistory " + examhistoryCollectionOldExamhistory + " since its userno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departmentnoNew != null) {
                departmentnoNew = em.getReference(departmentnoNew.getClass(), departmentnoNew.getDepartmentno());
                members.setDepartmentno(departmentnoNew);
            }
            Collection<Examhistory> attachedExamhistoryCollectionNew = new ArrayList<Examhistory>();
            for (Examhistory examhistoryCollectionNewExamhistoryToAttach : examhistoryCollectionNew) {
                examhistoryCollectionNewExamhistoryToAttach = em.getReference(examhistoryCollectionNewExamhistoryToAttach.getClass(), examhistoryCollectionNewExamhistoryToAttach.getHistoryno());
                attachedExamhistoryCollectionNew.add(examhistoryCollectionNewExamhistoryToAttach);
            }
            examhistoryCollectionNew = attachedExamhistoryCollectionNew;
            members.setExamhistoryCollection(examhistoryCollectionNew);
            members = em.merge(members);
            if (departmentnoOld != null && !departmentnoOld.equals(departmentnoNew)) {
                departmentnoOld.getMembersCollection().remove(members);
                departmentnoOld = em.merge(departmentnoOld);
            }
            if (departmentnoNew != null && !departmentnoNew.equals(departmentnoOld)) {
                departmentnoNew.getMembersCollection().add(members);
                departmentnoNew = em.merge(departmentnoNew);
            }
            for (Examhistory examhistoryCollectionNewExamhistory : examhistoryCollectionNew) {
                if (!examhistoryCollectionOld.contains(examhistoryCollectionNewExamhistory)) {
                    Members oldUsernoOfExamhistoryCollectionNewExamhistory = examhistoryCollectionNewExamhistory.getUserno();
                    examhistoryCollectionNewExamhistory.setUserno(members);
                    examhistoryCollectionNewExamhistory = em.merge(examhistoryCollectionNewExamhistory);
                    if (oldUsernoOfExamhistoryCollectionNewExamhistory != null && !oldUsernoOfExamhistoryCollectionNewExamhistory.equals(members)) {
                        oldUsernoOfExamhistoryCollectionNewExamhistory.getExamhistoryCollection().remove(examhistoryCollectionNewExamhistory);
                        oldUsernoOfExamhistoryCollectionNewExamhistory = em.merge(oldUsernoOfExamhistoryCollectionNewExamhistory);
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
                Integer id = members.getUserno();
                if (findMembers(id) == null) {
                    throw new NonexistentEntityException("The members with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Members members;
            try {
                members = em.getReference(Members.class, id);
                members.getUserno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The members with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Examhistory> examhistoryCollectionOrphanCheck = members.getExamhistoryCollection();
            for (Examhistory examhistoryCollectionOrphanCheckExamhistory : examhistoryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Members (" + members + ") cannot be destroyed since the Examhistory " + examhistoryCollectionOrphanCheckExamhistory + " in its examhistoryCollection field has a non-nullable userno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Department departmentno = members.getDepartmentno();
            if (departmentno != null) {
                departmentno.getMembersCollection().remove(members);
                departmentno = em.merge(departmentno);
            }
            em.remove(members);
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

    public List<Members> findMembersEntities() {
        return findMembersEntities(true, -1, -1);
    }

    public List<Members> findMembersEntities(int maxResults, int firstResult) {
        return findMembersEntities(false, maxResults, firstResult);
    }

    private List<Members> findMembersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Members.class));
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

    public Members findMembers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Members.class, id);
        } finally {
            em.close();
        }
    }

    public int getMembersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Members> rt = cq.from(Members.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
