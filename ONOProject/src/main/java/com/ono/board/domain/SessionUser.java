package com.ono.board.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class SessionUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private String picture;
	private String nickname;
	private String gender;
	
	public SessionUser(SiteUser siteUser) {
		this.name = siteUser.getName();
		this.email = siteUser.getEmail();
		this.picture = siteUser.getProfilePath();
		this.nickname = siteUser.getNickname();
		this.gender = siteUser.getGender();
	}
	
}