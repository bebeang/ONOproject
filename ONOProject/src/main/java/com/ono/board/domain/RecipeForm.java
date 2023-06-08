package com.ono.board.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RecipeForm {

	@NotEmpty(message="제목은 필수항목입니다.")
	@Size(max=200)
	private String subject;
	
	private MultipartFile file;
	
	@NotEmpty(message="내용은 필수항목입니다.")
	private String cookIntro;     // 요리소개(간단히 요리를 소개합니다.)
	
    private String category;     // 카테고리
	
    private String cookInfo_level;     // 요리 정보(인원)

	private String cookInfo_people;     // 요리 정보(시간)

	private String cookInfo_time;     // 요리 정보(난이도)
	
    private String ingredient;        // 요리 재료
	
	private String capacity;         // 재료 용량
	
    private String content;         // 레시피 내용
	
	private MultipartFile contentFile;      // 레시피 이미지
	
}
