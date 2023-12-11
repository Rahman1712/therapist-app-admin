package com.ar.therapist.admin.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "admins_table")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String fullname;
	private String mobile;
	private String username;
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Lob 
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "admin_image",length=100000)
	private byte[] image;
	
	@Column(name = "admin_image_name")
	private String imageName;
	
	@Column(name = "admin_image_type")
	private String imageType;
	
	@Column(name = "non_locked")
	private boolean nonLocked;
	private boolean enabled;
	
}
