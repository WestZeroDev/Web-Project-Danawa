package me.danawa.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import me.danawa.beans.Like;

public class LikeRepositoryCustomImpl implements LikeRepositoryCustom {
	@PersistenceContext
	private final EntityManager em;
	
	public LikeRepositoryCustomImpl(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Optional<Like> findByFnumAndEmail(long fnum, String email) {
		List<Like> result = em.createQuery("select l from Like l where l.fnum = :fnum and l.lemail = :email", Like.class)
				.setParameter("fnum", fnum)
				.setParameter("email", email)
				.getResultList();
		return result.stream().findAny();
	}

}
