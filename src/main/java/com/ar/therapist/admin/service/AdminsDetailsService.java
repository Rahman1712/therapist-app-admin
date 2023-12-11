package com.ar.therapist.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ar.therapist.admin.repo.AdminRepository;

public class AdminsDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUsername(username).map(AdminsDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not Found"));
	}

}
