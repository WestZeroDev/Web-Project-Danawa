package me.danawa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class FreePostRepositoryCustomImpl implements FreePostRepositoryCustom {
	@PersistenceContext
	private final EntityManager em;
	
	public FreePostRepositoryCustomImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public int updateNickname(String email, String nickname) {
		return em.createQuery("update FreePost p set p.fnickname = :nickname where p.femail = :email")
				.setParameter("email", email)
				.setParameter("nickname", nickname)
				.executeUpdate();
	}

}
