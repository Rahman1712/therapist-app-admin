package com.ar.therapist.admin.vendor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.admin.vendor.model.CategoryDTO;
import com.ar.therapist.admin.vendor.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
@PreAuthorize("hasAnyRole({'ADMIN','ADMINTRAINEE','EDITOR'})") //@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CategoryServiceController {

	@Autowired private CategoryService categoryService;
	
	@PostMapping("/save")
	@PreAuthorize("hasAuthority('therapist:create')")
	public ResponseEntity<CategoryDTO> addCategory(
			@RequestPart("category") CategoryDTO category,
			@RequestParam("file") MultipartFile file
			) {
		return ResponseEntity.ok(categoryService.addCategory(category, file));
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('therapist:update')")
	public ResponseEntity<CategoryDTO> updateCategory(
			@PathVariable("id") Long id,
			@RequestPart("category") CategoryDTO category,
			@RequestParam("file") MultipartFile file){
		return ResponseEntity.ok(categoryService.updateCategory(id, category, file));
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('therapist:delete')")
	public ResponseEntity<String> deleteCategory(
			@PathVariable("id") Long id){
		return ResponseEntity.ok(categoryService.deleteCategory(id));
	}
}
