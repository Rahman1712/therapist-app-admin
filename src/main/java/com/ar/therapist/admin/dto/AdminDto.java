package com.ar.therapist.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDto {

	private Long id;
	private String username;
	private String fullname;
	private String email;
	private String mobile;
	private String role;
	private byte[] image;
	private String imageName;
	private String imageType;
	private boolean nonLocked;
	private boolean enabled;
}
