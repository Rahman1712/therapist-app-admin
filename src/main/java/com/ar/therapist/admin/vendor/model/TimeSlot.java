package com.ar.therapist.admin.vendor.model;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
	
    private Long tid; // id

    private LocalTime time;

    private boolean isBooked;

	public TimeSlot(LocalTime time, boolean isBooked) {
		this.time = time;
		this.isBooked = isBooked;
	}
    
}