package com.ar.therapist.admin.vendor.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TherapistDTO {

	private Long id;
	private String username;
	private String fullname;
	private String email;
	private String mobile;
	private String role;
	
	private String imageUrl;
//	private byte[] image;
//	private String imageName;
//	private String imageType;
	
	private boolean nonLocked;
	private boolean enabled;
	private boolean activated;
	private boolean submited;
	
	private LocalDateTime created;
	
	private TherapistInfoDTO therapistInfoDto;
}
