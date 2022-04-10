package me.danawa.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.FreePost;

public interface FreePostRepository extends JpaRepository<FreePost, Long>, FreePostRepositoryCustom {
	public void deleteById(long fnum);
	public void deleteAllByFemail(String femail);
	public Page<FreePost> findByFemail(String femail, Pageable pageable);
	public Page<FreePost> findByFnicknameContainsIgnoreCase(String keyword, Pageable pageable);
	public Page<FreePost> findByFtitleContainsIgnoreCase(String keyword, Pageable pageable);
	public Page<FreePost> findByFcontentContainsIgnoreCase(String keyword, Pageable pageable);
	public List<FreePost> findByFtitleContainsIgnoreCase(String keyword);
}
