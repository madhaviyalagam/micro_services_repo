package com.ecommerce.user;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String city;
    private String country;
    private String state;
    private String street;
    private String zipcode;
}
