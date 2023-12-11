package com.ar.therapist.admin.vendor.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

	private String pid;
	
	private String bookingId;
	private Double amount;
	private String razorPaymentId;
	
	private PaymentMethod paymentMethod;
	
	private LocalDateTime paymentDate;
}