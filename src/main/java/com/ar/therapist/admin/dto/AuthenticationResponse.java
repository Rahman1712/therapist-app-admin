package com.ar.therapist.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	@JsonProperty("user_id")
	private Long id;
	private String username;
	private String email;
	private String role;
	
	//private String token;
	@JsonProperty("access_token")
	private String accessToken;
	
//	@JsonProperty("refresh_token")
//	private String refreshToken; 
}