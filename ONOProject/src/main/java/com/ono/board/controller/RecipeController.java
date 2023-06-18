package com.ono.board.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ono.board.domain.AnswerFormDto;
import com.ono.board.domain.Recipe;
import com.ono.board.domain.RecipeFormDto;
import com.ono.board.domain.SiteUser;
import com.ono.board.repository.UserRepository;
import com.ono.board.service.RecipeService;
import com.ono.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/recipe")
@RequiredArgsConstructor
@Controller
@Slf4j
public class RecipeController {

	private final RecipeService recipeService;
	private final UserService userService;
	private final UserRepository userRepository;

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<Recipe> recipeList = this.recipeService.getList();
//        model.addAttribute("recipeList", recipeList);
//        return "recipe_list";
//    }

	// 페이징 구현(검색 기능 추가)
	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Recipe> paging = this.recipeService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "recipe_list";
	}

	// 레시피 추가
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerFormDto answerForm) {
		Recipe recipe = recipeService.getRecipe(id);
		recipeService.incrementViewCount(id);
		String profilePath = recipe.getAuthor().getProfilePath();
		model.addAttribute("profilePath", profilePath);
		model.addAttribute("recipe", recipe);
		return "recipe_detail";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String recipeCreate(RecipeFormDto recipeForm) {
		return "recipe_form";
	}

	// 레시피 작성
	// 레시피 작성
	   @PreAuthorize("isAuthenticated()")
	   @PostMapping("/create")
	   public String recipeCreate(@Valid RecipeFormDto recipeForm, BindingResult bindingResult, Principal principal,
	         MultipartFile file) throws Exception {
	      if (bindingResult.hasErrors()) {
	         return "recipe_form";
	  }
	  if (principal instanceof OAuth2AuthenticationToken) {
	     String email = this.userService.getEmailFromPrincipal(principal);
	
	     List<MultipartFile> contentFiles = recipeForm.getContentFiles();
	
	     if (contentFiles == null || contentFiles.isEmpty()) {
	        throw new IllegalArgumentException("Recipe images cannot be null or empty");
	     }
	
	     file = recipeForm.getFile();
	
	     List<MultipartFile> recipeImages = contentFiles.subList(0, contentFiles.size());
	
	     this.recipeService.create(recipeForm.getSubject(), recipeForm.getContent(), null, email,
	           recipeForm.getFile(), recipeForm.getCookIntro(), recipeForm.getCategory(),
	           recipeForm.getCookInfo_level(), recipeForm.getCookInfo_people(), recipeForm.getCookInfo_time(),
	           recipeForm.getIngredient(), recipeForm.getCapacity(), recipeImages);
	     
	  } else if (principal instanceof UsernamePasswordAuthenticationToken) {
	     String username = this.userService.getUsernameFromPrincipal(principal);
	
	     List<MultipartFile> contentFiles = recipeForm.getContentFiles();
	
	     if (contentFiles == null || contentFiles.isEmpty()) {
	        throw new IllegalArgumentException("Recipe images cannot be null or empty");
	     }
	
	     file = recipeForm.getFile();
	
	     List<MultipartFile> recipeImages = contentFiles.subList(0, contentFiles.size());
	
	     this.recipeService.create(recipeForm.getSubject(), recipeForm.getContent(), username, null,
	           recipeForm.getFile(), recipeForm.getCookIntro(), recipeForm.getCategory(),
	           recipeForm.getCookInfo_level(), recipeForm.getCookInfo_people(), recipeForm.getCookInfo_time(),
	           recipeForm.getIngredient(), recipeForm.getCapacity(), recipeImages);
	  }
	  return "redirect:/recipe/list";
	   }

	// 레시피 수정
	   @PreAuthorize("isAuthenticated()")
	   @GetMapping("/modify/{id}")
	   public String recipeModify(RecipeFormDto recipeForm, @PathVariable("id") Integer id, Principal principal, 
	         MultipartFile file) {
	      Recipe recipe = this.recipeService.getRecipe(id);
	      if (principal instanceof OAuth2AuthenticationToken) {
	         String email = this.userService.getEmailFromPrincipal(principal);
	         if ((!recipe.getAuthor().getEmail().equals(email))) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	         }
	      } else if (principal instanceof UsernamePasswordAuthenticationToken) {
	         String username = this.userService.getUsernameFromPrincipal(principal);
	         if (!recipe.getAuthor().getUsername().equals(username)) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	         }
	      }
	      recipeForm.setSubject(recipe.getSubject());
	      recipeForm.setContent(recipe.getContent());
	      recipeForm.setCookIntro(recipe.getCookIntro());
	      recipeForm.setCategory(recipe.getCategory());
	      recipeForm.setCookInfo_people(recipe.getCookInfo_level());
	      recipeForm.setCookInfo_time(recipe.getCookInfo_people());
	      recipeForm.setCookInfo_level(recipe.getCookInfo_time());
	      recipeForm.setIngredient(recipe.getIngredient());
	      recipeForm.setCapacity(recipe.getCapacity());
	      recipeForm.setFile(file);
	      return "recipe_form";
	   }

	   // 레시피 수정 후 저장
	   @PreAuthorize("isAuthenticated()")
	   @PostMapping("/modify/{id}")
	   public String recipeModify(@Valid RecipeFormDto recipeForm, BindingResult bindingResult, Principal principal,
	         @PathVariable("id") Integer id, MultipartFile file) throws Exception {
	      if (bindingResult.hasErrors()) {
	         return "recipe_form";
	      }
	      Recipe recipe = this.recipeService.getRecipe(id);
	      if (principal instanceof OAuth2AuthenticationToken) {
	         String email = this.userService.getEmailFromPrincipal(principal);
	         if ((!recipe.getAuthor().getEmail().equals(email))) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	         }
	      } else if (principal instanceof UsernamePasswordAuthenticationToken) {
	         String username = this.userService.getUsernameFromPrincipal(principal);
	         if (!recipe.getAuthor().getUsername().equals(username)) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
	         }
	      }
	      List<MultipartFile> contentFiles = recipeForm.getContentFiles();

	      if (contentFiles == null || contentFiles.isEmpty()) {
	         throw new IllegalArgumentException("Recipe images cannot be null or empty");
	      }
	      file = recipeForm.getFile();
	      List<MultipartFile> recipeImages = contentFiles.subList(0, contentFiles.size());
	      
	      this.recipeService.modify(recipe, recipeForm.getSubject(), recipeForm.getContent(), recipeForm.getFile(),
	            recipeForm.getCookIntro(), recipeForm.getCategory(), recipeForm.getCookInfo_level(),
	            recipeForm.getCookInfo_people(), recipeForm.getCookInfo_time(), recipeForm.getIngredient(),
	            recipeForm.getCapacity(), recipeImages);
	      return String.format("redirect:/recipe/detail/%s", id);
	   }
	   
	// 레시피 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String recipeDelete(Principal principal, @PathVariable("id") Integer id) {
		Recipe recipe = this.recipeService.getRecipe(id);
		if (principal instanceof OAuth2AuthenticationToken) {
			String email = this.userService.getEmailFromPrincipal(principal);
			if ((!recipe.getAuthor().getEmail().equals(email))) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
			}
		} else if (principal instanceof UsernamePasswordAuthenticationToken) {
			String username = this.userService.getUsernameFromPrincipal(principal);
			if (!recipe.getAuthor().getUsername().equals(username)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
			}
		}
		this.recipeService.delete(recipe);
		return "redirect:/";
	}

	// 추천 버튼 클릭 시 호출
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String recipeVote(Principal principal, @PathVariable("id") Integer id) {
		Recipe recipe = this.recipeService.getRecipe(id);
		SiteUser siteUser = new SiteUser();
		if (principal instanceof OAuth2AuthenticationToken) {
			String email = this.userService.getEmailFromPrincipal(principal);
			siteUser = this.userService.getUserByEmail(email);
		} else if (principal instanceof UsernamePasswordAuthenticationToken) {
			siteUser = this.userService.getUserByUsername(principal.getName());
		}
		this.recipeService.vote(recipe, siteUser);
		return String.format("redirect:/recipe/detail/%s", id);
	}
	
	// 지도 주소 호출
	@GetMapping("/presentAddress")
	   @ResponseBody
	   public String presentAddress(Principal principal) {
	      if (principal instanceof UsernamePasswordAuthenticationToken) {
	         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	         String username = authentication.getName();
	         Optional<SiteUser> optionalUser = userRepository.findByUsername(username);

	         if (optionalUser.isPresent()) {
	            SiteUser user = optionalUser.get();
	            String encodedAddress = null;
	            try {
	               encodedAddress = URLEncoder.encode(user.getAddress1(), "UTF-8");
	            } catch (UnsupportedEncodingException e) {
	               e.printStackTrace();
	               return null;
	            }
	            return encodedAddress;
	         } else {
	            return null;
	         }
	      } else if (principal instanceof OAuth2AuthenticationToken) {
	         String email = userService.getEmailFromPrincipal(principal);
	         SiteUser siteUser = userService.getUserByEmail(email);

	         log.info("principal email: " + email);
	         String encodedAddress = null;

	         try {
	            encodedAddress = URLEncoder.encode(siteUser.getAddress1(), "UTF-8");
	            return encodedAddress;
	         } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	            return null;
	         }
	      } else {
	         return null;
	      }
	      
	   }
}
