package me.danawa.dao;

import java.util.Optional;

import me.danawa.beans.Like;

public interface LikeRepositoryCustom {
	public Optional<Like> findByFnumAndEmail(long fnum, String email);
}
