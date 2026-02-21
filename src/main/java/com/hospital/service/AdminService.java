package com.hospital.service;

import com.hospital.entity.Admin;
import com.hospital.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public Admin create(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Admin email already exists: " + admin.getEmail());
        }
        return adminRepository.save(admin);
    }

    @Transactional(readOnly = true)
    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }
}
