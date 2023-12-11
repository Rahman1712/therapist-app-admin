package com.ar.therapist.admin.vendor.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TherapistInfoDTO {

	private Long id;
	private String bio;
	@JsonProperty(value = "experience_years")
	private int experienceYears;
	@JsonProperty(value = "hourly_rate")
	private double hourlyRate;//private float hourlyRate;
    @JsonProperty(value = "is_certified") 
    private boolean isCertified;
    private String qualification;
    private Address address;
    private List<String> languages;
	private List<CategoryDTO> categories;
	private Long therapistId;
	
	private byte[] educationalCertificate; 
	private byte[] experienceCertificate;
	private byte[] additionalCertificate;
	private String experienceCertificateType;
	private String educationalCertificateType;
	private String additionalCertificateType;

}
