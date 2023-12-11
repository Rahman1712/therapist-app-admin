package com.ar.therapist.admin.vendor.model;
public enum PaymentMethod {
    
	ONLINE("Online"),
    WALLET("Wallet Payment");
    
    private final String status;
    
    private PaymentMethod(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
}