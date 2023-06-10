package com.ono.board.domain;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Entity // 
@Data // Getter, Setter, ToString, EqualsAndHashCode, RequiredArgsConstructor 
public class Answer {
	
	@Id // Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 답변의 고유 번호
	
	@Column(columnDefinition = "TEXT")  // 데이타베이스의 테이블에 있는 컴럼과 동일하게 1:1로 매칭
	private String content; // 질문(어떤 질문의 답변인지 알아야 하므로 질문 속성이 필요하다)
	
	@CreatedDate
	private LocalDateTime createDate; // 답변의 내용
	
	@ManyToOne  //  ManyToOne 은 부모 자식 관계를 갖는 구조에서 사용한다. 부모 = Question / 자식 = Answer
	private Recipe recipe;  // 답변을 작성한 일시
	
	@ManyToOne  // OneToMay => 1:N , ManyToOne => N:1
	private SiteUser author;    // Answer 속성에 author 속성 추가
	
	private LocalDateTime modifyDate;   // 답변 수정 일시 표기
	
	@ManyToMany    // N:N
	Set<SiteUser> voter;    // voter(추천인) 속성 추가
	
}
