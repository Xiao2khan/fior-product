package com.fiordelisi.fiordelisiproduct.entity;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private String fullName;
    private String phone;
    private String email;
    private String city;
    private String district;
    private String ward;
    private String street;
}
