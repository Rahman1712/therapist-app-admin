package com.ar.therapist.admin.utils;

import com.ar.therapist.admin.dto.AdminDto;
import com.ar.therapist.admin.entity.Admin;

public class AdminUtils {

	public static AdminDto adminToadminDto(Admin admin) {
		return AdminDto.builder()
				.id(admin.getId())
				.username(admin.getUsername())
				.fullname(admin.getFullname())
				.email(admin.getEmail())
				.mobile(admin.getMobile())
				.role(admin.getRole().name())
				.image(
						admin.getImage() == null ? 
							admin.getImage() : 
							ImageUtils.decompress(admin.getImage())
				)
				.imageName(admin.getImageName())
				.imageType(admin.getImageType())
				.nonLocked(admin.isNonLocked())
				.enabled(admin.isEnabled())
				.build();
	}
}