package me.danawa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	public Optional<Member> findByEmail(String email);
	public Optional<Member> findByNickname(String nickname);
}
