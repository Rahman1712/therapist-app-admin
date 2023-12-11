package com.ar.therapist.admin.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayment {

	private String razorpay_payment_id;
	private String razorpay_order_id;
	private String razorpay_signature;
	private String bookingId;
	private Double amount;
}