package me.danawa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.danawa.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	public Page<Post> findByEmail(String email, Pageable pageable);
	public Page<Post> findByNicknameContainsIgnoreCase(String keyword, Pageable pageable);
	public Page<Post> findByTitleContainsIgnoreCase(String keyword, Pageable pageable);
	public Page<Post> findByContentContainsIgnoreCase(String keyword, Pageable pageable);
	public List<Post> findByTitleContainsIgnoreCase(String keyword);

	@Modifying
	@Query("update Post p set p.nickname = :nickname where p.email = :email")
	public void updateNickname(@Param("email") String email, @Param("nickname") String nickname);
}
