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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import model.Department;
import model.Members;

/**
 *
 * @author ICE
 */
public class DepartmentJpaController implements Serializable {

    public DepartmentJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Department department) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (department.getSubjectsCollection() == null) {
            department.setSubjectsCollection(new ArrayList<Subjects>());
        }
        if (department.getMembersCollection() == null) {
            department.setMembersCollection(new ArrayList<Members>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Subjects> attachedSubjectsCollection = new ArrayList<Subjects>();
            for (Subjects subjectsCollectionSubjectsToAttach : department.getSubjectsCollection()) {
                subjectsCollectionSubjectsToAttach = em.getReference(subjectsCollectionSubjectsToAttach.getClass(), subjectsCollectionSubjectsToAttach.getSubjectno());
                attachedSubjectsCollection.add(subjectsCollectionSubjectsToAttach);
            }
            department.setSubjectsCollection(attachedSubjectsCollection);
            Collection<Members> attachedMembersCollection = new ArrayList<Members>();
            for (Members membersCollectionMembersToAttach : department.getMembersCollection()) {
                membersCollectionMembersToAttach = em.getReference(membersCollectionMembersToAttach.getClass(), membersCollectionMembersToAttach.getUserno());
                attachedMembersCollection.add(membersCollectionMembersToAttach);
            }
            department.setMembersCollection(attachedMembersCollection);
            em.persist(department);
            for (Subjects subjectsCollectionSubjects : department.getSubjectsCollection()) {
                subjectsCollectionSubjects.getDepartmentCollection().add(department);
                subjectsCollectionSubjects = em.merge(subjectsCollectionSubjects);
            }
            for (Members membersCollectionMembers : department.getMembersCollection()) {
                Department oldDepartmentnoOfMembersCollectionMembers = membersCollectionMembers.getDepartmentno();
                membersCollectionMembers.setDepartmentno(department);
                membersCollectionMembers = em.merge(membersCollectionMembers);
                if (oldDepartmentnoOfMembersCollectionMembers != null) {
                    oldDepartmentnoOfMembersCollectionMembers.getMembersCollection().remove(membersCollectionMembers);
                    oldDepartmentnoOfMembersCollectionMembers = em.merge(oldDepartmentnoOfMembersCollectionMembers);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDepartment(department.getDepartmentno()) != null) {
                throw new PreexistingEntityException("Department " + department + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Department department) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Department persistentDepartment = em.find(Department.class, department.getDepartmentno());
            Collection<Subjects> subjectsCollectionOld = persistentDepartment.getSubjectsCollection();
            Collection<Subjects> subjectsCollectionNew = department.getSubjectsCollection();
            Collection<Members> membersCollectionOld = persistentDepartment.getMembersCollection();
            Collection<Members> membersCollectionNew = department.getMembersCollection();
            List<String> illegalOrphanMessages = null;
            for (Members membersCollectionOldMembers : membersCollectionOld) {
                if (!membersCollectionNew.contains(membersCollectionOldMembers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Members " + membersCollectionOldMembers + " since its departmentno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Subjects> attachedSubjectsCollectionNew = new ArrayList<Subjects>();
            for (Subjects subjectsCollectionNewSubjectsToAttach : subjectsCollectionNew) {
                subjectsCollectionNewSubjectsToAttach = em.getReference(subjectsCollectionNewSubjectsToAttach.getClass(), subjectsCollectionNewSubjectsToAttach.getSubjectno());
                attachedSubjectsCollectionNew.add(subjectsCollectionNewSubjectsToAttach);
            }
            subjectsCollectionNew = attachedSubjectsCollectionNew;
            department.setSubjectsCollection(subjectsCollectionNew);
            Collection<Members> attachedMembersCollectionNew = new ArrayList<Members>();
            for (Members membersCollectionNewMembersToAttach : membersCollectionNew) {
                membersCollectionNewMembersToAttach = em.getReference(membersCollectionNewMembersToAttach.getClass(), membersCollectionNewMembersToAttach.getUserno());
                attachedMembersCollectionNew.add(membersCollectionNewMembersToAttach);
            }
            membersCollectionNew = attachedMembersCollectionNew;
            department.setMembersCollection(membersCollectionNew);
            department = em.merge(department);
            for (Subjects subjectsCollectionOldSubjects : subjectsCollectionOld) {
                if (!subjectsCollectionNew.contains(subjectsCollectionOldSubjects)) {
                    subjectsCollectionOldSubjects.getDepartmentCollection().remove(department);
                    subjectsCollectionOldSubjects = em.merge(subjectsCollectionOldSubjects);
                }
            }
            for (Subjects subjectsCollectionNewSubjects : subjectsCollectionNew) {
                if (!subjectsCollectionOld.contains(subjectsCollectionNewSubjects)) {
                    subjectsCollectionNewSubjects.getDepartmentCollection().add(department);
                    subjectsCollectionNewSubjects = em.merge(subjectsCollectionNewSubjects);
                }
            }
            for (Members membersCollectionNewMembers : membersCollectionNew) {
                if (!membersCollectionOld.contains(membersCollectionNewMembers)) {
                    Department oldDepartmentnoOfMembersCollectionNewMembers = membersCollectionNewMembers.getDepartmentno();
                    membersCollectionNewMembers.setDepartmentno(department);
                    membersCollectionNewMembers = em.merge(membersCollectionNewMembers);
                    if (oldDepartmentnoOfMembersCollectionNewMembers != null && !oldDepartmentnoOfMembersCollectionNewMembers.equals(department)) {
                        oldDepartmentnoOfMembersCollectionNewMembers.getMembersCollection().remove(membersCollectionNewMembers);
                        oldDepartmentnoOfMembersCollectionNewMembers = em.merge(oldDepartmentnoOfMembersCollectionNewMembers);
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
                String id = department.getDepartmentno();
                if (findDepartment(id) == null) {
                    throw new NonexistentEntityException("The department with id " + id + " no longer exists.");
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
            Department department;
            try {
                department = em.getReference(Department.class, id);
                department.getDepartmentno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The department with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Members> membersCollectionOrphanCheck = department.getMembersCollection();
            for (Members membersCollectionOrphanCheckMembers : membersCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Members " + membersCollectionOrphanCheckMembers + " in its membersCollection field has a non-nullable departmentno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Subjects> subjectsCollection = department.getSubjectsCollection();
            for (Subjects subjectsCollectionSubjects : subjectsCollection) {
                subjectsCollectionSubjects.getDepartmentCollection().remove(department);
                subjectsCollectionSubjects = em.merge(subjectsCollectionSubjects);
            }
            em.remove(department);
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

    public List<Department> findDepartmentEntities() {
        return findDepartmentEntities(true, -1, -1);
    }

    public List<Department> findDepartmentEntities(int maxResults, int firstResult) {
        return findDepartmentEntities(false, maxResults, firstResult);
    }

    private List<Department> findDepartmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Department.class));
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

    public Department findDepartment(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Department> rt = cq.from(Department.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
