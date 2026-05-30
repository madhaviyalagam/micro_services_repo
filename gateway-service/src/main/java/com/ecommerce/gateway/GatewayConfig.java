package com.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("user-service", r -> r.path("/api/users/**")
						.filters(f -> f.rewritePath("/api/users/(?<segment>.*)", "/${segment}"))
						.uri("lb://user-service"))
				.route("product-service", r -> r.path("/api/products/**")
						.filters(f -> f.rewritePath("/api/products/(?<segment>.*)", "/${segment}"))
						.uri("lb://product-service"))
				.route("order-service-orders", r -> r.path("/api/orders")
						.filters(f -> f.rewritePath("/api/orders", "/"))
						.uri("lb://order-service"))
				.route("order-service-cart", r -> r.path("/api/cart/**")
						.uri("lb://order-service"))
				.build();
	}
}
