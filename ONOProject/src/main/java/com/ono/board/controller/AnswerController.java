package com.ono.board.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ono.board.domain.Answer;
import com.ono.board.domain.AnswerForm;
import com.ono.board.domain.Recipe;
import com.ono.board.domain.SiteUser;
import com.ono.board.service.AnswerService;
import com.ono.board.service.RecipeService;
import com.ono.board.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final RecipeService recipeService;
    private final AnswerService answerService;
    private final UserService userService;
    
    // 댓글 생성 (앵커 추가)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, 
            @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        Recipe recipe = this.recipeService.getRecipe(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipe);
            return "recipe_detail";
        }
        Answer answer = this.answerService.create(recipe, 
                answerForm.getContent(), siteUser);
        return String.format("redirect:/recipe/detail/%s#answer_%s", 
                answer.getRecipe().getId(), answer.getId());
    }
    
    // 댓글 수정 Get 방식 처리
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정완료되었습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }
    
    // 댓글 수정 후 저장 Post 방식 처리(앵커 추가)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
            @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정완료되었습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/recipe/detail/%s#answer_%s", 
                answer.getRecipe().getId(), answer.getId());
    }
    
    
    // 댓글 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
    	Answer answer = this.answerService.getAnswer(id);
    	if (!answer.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제하시겠습니까?.");
    	}
    	this.answerService.delete(answer);
    	return String.format("redirect:/recipe/detail/%s", answer.getRecipe().getId());
    } 
    
     // 댓글 추천 버튼 클릭시 호출(답변 앵커 추가)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
    	Answer answer = this.answerService.getAnswer(id);
    	SiteUser siteUser = this.userService.getUser(principal.getName());
    	this.answerService.vote(answer, siteUser);
    	return String.format("redirect:/recipe/detail/%s#answer_%s", 
    			answer.getRecipe().getId(),answer.getId());
    }
    
    
}
