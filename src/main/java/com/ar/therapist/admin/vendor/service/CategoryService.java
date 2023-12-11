package com.ar.therapist.admin.vendor.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ar.therapist.admin.cloudinary.CloudinaryImageServiceImpl;
import com.ar.therapist.admin.common.config.JwtCommonService;
import com.ar.therapist.admin.vendor.model.CategoryDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
public class CategoryService {

//	private final WebClient webClient;
	private final JwtCommonService jwtCommonService;
	private final CloudinaryImageServiceImpl cloudService;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
	private final WebClient.Builder loadBalancedWebClientBuilder;
	
	@Value("${category.service.api.url.add}")
	private String CATEGORY_SERVICE_URL_ADD;
	
	@Value("${category.service.api.url.delete}")
	private String CATEGORY_SERVICE_URL_DELETE;
	
	@Value("${category.service.api.url.update}")
	private String CATEGORY_SERVICE_URL_UPDATE;
	
	public CategoryDTO addCategory(CategoryDTO category, MultipartFile file) {
		Map uploaded = cloudService.upload(file);
		System.err.println(uploaded);
		String url = (String) uploaded.get("url");
		category.setImageUrl(url);

		
		CategoryDTO categorySaved = 
		loadBalancedWebClientBuilder.filter(lbFunction).build()		
		.post()
		.uri(CATEGORY_SERVICE_URL_ADD)
		.header(HttpHeaders.AUTHORIZATION,
				"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
		.header("Username", HelperUtils.getAuthenticationUsername())
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(category))
		.retrieve()
		.onStatus(HttpStatusCode::isError, response-> HelperUtils.handleErrorResponse(response))
		.bodyToMono(CategoryDTO.class).block();
		
		return categorySaved;
	}


	public CategoryDTO updateCategory(Long id, CategoryDTO category, MultipartFile file) {
		Map uploaded = cloudService.upload(file);
		System.err.println(uploaded);
		String url = (String) uploaded.get("url");
		category.setImageUrl(url);
		
		CategoryDTO updatedCategory = 
		loadBalancedWebClientBuilder.filter(lbFunction).build()		
		.put()
		.uri(CATEGORY_SERVICE_URL_UPDATE+id)
		.header(HttpHeaders.AUTHORIZATION,
				"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
		.header("Username", HelperUtils.getAuthenticationUsername())
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(category))
		.retrieve()
		.onStatus(HttpStatusCode::isError, response-> HelperUtils.handleErrorResponse(response))
		.bodyToMono(CategoryDTO.class).block();
		
		
		return updatedCategory;
	}


	public String deleteCategory(Long id) {
		return 
				loadBalancedWebClientBuilder.filter(lbFunction).build()
				.delete()
				.uri(CATEGORY_SERVICE_URL_DELETE+id)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
				.header("Username", HelperUtils.getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, response-> HelperUtils.handleErrorResponse(response))
				.bodyToMono(String.class)
				.block();
	}
	
}
