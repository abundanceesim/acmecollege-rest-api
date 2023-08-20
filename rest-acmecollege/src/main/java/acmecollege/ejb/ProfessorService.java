/**
 * File:  ProfessorService.java
 *
 * Created by: Group 12
 *  Abundance Esim 	
 *  Harshang Shah  
 *  Mei Zhi Li   
 *  Sergio  Machado  
 * 
 */
package acmecollege.ejb;

import static acmecollege.utility.MyConstants.PU_NAME;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import acmecollege.entity.Professor;

@Singleton
public class ProfessorService implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<Professor> getAllProfessors() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Professor> cq = cb.createQuery(Professor.class);
		cq.select(cq.from(Professor.class));
		return em.createQuery(cq).getResultList();
	}

	public Professor getProfessorId(int id) {
		Professor add = em.find(Professor.class, id);
		return add;
	}

	@Transactional
	public Professor persistProfessor(Professor newProfessor) {
		em.persist(newProfessor);
		return newProfessor;
	}

	@Transactional
	public void deleteProfessorById(int id) {
		Professor prof = getProfessorId(id);
		if (prof != null) {
			em.remove(prof);
		}
	}
}

