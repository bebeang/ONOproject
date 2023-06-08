package com.ono.board.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class SiteUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;          // 게시물 ID
	
	@Column(nullable = false, unique = true, name = "username")
	private String username; // 아이디
	
	@Column(name = "password")
	private String password; // 비밀번호
	
	@Column(unique = true, name = "email")
	private String email; // 이메일
	
	@Column(name = "name")
	private String name; // 이름
	
	@Column(name = "nickname")
	private String nickname; // 닉네임

	@Column(unique = true, name = "mobile")
	private String mobile; // 휴대폰 번호
	
	@Column(name = "zip")
	private String zip; // 우편번호
	
	@Column(name = "address1")
	private String address1; // 주소
		
	@Column(name = "address2")
	private String address2; // 상세주소
	
	@Column(name = "joindate", nullable = false, updatable = false)
	private Date joindate; // 가입일
	
	@Column(name = "profileName")
	private String profileName; // 프로필사진 이름

	@Column(name = "profilePath")
	private String profilePath; // 프로필사진 경로
}