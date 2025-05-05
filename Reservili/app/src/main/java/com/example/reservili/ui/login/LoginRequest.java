package com.example.reservili.ui.login;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // getters nécessaires à Gson
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
