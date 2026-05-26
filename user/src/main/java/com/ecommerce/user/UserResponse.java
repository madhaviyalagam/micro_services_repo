package com.ecommerce.user;


import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fristName;
    private String lastName;
    private String email;
    private Integer phone_number;
    private UserRole role = UserRole.CUSTOMER;
    private AddressDto addressDto;
}
