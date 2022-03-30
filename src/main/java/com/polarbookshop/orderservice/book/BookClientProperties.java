package com.polarbookshop.orderservice.book;

import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@Data
@ConfigurationProperties(prefix = "polar")
public class BookClientProperties {

	@NotNull
	private URI catalogServiceUrl;
}
