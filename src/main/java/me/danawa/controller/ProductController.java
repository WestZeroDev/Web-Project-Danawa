package me.danawa.controller;

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

import lombok.RequiredArgsConstructor;
import me.danawa.beans.PriceInfo;
import me.danawa.beans.Product;
import me.danawa.service.PriceService;
import me.danawa.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	private final PriceService priceService;
	
	//노트북 전체
	@GetMapping("/notebook")
	public String notebook(@PageableDefault Pageable pageable, Model model) {
		Page<Product> prodList = productService.getProdList(pageable, "latest");
		model.addAttribute("sortMethod", "latest");
		model.addAttribute("prodList", prodList);
		return "notebook";
	}
	
	//노트북 정보 보기
	@GetMapping("/notebook/info/{pkey}")
	public String freeBoardRead(@PathVariable long pkey, Model model) {
		Product prod = productService.findById(pkey);
		List<PriceInfo> priceList = priceService.getPriceList(prod.getName());
		model.addAttribute("prod", prod);
		model.addAttribute("priceList", priceList);
		return "notebookInfo";
	}

	//노트북 상세검색
	@PostMapping("/notebook/option")
	public String option(@RequestParam(value="brand[]", required=false) List<String> brand,
			@RequestParam(value="cpu[]", required=false) List<String> cpu,
			@RequestParam(value="size[]", required=false) int[] size,
			@RequestParam(value="memory[]", required=false) List<String> memory,
			@RequestParam(value="storage[]", required=false) int[] storage,
			@RequestParam(value="os[]", required=false) List<String> os,
			@RequestParam(value="weight[]", required=false) int[] weight,
			@RequestParam(value="sort", required=false) String sort,
			@RequestParam(value="minPrice", required=false) String minPrice,
			@RequestParam(value="maxPrice", required=false) String maxPrice,
			@RequestParam(value="keyword", required=false) String keyword,
			@PageableDefault Pageable pageable, Model model) {
		if(sort == "" || sort == null) {
			sort = "latest";
		}

		if(brand == null && cpu == null && size == null && memory == null && storage == null
				&& os == null && weight == null && minPrice == "" && maxPrice == "" && keyword == "") {
			Page<Product> prodList = productService.getProdList(pageable, sort);
			model.addAttribute("sortMethod", sort);
			model.addAttribute("prodList", prodList);
			return "notebook :: #ajaxArea";
		}
		
		Page<Product> prodList = productService.search(brand, cpu, size, memory, storage, os, weight, sort, minPrice, maxPrice, keyword, pageable);
		model.addAttribute("sortMethod", sort);
		model.addAttribute("prodList", prodList);
		return "notebook :: #ajaxArea";
	}
	
	@GetMapping("/notebook/updateData")
	public String updateData(HttpSession session) {
		if(session.getAttribute("idKey") != null) {
			//관리자 계정만 허용
			if(session.getAttribute("idKey").toString().equals("admin@gmail.com")) {
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
