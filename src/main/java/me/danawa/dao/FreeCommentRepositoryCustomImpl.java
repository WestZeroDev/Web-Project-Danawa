package me.danawa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class FreeCommentRepositoryCustomImpl implements FreeCommentRepositoryCustom {
	@PersistenceContext
	private final EntityManager em;
	
	public FreeCommentRepositoryCustomImpl(EntityManager em) {
		this.em = em;
	}

	@Override
	public int updateNickname(String email, String nickname) {
		return em.createQuery("update FreeComment c set c.fcnickname = :nickname where c.fcemail = :email")
				.setParameter("email", email)
				.setParameter("nickname", nickname)
				.executeUpdate();
	}
	
}
