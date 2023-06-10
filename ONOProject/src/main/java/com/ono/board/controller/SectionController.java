package com.ono.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ono.board.domain.Recipe;
import com.ono.board.domain.SiteUser;
import com.ono.board.service.RecipeService;
import com.ono.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SectionController {
	
	private final RecipeService recipeService;
	private final UserService userService;
	
	// 페이징 구현(검색 기능 추가)
	@GetMapping("/section")
	public String list(Model model,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "kw", defaultValue = "") String kw,
	        @RequestParam(value = "category", defaultValue = "") String category) {
		
	    Page<Recipe> paging3 = this.recipeService.getAllList(page, kw);
	    List<SiteUser> siteUsers3 = this.userService.getUsers(paging3);
	    long totalCount = this.recipeService.getTotalCount();
	    
	    model.addAttribute("siteUsers3", siteUsers3);    
	    model.addAttribute("paging3", paging3);   // 전체 레시피 리스트
	    model.addAttribute("kw", kw);  // keyword
	    model.addAttribute("totalCount", totalCount);  // 총 레시피
	    
	    return "section";
	}
	
}