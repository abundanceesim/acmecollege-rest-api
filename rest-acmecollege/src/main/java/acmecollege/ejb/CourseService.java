/**
 * File:  CourseService.java
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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.transaction.Transactional;
import acmecollege.entity.Course;

@Singleton
public class CourseService implements Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<Course> getAllCourses() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Course> cq = cb.createQuery(Course.class);
		Root<Course> root = cq.from(Course.class);
		cq.select(root);
		TypedQuery<Course> tq = em.createQuery(cq);
		List<Course> ret = tq.getResultList();
		return ret;
	}

	public Course getCourseId(int id) {
		Course course = em.find(Course.class, id);
		return course;
	}

	@Transactional
	public Course persistCourse(Course course) {
		em.persist(course);
		return course;
	}

	@Transactional
	public void deleteCourseById(int id) {
		Course course = getCourseId(id);
		if (course != null) {
			em.remove(course);
		}
	}
}