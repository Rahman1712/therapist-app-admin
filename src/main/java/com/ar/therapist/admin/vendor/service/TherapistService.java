package com.ar.therapist.admin.vendor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.ar.therapist.admin.common.config.JwtCommonService;
import com.ar.therapist.admin.vendor.model.TherapistDTO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TherapistService {

//	private final WebClient webClient;
	private final JwtCommonService jwtCommonService;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	private final WebClient.Builder loadBalancedWebClientBuilder;
	public WebClient.Builder loadBalancedWebClientBuilder() {
	    return WebClient.builder();
	}
	
//	@Autowired 
//	private JwtCommonService jwtCommonService;
//	@Autowired
//	private ReactorLoadBalancerExchangeFilterFunction lbFunction;
//	@Bean
//	public WebClient.Builder loadBalancedWebClientBuilder() {
//	    return WebClient.builder();
//	}

	@Value("${therapist.service.api.url.getall}")
	private String THERAPIST_SERVICE_URL_GETALL;
	
	@Value("${therapist.service.api.url.getbyid}")
	private String THERAPIST_SERVICE_URL_GETBYID;
	
	@Value("${therapist.service.api.url.activate}")
	private String THERAPIST_SERVICE_URL_UPDATE_ACTIVATE;
	
	@Value("${therapist.service.api.url.enabled}")
	private String THERAPIST_SERVICE_URL_UPDATE_ENABLED;
	
	@Value("${therapist.service.api.url.update_categories}")
	private String THERAPIST_SERVICE_URL_UPDATE_CATEGORIES;
	
	public List<TherapistDTO> getAllUsers() {
		List<TherapistDTO> usersList = 
				loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(THERAPIST_SERVICE_URL_GETALL)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
				.header("Username", HelperUtils.getAuthenticationUsername())
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatusCode::isError, t -> t.createError())
				.bodyToFlux(TherapistDTO.class)
				.collectList()
				.block()
				;
		return usersList;
	}

	public String activateOrDeactivateById(Long therapistId, boolean activate) {
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(THERAPIST_SERVICE_URL_UPDATE_ACTIVATE+therapistId)
				.queryParam("activate", activate);
		
		String uriPathWithQueryParams = uriBuilder.toUriString();
		System.err.println("ddd :"+uriPathWithQueryParams);
		return  loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
				.header("Username", HelperUtils.getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, t-> t.createError())
				.bodyToMono(String.class)
				.block();
	}
	
	public String enableDisableById(Long therapistId, boolean enabled) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(THERAPIST_SERVICE_URL_UPDATE_ENABLED+therapistId)
				.queryParam("enabled", enabled);
		
		String uriPathWithQueryParams = uriBuilder.toUriString();
		return loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
				.header("Username", HelperUtils.getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, t-> t.createError())
				.bodyToMono(String.class)
				.block();
	}
	
	public TherapistDTO getUserById(Long id) {
		TherapistDTO user = loadBalancedWebClientBuilder().filter(lbFunction).build()
				.get()
				.uri(THERAPIST_SERVICE_URL_GETBYID+id)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
				.header("Username", HelperUtils.getAuthenticationUsername())
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.onStatus(HttpStatusCode::isError, t -> t.createError())
				.bodyToMono(TherapistDTO.class)
				.block()
				;
		return user;
	}

	public String updateCategoriesToTherapistInfo(Long therapistInfoId, List<String> categoryNames) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(THERAPIST_SERVICE_URL_UPDATE_CATEGORIES+therapistInfoId)
				.queryParam("categoryNames", categoryNames);
		
		String uriPathWithQueryParams = uriBuilder.toUriString();
		return loadBalancedWebClientBuilder().filter(lbFunction).build()
				.put()
				.uri(uriPathWithQueryParams)
				.header(HttpHeaders.AUTHORIZATION,
						"Bearer "+jwtCommonService.generateToken(HelperUtils.getAuthenticationUsername()))
				.header("Username", HelperUtils.getAuthenticationUsername())
				.retrieve()
				.onStatus(HttpStatusCode::isError, t-> t.createError())
				.bodyToMono(String.class)
				.block();
	}


}

//private final WebClient WebClient;


//@Autowired
//private ReactorLoadBalancerExchangeFilterFunction lbFunction;

//@Bean
//public WebClient.Builder loadBalancedWebClientBuilder() {
//    return WebClient.builder();
//}


//private final String THERAPIST_SERVICE_URL_GETALL = "http://THERAPIST-SERVICE/therapists/api/v1/public/get/allTherapists";
//private final String THERAPIST_SERVICE_URL_GETALL = "http://localhost:8082/therapists/api/v1/public/get/allTherapists";



