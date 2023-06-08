package com.ono.board.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ono.board.domain.AnswerForm;
import com.ono.board.domain.Recipe;
import com.ono.board.domain.RecipeForm;
import com.ono.board.domain.SiteUser;
import com.ono.board.repository.UserRepository;
import com.ono.board.service.RecipeService;
import com.ono.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/recipe")
@RequiredArgsConstructor
@Controller
public class RecipeController {

	private final RecipeService recipeService;
	private final UserService userService;
	private final UserRepository userRepository;

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<Recipe> questionList = this.questionService.getList();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }
	
	// 페이징 구현(검색 기능 추가)
	@GetMapping("/list")
	public String list(Model model, 
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Recipe> paging = this.recipeService.getList(page, kw);
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "recipe_list";
		}
		
	// 레시피 추가
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
	    Recipe recipe = recipeService.getRecipe(id);
	    recipeService.incrementViewCount(id);
	    
//	    log.info("유저네임" + question.getAuthor().getProfilePath());
	    String profilePath = recipe.getAuthor().getProfilePath();
	    model.addAttribute("profilePath", profilePath);
	    model.addAttribute("recipe", recipe);
	    return "recipe_detail";
	}
    
	// 접근제한(로그인 한 사용자만 접근가능)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String recipeCreate(RecipeForm recipeForm) {
        return "recipe_form";
    }
    
    // 레시피 작성
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String recipeCreate(@Valid RecipeForm recipeForm,
            BindingResult bindingResult, Principal principal, MultipartFile file) throws Exception{
        if (bindingResult.hasErrors()) {
            return "recipe_form";
            
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.recipeService.create(recipeForm.getSubject(), siteUser, recipeForm.getFile(),
        		recipeForm.getCookIntro(), recipeForm.getCategory(), recipeForm.getCookInfo_level(), 
        		recipeForm.getCookInfo_people(), recipeForm.getCookInfo_time(), recipeForm.getIngredient(),
        		recipeForm.getCapacity(), recipeForm.getContent(), recipeForm.getContentFile());
        log.info("로그(질문작성):" + recipeForm);
        return "redirect:/recipe/list";
    }
    
    // 질문 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String recipeModify(RecipeForm recipeForm, @PathVariable("id") Integer id, Principal principal, 
    		MultipartFile file) {
    	Recipe recipe = this.recipeService.getRecipe(id);
    	if(!recipe.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    	}
    	recipeForm.setSubject(recipe.getSubject());
    	recipeForm.setCookIntro(recipe.getCookIntro());
    	recipeForm.setCategory(recipe.getCategory());
    	recipeForm.setCookInfo_people(recipe.getCookInfo_level());
    	recipeForm.setCookInfo_time(recipe.getCookInfo_people());
    	recipeForm.setCookInfo_level(recipe.getCookInfo_time());
    	recipeForm.setFile(file);
    	return "recipe_form";
    }
    
    // 질문 수정 후 저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String recipeModify(@Valid RecipeForm recipeForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "recipe_form";
        }
        Recipe recipe = this.recipeService.getRecipe(id);
        if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.recipeService.modify(recipe, recipeForm.getSubject(), recipeForm.getFile(),
        		recipeForm.getCookIntro(),recipeForm.getCategory(),recipeForm.getCookInfo_level(),
        		recipeForm.getCookInfo_people(),recipeForm.getCookInfo_time());
        return String.format("redirect:/recipe/detail/%s", id);
    }
    
    // 질문 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String recipeDaelete(Principal principal, @PathVariable("id") Integer id) {
    	Recipe recipe = this.recipeService.getRecipe(id);
    	if (!recipe.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
    	}
    	this.recipeService.delete(recipe);
    	return "redirect:/";
    }
    
    // 추천 버튼 클릭 시 호출
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String recipeVote(Principal principal, @PathVariable("id") Integer id) {
    	Recipe recipe = this.recipeService.getRecipe(id);
    	SiteUser siteuser = this.userService.getUser(principal.getName());
    	this.recipeService.vote(recipe, siteuser);
    	return String.format("redirect:/recipe/detail/%s", id);
    }
      
}