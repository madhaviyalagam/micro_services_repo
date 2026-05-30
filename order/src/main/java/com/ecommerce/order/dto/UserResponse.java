package com.ecommerce.order.dto;

import lombok.Data;

@Data
public class UserResponse {
	private Long id;
	private String fristName;
	private String lastName;
	private String email;
	private Integer phone_number;
	private String role;
}
