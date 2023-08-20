/**
 * File:  MembershipCardService.java
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
import acmecollege.entity.MembershipCard;

@Singleton
public class MembershipCardService implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(name = PU_NAME)
	protected EntityManager em;
	
	@Inject
	protected Pbkdf2PasswordHash pbAndjPasswordHash;

	public List<MembershipCard> getAllCards() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MembershipCard> cq = cb.createQuery(MembershipCard.class);
		Root<MembershipCard> root = cq.from(MembershipCard.class);
		cq.select(root);
		TypedQuery<MembershipCard> tq = em.createQuery(cq);
		List<MembershipCard> result = tq.getResultList();
		return result;
	}

	public MembershipCard getMembershipCardId(int id) {
		return em.find(MembershipCard.class, id);
	}

	public MembershipCard persistMembershipCard(MembershipCard newCard) {
		em.persist(newCard);
		return newCard;
	}

	public void deleteMembershipCardById(int id) {
		MembershipCard mc = getMembershipCardId(id);
		if (mc != null) {
			em.remove(mc);
		}
	}

}