/**
 * File:  CourseRegistrationService.java
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

import acmecollege.entity.CourseRegistration;

@Singleton
public class CourseRegistrationService implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<CourseRegistration> getAllCourseRegistrations() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CourseRegistration> cq = cb.createQuery(CourseRegistration.class);
		cq.select(cq.from(CourseRegistration.class));
		return em.createQuery(cq).getResultList();
	}

	public CourseRegistration getCourseRegistrationById(int id) {
		CourseRegistration add = em.find(CourseRegistration.class, id);
		return add;
	}

	@Transactional
	public CourseRegistration persistCourseRegistration(CourseRegistration newCourseRegistration) {
		em.persist(newCourseRegistration);
		return newCourseRegistration;
	}

	@Transactional
	public void deleteCourseRegistrationById(int id) {
		CourseRegistration registration = getCourseRegistrationById(id);
		if (registration != null) {
			em.remove(registration);
		}
	}
}

