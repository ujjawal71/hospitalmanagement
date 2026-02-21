package com.hospital.service;

import com.hospital.dto.LoginRequest;
import com.hospital.dto.LoginResponse;
import com.hospital.entity.Admin;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.AdminRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TokenStore tokenStore;

    public AuthService(AdminRepository adminRepository, DoctorRepository doctorRepository,
                        PatientRepository patientRepository, TokenStore tokenStore) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.tokenStore = tokenStore;
    }

    public LoginResponse login(LoginRequest request) {
        String role = request.getRole().toUpperCase();
        switch (role) {
            case "ADMIN" -> {
                return adminRepository.findByEmail(request.getEmail())
                        .filter(a -> a.getPassword().equals(request.getPassword()))
                        .map(this::buildAdminResponse)
                        .orElse(LoginResponse.builder().success(false).message("Invalid credentials").build());
            }
            case "DOCTOR" -> {
                return doctorRepository.findByEmail(request.getEmail())
                        .filter(d -> d.getPassword().equals(request.getPassword()))
                        .map(this::buildDoctorResponse)
                        .orElse(LoginResponse.builder().success(false).message("Invalid credentials").build());
            }
            case "PATIENT" -> {
                return patientRepository.findByEmail(request.getEmail())
                        .filter(p -> p.getPassword().equals(request.getPassword()))
                        .map(this::buildPatientResponse)
                        .orElse(LoginResponse.builder().success(false).message("Invalid credentials").build());
            }
            default -> {
                return LoginResponse.builder().success(false).message("Invalid role").build();
            }
        }
    }

    private LoginResponse buildAdminResponse(Admin a) {
        String token = tokenStore.createToken("ADMIN", a.getId());
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .role("ADMIN")
                .userId(a.getId())
                .name(a.getName())
                .token(token)
                .build();
    }

    private LoginResponse buildDoctorResponse(Doctor d) {
        String token = tokenStore.createToken("DOCTOR", d.getDoctorId());
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .role("DOCTOR")
                .userId(d.getDoctorId())
                .name(d.getName())
                .token(token)
                .build();
    }

    private LoginResponse buildPatientResponse(Patient p) {
        String token = tokenStore.createToken("PATIENT", p.getPatientId());
        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .role("PATIENT")
                .userId(p.getPatientId())
                .name(p.getName())
                .token(token)
                .build();
    }

    public void logout(String token) {
        tokenStore.removeToken(token);
    }
}
