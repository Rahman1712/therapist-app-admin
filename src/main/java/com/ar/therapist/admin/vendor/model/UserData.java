package com.ar.therapist.admin.vendor.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private Long userId;
    private String fullname;
    private String email;
    private String mobile;
    private String imageUrl;
}
