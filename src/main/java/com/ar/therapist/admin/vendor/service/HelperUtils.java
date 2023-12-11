package com.ar.therapist.admin.vendor.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientResponse;

import reactor.core.publisher.Mono;

public class HelperUtils {
	
	public static String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}
	
	public static Mono<? extends Throwable> handleErrorResponse(ClientResponse response) {
		return response.bodyToMono(ErrorResponse.class)
				.flatMap(errorBody -> {
					return Mono.error(new ApiCallException(errorBody));
				});
	}
}
