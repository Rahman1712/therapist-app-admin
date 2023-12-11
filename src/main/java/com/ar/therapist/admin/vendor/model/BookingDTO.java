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
public class BookingDTO {

	private String id;
	private LocalDateTime appointmentDateTime;
	private LocalDateTime rescheduleDateTime;
	private LocalDateTime cancellationDateTime;
	private String date;
	private String notes;
	private Long minutes;
	private Double amount;
	private TimeSlot timeSlot;
	private UserData userData;
	private TherapistInfoUserDto therapistInfo; // therapistInfo ithanu name vendathu
	private BookingStatus bookingStatus;
	private PaymentStatus paymentStatus;
	private BookingType bookingType;
	private Payment payment;
}
