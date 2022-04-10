package me.danawa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.FreePost;
import me.danawa.dao.FreePostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FreePostService {
	private final FreePostRepository freePostRepository;
	
	public FreePost save(FreePost post) {
		return freePostRepository.save(post);
	}
	
	public void delete(long fnum) {
		freePostRepository.deleteById(fnum);
	}
	
	public FreePost findById(long fnum) {
		return freePostRepository.findById(fnum).get();
	}
	
	public int updateNickname(String email, String nickname) {
		return freePostRepository.updateNickname(email, nickname);
	}
	
	public Page<FreePost> getBoardList(Pageable pageable, String sort) {
        pageable = sort(pageable, sort);
        return freePostRepository.findAll(pageable);
    }
	
	public Pageable sort(Pageable pageable, String sort) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		if(sort.equals("latest")) {
        	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fnum"));
        }
        else if(sort.equals("view")) {
        	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fcount").and(Sort.by(Sort.Direction.DESC, "fnum")));
        }
        else if(sort.equals("like")) {
        	pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "flike").and(Sort.by(Sort.Direction.DESC, "fnum")));
        }
		return pageable;
	}
	
	public Page<FreePost> findByEmail(String email, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fnum"));
		return freePostRepository.findByFemail(email, pageable);
	}
	
	public Page<FreePost> searchByNickname(String keyword, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fnum"));
		return freePostRepository.findByFnicknameContainsIgnoreCase(keyword, pageable);
	}
	
	public Page<FreePost> searchByTitle(String keyword, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fnum"));
		return freePostRepository.findByFtitleContainsIgnoreCase(keyword, pageable);
	}
	
	public Page<FreePost> searchByContent(String keyword, Pageable pageable) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "fnum"));
		return freePostRepository.findByFcontentContainsIgnoreCase(keyword, pageable);
	}
	
	public List<FreePost> searchByTitle(String keyword) {
		return freePostRepository.findByFtitleContainsIgnoreCase(keyword);
	}
	
}
