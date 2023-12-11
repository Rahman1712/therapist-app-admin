package com.ar.therapist.admin.cloudinary;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

	
	@Bean
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Cloudinary getCloudinary() {
		Map config = new HashMap<>();
		config.put("cloud_name", "dsrwvrbp9");
		config.put("api_key", "392342135431296");
		config.put("api_secret", "JktGIhWoiqDi_OFyRmgHu-7Cb9M");
		config.put("secure", true);
		
		return new Cloudinary(config);
		
	}
}


/*
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", "dsrwvrbp9",
				  "api_key", "392342135431296",
				  "api_secret", "JktGIhWoiqDi_OFyRmgHu-7Cb9M",
				  "secure", true
				));
*/