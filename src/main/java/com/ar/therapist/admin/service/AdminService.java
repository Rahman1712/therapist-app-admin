package com.ar.therapist.admin.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ar.therapist.admin.dto.AdminDto;
import com.ar.therapist.admin.dto.AdminRequest;
import com.ar.therapist.admin.entity.Admin;
import com.ar.therapist.admin.exception.AdminException;
import com.ar.therapist.admin.repo.AdminRepository;
import com.ar.therapist.admin.utils.AdminUtils;
import com.ar.therapist.admin.utils.ImageUtils;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<AdminDto> findAll(){
		return adminRepository.findAll()
			.stream()
			.map(AdminUtils::adminToadminDto)
			.collect(Collectors.toList());
	}
	
	public AdminDto findById(Long id){
		return adminRepository.findById(id)
				.map(AdminUtils::adminToadminDto)
				.orElse(null);
	}
	
	public AdminDto findByUsername(String username) {
		return adminRepository.findByUsername(username)
				.map(AdminUtils::adminToadminDto)
				.orElse(null);
	}
	
	public Admin findUserByEmail(String email) {
		return adminRepository.findByEmail(email)
				.orElse(null);
	}
	
	public AdminDto findByEmail(String email) {
		return adminRepository.findByEmail(email)
				.map(AdminUtils::adminToadminDto)
				.orElse(null);
	}
	
	public void updateEnabledById(Long id, boolean enabled) {
		adminRepository.updateEnabledById(id, enabled);
	}

	public String updatePasswordById(Long id, String currentPassword, String newPassword) {
		Admin admin = adminRepository.findById(id).get();
		if(passwordEncoder.matches(currentPassword, admin.getPassword())){
			if(passwordEncoder.matches(newPassword, admin.getPassword())) {
				throw new AdminException("current password and new password is same change it.");
			}
			adminRepository.updatePassword(id,passwordEncoder.encode(newPassword));
		}else {
			throw new AdminException("current password doesn't match");
		}
		return "Admin detail updated successfully ... ";
	}
	
	
	public String updateAdminImageById(Long id,MultipartFile file) throws IOException {
		adminRepository.updateAdminImageById(id,
				ImageUtils.compress(file.getBytes()),
				file.getOriginalFilename(),
				file.getContentType()
				);
		return "Admin detail updated successfully ... "; 
	}
	
	public String updateAdminById(Long id, AdminRequest request) throws IOException {
		adminRepository.updateAdminById(id,
				request.getFullname(),
				request.getMobile(),
				request.getEmail()
				);
		return "Admin detail updated successfully ... "; 
	}
}
