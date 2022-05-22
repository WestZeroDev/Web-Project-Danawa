package me.danawa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.Member;
import me.danawa.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public Member save(Member member) {
		return memberRepository.save(member);
	}
	
	public void delete(Member member) {
		memberRepository.delete(member);
	}
	
	public List<Member> findAll() {
		return memberRepository.findAll();
	}
	
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
	
	public Optional<Member> findByNickname(String nickname) {
		return memberRepository.findByNickname(nickname);
	}
}
