package com.ar.therapist.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ar.therapist.admin.dto.AdminRequest;
import com.ar.therapist.admin.service.AdminAuthenticateService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class AdminBackendApplication {
	
	@Autowired private AdminAuthenticateService adminAuthenticateService;

	public static void main(String[] args) {
		SpringApplication.run(AdminBackendApplication.class, args); 
	}

//	@PostConstruct
//	public void register() {
//		AdminRequest request = new AdminRequest(
//				"Abdul Rahman", "donrahman6@gmail.com", "9605685717", "rahman", "rahman");
//		adminAuthenticateService.register(request);
//	}
}
