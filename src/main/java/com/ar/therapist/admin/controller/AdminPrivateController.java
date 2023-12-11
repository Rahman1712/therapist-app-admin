package com.ar.therapist.admin.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.admin.dto.AdminDto;
import com.ar.therapist.admin.dto.AdminRequest;
import com.ar.therapist.admin.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/private")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole({'ADMIN','ADMINTRAINEE','EDITOR'})") //@PreAuthorize("hasRole('ROLE_ADMIN')") 
public class AdminPrivateController {

	private final AdminService adminService;
	
	@GetMapping("/get/byUsername/{username}") 
	@PreAuthorize("hasAuthority('self:read')")  
	public ResponseEntity<AdminDto> getDetailsByUserName(@PathVariable("username")String username){
		return ResponseEntity.ok(adminService.findByUsername(username));
	}
	
	@PutMapping("/update/password/byId/{id}")
	@PreAuthorize("hasAuthority('self:update')")
	public ResponseEntity<String> updatePasswordById(@PathVariable("id")Long id,
			@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword
			){
		return ResponseEntity.ok(adminService.updatePasswordById(id,currentPassword,newPassword));
	}
	
	@PutMapping("/update-image/byId/{id}")
	@PreAuthorize("hasAuthority('self:update')")
	public ResponseEntity<String> updateAdminImageById(@PathVariable("id")Long id,
			@RequestParam("file") MultipartFile file){
		try {
			return ResponseEntity.ok(adminService.updateAdminImageById(id, file));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Updation Failed");
		}
	}
	
	@PutMapping("/update-details/byId/{id}")
	@PreAuthorize("hasAuthority('self:update')")
	public ResponseEntity<String> updateById(@PathVariable("id")Long id,
			@RequestBody AdminRequest request){
		try {
			return ResponseEntity.ok(adminService.updateAdminById(id, request));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Updation Failed");
		}
	}
}
