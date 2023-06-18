package com.ono.board.service;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ono.board.domain.OAuthAttributes;
import com.ono.board.domain.SessionUser;
import com.ono.board.domain.SiteUser;
import com.ono.board.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	private final UserRepository userRepository;
	private final HttpSession httpSession;
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint().getUserNameAttributeName();
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
		
		SiteUser siteUser = saveOrUpdate(attributes);
		
		httpSession.setAttribute("user", new SessionUser(siteUser));
		
		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(siteUser.getRoleKey())),
				attributes.getAttributes(),
				attributes.getNameAttributeKey());
	}
	
	private SiteUser saveOrUpdate(OAuthAttributes attributes) {
        SiteUser siteUser = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());
        return userRepository.save(siteUser);
    }
	
}
