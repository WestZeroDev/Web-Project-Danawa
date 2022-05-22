package me.danawa.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.Comment;
import me.danawa.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	
	public void save(Comment comment) {
		commentRepository.save(comment);
	}
		
	public void deleteById(Long commentId) {
		commentRepository.deleteById(commentId);
	}
	
	public void updateNickname(String email, String nickname) {
		commentRepository.updateNickname(email, nickname);
	}
	
}
