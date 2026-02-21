package com.hospital.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String role;
    private Long userId;
    private String name;
    private String token;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public static LoginResponse builder() {
        return new LoginResponse();
    }
    public LoginResponse success(boolean s) { this.success = s; return this; }
    public LoginResponse message(String m) { this.message = m; return this; }
    public LoginResponse role(String r) { this.role = r; return this; }
    public LoginResponse userId(Long id) { this.userId = id; return this; }
    public LoginResponse name(String n) { this.name = n; return this; }
    public LoginResponse token(String t) { this.token = t; return this; }
    public LoginResponse build() { return this; }
}
