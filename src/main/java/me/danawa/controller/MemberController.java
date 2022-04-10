package me.danawa.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import me.danawa.beans.Member;
import me.danawa.beans.MemberForm;
import me.danawa.service.FreePostService;
import me.danawa.service.MemberService;
import me.danawa.service.ProductService;

@Controller
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final FreePostService freePostService;
	private final ProductService productService;
	
	//회원가입
	@GetMapping("/members/join")
	public String join(HttpSession session) {
		if(session.getAttribute("idKey") != null) {
			return "redirect:/";
		}
		return "join";
	}
	@PostMapping("/members/join")
	public String doJoin(MemberForm form, HttpSession session) {
		Member member = new Member();
		member.setEmail(form.getEmail());
		member.setPwd(form.getPwd());
		member.setName(form.getName());
		member.setNickname(form.getNickname());
		memberService.join(member);
		session.setAttribute("idKey", member.getEmail());
		session.setAttribute("nickname", member.getNickname());
		return "redirect:/";
	}
	
	//비밀번호 확인
	@PostMapping("/members/pwd")
	public String pwdCheck(MemberForm form, HttpSession session, Model model) {
		String email = session.getAttribute("idKey").toString();
		Member member = memberService.findById(email).get();
		Boolean isEqual = member.getPwd().equals(form.getPwd());
		if(isEqual) {
			model.addAttribute("pwdValid", "true");
		}
		else {
			model.addAttribute("pwdValid", "false");
		}
		return "modifyMember :: #ajaxPwd";
	}
	
	//아이디 중복확인
	@PostMapping("/members/join/id")
	public String idCheck(MemberForm form, Model model) {
		String email = form.getEmail();
		if(memberService.findById(email).isPresent()) {
			model.addAttribute("emailValid", "false");
		}
		else {
			model.addAttribute("emailValid", "true");
		}
		return "join :: #ajaxId";
	}
	
	//닉네임 중복확인
	@PostMapping("/members/join/nickname")
	public String nicknameCheck(MemberForm form, Model model) {
		String nickname = form.getNickname();
		if(memberService.findByNickname(nickname).isPresent()) {
			model.addAttribute("nicknameValid", "false");
		}
		else {
			model.addAttribute("nicknameValid", "true");
		}
		return "join :: #ajaxNickname";
	}
	
	//로그인
	@GetMapping("/members/login")
	public String login(HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("idKey") != null) {
			return "redirect:/";
		}
		//이전 페이지 저장
		String url = request.getHeader("Referer");
		if(url.contains("modifyResult")) url = "/";
		session.setAttribute("url", url);
		return "login";
	}
	@PostMapping("/members/login")
	public String doLogin(MemberForm form, HttpSession session, RedirectAttributes rttr) {
		String url = session.getAttribute("url").toString();
		if(memberService.findById(form.getEmail()).isPresent()) {
			Member member = memberService.findById(form.getEmail()).get();
			Boolean isEqual = member.getPwd().equals(form.getPwd());
			if(isEqual) {
				session.setAttribute("idKey", member.getEmail());
				session.setAttribute("nickname", member.getNickname());
				//자유게시판 글 작성 시도한 경우
				if(session.getAttribute("freepost") != null) {
					session.removeAttribute("freepost");
					return "freeBoardPost";
				}
				//자유게시판 댓글 작성 시도한 경우
				if(session.getAttribute("fnum") != null) {
					String fnum = session.getAttribute("fnum").toString();
					session.removeAttribute("fnum");
					return "redirect:/board/free/view/" + fnum;
				}
				return "redirect:" + url;
			}
			else {
				//비밀번호가 틀린 경우
				rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
				return "redirect:/members/login";
			}
		}
		else {
			//아이디가 없는 경우
			rttr.addFlashAttribute("msg", "존재하지 않는 아이디입니다.");
			return "redirect:/members/login";
		}
	}
	
	//비밀번호 확인 (회원정보 수정을 위한)
	@GetMapping("/members/checkMemberPwd")
	public String checkMemberPwd(HttpSession session) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		return "checkMemberPwd";
	}
	@PostMapping("/members/checkMemberPwd")
	public String doChkPwd(MemberForm form, HttpSession session, RedirectAttributes rttr) {
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		boolean isEqual = member.getPwd().equals(form.getPwd());
		if(isEqual) {
			return "redirect:/members/modifyMember";
		}
		else {
			//비밀번호가 일치하지 않는 경우
			rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "redirect:/members/checkMemberPwd";
		}
	}
	
	//회원정보 수정
	@GetMapping("/members/modifyMember")
	public String modifyMember(HttpSession session) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		return "modifyMember";
	}
	@PostMapping("/members/modifyMember")
	public String doModify(MemberForm form, HttpSession session) {
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		String nickname = form.getNickname();
		//닉네임 변경
		if(!member.getNickname().equals(nickname)) {
			member.setNickname(nickname);
			//닉네임 수정할 경우 게시물 닉네임도 업데이트
			freePostService.updateNickname(idKey, nickname);
		}
		//비밀번호 변경
		if(!form.getNewPwd().isEmpty()) {
			String newPwd = form.getNewPwd();
			member.setPwd(newPwd);
		}
		memberService.update(member);
		session.setAttribute("modifyId", idKey);
		session.removeAttribute("idKey");
		return "redirect:/members/modifyResult";
	}
	
	//회원정보 수정 완료
	@GetMapping("/members/modifyResult")
	public String modifyResult(HttpSession session) {
		if(session.getAttribute("modifyId") == null) {
			return "redirect:/";
		}
		return "modifyMemberInfoResult";
	}
	
	//마이페이지
	@GetMapping("/members/myPage")
	public String myPage(@PageableDefault Pageable pageable, Model model, HttpSession session) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		model.addAttribute("wishList", productService.getWishList(member.getWishList(), pageable, "none"));
		model.addAttribute("myPostList", freePostService.findByEmail(idKey, pageable));
		return "myPage";
	}
	
	//내가쓴글
	@GetMapping("/members/myPage/myAct")
	public String myAct(@PageableDefault Pageable pageable, Model model, HttpSession session) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		String idKey = session.getAttribute("idKey").toString();
		model.addAttribute("boardList", freePostService.findByEmail(idKey, pageable));
		return "myAct";
	}
	
	//내가쓴글 다중선택삭제
	@PostMapping("/members/myPage/myAct/delete")
	public String myActDelete(@RequestBody List<String> fnumArray, @PageableDefault Pageable pageable, Model model, HttpSession session) {
		for(String fnum : fnumArray) {
			freePostService.delete(Long.parseLong(fnum));
		}
		String idKey = session.getAttribute("idKey").toString();
		model.addAttribute("boardList", freePostService.findByEmail(idKey, pageable));
		return "myAct :: #ajaxPostArea";
	}
	
	//관심상품 등록
	@ResponseBody @PostMapping("/members/wish/register")
	public String wishRegister(@RequestParam(value="pkey") long pkey, HttpSession session) {
		String email = session.getAttribute("idKey").toString();
		Member member = memberService.findById(email).get();
		List<Long> wishList = member.getWishList();
		if(wishList.contains(pkey)) {
			return "이미 등록된 상품입니다.";
		}
		else {
			wishList.add(pkey);
			member.setWishList(wishList);
			memberService.update(member);
			return "관심상품으로 등록되었습니다.";
		}
	}
	
	//관심상품
	@GetMapping("/members/myPage/myWish")
	public String myWish(@PageableDefault Pageable pageable, Model model, HttpSession session) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		String sort = "none";
		model.addAttribute("sortMethod", sort);
		model.addAttribute("wishList", productService.getWishList(member.getWishList(), pageable, sort));
		return "myWish";
	}
	
	//관심상품 정렬
	@PostMapping("/members/myPage/myWish/sort")
	public String myWishSort(@RequestParam(value="sort", required=false) String sort,
			@PageableDefault Pageable pageable, Model model, HttpSession session) {
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		model.addAttribute("sortMethod", sort);
		model.addAttribute("wishList", productService.getWishList(member.getWishList(), pageable, sort));
		return "myWish :: #ajaxArea";
	}
	
	//관심상품 다중선택삭제
	@PostMapping("/members/myPage/myWish/delete")
	public String myWishSort(@RequestParam(value="pkey[]") List<Long> pkey,
			@RequestParam(value="sort", required=false) String sort,
			@PageableDefault Pageable pageable, Model model, HttpSession session) {
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		List<Long> wishList = member.getWishList();
		wishList.removeAll(pkey);
		memberService.update(member);
		model.addAttribute("sortMethod", sort);
		model.addAttribute("wishList", productService.getWishList(member.getWishList(), pageable, sort));
		return "myWish :: #ajaxArea";
	}
	
	//로그아웃
	@GetMapping("/members/logout")
	public String doLogout(Model model, HttpSession session, HttpServletRequest request) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		session.invalidate();
		return "redirect:" + request.getHeader("Referer");
	}
	
	//회원탈퇴
	@GetMapping("/members/secede")
	public String secedeMember(HttpSession session) {
		if(session.getAttribute("idKey") == null) {
			return "redirect:/";
		}
		return "secedeMember";
	}
	@PostMapping("/members/secede")
	public String doSecede(MemberForm form, HttpSession session, RedirectAttributes rttr) {
		String idKey = session.getAttribute("idKey").toString();
		Member member = memberService.findById(idKey).get();
		boolean isEqual = member.getPwd().equals(form.getPwd());
		if(isEqual) {
			memberService.delete(member);
			session.invalidate();
			return "redirect:/";
		}
		else {
			//비밀번호가 일치하지 않는 경우
			rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "redirect:/members/secede";
		}
	}
	
}
