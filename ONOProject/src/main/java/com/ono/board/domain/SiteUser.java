package com.ono.board.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SiteUser extends BaseTimeEntity{
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;          // 아이디
   
   @Column(unique = true)
   private String username; // 아이디
   
   @Column
   private String password; // 비밀번호
   
   @Column(nullable = false, unique = true)
   private String email; // 이메일
   
   @Column(nullable = false)
   private String name; // 이름
   
   @Column
   private String nickname; // 닉네임
   
   @Column
   private String profileName; // 프로필사진 이름
   
   @Column
   private String profilePath; // 프로필사진 경로
   
   @Column
   private String gender; // 성별
   
   @Column(unique = true)
   private String mobile; // 휴대폰 번호
   
   @Column
   private String zip; // 우편번호
   
   @Column
   private String address1; // 주소
      
   @Column
   private String address2; // 상세주소
   
   @Column
   private Date birthday; // 생년월일
   
   @Column(updatable = false)
   private Date joindate; // 가입일
   
   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private UserRole role; // 롤
   
   @Builder
   public SiteUser(String name, String email, String profilePath, String nickname, String gender, UserRole role) {
      this.name = name;
      this.email = email;
      this.profilePath = profilePath;
      this.nickname = nickname;
      this.gender = gender;
      this.role = role;
   }
   
   public SiteUser update(String name) {
      this.name = name;
//      this.profilePath = profilePath;
//      this.nickname = nickname;
//      this.gender = gender;
      return this;
   }
   
   public String getRoleKey() {
      return UserRole.USER.getValue();
      // return this.role.getKey();
      // return this.role.getKey() == null ? UserRole.USER.getValue() : this.role.getKey();
   }
}