package me.danawa.controller;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import me.danawa.service.PostService;
import me.danawa.service.ProductService;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final ProductService productService;
	private final PostService freePostService;
	
	//메인페이지
	@GetMapping("/")
	public String main(@PageableDefault Pageable pageable, Model model, HttpSession session) {
		model.addAttribute("prodList", productService.getProdList(pageable, "latest"));
		session.removeAttribute("modifyId");
		return "main";
	}
	
	//검색
	@GetMapping("/search")
	public String mainSearch(String keyword, @PageableDefault Pageable pageable, Model model) {
		model.addAttribute("keyword", keyword);
		model.addAttribute("prodList", productService.mainSearch(pageable, keyword));
		model.addAttribute("boardList", freePostService.searchByTitle(keyword));
		return "mainSearchResult";
	}

}
