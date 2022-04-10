package me.danawa.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.FreeComment;
import me.danawa.beans.FreeCommentForm;
import me.danawa.beans.FreePost;
import me.danawa.beans.FreePostForm;
import me.danawa.beans.Like;
import me.danawa.beans.LikeForm;
import me.danawa.service.FreeCommentService;
import me.danawa.service.FreePostService;
import me.danawa.service.LikeService;
import me.danawa.service.UploadFileService;

@Controller
@RequiredArgsConstructor
public class FreeBoardController {
	private final FreePostService freePostService;
	private final FreeCommentService freeCommentService;
	private final LikeService likeService;
	private final UploadFileService uploadFileService;
	
	//자유게시판
	@GetMapping("/board/free")
	public String freeBoardList(@PageableDefault Pageable pageable, @RequestParam(value="sort", required=false) String sort, Model model) {
		List<String> sortList = Arrays.asList("latest", "view", "like");
		if(sort == null || !sortList.contains(sort)) {
			sort = "latest";
		}
		Page<FreePost> boardList = freePostService.getBoardList(pageable, sort);
        model.addAttribute("boardList", boardList);
        model.addAttribute("sortMethod", sort);
		return "freeBoardList";
	}
	
	//게시물 검색
	@GetMapping("/board/free/search")
	public String search(@PageableDefault Pageable pageable, String keyfield, String keyword, Model model) {
		Page<FreePost> searchResult = null;
		if(keyfield.equals("title")) {
			searchResult = freePostService.searchByTitle(keyword, pageable);
		}
		else if(keyfield.equals("content")) {
			searchResult = freePostService.searchByContent(keyword, pageable);
		}
		else if(keyfield.equals("nickname")) {
			searchResult = freePostService.searchByNickname(keyword, pageable);
		}
		model.addAttribute("keyfield", keyfield);
		model.addAttribute("keyword", keyword);
		model.addAttribute("boardList", searchResult);
	    return "freeBoardSearchResult";
	}
	
	//게시물 작성
	@GetMapping("/board/free/post")
	public String freeBoardPost(HttpSession session) {
		//로그인 안했는데 글 작성 시도한 경우
		if(session.getAttribute("idKey") == null) {
			session.setAttribute("freepost", "freepost");
			return "redirect:/members/login";
		}
		return "freeBoardPost";
	}
	
	//게시물 등록
	@PostMapping("/board/free/register")
	public String postRegister(FreePostForm form, HttpSession session) {
		String idKey = session.getAttribute("idKey").toString();
		String nickname = session.getAttribute("nickname").toString();
		FreePost post = new FreePost();
		post.setFemail(idKey);
		post.setFnickname(nickname);
		post.setFtitle(form.getTitle());
		post.setFcontent(form.getContent());
		post.setFileList(form.getFileList());
		freePostService.save(post);
		return "redirect:/board/free";
	}
	
	//게시물 작성 취소
	@ResponseBody @PostMapping("/board/free/cancel")
	public String postCancel(@RequestParam(value="fileList[]", required=false) List<Long> fileList) {
		if(fileList != null) {
			uploadFileService.delete(fileList);
		}
		return "success";
	}
	
	//게시물 읽기
	@GetMapping("/board/free/view/{fnum}")
	public String freeBoardRead(@PathVariable long fnum, Model model) {
		FreePost post = freePostService.findById(fnum);
		int count = post.getFcount() + 1;
		post.setFcount(count);
		freePostService.save(post);
		model.addAttribute("post", post);
		model.addAttribute("commentList", freeCommentService.findByFnum(fnum));
		model.addAttribute("like", likeService.likeCount(fnum));
		return "freeBoardRead";
	}
	
