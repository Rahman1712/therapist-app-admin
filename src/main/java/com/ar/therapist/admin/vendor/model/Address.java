package com.ar.therapist.admin.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
    private String building;
    private String street;
    private String district;
    private String state;
    private String zipcode;
}
