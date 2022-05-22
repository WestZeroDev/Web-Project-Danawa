package me.danawa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.Post;
import me.danawa.repository.PostRepository;
import me.danawa.repository.UploadFileRepository;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final UploadFileRepository uploadFileRepository;
	
	public Post save(Post post) {
		return postRepository.save(post);
	}
	
	public void delete(Post post) {
		//첨부된 파일 삭제
		for(Long fileId : post.getFileList()) {
			uploadFileRepository.deleteById(fileId);
		}
		postRepository.delete(post);
	}
	
	public void deleteById(Long postId) {
		//첨부된 파일 삭제
		Post post = postRepository.findById(postId).get();
		for(Long fileId : post.getFileList()) {
			uploadFileRepository.deleteById(fileId);
		}
		postRepository.deleteById(postId);
	}

	public void modify(Long postId, String title, String content, List<Long> fileList) {
		Post post = postRepository.findById(postId).get();
		post.setTitle(title);
		post.setContent(content);
		post.setFileList(fileList);
	}
	
	public Post update(Long postId) {
		Post post = postRepository.findById(postId).get();
		post.updateLike();
		post.updateComment();
		return post;
	}
	
	public Optional<Post> findById(Long postId) {
		return postRepository.findById(postId);
	}
	
	public void updateNickname(String email, String nickname) {
		postRepository.updateNickname(email, nickname);
	}
	
	public Page<Post> getBoardList(Pageable pageable, String sort) {
        pageable = sort(pageable, sort);
        return postRepository.findAll(pageable);
    }
	
	public Pageable sort(Pageable pageable, String sort) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		if(sort.equals("latest")) {
        	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        }
        else if(sort.equals("view")) {
        	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "view").and(Sort.by(Sort.Direction.DESC, "id")));
        }
        else if(sort.equals("like")) {
        	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "like").and(Sort.by(Sort.Direction.DESC, "id")));
        }
		return pageable;
	}
	
	public Page<Post> findByEmail(String email, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
		return postRepository.findByEmail(email, pageable);
	}
	
	public Page<Post> searchByNickname(String keyword, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
		return postRepository.findByNicknameContainsIgnoreCase(keyword, pageable);
	}
	
	public Page<Post> searchByTitle(String keyword, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
		return postRepository.findByTitleContainsIgnoreCase(keyword, pageable);
	}
	
	public Page<Post> searchByContent(String keyword, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
		return postRepository.findByContentContainsIgnoreCase(keyword, pageable);
	}
	
	public List<Post> searchByTitle(String keyword) {
		return postRepository.findByTitleContainsIgnoreCase(keyword);
	}
	
}