	//게시물 수정
	@GetMapping("/board/free/modify/{fnum}")
	public String freeBoardModify(@PathVariable long fnum, Model model, HttpSession session) {
		FreePost post = freePostService.findById(fnum);
		if(session.getAttribute("idKey") != null 
				&& post.getFemail().equals(session.getAttribute("idKey").toString())) {
			model.addAttribute("post", post);
			return "freeBoardModify";
		}
		return "redirect:/board/free";
	}
	@PostMapping("/board/free/modify/{fnum}")
	public String postModify(@PathVariable long fnum, FreePostForm form, Model model) {
		FreePost post = freePostService.findById(fnum);
		post.setFtitle(form.getTitle());
		post.setFcontent(form.getContent());
		post.setFileList(form.getFileList());
		freePostService.save(post);
		return "redirect:/board/free/view/" + fnum;
	}
	
	//게시물 삭제
	@GetMapping("/board/free/delete/{fnum}")
	public String postDelete(@PathVariable long fnum, Model model, HttpSession session) {
		FreePost post = freePostService.findById(fnum);
		if(session.getAttribute("idKey") != null 
				&& post.getFemail().equals(session.getAttribute("idKey").toString())) {
			uploadFileService.delete(post.getFileList()); //첨부된 파일 삭제
			freeCommentService.deleteByFnum(fnum); //게시물에 있는 댓글 삭제
			likeService.delete(fnum); //게시물 추천 삭제
			freePostService.delete(fnum); //게시물 삭제
		}
		return "redirect:/board/free";
	}
	
	//댓글 작성
	@PostMapping("/board/free/comment")
	public String commentRegister(FreeCommentForm form, Model model, HttpSession session) {
		long fnum = form.getFnum();
		
		//로그인 안했는데 댓글 작성 시도한 경우
		if(session.getAttribute("idKey") == null) {
			session.setAttribute("fnum", fnum);
			return "redirect:/members/login";
		}
		
		String idKey = session.getAttribute("idKey").toString();
		String nickname = session.getAttribute("nickname").toString();
		
		FreeComment comment = new FreeComment();
		comment.setFnum(fnum);
		comment.setFcemail(idKey);
		comment.setFcnickname(nickname);
		comment.setFccontent(form.getComment());
		freeCommentService.save(comment);
		
		List<FreeComment> commentList = freeCommentService.findByFnum(fnum);
		FreePost post = freePostService.findById(fnum);
		post.setFcomment(commentList.size());
		FreePost updatePost = freePostService.save(post);
		
		model.addAttribute("commentList", commentList);
		model.addAttribute("post", updatePost);
		return "freeBoardRead :: #ajaxArea";
	}
	
	//댓글 삭제
	@PostMapping("/board/free/comment/delete")
	public String commentDelete(FreeCommentForm form, Model model) {
		long fnum = form.getFnum();
		long fcnum = form.getFcnum();
		freeCommentService.delete(fcnum);
		List<FreeComment> commentList = freeCommentService.findByFnum(fnum);
		FreePost post = freePostService.findById(fnum);
		post.setFcomment(commentList.size());
		FreePost updatePost = freePostService.save(post);
		
		model.addAttribute("commentList", commentList);
		model.addAttribute("post", updatePost);
		return "freeBoardRead :: #ajaxArea";
	}
	
	//추천
	@PostMapping("/board/free/like")
	public String doLike(LikeForm form, Model model, HttpSession session) {
		long fnum = form.getFnum();
		String idKey = session.getAttribute("idKey").toString();
		
		//이미 추천했으면 중복 추천 불가
		if(likeService.likeCheck(fnum, idKey)) {
			model.addAttribute("likeValid", "fail");
			model.addAttribute("post", freePostService.findById(fnum));
			model.addAttribute("commentList", freeCommentService.findByFnum(fnum));
			return "freeBoardRead :: #ajaxArea";
		}

		Like like = new Like();
		like.setFnum(fnum);
		like.setLemail(idKey);
		likeService.save(like);
		
		int count = likeService.likeCount(fnum);
		FreePost post = freePostService.findById(fnum);
		post.setFlike(count);
		freePostService.save(post);
		
		model.addAttribute("post", freePostService.findById(fnum));
		model.addAttribute("commentList", freeCommentService.findByFnum(fnum));
		return "freeBoardRead :: #ajaxArea";
	}
	
}
