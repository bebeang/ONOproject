package com.ono.board.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Entity
@Data
public class Recipe {
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 레시피의 고유 번호 - 기본키(primary key)
	
	@Column(length = 200)
	private String subject;  // 레시피 제목
	
	@Column(name = "content")
	private String content;  // 레시피 내용
	
	private LocalDateTime createDate;  // 레시피 최초 작성 일시
	
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
	
	@ManyToOne
	private SiteUser author;  // 레시피에 author 속성을 추가(엔티티 변경)
	
	private LocalDateTime modifyDate;   // 레시피 수정 일시
	
	@ManyToMany  // N:N
	Set<SiteUser> voter;     // 추천인(voter) 속성 추가
	                         // Set은 중복을 허용하지 않기 떄문에 사용
	
	private String fileName; // 썸네일 파일 이름
	
	private String filePath; // 썸네일 파일 주소
	
	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer view = 0; // 조회수

	private String cookIntro;     // 요리소개(간단히 요리를 소개합니다.)	

	private String cookInfo;      // 요리 정보(인원+시간+난이도)
	
	private String category;      // 카테고리
    
	private String cookInfo_level;     // 요리 정보(인원)

	private String cookInfo_people;     // 요리 정보(시간)

	private String cookInfo_time;     // 요리 정보(난이도)
	
    private String ingredient;         // 요리 재료
	
	private String capacity;           // 재료 량
	
	private String contentFilePath;    // 레시피 이미지
	
}
