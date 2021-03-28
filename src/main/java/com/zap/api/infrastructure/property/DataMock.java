package com.zap.api.infrastructure.property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zap.api.domain.property.Property;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DataMock {
	public static List<Property> properties;

	public static void loadDBMocked() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Property.class, new PropertyDeserializer(null));
		objectMapper.registerModule(module);

		InputStream resource = new ClassPathResource("source-2").getInputStream();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {

			properties = reader.lines().map(json -> {
				try {
					return objectMapper.readValue(json, Property.class);
				} catch (JsonProcessingException e) {
					log.error("Error during load data mock.", e);
				}
				return null;
			}).filter(property -> !Objects.isNull(property)).collect(Collectors.toList());

		}
	}
}
