package com.zap.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ConfigurationProperties(prefix = "zap")
@RequiredArgsConstructor
@Component
@Getter
@Setter
public class ZapProperties {
	private Integer pageNumber;
	private Integer pageSize;
}
