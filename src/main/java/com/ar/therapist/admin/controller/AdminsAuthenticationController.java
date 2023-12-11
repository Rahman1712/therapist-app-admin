package com.ar.therapist.admin.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.admin.dto.AuthenticationRequest;
import com.ar.therapist.admin.dto.AuthenticationResponse;
import com.ar.therapist.admin.service.AdminAuthenticateService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AdminsAuthenticationController {

	private final AdminAuthenticateService adminAuthenticateService;
	
	@GetMapping("/work")
	public String work() {
		return "Worked";
	}
	
	
	@GetMapping("/set")
	public String set(HttpServletResponse response) {
		Cookie c = new Cookie("apples", "kasmirappple_orange");
		c.setPath("/");
//		c.setSecure(true);
		c.setHttpOnly(true);
		response.addCookie(c);
		
		return "SETTTT";
	}
	@GetMapping("/get")
	public String get(@CookieValue(value = "apples", required = false) String a ,HttpServletRequest request) {
		System.err.println(request.getCookies());
		if(a!=null) return "Cookie val :"+a;
		else return "Not Cookie got";
	}

//	@PostMapping("/register")
//	public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid UserRegisterRequest request) {
//		return new ResponseEntity<>(userAuthenticateService.register(request), HttpStatus.CREATED); 
//	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody AuthenticationRequest authRequest, HttpServletResponse response) {
		return ResponseEntity.ok(adminAuthenticateService.authenticate(authRequest, response));
	}
	
	@PostMapping("/refresh-token")
	public void refresh(
		HttpServletRequest request,
		HttpServletResponse response
	) throws StreamWriteException, DatabindException, IOException {
		
		adminAuthenticateService.refreshToken(request, response);
	}
	
	@PostMapping("/verify-otp") // For OTP verify at signup
	public ResponseEntity<AuthenticationResponse> verifyOtp(@RequestParam String email, @RequestParam String otp, HttpServletResponse response) {
		return ResponseEntity.ok(adminAuthenticateService.verifyOtp(email, otp, response));
	}
	
	@PostMapping("/verify-forgot-otp") // For OTP verify at forgot time : return token
	public ResponseEntity<?> verifyForgotOtp(@RequestParam String email, @RequestParam String otp) {
		return ResponseEntity.ok(adminAuthenticateService.verifyForgotOtp(email, otp));
	}
	
	@PostMapping("/mail-reset-otp") // For OTP at forgot time , (also that function use in register)
	public ResponseEntity<String> verifyOtp(@RequestParam String email) throws UnsupportedEncodingException, MessagingException {
		return ResponseEntity.ok(adminAuthenticateService.sendMailForVerify(email));
	}
	
	@PutMapping("/resend-otp") // For OTP at signup, forgot time
	public ResponseEntity<String> resendOtp(@RequestParam String email) {
		return ResponseEntity.ok(adminAuthenticateService.refreshOtp(email)); 
	}
	
	@PutMapping("/update-password") // For Password update at forgot time 
	public ResponseEntity<String> resendOtp(@RequestParam String username, String newPassword, String token) {
		return ResponseEntity.ok(adminAuthenticateService.updateForgotPassword(username, newPassword, token)); 
	}

	
}