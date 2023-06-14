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

import lombok.Data;

@Entity
@Data
public class Recipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 질문의 고유 번호 - 기본키(primary key)

	@Column(length = 200)
	private String subject; // 질문의 제목

	@Column(columnDefinition = "TEXT")
	private String content; // 질문의 내용

	@Column
	private LocalDateTime createDate; // 질문을 작성한 일시

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;

	@ManyToOne
	private SiteUser author; // 질문에 author 속성을 추가(엔티티 변경)

	@Column
	private LocalDateTime modifyDate; // 질문 수정 일시

	@ManyToMany // N:N
	Set<SiteUser> voter; // 추천인(voter) 속성 추가

	@Column
	private String fileName; // 이미지 이름

	@Column
	private String filePath; // 이미지 주소

	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer view = 0; // 조회수

	@Column
	private String category; // 카테고리

	@Column
	private String cookInfo; // 요리 정보(인원+시간+난이도)

	@Column
	private String cookInfo_people; // 요리 정보(인원)

	@Column
	private String cookInfo_time; // 요리 정보(시간)

	@Column
	private String cookInfo_level; // 요리 정보(난이도)

	@Column
	private String cookIntro; // 요리소개(간단히 요리를 소개합니다.)

	@Column
	private String ingredient; // 요리 재료

	@Column
	private String capacity; // 재료 량

	@Column
	@ElementCollection
	private List<String> contentFilePaths; // 레시피 이미지
}
