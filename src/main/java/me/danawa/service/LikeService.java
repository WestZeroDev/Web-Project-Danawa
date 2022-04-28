package me.danawa.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.Like;
import me.danawa.repository.LikeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
	private final LikeRepository likeRepository;
	
	public boolean save(Like like) {
		//이미 추천했는지 확인 후 저장
		if(likeRepository.findByMemberAndPost(like.getMember(), like.getPost()).isEmpty()) {
			likeRepository.save(like);
			return true;
		}
		else return false;
	}
	
}
