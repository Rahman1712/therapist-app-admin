package com.ar.therapist.admin.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.admin.user.model.UserDTO;
import com.ar.therapist.admin.user.service.UserService;


@RestController
@RequestMapping("/api/v1/users")
//@PreAuthorize("hasAnyRole({'ADMIN','ADMINTRAINEE','EDITOR'})") //@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserServiceController {

	@Autowired
	private UserService userService;
	
    @GetMapping("/demo")   
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok(userService.demoGet());
    } 

	
	@GetMapping("/getall")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@GetMapping("/getbyid/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id){
		return ResponseEntity.ok(userService.getUserById(id));
	}
	
	@PutMapping("/update/nonlocked/byid/{userId}")
	@PreAuthorize("hasAuthority('user:update')")
	public ResponseEntity<String> updateNonLockedByUserId(@PathVariable("userId")Long userId,
			@RequestParam("nonlocked")boolean nonlocked) {
		return ResponseEntity.ok(userService.updateNonLockedByUserId(userId, nonlocked));
	}
}
