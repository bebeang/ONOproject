package com.ono.board.domain;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class OAuthAttributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String nickname;
	private String picture;
	
	private String authVendor;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String nickname) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.nickname = nickname;
		this.picture = picture;
	}
	
	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		OAuthAttributes result = null;
		if("naver".equals(registrationId)) {
			result = ofNaver("id", attributes);
//		} else if("google".equals(registrationId)) {
//			result = ofGoogle(userNameAttributeName, attributes);
//		} else if("kakao".equals(registrationId)) {
//			result = ofKakao("id", attributes);
		}
		log.info("registrationId : " + registrationId);
		result.setAuthVendor(registrationId);
		return result;
	}
	
	// naver
	@SuppressWarnings("unchecked")
	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuthAttributes.builder()
				.name((String) response.get("name"))
				.email((String) response.get("email"))
				.picture((String) response.get("profile_image"))
				.nickname((String) response.get("nickname"))
				.attributes(response)
				.nameAttributeKey(userNameAttributeName)
				.build();
	}
	
	// google
//	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
//		return OAuthAttributes.builder()
//				.name((String) attributes.get("name"))
//				.email((String) attributes.get("email"))
//				.picture((String) attributes.get("picture"))
//				.attributes(attributes)
//				.nameAttributeKey(userNameAttributeName)
//				.build();
//	}
	
	 // kakao
//    @SuppressWarnings("unchecked")
//    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
//		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
//        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
//        return OAuthAttributes.builder()
//                .name((String) kakaoProfile.get("nickname"))
//                .email((String) kakaoAccount.get("email"))
//                .picture((String) kakaoProfile.get("image"))
//                .nameAttributeKey(userNameAttributeName)
//                .attributes(attributes)
//                .build();
//    }
	
	public SiteUser toEntity() {
		return SiteUser.builder()
				.name(name)
				.email(email)
				.profilePath(picture)
				.nickname(nickname)
				.role(UserRole.USER)
				.build();
	}
	
}
