package com.zap.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zap.api.domain.property.converter.PropertyConverter;

@Configuration
public class ZapConfiguration implements WebMvcConfigurer {

	@Bean
	public ObjectMapper objectMapper() {
		return new Jackson2ObjectMapperBuilder().indentOutput(true)
				.propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new PropertyConverter());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/v1/**").allowedOrigins("*").allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
				.allowedHeaders("*").maxAge(3600);
	}
}
