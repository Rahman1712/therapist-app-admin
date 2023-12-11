package com.ar.therapist.admin.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ar.therapist.admin.config.JwtService;
import com.ar.therapist.admin.controller.AdminRegisterException;
import com.ar.therapist.admin.dto.AuthenticationRequest;
import com.ar.therapist.admin.dto.AuthenticationResponse;
import com.ar.therapist.admin.dto.AdminRequest;
import com.ar.therapist.admin.entity.Role;
import com.ar.therapist.admin.entity.Token;
import com.ar.therapist.admin.entity.TokenType;
import com.ar.therapist.admin.entity.Admin;
import com.ar.therapist.admin.exception.ErrorResponse;
import com.ar.therapist.admin.exception.AdminException;
import com.ar.therapist.admin.repo.TokenRepository;
import com.ar.therapist.admin.utils.CookieUtils;
import com.ar.therapist.admin.repo.AdminRepository;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AdminAuthenticateService {

	@Autowired private AdminRepository adminRepository;
	@Autowired private JwtService jwtService;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private MailService mailService;
	@Autowired private OTPService otpService;
	@Autowired private TokenRepository tokenRepo;
	
	// register
	public AuthenticationResponse register(AdminRequest request) {
			var user = Admin.builder()
					.fullname(request.getFullname())
					.mobile(request.getMobile())
					.email(request.getEmail())
					.username(request.getUsername())
					.password(passwordEncoder.encode(request.getPassword()))
					.role(Role.ADMIN)
					.nonLocked(true) // at register not lock true
					.enabled(true) // true : enable           // false: not enable at start
					.build();
			
			adminRepository.findByUsername(request.getUsername()).ifPresent(
					u-> {
						throw new AdminRegisterException(new ErrorResponse("username already exists.", "username", request.getUsername()));
					});
			adminRepository.findByEmail(request.getEmail()).ifPresent(
					u-> {
						throw new AdminRegisterException(new ErrorResponse("email already exists.", "email", request.getEmail()));
					});
			
			Admin adminSaved = adminRepository.save(user);
			
			try {
				sendMailForVerify(adminSaved);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
				throw new AdminException("Error in new Registration ...");
			}
			
			//var jwtToken = jwtService.generateToken(new AdminsDetails(adminSaved));
			//var refreshToken = jwtService.generateRefreshToken(new AdminsDetails(adminSaved));
			//saveUserToken(adminSaved, jwtToken);
			
			return AuthenticationResponse.builder()
					.accessToken("jwtToken")
					//.refreshToken("refreshToken")
					.build();
	}

	// login
	public AuthenticationResponse authenticate(
			AuthenticationRequest request, 
			HttpServletResponse response) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						 request.getUsername(),
						 request.getPassword()
						 )
				);
		
		var admin = adminRepository.findByUsername(request.getUsername())
				.orElseThrow();
		var jwtToken = jwtService.generateToken(new AdminsDetails(admin));
		var refreshToken = jwtService.generateRefreshToken(new AdminsDetails(admin));
		
		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);
		CookieUtils.addCookie(response, "aaple", "apple", 7 * 24 * 3600);
		
		revokeAllUserTokens(admin);
		saveUserToken(admin, jwtToken);
		
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				//.refreshToken(refreshToken)
				.id(admin.getId())
				.username(admin.getUsername())
				.email(admin.getEmail())
				.role(admin.getRole().name())
				.build();
	}
	
	private void saveUserToken(Admin admin, String jwtToken) {
		var token = Token.builder()
				.admin(admin)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepo.save(token);
	}
	
	private void revokeAllUserTokens(Admin admin) {
		var validAdminTokens = tokenRepo.findAllValidTokensByAdmin(admin.getId());
		
		if(validAdminTokens.isEmpty()) return;
		
		validAdminTokens.forEach(t ->{
			t.setExpired(true);
			t.setRevoked(true);
		});
		
		tokenRepo.saveAll(validAdminTokens);
	}
	// refresh tokens
	public void refreshToken(
			HttpServletRequest request, 
			HttpServletResponse response) 
		throws StreamWriteException, DatabindException, IOException {
		
		final String refreshToken;
		final String userName;

		System.err.println("Refresh =========== CALL");
		Optional<Cookie> cookie = CookieUtils.getCookie(request, "refresh_token");
		System.err.println(cookie);
		System.err.println("IS COOKIE NULL "+ cookie);
		if(cookie.isEmpty()) {
			//return;
			throw new AdminException("Your Session has been Expired Please Login");
		}
		refreshToken = cookie.get().getValue();
		try {
			userName = jwtService.extractUsername(refreshToken);
			
			if(userName != null) {
				var user = this.adminRepository.findByUsername(userName)
						.orElseThrow();
				
				if(jwtService.isTokenValid(refreshToken, new AdminsDetails(user))) {
					var accessToken = jwtService.generateToken(new AdminsDetails(user));
					
					revokeAllUserTokens(user);  /// 
					saveUserToken(user, accessToken);  /// 
					
					var authResponse = AuthenticationResponse.builder()
							.accessToken(accessToken)
							//.refreshToken(refreshToken)
							.build();
					new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
				}
			}
		}catch(ExpiredJwtException ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			new ObjectMapper().writeValue(response.getOutputStream(), "Session has been Expired");
		}catch(Exception ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			httpResponse.getWriter().write("An Error occured");
		}
	}

	// send mail function user
	public String sendMailForVerify(Admin admin) throws UnsupportedEncodingException, MessagingException {
    	String otp = otpService.generateOTP(admin);
    	System.out.println("OTP : "+otp);
    	return mailService.sendOTPMail(admin, otp);
//    	return "OTP sended to ✉️ : "+admin.getEmail();
	}
	
	// send mail function email -> send mail abow abow function
	public String sendMailForVerify(String adminEmail) throws UnsupportedEncodingException, MessagingException {
    	Admin admin = adminRepository.findByEmail(adminEmail)
        		.orElseThrow(() -> new AdminException("Not Valid Email Id"));
    	return sendMailForVerify(admin);	
	}
	
	// verify otp  -> this on signup
	public AuthenticationResponse verifyOtp(String email, String otp, HttpServletResponse response) {
		var admin = this.adminRepository.findByEmail(email)
				.orElseThrow(() -> new AdminException("Not Valid Username"));
		
		if(otpService.verifyOTP(admin, otp)) {
			adminRepository.updateEnabledById(admin.getId(), true); //TRUE  : also update to enabled to true  
		}
		else {
			throw new AdminException("Invalid otp : OTP is incorrect ❌");
		}
		
		System.out.println("OTP verified successfully ✅.");//"OTP verified successfully ✅. Please login to continue."
		
		var jwtToken = jwtService.generateToken(new AdminsDetails(admin));
		var refreshToken = jwtService.generateRefreshToken(new AdminsDetails(admin));

		CookieUtils.addCookie(response, "refresh_token", refreshToken, 7 * 24 * 3600);
		
		revokeAllUserTokens(admin);
		saveUserToken(admin, jwtToken);
		
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.id(admin.getId())
				.username(admin.getUsername())
				.email(admin.getEmail())
				.role(admin.getRole().name())
				.build();
	}
	
	// verify forgot otp and : return token
	public String verifyForgotOtp(String email, String otp) {
		var admin = this.adminRepository.findByEmail(email)
				.orElseThrow(() -> new AdminException("Not Valid Username"));
		
		if(otpService.verifyOTP(admin, otp)) {
			adminRepository.updateEnabledById(admin.getId(), true); //TRUE  : also update to enabled to true  
		}
		else {
			throw new AdminException("Invalid otp : OTP is incorrect ❌");
		}
		
		var jwtToken = jwtService.generateToken(new AdminsDetails(admin));
		
		return jwtToken; //"OTP verified successfully ✅."
	}
	
	// refresh otp
	public String refreshOtp(String email) {
		Admin admin = this.adminRepository.findByEmail(email)
				.orElseThrow(() -> new AdminException("Not a Valid User")); 
		
		try {
			sendMailForVerify(admin);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
			throw new AdminException("Error in Otp Resend");
		}
		
		return "OTP Resended to ✉️ : "+admin.getEmail();
	}
	
	// update forgot password
	public String updateForgotPassword(String username, String newPassword, String token) {
		Admin admin = adminRepository.findByUsername(username)
        		.orElseThrow(() -> new AdminException("Not Valid Username ❌"));  
		
		if(!jwtService.isTokenValid(token, new AdminsDetails(admin))) {
			throw new AdminException("Not Valid Token ❌");
		}
		
		adminRepository.updatePassword(admin.getId(), passwordEncoder.encode(newPassword));
		return "Password updated successfully ✅"; 
	}


	
	
}

/*
public void refreshToken(
			HttpServletRequest request, 
			HttpServletResponse response) 
		throws StreamWriteException, DatabindException, IOException {
		
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userName;
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.split(" ")[1].trim(); //refreshToken = authHeader.substring(7);
		userName = jwtService.extractUsername(refreshToken);

		
		if(userName != null) {
			var user = this.adminRepository.findByUsername(userName)
					.orElseThrow();
			
			if(jwtService.isTokenValid(refreshToken, new AdminsDetails(user))) {
				var accessToken = jwtService.generateToken(new AdminsDetails(user));
				
				revokeAllUserTokens(user);  /// 
				saveUserToken(user, accessToken);  /// 
				
				var authResponse = AuthenticationResponse.builder()
						.accessToken(accessToken)
						.refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
 * 
 */
