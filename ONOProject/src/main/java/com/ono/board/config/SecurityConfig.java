package com.ono.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ono.board.domain.UserRole;
import com.ono.board.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
// @EnableMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	
	private final CustomOAuth2UserService customOAuth2UserService;
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().requestMatchers(
                new AntPathRequestMatcher("/**"))
            .permitAll()
        	.and()
        		.csrf()
        		.ignoringRequestMatchers(
        				new AntPathRequestMatcher("/**"))
        	.and()
//                .headers()
//                .addHeaderWriter(new XFrameOptionsHeaderWriter(
//                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
        	.headers().frameOptions().disable()
            .and()
                //.authorizeRequests()
            	.authorizeHttpRequests()
                // .antMatchers("/", "/css/**", "/webjars/**", "/images/**", "/js/**", "/profile").permitAll()
            	.antMatchers("/", "/css/**", "/webjars/**", "/images/**", "/js/**", "/profile").permitAll()
                // .antMatchers("/api/v1/**").hasRole(Role.USER.name())
            	.antMatchers("/api/v1/**").hasRole(UserRole.USER.name())
                // .antMatchers("/login/oauth2/**").hasRole(Role.USER.name())
	                .anyRequest().authenticated()
                
            .and()
	    			.formLogin()
	    			.loginPage("/user/login")
	    			.defaultSuccessUrl("/")
	    	.and()
	    			.oauth2Login()
	    			.userInfoEndpoint()
	    			.userService(customOAuth2UserService)
	    		;
    			
	    	// .and()
        	http
				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
    			;
        
        return http.build();
    }
    
    @Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
		
    	// swagger config
    	return (web) -> web.ignoring().antMatchers("/webjars/**", "/summernote/**",
    			"/image/**", "/bootstrap/**", "/jquery/**", "/js/**");
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}