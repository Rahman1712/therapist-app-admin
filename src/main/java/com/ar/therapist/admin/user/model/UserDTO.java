package com.ar.therapist.admin.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	private Long id;
	private String fullname;
	private String email;
	private String mobile;
	private String username;
	private String role;
	private byte[] image;
	private String imageName;
	private String imageType;
	private String imageUrl;
	private boolean enabled;
	private boolean nonLocked;
	
	
}
