package com.hospital.config;

import com.hospital.entity.Admin;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.repository.AdminRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;

    public DataInitializer(DepartmentRepository departmentRepository, AdminRepository adminRepository,
                           DoctorRepository doctorRepository) {
        this.departmentRepository = departmentRepository;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            List<String> depts = List.of("Cardiology", "Dental", "General Medicine", "Neurology", "Orthopedics", "Pediatrics");
            for (String name : depts) {
                Department d = new Department();
                d.setName(name);
                d.setDescription(name + " Department");
                departmentRepository.save(d);
            }
        }
        if (adminRepository.findByEmail("admin@hospital.com").isEmpty()) {
            Admin admin = new Admin();
            admin.setName("System Admin");
            admin.setEmail("admin@hospital.com");
            admin.setPassword("admin123");
            adminRepository.save(admin);
        }
        if (doctorRepository.findByEmail("doctor@hospital.com").isEmpty() && departmentRepository.count() > 0) {
            Doctor doctor = new Doctor();
            doctor.setName("Dr. John Smith");
            doctor.setEmail("doctor@hospital.com");
            doctor.setPassword("doctor123");
            doctor.setDepartment(departmentRepository.findAll().get(0));
            doctor.setSpecialization("General Physician");
            doctor.setAvailability("Mon-Fri 9:00-17:00");
            doctorRepository.save(doctor);
        }
    }
}
