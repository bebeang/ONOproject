package com.ono.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		
		registry.addResourceHandler("/jquery/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/jquery/");
		
		registry.addResourceHandler("/bootstrap/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/");
		
		registry.addResourceHandler("/image/**")
				.addResourceLocations("classpath:/static/image/").setCachePeriod(60 * 60 * 24 * 365);
		
		/* '/js/**'로 호출하는 자원은 '/static/js/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/").setCachePeriod(60 * 60 * 24 * 365); 
		
        /* '/css/**'로 호출하는 자원은 '/static/css/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/").setCachePeriod(60 * 60 * 24 * 365); 
		
        /* '/font/**'로 호출하는 자원은 '/static/font/' 폴더 아래에서 찾는다. */ 
        registry.addResourceHandler("/font/**")
                .addResourceLocations("classpath:/static/font/").setCachePeriod(60 * 60 * 24 * 365); 
		
        /* '/files/**'로 호출하는 자원은 '/static/files/' 폴더 아래에서 찾는다. */
        registry.addResourceHandler("/files/**")
          .addResourceLocations("file:///D:/kim/boot/files/").setCachePeriod(60 * 60 * 24 * 365); // 절대경로
        //.addResourceLocations("classpath:/static/files/").setCachePeriod(60 * 60 * 24 * 365);
        
        
        /* '/profiles/**'로 호출하는 자원은 '/static/profiles/' 폴더 아래에서 찾는다. */
        registry.addResourceHandler("/profiles/**")
          .addResourceLocations("file:///D:/kim/boot/profiles/").setCachePeriod(60 * 60 * 24 * 365); // 절대경로
        //.addResourceLocations("classpath:/static/profiles/").setCachePeriod(60 * 60 * 24 * 365);
        
        /* '/contentfiles/**'로 호출하는 자원은 '/static/contents/' 폴더 아래에서 찾는다. */
        registry.addResourceHandler("/contents/**")
          .addResourceLocations("file:///D:/kim/boot/files/contents/").setCachePeriod(60 * 60 * 24 * 365); // 절대경로
        //.addResourceLocations("classpath:/static/contents/").setCachePeriod(60 * 60 * 24 * 365);
        
	}
}

