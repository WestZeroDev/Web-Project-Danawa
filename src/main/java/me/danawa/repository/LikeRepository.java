package me.danawa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.domain.Like;
import me.danawa.domain.Member;
import me.danawa.domain.Post;

public interface LikeRepository extends JpaRepository<Like, Long> {
	public Optional<Like> findByMemberAndPost(Member member, Post post);
}
