package me.danawa.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.danawa.beans.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	public Optional<Member> findByNickname(String nickname);
}
