package teamproject.aipro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("http://localhost:3000", "https://ai-pro-fe.vercel.app/chat")
			.allowedMethods("*")
			.allowCredentials(true);
	}
}
