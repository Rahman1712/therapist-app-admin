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
public class PaymentDto {

    private String pid;
	private String bookingId;
	private Double amount;
	private String razorPaymentId;
	private RazorPayment razorPayment;
	private PaymentMethod paymentMethod;
	private LocalDateTime paymentDate;
}