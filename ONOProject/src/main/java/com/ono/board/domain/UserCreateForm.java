package com.ono.board.domain;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserCreateForm {
	
	/** 아이디 */
	@Pattern(regexp="[a-zA-Z]{1}\\w{5,19}",
			 message="비밀번호는 영문을 포함한 6~20자로 입력하십시오.")
	@NotEmpty(message="아이디는 필수사항입니다.")
	private String username;
	
	/**비밀번호 */
	@Pattern(regexp="(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W).{8,20}",
			 message="비밀번호는 8~20사이의 영문 대소/특수문자를 최소 1자 이상씩  입력하십시오.")
	@NotEmpty(message="비밀번호는 필수사항입니다.")
	private String password1;
	
	/** 비밀번호 확인 */
	@Pattern(regexp="(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W).{8,20}",
		   	message="비밀번호는 8~20사이의 영문 대소/특수문자를 최소 1자 이상씩  입력하십시오.")
	@NotEmpty(message="비밀번호 확인은 필수사항입니다.")
	private String password2;
	
	/** 이메일 */
	@Email(message="회원 이메일을 정확한 이메일 형식(abcd@abcd.com)으로 입력하십시오.")
	@NotEmpty(message="이메일은 필수사항입니다.")
	private String email;
	
	/** 이름 */
	@Pattern(regexp="[ 가-힣]{2,30}",
			message="회원 이름은 한글만 허용됩니다.")
	@NotEmpty(message="이름은 필수사항입니다.")
	private String name;
	
	/** 닉네임 */
	@Pattern(regexp="^[a-zA-Z0-9가-힣]{2,10}$",
			message="닉네임은 특수문자를 제외한 2~10자로 입력하십시오.")
	@NotEmpty(message="닉네임은 필수사항입니다.")
	private String nickname;
	
//	/** 성별 */
//	@Pattern(regexp="[f, m]{1}",
//			message="회원 성별을 입력하십시오.")
//	@NotEmpty(message="성별은 필수사항입니다.")
//	private String gender;
	
	/** 휴대폰 번호 */
	@Pattern(regexp="010-\\d{4}-\\d{4}",
			message="회원 연락처 정확한 전화번호 형식(010-1234-5678)으로 입력하십시오.")
	@NotEmpty(message="휴대폰 번호는 필수사항입니다.")
	private String mobile;
	
	/** 우편번호 */
	@NotEmpty(message="우편번호는 필수사항입니다.")
	private String zip;
	
	/** 주소*/
	@NotEmpty(message="주소는 필수사항입니다.")
	private String address1;
	
	/** 상세주소 */
	private String address2;
	
//	/** 생년월일*/
//	@Past(message="생일은 금일 기준 이전 일이 들어가야 합니다.")
//	@DateTimeFormat(pattern="yyyy-MM-dd")
//	@Temporal(TemporalType.DATE)
//	private Date birthday;
	
	/** 가입일 */
	private Date joindate;
	
//	/** 등급 */
//	private String grade;
	
	/** 프로필사진 */
	private MultipartFile profile;
	
}