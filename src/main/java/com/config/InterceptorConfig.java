package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.interceptor.AuthorizationInterceptor;

/**
 * 拦截器配置
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{

	@Bean
	public AuthorizationInterceptor getAuthorizationInterceptor() {
		return new AuthorizationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 在此处添加 .excludePathPatterns("/upload/**") 确保图片访问不被拦截
		registry.addInterceptor(getAuthorizationInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/static/**")
				.excludePathPatterns("/upload/**")
				.excludePathPatterns("/admin/**")
				.excludePathPatterns("/front/**")
				.excludePathPatterns("/public/**");
		super.addInterceptors(registry);
	}

	/**
	 * springboot 2.0配置WebMvcConfigurationSupport之后，会导致默认配置被覆盖，要访问静态资源需要重写addResourceHandlers方法
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String projectPath = System.getProperty("user.dir");

		// 【关键修改】：将路径映射到你刚才发现的那个真正的 upload 文件夹
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:" + projectPath + "/upload/");

		// 下面是原有的静态资源映射，保持不动
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/resources/")
				.addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/admin/")
				.addResourceLocations("classpath:/front/")
				.addResourceLocations("classpath:/public/");
		super.addResourceHandlers(registry);
	}
}
