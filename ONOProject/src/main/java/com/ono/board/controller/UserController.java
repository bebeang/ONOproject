package com.ono.board.controller;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ono.board.domain.UserCreateForm;
import com.ono.board.repository.UserRepository;
import com.ono.board.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
	
	 private final UserService userService;
	 private final UserRepository userRepository;
	 
	 @GetMapping("/signup")
	   public String signup(UserCreateForm userCreateForm) {
	      return "signup_form";
	   }
	 
	 @PostMapping("/signup")
	    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {

	     if (bindingResult.hasErrors()) {

	         // 회원가입 실패시 입력 데이터 값을 유지
	            return "signup_form";
	        }

	         // 비밀번호 1번과 2번이 다릅니다. 
	        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
	            bindingResult.rejectValue("password2", "passwordInCorrect", 
	                    "2개의 패스워드가 일치하지 않습니다.");
	            return "signup_form";
	        }

	        try {
				userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), 
				userCreateForm.getEmail(), userCreateForm.getName(),
				userCreateForm.getNickname(), userCreateForm.getProfile(),
				userCreateForm.getMobile(), userCreateForm.getZip(), 
				userCreateForm.getAddress1(), userCreateForm.getAddress2(),
				userCreateForm.getJoindate());

	        } catch(DataIntegrityViolationException e) {
	            e.printStackTrace();
	            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
	            return "signup_form";

	        } catch(Exception e) {
	            e.printStackTrace();
	            bindingResult.reject("signupFailed", e.getMessage());
	            return "signup_form";
	        }

	        return "redirect:/";
	    }

	    // 로그인 버튼 
	    @GetMapping("/login")
	    public String login() {
	        return "login_form";
	    }

	    // 아이디 중복 체크
	    @GetMapping("/check-username")
	    public ResponseEntity<?> checkUsernameAvailability(@RequestParam(value = "username") String username) {
	        if (userRepository.existsByUsername(username)) {
	            return ResponseEntity.badRequest().body("Username is already taken!");
	        } else {
	            return ResponseEntity.ok().build();
	        }
	    }
	    
	    // 이메일 중복체크
	    @GetMapping("/check-email")
	    public ResponseEntity<?> checkEmailAvailability(@RequestParam(value = "email") String email) {
	        if (userRepository.existsByEmail(email)) {
	            return ResponseEntity.badRequest().body("Email is already taken!");
	        } else {
	            return ResponseEntity.ok().build();
	        }
	    }
	    
	    
	    // 닉네임 중복체크
	    @GetMapping("/check-nickname")
	    public ResponseEntity<?> checkNicknameAvailability(@RequestParam(value = "nickname") String nickname) {
	        if (userRepository.existsByNickname(nickname)) {
	            return ResponseEntity.badRequest().body("Nickname is already taken!");
	        } else {
	            return ResponseEntity.ok().build();
	        }
	    }
	    
	    // 핸드폰번호 중복체크
	    @GetMapping("/check-mobile")
	    public ResponseEntity<?> checkMobileAvailability(@RequestParam(value = "mobile") String mobile) {
	        if (userRepository.existsByMobile(mobile)) {
	            return ResponseEntity.ok().body(false); // 이미 사용 중인 경우 false를 반환
	        } else {
	            return ResponseEntity.ok().body(true); // 사용 가능한 경우 true를 반환
	        }
	    }
}

