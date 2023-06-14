package com.ono.board.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ono.board.domain.Answer;
import com.ono.board.domain.Recipe;
import com.ono.board.domain.SiteUser;
import com.ono.board.repository.RecipeRepository;
import com.ono.board.repository.UserRepository;
import com.ono.board.util.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class RecipeService {

	private final RecipeRepository recipeRepository;
	private final UserRepository userRepository;
	
	// 검색 기능 (검색값 : kw)
	// Specification => 여러 테이블에서 데이터를 검색해야 할 경우에 JPA가 제공하는 인터페이스
	private Specification<Recipe> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Recipe> r, CriteriaQuery<?> query, CriteriaBuilder cb) {

				// r : 기준을 의미하는 Recipe
				query.distinct(true); // 중복을 제거

				Join<Recipe, SiteUser> u1 = r.join("author", JoinType.LEFT);
				// u1 : Recipe엔티티와 SiteUser 엔티티를 아우터 조인 하여 만든 SiteUser 엔티티의 객체

				Join<Recipe, Answer> a = r.join("answerList", JoinType.LEFT);
				// a : Recipe 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체

				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				// u2 : a 와 다시한번 SiteUser 엔티티와 아우터 조인하여 SiteUser 엔티티의 객체(답변 작성자를 검색하기 위해서 필요)

				return cb.or(cb.like(r.get("subject"), "%" + kw + "%"), // 제목
						cb.like(r.get("content"), "%" + kw + "%"), // 내용
						cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%"), // 답변 작성자
						cb.like(r.get("category"), "%" + kw + "%"), // 카테고리 << 추가 1
						cb.like(r.get("cookInfo"), "%" + kw + "%")); // 요리정보 << 추가 2
			}
		};
	}

	public List<Recipe> getList() {
		return this.recipeRepository.findAll();
	}

	public Recipe getRecipe(Integer id) {
		Optional<Recipe> recipe = this.recipeRepository.findById(id);
		if (recipe.isPresent()) {
			return recipe.get();
		} else {
			throw new DataNotFoundException("recipe not found");
		}
	}

	// 레시피 저장 기능
	public void create(String subject, String content, String username, String email, MultipartFile file, String cookIntro,
			String category, String cookInfo_level, String cookInfo_people, String cookInfo_time, String ingredient, 
			String capacity, List<MultipartFile> contentFiles) throws Exception {

		if (file.isEmpty() || contentFiles.isEmpty()) {
            throw new IllegalArgumentException("File parameters cannot be null or empty");
       }
        
        String projectPath = "D://kim//boot//files";
        UUID uuid = UUID.randomUUID();

        List<String> contentFilePaths = new ArrayList<>();

        // 요리 썸네일
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        
        log.info("file : " + file);
       

        // 레시피 순 이미지
        for (MultipartFile contentFile : contentFiles) {
            String contentFileName = uuid + "_" + contentFile.getOriginalFilename();
            File contentSaveFile = new File(projectPath + "/contents", contentFileName);
            contentFile.transferTo(contentSaveFile);
            contentFilePaths.add("/files/contents/" + contentFileName);
        }
        
        log.info("contentFiles : " + contentFiles);

        Recipe r = new Recipe();

        // Set the updated fields
        r.setFileName(fileName);
        r.setFilePath("/files/" + fileName);
        r.setSubject(subject);
        r.setCreateDate(LocalDateTime.now());
        r.setCookIntro(cookIntro);
        r.setCategory(category);
        r.setCookInfo(cookInfo_level + cookInfo_people + cookInfo_time);
        r.setCookInfo_level(cookInfo_level);
        r.setCookInfo_people(cookInfo_people);
        r.setCookInfo_time(cookInfo_time);
        r.setIngredient(ingredient);
        r.setCapacity(capacity);
        r.setContent(content);
        r.setContentFilePaths(contentFilePaths);

		SiteUser siteUser = null;
		if(username != null) {
			siteUser = this.userRepository.findByUsernameOrEmail(username, null);
		} else if(email != null) {
			siteUser = this.userRepository.findByUsernameOrEmail(null, email);
		} if(siteUser == null) {
			throw new DataNotFoundException("존재하지 않는 사용자입니다.");
		}
		
		r.setAuthor(siteUser);
		this.recipeRepository.save(r);
	}
	
	// 페이징 구현 기능(검색 기능 추가)
	public Page<Recipe> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Recipe> spec = search(kw);
		return this.recipeRepository.findAll(spec, pageable);
	}

	// 최근 게시물
	public Page<Recipe> getRecentlyList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 4, Sort.by(sorts));
		Specification<Recipe> spec = search(kw);
		return this.recipeRepository.findAll(spec, pageable);
	}

	// 조회수 많은 게시물
	public Page<Recipe> getTopList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("view"));
		Pageable pageable = PageRequest.of(page, 4, Sort.by(sorts));
		Specification<Recipe> spec = search(kw);
		return this.recipeRepository.findAll(spec, pageable);
	}

	// 모든 게시물
	public Page<Recipe> getAllList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		int pageSize = 20; // 한 페이지에 나오는 항목 수

		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
		Specification<Recipe> spec = search(kw);

		return this.recipeRepository.findAll(spec, pageable);
	}

	// 레시피 수정 기능
	   public void modify(Recipe recipe, String subject, String content, MultipartFile file, String cookIntro,
	         String category, String cookInfo_level, String cookInfo_people, String cookInfo_time, String ingredient,
	         String capacity, List<MultipartFile> contentFiles) throws Exception {
	      
	      if (file.isEmpty() || contentFiles.isEmpty()) {
	         throw new IllegalArgumentException("File parameters cannot be null or empty");
	      }
	      String projectPath = "D://kim//boot//files";
	      UUID uuid = UUID.randomUUID();
	      List<String> contentFilePaths = new ArrayList<>();

	      // 요리 썸네일
	      String fileName = uuid + "_" + file.getOriginalFilename();
	      File saveFile = new File(projectPath, fileName);
	      file.transferTo(saveFile);

	      // 레시피 순 이미지
	      for (MultipartFile contentFile : contentFiles) {
	         String contentFileName = uuid + "_" + contentFile.getOriginalFilename();
	         File contentSaveFile = new File(projectPath + "/contents", contentFileName);
	         contentFile.transferTo(contentSaveFile);
	         contentFilePaths.add("/files/contents/" + contentFileName);
	      }
	      
	      recipe.setFileName(fileName);
	      recipe.setFilePath("/files/" + fileName);
	      recipe.setSubject(subject);
	      recipe.setContent(content);
	      recipe.setCookIntro(cookIntro);
	      recipe.setCategory(category);
	      recipe.setCookInfo(cookInfo_level + cookInfo_people + cookInfo_time);
	      recipe.setCookInfo_level(cookInfo_level);
	      recipe.setCookInfo_people(cookInfo_people);
	      recipe.setCookInfo_time(cookInfo_time);
	      recipe.setIngredient(ingredient);
	      recipe.setCapacity(capacity);
	      recipe.setContentFilePaths(contentFilePaths);
	      recipe.setModifyDate(LocalDateTime.now());
	      this.recipeRepository.save(recipe);
	   }

	// 레시피 삭제 기능
	public void delete(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}

	// 추천
	public void vote(Recipe recipe, SiteUser siteuser) {
		recipe.getVoter().add(siteuser);
		this.recipeRepository.save(recipe);
	}

	// 전체 레시피 수
	public long getTotalCount() {
		return recipeRepository.count();
	}

	@Transactional
	public void incrementViewCount(int id) {
		recipeRepository.incrementViewCount(id);
	}

}
