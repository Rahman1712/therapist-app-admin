package com.ar.therapist.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.admin.dto.AdminDto;
import com.ar.therapist.admin.service.AdminService;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {
	
	@Autowired private AdminService adminService;
	
	@GetMapping
	public String worked() {
		System.err.println("DEMOOOOOOOO");
		return "Hey its a private data 📁 🔐"; 
	}
	
	@GetMapping("/users")
    public ResponseEntity<List<AdminDto>> allAdmins(){
    	return ResponseEntity.ok(adminService.findAll());
    }
}
