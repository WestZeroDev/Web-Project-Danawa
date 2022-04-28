package me.danawa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import me.danawa.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Modifying
	@Query("update Comment c set c.nickname = :nickname where c.email = :email")
	public void updateNickname(@Param("email") String email, @Param("nickname") String nickname);
}
