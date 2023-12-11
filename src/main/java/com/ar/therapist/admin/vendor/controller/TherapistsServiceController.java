package com.ar.therapist.admin.vendor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.therapist.admin.vendor.model.TherapistDTO;
import com.ar.therapist.admin.vendor.service.TherapistService;

@RestController
@RequestMapping("/api/v1/therapists")
@PreAuthorize("hasAnyRole({'ADMIN','ADMINTRAINEE','EDITOR'})") //@PreAuthorize("hasRole('ROLE_ADMIN')")
public class TherapistsServiceController {

	@Autowired
	private TherapistService therapistService;
	
	@GetMapping("/getall")
	@PreAuthorize("hasAuthority('therapist:read')")
	public ResponseEntity<List<TherapistDTO>> getAllUsers(){
		return ResponseEntity.ok(therapistService.getAllUsers());
	}
	
	@GetMapping("/getbyid/{id}")
	@PreAuthorize("hasAuthority('therapist:read')")
	public ResponseEntity<TherapistDTO> getUserById(@PathVariable("id") Long id){
		return ResponseEntity.ok(therapistService.getUserById(id));
	}
	
	@PutMapping("/activate/byid/{therapistId}")
	@PreAuthorize("hasAuthority('therapist:update')")
	public ResponseEntity<String> activateOrDeactivateById(@PathVariable("therapistId")Long therapistId,
			@RequestParam("activate")boolean activate) {
		return ResponseEntity.ok(therapistService.activateOrDeactivateById(therapistId, activate));
	}
	
	@PutMapping("/enabled/byid/{therapistId}")
	@PreAuthorize("hasAuthority('therapist:update')")
	public ResponseEntity<String> enableDisableById(@PathVariable("therapistId")Long therapistId,
			@RequestParam("enabled")boolean enabled) {
		return ResponseEntity.ok(therapistService.enableDisableById(therapistId, enabled));
	}
	
	@PutMapping("/update-categories/byid/{therapistInfoId}")
	public ResponseEntity<String> updateCategoriesToTherapistInfo(@PathVariable("therapistInfoId") Long therapistInfoId, 
			@RequestParam("categoryNames") List<String> categoryNames){
		therapistService.updateCategoriesToTherapistInfo(therapistInfoId, categoryNames);
		return ResponseEntity.ok("Activated Successfully");
	}
}
