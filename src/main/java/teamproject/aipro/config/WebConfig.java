import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("https://www.mjuaipro.site", "https://ai-pro-fe.vercel.app")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("Content-Type", "Authorization", "Accept",
				"Sec-CH-UA", "Sec-CH-UA-Mobile", "Sec-CH-UA-Platform")
			.exposedHeaders("Authorization")
			.allowCredentials(true);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
