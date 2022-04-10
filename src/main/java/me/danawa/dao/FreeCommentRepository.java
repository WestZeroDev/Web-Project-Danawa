package me.danawa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.FreeComment;

public interface FreeCommentRepository extends JpaRepository<FreeComment, Long>, FreeCommentRepositoryCustom {
	public List<FreeComment> findAllByFnum(long fnum);
	public void deleteById(long fcnum);
	public void deleteAllByFnum(long fnum);
}
