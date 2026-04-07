package com.hospital.controller;

import com.hospital.dto.DepartmentDTO;
import com.hospital.dto.DoctorDTO;
import com.hospital.dto.PublicStatsDTO;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.DepartmentService;
import com.hospital.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public API for listing departments and doctors by department (e.g. for login/landing page).
 */
@RestController
@RequestMapping("/api")
public class PublicController {

    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public PublicController(DepartmentService departmentService, DoctorService doctorService,
                            DoctorRepository doctorRepository, DepartmentRepository departmentRepository,
                            AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/departments/list")
    public ResponseEntity<List<DepartmentDTO>> listDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/doctors/by-department")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(@RequestParam Long departmentId) {
        return ResponseEntity.ok(doctorService.findByDepartment(departmentId));
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<PublicStatsDTO> getPublicStatsSummary() {
        return ResponseEntity.ok(new PublicStatsDTO(
                doctorRepository.count(),
                departmentRepository.count(),
                appointmentRepository.count(),
                patientRepository.count()
        ));
    }
}
