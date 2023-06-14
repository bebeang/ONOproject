package com.ono.board.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ono.board.domain.SiteUser;
import com.ono.board.domain.UserModifyFormDto;
import com.ono.board.repository.UserRepository;
import com.ono.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MypageController {

	private final UserService userService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/mypage/modifyProfile")
	public String modify(Model model, Principal principal) { // , @PathVariable("userId") String userId

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (principal instanceof UsernamePasswordAuthenticationToken) {
			String username = authentication.getName();
			log.info("userId : " + authentication);
			Optional<SiteUser> userOptional = userRepository.findByUsername(username);
			if (userOptional.isPresent()) {
				SiteUser siteUser = userOptional.get();

				log.info("get siteUser : " + siteUser);
				model.addAttribute("userModifyForm", new UserModifyFormDto(siteUser));
				log.info("userModifyForm : " + model);

			} else {
				model.addAttribute("errorMessage", "User not found");
			}
		} else if (principal instanceof OAuth2AuthenticationToken) {
			String email = this.userService.getEmailFromPrincipal(principal);
			log.info("userEmail : " + email);
			Optional<SiteUser> userOptional = userRepository.findByEmail(email);
			if (userOptional.isPresent()) {
				SiteUser siteUser = userOptional.get();

				log.info("get siteUser : " + siteUser);
				model.addAttribute("userModifyForm", new UserModifyFormDto(siteUser));
				log.info("userModifyForm : " + model);

			} else {
				model.addAttribute("errorMessage", "User not found");
			}
		}
		return "mypage";
	}

	@PostMapping("/mypage") //
	public String modify(@Valid @ModelAttribute("userModifyForm") UserModifyFormDto userModifyForm,
			BindingResult bindingResult, Model model, Principal principal) {
		log.info("회원정보수정");
		log.info("set userModifyForm(전송) : " + userModifyForm);

		if (bindingResult.hasErrors()) {
			return "mypage";
		}

		// 현재 비밀번호와 일치하지 않을때 + 비밀번호 1번 2번이 다를때
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			String username = authentication.getName();
	
			Optional<SiteUser> optionalUser = userRepository.findByUsername(username);
			if (optionalUser.isPresent()) {
				SiteUser siteUser = optionalUser.get();
	
				if (!passwordEncoder.matches(userModifyForm.getPassword_now(), siteUser.getPassword())) {
					bindingResult.rejectValue("password_now", "password.mismatch", "~");
	
					log.info("현재 비밀번호 불일치");
					log.info("현재 비밀번호 : " + siteUser.getPassword());
					log.info("확인용 비밀번호 : " + userModifyForm.getPassword_now());
	
					return "mypage";
				}
	
				if (!userModifyForm.getPassword1().equals(userModifyForm.getPassword2())) {
					bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
					log.info("두 비밀번호 불일치");
					return "mypage";
				}
			}
	
			try {
				userService.modify(userModifyForm.getUsername(), userModifyForm.getPassword1(), userModifyForm.getEmail(),
						userModifyForm.getNickname(), userModifyForm.getMobile(), userModifyForm.getZip(),
						userModifyForm.getAddress1(), userModifyForm.getAddress2());
	
			} catch (DataIntegrityViolationException e) {
				e.printStackTrace();
				bindingResult.reject("modifyFailed", "개인정보 수정에 실패했습니다.");
				log.info("첫번째 캐치문");
				return "mypage";
	
			} catch (Exception e) {
				e.printStackTrace();
				bindingResult.reject("modifyFailed", e.getMessage());
				log.info("두번째 캐치문");
				return "mypage";
			}
		} else if (principal instanceof OAuth2AuthenticationToken) {
			
		}

		// 추가
		model.addAttribute("userModifyForm", userModifyForm);
		// 개인정보수정 완료 시 index페이지로 돌아가기
		return "redirect:/index";
	}

	@ResponseBody
	@PostMapping("/update-profile-pic")
	public String updateProfilePic(@RequestParam("profilePicPreview") MultipartFile profile) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Optional<SiteUser> optionalUser = userRepository.findByUsername(username);

		if (optionalUser.isPresent()) {
			SiteUser user = optionalUser.get();
			log.info(username);

			String profilePath = "D:\\kim\\boot\\profiles";
			String previous_ProfilePath = profilePath + user.getProfileName();
			File previousProfile = new File(previous_ProfilePath);

			log.info(previous_ProfilePath);
			previousProfile.delete();

			UUID uuid = UUID.randomUUID();
			String profileName = uuid + "_" + profile.getOriginalFilename();
			File saveProfile = new File(profilePath, profileName);
			try {
				profile.transferTo(saveProfile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			user.setProfileName(profileName);
			user.setProfilePath("/profiles/" + profileName);
			userRepository.save(user);
		}
		return "mypage/modifyProfile";
	}

	@ResponseBody
	@PostMapping("/delete-profile-pic")
	public String deleteProfilePic() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		Optional<SiteUser> optionalUser = userRepository.findByUsername(username);

		if (optionalUser.isPresent()) {
			log.info("프로필 삭제");
			SiteUser user = optionalUser.get();

			String defaultProfilePath = "/image/기본 프로필.jfif";
			String defaultProfileName = "기본 프로필.jfif";

			if (user.getProfileName() != defaultProfileName) {

				String profilePath = "D:\\kim\\boot\\profiles";
				String previous_ProfilePath = profilePath + user.getProfileName();
				File previousProfile = new File(previous_ProfilePath);

				log.info(previous_ProfilePath);

				previousProfile.delete();

				user.setProfileName(defaultProfileName);
				user.setProfilePath(defaultProfilePath);
				userRepository.save(user);
			} else {
				return "mypage/modifyProfile";
			}
		}
		return "mypage/modifyProfile";
	}
}