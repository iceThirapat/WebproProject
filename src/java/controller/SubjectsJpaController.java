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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import model.Examhistory;
import model.Question;
import model.Subjects;

/**
 *
 * @author ICE
 */
public class SubjectsJpaController implements Serializable {

    public SubjectsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subjects subjects) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (subjects.getDepartmentCollection() == null) {
            subjects.setDepartmentCollection(new ArrayList<Department>());
        }
        if (subjects.getExamhistoryCollection() == null) {
            subjects.setExamhistoryCollection(new ArrayList<Examhistory>());
        }
        if (subjects.getQuestionCollection() == null) {
            subjects.setQuestionCollection(new ArrayList<Question>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Department> attachedDepartmentCollection = new ArrayList<Department>();
            for (Department departmentCollectionDepartmentToAttach : subjects.getDepartmentCollection()) {
                departmentCollectionDepartmentToAttach = em.getReference(departmentCollectionDepartmentToAttach.getClass(), departmentCollectionDepartmentToAttach.getDepartmentno());
                attachedDepartmentCollection.add(departmentCollectionDepartmentToAttach);
            }
            subjects.setDepartmentCollection(attachedDepartmentCollection);
            Collection<Examhistory> attachedExamhistoryCollection = new ArrayList<Examhistory>();
            for (Examhistory examhistoryCollectionExamhistoryToAttach : subjects.getExamhistoryCollection()) {
                examhistoryCollectionExamhistoryToAttach = em.getReference(examhistoryCollectionExamhistoryToAttach.getClass(), examhistoryCollectionExamhistoryToAttach.getHistoryno());
                attachedExamhistoryCollection.add(examhistoryCollectionExamhistoryToAttach);
            }
            subjects.setExamhistoryCollection(attachedExamhistoryCollection);
            Collection<Question> attachedQuestionCollection = new ArrayList<Question>();
            for (Question questionCollectionQuestionToAttach : subjects.getQuestionCollection()) {
                questionCollectionQuestionToAttach = em.getReference(questionCollectionQuestionToAttach.getClass(), questionCollectionQuestionToAttach.getQuestionno());
                attachedQuestionCollection.add(questionCollectionQuestionToAttach);
            }
            subjects.setQuestionCollection(attachedQuestionCollection);
            em.persist(subjects);
            for (Department departmentCollectionDepartment : subjects.getDepartmentCollection()) {
                departmentCollectionDepartment.getSubjectsCollection().add(subjects);
                departmentCollectionDepartment = em.merge(departmentCollectionDepartment);
            }
            for (Examhistory examhistoryCollectionExamhistory : subjects.getExamhistoryCollection()) {
                Subjects oldSubjectnoOfExamhistoryCollectionExamhistory = examhistoryCollectionExamhistory.getSubjectno();
                examhistoryCollectionExamhistory.setSubjectno(subjects);
                examhistoryCollectionExamhistory = em.merge(examhistoryCollectionExamhistory);
                if (oldSubjectnoOfExamhistoryCollectionExamhistory != null) {
                    oldSubjectnoOfExamhistoryCollectionExamhistory.getExamhistoryCollection().remove(examhistoryCollectionExamhistory);
                    oldSubjectnoOfExamhistoryCollectionExamhistory = em.merge(oldSubjectnoOfExamhistoryCollectionExamhistory);
                }
            }
            for (Question questionCollectionQuestion : subjects.getQuestionCollection()) {
                Subjects oldSubjectnoOfQuestionCollectionQuestion = questionCollectionQuestion.getSubjectno();
                questionCollectionQuestion.setSubjectno(subjects);
                questionCollectionQuestion = em.merge(questionCollectionQuestion);
                if (oldSubjectnoOfQuestionCollectionQuestion != null) {
                    oldSubjectnoOfQuestionCollectionQuestion.getQuestionCollection().remove(questionCollectionQuestion);
                    oldSubjectnoOfQuestionCollectionQuestion = em.merge(oldSubjectnoOfQuestionCollectionQuestion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findSubjects(subjects.getSubjectno()) != null) {
                throw new PreexistingEntityException("Subjects " + subjects + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subjects subjects) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Subjects persistentSubjects = em.find(Subjects.class, subjects.getSubjectno());
            Collection<Department> departmentCollectionOld = persistentSubjects.getDepartmentCollection();
            Collection<Department> departmentCollectionNew = subjects.getDepartmentCollection();
            Collection<Examhistory> examhistoryCollectionOld = persistentSubjects.getExamhistoryCollection();
            Collection<Examhistory> examhistoryCollectionNew = subjects.getExamhistoryCollection();
            Collection<Question> questionCollectionOld = persistentSubjects.getQuestionCollection();
            Collection<Question> questionCollectionNew = subjects.getQuestionCollection();
            List<String> illegalOrphanMessages = null;
            for (Examhistory examhistoryCollectionOldExamhistory : examhistoryCollectionOld) {
                if (!examhistoryCollectionNew.contains(examhistoryCollectionOldExamhistory)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examhistory " + examhistoryCollectionOldExamhistory + " since its subjectno field is not nullable.");
                }
            }
            for (Question questionCollectionOldQuestion : questionCollectionOld) {
                if (!questionCollectionNew.contains(questionCollectionOldQuestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Question " + questionCollectionOldQuestion + " since its subjectno field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Department> attachedDepartmentCollectionNew = new ArrayList<Department>();
            for (Department departmentCollectionNewDepartmentToAttach : departmentCollectionNew) {
                departmentCollectionNewDepartmentToAttach = em.getReference(departmentCollectionNewDepartmentToAttach.getClass(), departmentCollectionNewDepartmentToAttach.getDepartmentno());
                attachedDepartmentCollectionNew.add(departmentCollectionNewDepartmentToAttach);
            }
            departmentCollectionNew = attachedDepartmentCollectionNew;
            subjects.setDepartmentCollection(departmentCollectionNew);
            Collection<Examhistory> attachedExamhistoryCollectionNew = new ArrayList<Examhistory>();
            for (Examhistory examhistoryCollectionNewExamhistoryToAttach : examhistoryCollectionNew) {
                examhistoryCollectionNewExamhistoryToAttach = em.getReference(examhistoryCollectionNewExamhistoryToAttach.getClass(), examhistoryCollectionNewExamhistoryToAttach.getHistoryno());
                attachedExamhistoryCollectionNew.add(examhistoryCollectionNewExamhistoryToAttach);
            }
            examhistoryCollectionNew = attachedExamhistoryCollectionNew;
            subjects.setExamhistoryCollection(examhistoryCollectionNew);
            Collection<Question> attachedQuestionCollectionNew = new ArrayList<Question>();
            for (Question questionCollectionNewQuestionToAttach : questionCollectionNew) {
                questionCollectionNewQuestionToAttach = em.getReference(questionCollectionNewQuestionToAttach.getClass(), questionCollectionNewQuestionToAttach.getQuestionno());
                attachedQuestionCollectionNew.add(questionCollectionNewQuestionToAttach);
            }
            questionCollectionNew = attachedQuestionCollectionNew;
            subjects.setQuestionCollection(questionCollectionNew);
            subjects = em.merge(subjects);
            for (Department departmentCollectionOldDepartment : departmentCollectionOld) {
                if (!departmentCollectionNew.contains(departmentCollectionOldDepartment)) {
                    departmentCollectionOldDepartment.getSubjectsCollection().remove(subjects);
                    departmentCollectionOldDepartment = em.merge(departmentCollectionOldDepartment);
                }
            }
            for (Department departmentCollectionNewDepartment : departmentCollectionNew) {
                if (!departmentCollectionOld.contains(departmentCollectionNewDepartment)) {
                    departmentCollectionNewDepartment.getSubjectsCollection().add(subjects);
                    departmentCollectionNewDepartment = em.merge(departmentCollectionNewDepartment);
                }
            }
            for (Examhistory examhistoryCollectionNewExamhistory : examhistoryCollectionNew) {
                if (!examhistoryCollectionOld.contains(examhistoryCollectionNewExamhistory)) {
                    Subjects oldSubjectnoOfExamhistoryCollectionNewExamhistory = examhistoryCollectionNewExamhistory.getSubjectno();
                    examhistoryCollectionNewExamhistory.setSubjectno(subjects);
                    examhistoryCollectionNewExamhistory = em.merge(examhistoryCollectionNewExamhistory);
                    if (oldSubjectnoOfExamhistoryCollectionNewExamhistory != null && !oldSubjectnoOfExamhistoryCollectionNewExamhistory.equals(subjects)) {
                        oldSubjectnoOfExamhistoryCollectionNewExamhistory.getExamhistoryCollection().remove(examhistoryCollectionNewExamhistory);
                        oldSubjectnoOfExamhistoryCollectionNewExamhistory = em.merge(oldSubjectnoOfExamhistoryCollectionNewExamhistory);
                    }
                }
            }
            for (Question questionCollectionNewQuestion : questionCollectionNew) {
                if (!questionCollectionOld.contains(questionCollectionNewQuestion)) {
                    Subjects oldSubjectnoOfQuestionCollectionNewQuestion = questionCollectionNewQuestion.getSubjectno();
                    questionCollectionNewQuestion.setSubjectno(subjects);
                    questionCollectionNewQuestion = em.merge(questionCollectionNewQuestion);
                    if (oldSubjectnoOfQuestionCollectionNewQuestion != null && !oldSubjectnoOfQuestionCollectionNewQuestion.equals(subjects)) {
                        oldSubjectnoOfQuestionCollectionNewQuestion.getQuestionCollection().remove(questionCollectionNewQuestion);
                        oldSubjectnoOfQuestionCollectionNewQuestion = em.merge(oldSubjectnoOfQuestionCollectionNewQuestion);
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
                String id = subjects.getSubjectno();
                if (findSubjects(id) == null) {
                    throw new NonexistentEntityException("The subjects with id " + id + " no longer exists.");
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
            Subjects subjects;
            try {
                subjects = em.getReference(Subjects.class, id);
                subjects.getSubjectno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subjects with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Examhistory> examhistoryCollectionOrphanCheck = subjects.getExamhistoryCollection();
            for (Examhistory examhistoryCollectionOrphanCheckExamhistory : examhistoryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subjects (" + subjects + ") cannot be destroyed since the Examhistory " + examhistoryCollectionOrphanCheckExamhistory + " in its examhistoryCollection field has a non-nullable subjectno field.");
            }
            Collection<Question> questionCollectionOrphanCheck = subjects.getQuestionCollection();
            for (Question questionCollectionOrphanCheckQuestion : questionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Subjects (" + subjects + ") cannot be destroyed since the Question " + questionCollectionOrphanCheckQuestion + " in its questionCollection field has a non-nullable subjectno field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Department> departmentCollection = subjects.getDepartmentCollection();
            for (Department departmentCollectionDepartment : departmentCollection) {
                departmentCollectionDepartment.getSubjectsCollection().remove(subjects);
                departmentCollectionDepartment = em.merge(departmentCollectionDepartment);
            }
            em.remove(subjects);
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

    public List<Subjects> findSubjectsEntities() {
        return findSubjectsEntities(true, -1, -1);
    }

    public List<Subjects> findSubjectsEntities(int maxResults, int firstResult) {
        return findSubjectsEntities(false, maxResults, firstResult);
    }

    private List<Subjects> findSubjectsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subjects.class));
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

    public Subjects findSubjects(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subjects.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubjectsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subjects> rt = cq.from(Subjects.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
