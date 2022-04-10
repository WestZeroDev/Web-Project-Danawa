package me.danawa.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.Like;
import me.danawa.dao.LikeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
	private final LikeRepository likeRepository;
	
	public void save(Like like) {
		likeRepository.save(like);
	}
	
	public void delete(long fnum) {
		likeRepository.deleteAllByFnum(fnum);
	}
	
	public int likeCount(long fnum) {
		return likeRepository.findByFnum(fnum).size();
	}
	
	public boolean likeCheck(long fnum, String email) {
		return likeRepository.findByFnumAndEmail(fnum, email).isPresent();
	}
	
}
