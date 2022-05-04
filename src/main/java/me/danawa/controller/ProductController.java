package me.danawa.controller;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import me.danawa.domain.Product;
import me.danawa.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	
	//노트북 전체
	@GetMapping("/notebook")
	public String notebook(@PageableDefault Pageable pageable, Model model) {
		Page<Product> prodList = productService.getProdList(pageable, "latest");
		model.addAttribute("sortMethod", "latest");
		model.addAttribute("prodList", prodList);
		return "notebook";
	}
	
	//노트북 정보 보기
	@GetMapping("/notebook/info/{prodId}")
	public String freeBoardRead(@PathVariable Long prodId, Model model) {
		Product prod = productService.findById(prodId).get();		
		model.addAttribute("prod", prod);
		model.addAttribute("priceList", prod.getPriceInfoList());
		return "notebookInfo";
	}

	//노트북 상세검색
	@PostMapping("/notebook/option")
	public String option(SearchForm form, String sort, @PageableDefault Pageable pageable, Model model) {
		SearchForm emptyForm = new SearchForm();
		Page<Product> prodList = productService.getProdList(pageable, sort);
		if(!form.equals(emptyForm)) prodList = productService.search(form, sort, pageable);
		
		model.addAttribute("sortMethod", sort);
		model.addAttribute("prodList", prodList);
		return "notebook :: #ajaxArea";
	}
	
	@GetMapping("/notebook/updateData")
	public String updateData(HttpSession session) {	
		//관리자 계정만 허용
		if(session.getAttribute("idKey") != null) {
			if(session.getAttribute("idKey").toString().equals("admin@gmail.com")) {
				//제품 크롤링
				//productService.crawling();
				//가격정보 업데이트
				productService.updatePriceInfo();
				//최저가 업데이트
				productService.updateMinPrice();
				return "redirect:/notebook";
			}
		}
		return "redirect:/";
	}

}
