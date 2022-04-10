package me.danawa.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.Member;
import me.danawa.dao.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public void join(Member member) {
		memberRepository.save(member);
	}
	
	public void update(Member member) {
		memberRepository.save(member);
	}
	
	public void delete(Member member) {
		memberRepository.delete(member);
	}
	
	public List<Member> findAll() {
		return memberRepository.findAll();
	}
	
	public Optional<Member> findById(String email) {
		return memberRepository.findById(email);
	}
	
	public Optional<Member> findByNickname(String nickname) {
		return memberRepository.findByNickname(nickname);
	}
}
