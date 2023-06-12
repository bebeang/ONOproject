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
	
	@Column(name = "subject")
	@NotEmpty(message="제목은 필수항목입니다.")
	@Size(max=200)
	private String subject;
	
	@Column(name = "file")
	private MultipartFile file;
	
	@Column(name = "cookintro")
	@NotEmpty(message="내용은 필수항목입니다.")
	private String cookIntro;     // 요리소개(간단히 요리를 소개합니다.)
	
	@Column(name = "category")
    private String category;     // 카테고리
	
	@Column(name = "cookinfo_level")
    private String cookInfo_level;     // 요리 정보(인원)

	@Column(name = "cookinfo_people")	
	private String cookInfo_people;     // 요리 정보(시간)

	@Column(name = "cookinfo_time")
	private String cookInfo_time;     // 요리 정보(난이도)
	
	@Column(name = "ingredient")
    private String ingredient;        // 요리 재료
	
	@Column(name = "capacity")
	private String capacity;         // 재료 용량
	
	@Column(name = "content")
    private String content;         // 레시피 내용
	
	@Column(name = "contentfile")
	private MultipartFile contentFile;      // 레시피 이미지
	
}
