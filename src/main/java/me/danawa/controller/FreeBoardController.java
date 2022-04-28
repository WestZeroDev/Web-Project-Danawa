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
import me.danawa.domain.Comment;
import me.danawa.domain.Like;
import me.danawa.domain.Member;
import me.danawa.domain.Post;
import me.danawa.service.CommentService;
import me.danawa.service.LikeService;
import me.danawa.service.MemberService;
import me.danawa.service.PostService;
import me.danawa.service.UploadFileService;

@Controller
@RequiredArgsConstructor
public class FreeBoardController {
	private final MemberService memberService;
	private final PostService postService;
	private final CommentService commentService;
	private final LikeService likeService;
	private final UploadFileService uploadFileService;
	
	//자유게시판
	@GetMapping("/board/free")
	public String freeBoardList(@PageableDefault Pageable pageable, @RequestParam(value="sort", required=false) String sort, Model model) {
		List<String> sortList = Arrays.asList("latest", "view", "like");
		if(sort == null || !sortList.contains(sort)) {
			sort = "latest";
		}
		Page<Post> boardList = postService.getBoardList(pageable, sort);
        model.addAttribute("boardList", boardList);
        model.addAttribute("sortMethod", sort);
		return "freeBoardList";
	}
	
	//게시물 검색
	@GetMapping("/board/free/search")
	public String search(@PageableDefault Pageable pageable, String keyfield, String keyword, Model model) {
		Page<Post> searchResult = null;
		if(keyfield.equals("title")) {
			searchResult = postService.searchByTitle(keyword, pageable);
		}
		else if(keyfield.equals("content")) {
			searchResult = postService.searchByContent(keyword, pageable);
		}
		else if(keyfield.equals("nickname")) {
			searchResult = postService.searchByNickname(keyword, pageable);
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
			session.setAttribute("from", "freeBoardPost");
			return "redirect:/members/login";
		}
		return "freeBoardPost";
	}
	
	//게시물 등록
	@PostMapping("/board/free/register")
	public String postRegister(PostForm form, HttpSession session) {
		String idKey = session.getAttribute("idKey").toString();
		String nickname = session.getAttribute("nickname").toString();
		Post post = new Post();
		post.setEmail(idKey);
		post.setNickname(nickname);
		post.setTitle(form.getTitle());
		post.setContent(form.getContent());
		post.setFileList(form.getFileList());
		postService.save(post);
		return "redirect:/board/free/view/" + post.getId();
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
	@GetMapping("/board/free/view/{postId}")
	public String freeBoardRead(@PathVariable Long postId, Model model) {
		Post post = postService.findById(postId).get();
		post.updateView();
		postService.save(post);
		
		model.addAttribute("post", post);
		model.addAttribute("commentList", post.getCommentList());
		model.addAttribute("like", post.getLikeList().size());
		return "freeBoardRead";
	}
	
	//게시물 수정
	@GetMapping("/board/free/modify/{postId}")
	public String freeBoardModify(@PathVariable Long postId, Model model, HttpSession session) {
		Post post = postService.findById(postId).get();
		if(session.getAttribute("idKey") != null 
				&& post.getEmail().equals(session.getAttribute("idKey").toString())) {
			model.addAttribute("post", post);
			return "freeBoardModify";
		}
		return "redirect:/board/free";
	}
	@PostMapping("/board/free/modify/{postId}")
	public String postModify(@PathVariable Long postId, PostForm form, Model model) {
		postService.modify(postId, form.getTitle(), form.getContent(), form.getFileList());
		return "redirect:/board/free/view/" + postId;
	}
	
	//게시물 삭제
	@GetMapping("/board/free/delete/{postId}")
	public String postDelete(@PathVariable Long postId, Model model, HttpSession session) {
		Post post = postService.findById(postId).get();
		if(session.getAttribute("idKey") != null 
				&& post.getEmail().equals(session.getAttribute("idKey").toString())) {
			postService.delete(post);
		}
		return "redirect:/board/free";
	}
	
	//댓글 작성
	@PostMapping("/board/free/comment")
	public String commentRegister(CommentForm form, Model model, HttpSession session) {
		Long postId = form.getPostId();
		Post post = postService.findById(postId).get();
		
		//로그인 안했는데 댓글 작성 시도한 경우
		if(session.getAttribute("idKey") == null) {
			String url = "redirect:/board/free/view/" + postId;
			session.setAttribute("from", url);
			return "redirect:/members/login";
		}
		
		String idKey = session.getAttribute("idKey").toString();
		String nickname = session.getAttribute("nickname").toString();
		
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setEmail(idKey);
		comment.setNickname(nickname);
		comment.setContent(form.getComment());
		commentService.save(comment);
		
		model.addAttribute("post", postService.update(postId));
		model.addAttribute("commentList", post.getCommentList());
		return "freeBoardRead :: #ajaxArea";
	}
	
	//댓글 삭제
	@PostMapping("/board/free/comment/delete")
	public String commentDelete(CommentForm form, Model model) {
		Long postId = form.getPostId();
		Post post = postService.findById(postId).get();
		commentService.deleteById(form.getCommentId());
		
		model.addAttribute("post", postService.update(postId));
		model.addAttribute("commentList", post.getCommentList());
		return "freeBoardRead :: #ajaxArea";
	}
	
	//추천
	@PostMapping("/board/free/like")
	public String doLike(Long postId, Model model, HttpSession session) {
		Post post = postService.findById(postId).get();
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findByEmail(idKey).get();

		Like like = new Like();
		like.setPost(post);
		like.setMember(member);
		
		//중복 추천 불가
		if(!likeService.save(like)) {
			model.addAttribute("likeValid", "fail");
		}

		model.addAttribute("post", postService.update(postId));
		model.addAttribute("commentList", post.getCommentList());
		return "freeBoardRead :: #ajaxArea";
	}
	
}
