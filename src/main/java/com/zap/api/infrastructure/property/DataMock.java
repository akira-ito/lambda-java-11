package com.zap.api.infrastructure.property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zap.api.common.exception.FailedLoadDBMockedException;
import com.zap.api.domain.property.Property;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class DataMock {
	private Collection<Property> properties;
	
	public Collection<Property> loadAndGetProperties() throws FailedLoadDBMockedException {
		try {
			return this.loadDBMocked();
		} catch (IOException e) {
			throw new FailedLoadDBMockedException("Error during load data mock");
		}
	}

	/**
	 * @deprecated Deve ser usado cache para melhor performace.
	 * 
	 * @return
	 * @throws FailedLoadDBMockedException
	 */
	public Collection<Property> loadOrGetProperties() throws FailedLoadDBMockedException {
		if (Objects.isNull(this.properties)) {
			try {
				this.loadDBMocked();
			} catch (IOException e) {
				throw new FailedLoadDBMockedException("Error during load data mock");
			}
		}
		return this.properties;
	}

	public Collection<Property> loadDBMocked() throws IOException {
		synchronized (this) {
			ObjectMapper objectMapper = new ObjectMapper();
			SimpleModule module = new SimpleModule();
			module.addDeserializer(Property.class, new PropertyDeserializer(null));
			objectMapper.registerModule(module);

			InputStream resource = new ClassPathResource("source-2").getInputStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {

				this.properties = reader.lines().map(json -> {
					try {
						return objectMapper.readValue(json, Property.class);
					} catch (JsonProcessingException e) {
						log.error("Error during load data mock.", e);
					}
					return null;
				}).filter(property -> !Objects.isNull(property)).collect(Collectors.toUnmodifiableList());
				return this.properties;
			}
		}
	}
}
