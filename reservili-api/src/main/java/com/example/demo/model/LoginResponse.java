package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginResponse {
	 private int clientId;
	 private String firstName;
	    private String lastName;
	    private String email;
	    private String phone;
}
