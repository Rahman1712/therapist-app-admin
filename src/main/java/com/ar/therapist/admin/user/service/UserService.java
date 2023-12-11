package com.ar.therapist.admin.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.ar.therapist.admin.common.config.JwtCommonService;
import com.ar.therapist.admin.user.model.UserDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final WebClient webClient;
	private final JwtCommonService jwtCommonService;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	private final WebClient.Builder loadBalancedWebClientBuilder;

	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}

	@Value("${user.service.api.url.demo}")
	private String USER_SERVICE_URL_DEMO;
	
	@Value("${user.service.api.url.getall}")
	private String USER_SERVICE_URL_GETALL;
	
	@Value("${user.service.api.url.getbyid}")
	private String USER_SERVICE_URL_GETBYID;
	
	@Value("${user.service.api.url.update_nonlocked}")
	private String USER_SERVICE_URL_UPDATE_NONLOCKED;
	
	public List<UserDTO> getAllUsers() {
		List<UserDTO> usersList = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(USER_SERVICE_URL_GETALL)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatusCode::isError, t -> t.createError())
				.bodyToFlux(UserDTO.class)
				.collectList()
				.block()
				;
		return usersList;
	}
	
	public String updateNonLockedByUserId(Long userId, boolean nonlocked) {
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(USER_SERVICE_URL_UPDATE_NONLOCKED+userId)
				.queryParam("nonlocked", nonlocked);
		
		String uriPathWithQueryParams = uriBuilder.toUriString();
		System.err.println("ddd :"+uriPathWithQueryParams);
		return 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, t-> t.createError())
				.bodyToMono(String.class)
				.block();
	}
	
	private String getAuthenticationUsername() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		return authentication.getName();
	}

	public UserDTO getUserById(Long id) {
		UserDTO user = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(USER_SERVICE_URL_GETBYID+id)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(getAuthenticationUsername()))
				.header("Username", getAuthenticationUsername())
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatusCode::isError, t -> t.createError())
				.bodyToMono(UserDTO.class)
				.block()
				;
		return user;
	}

	public String demoGet() {
		return 
		loadBalancedWebClientBuilder().filter(lbFunction).build()		
		.get()
		.uri(USER_SERVICE_URL_DEMO)
		.header(HttpHeaders.AUTHORIZATION,
				"Bearer "+jwtCommonService.generateToken(getAuthenticationUsername()))
		.header("Username", getAuthenticationUsername())
		.retrieve()
		.onStatus(HttpStatusCode::isError, t-> t.createError())
		.bodyToMono(String.class)
		.block();
	}
}
