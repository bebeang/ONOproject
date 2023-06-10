package com.ono.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ono.board.domain.Recipe;
import com.ono.board.domain.SiteUser;
import com.ono.board.repository.UserRepository;
import com.ono.board.util.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password, String email,
    	     String name, String nickname, MultipartFile profile, 
    	     String mobile, String zip,
    	     String address1, String address2, Date joindate) throws Exception {

    	        SiteUser user = new SiteUser();
    	        
    	        if (profile == null) {
    	        	
    	            // 기본 프로필 사진 경로와 파일명
    	            String defaultProfilePath = "/image/기본 프로필.jfif";
    	            String defaultProfileName = "기본 프로필.jfif";

    	            user.setProfileName(defaultProfileName);
    	            user.setProfilePath(defaultProfilePath);

    	        } else {

    	            String profilePath = "D:\\kim\\boot\\profiles";
    	            UUID uuid = UUID.randomUUID();
    	            String profileName = uuid + "_" + profile.getOriginalFilename();
    	            File saveProfile = new File(profilePath, profileName);
    	            profile.transferTo(saveProfile);
    	            user.setProfileName(profileName);
    	            user.setProfilePath("/profiles/" + profileName);
    	        }

    	        user.setUsername(username);
    	        user.setPassword(passwordEncoder.encode(password));
    	        user.setEmail(email);
    	        user.setName(name);
    	        user.setNickname(nickname);
//    	        user.setGender(gender);
    	        user.setMobile(mobile);
    	        user.setZip(zip);
    	        user.setAddress1(address1);
    	        user.setAddress2(address2);
//    	        user.setBirthday(birthday);
//    	        user.setGrade("bronze");  // 기본
    	        user.setJoindate(new Date());
    	        this.userRepository.save(user);
    	        return user;
    	    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
    
    public SiteUser modify(String username, String password, String email, String nickname, String mobile, String zip,
    		String address1, String address2) {
    	
    		// 현재 사용자 개인정보를 저장하는 걸로 수정해야함
    		log.info("User service modify");
    		Optional<SiteUser> optionalUser = userRepository.findByusername(username);
    		SiteUser user = optionalUser.get();

    		user.setPassword(passwordEncoder.encode(password));
    		user.setEmail(email);
    		user.setNickname(nickname);
    		user.setMobile(mobile);
    		user.setZip(zip);
    		user.setAddress1(address1);
    		user.setAddress2(address2);

    		this.userRepository.save(user);

    		return user;
    		}
    
    public List<SiteUser> getUsers(Page<Recipe> recipes) {
        List<SiteUser> users = new ArrayList<>();
        List<Recipe> recipeList = recipes.toList();
        for (int i = 0; i < recipeList.size(); i++) {
            Recipe recipe = recipeList.get(i);
            SiteUser siteUser = userRepository.findById((long) (recipe.getAuthor().getId())).get();
            users.add(siteUser);
        }
        return users;
    }
    
}