package me.danawa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.FreeComment;
import me.danawa.dao.FreeCommentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeCommentService {
	private final FreeCommentRepository freeCommentRepository;
	
	public void save(FreeComment comment) {
		freeCommentRepository.save(comment);
	}
	
	public List<FreeComment> findByFnum(long fnum) {
		return freeCommentRepository.findAllByFnum(fnum);
	}
	
	public void delete(long fcnum) {
		freeCommentRepository.deleteById(fcnum);
	}
	
	public void deleteByFnum(long fnum) {
		freeCommentRepository.deleteAllByFnum(fnum);
	}
	
	public int updateNickname(String email, String nickname) {
		return freeCommentRepository.updateNickname(email, nickname);
	}
	
}
