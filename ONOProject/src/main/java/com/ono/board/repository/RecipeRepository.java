package com.ono.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ono.board.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	Recipe findBySubject(String subject);   // findBySubject
	List<Recipe> findBySubjectLike(String subject); // findBySubjectLike(특정문자열 포함)
	Page<Recipe> findAll(Pageable pageable); // 페이징 구현하기
	Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);  // 검색기능
	
	@Modifying
	@Query("UPDATE Recipe q SET q.view = q.view + 1 WHERE q.id = :id")
	void incrementViewCount(@Param("id") int id);                             // 추천레시피
}