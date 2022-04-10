package me.danawa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.Like;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
	public List<Like> findByFnum(long fnum);
	public void deleteAllByFnum(long fnum);
}
