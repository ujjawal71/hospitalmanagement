package com.hospital.controller;

import com.hospital.dto.DepartmentDTO;
import com.hospital.dto.DoctorDTO;
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

    public PublicController(DepartmentService departmentService, DoctorService doctorService) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/departments/list")
    public ResponseEntity<List<DepartmentDTO>> listDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/doctors/by-department")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(@RequestParam Long departmentId) {
        return ResponseEntity.ok(doctorService.findByDepartment(departmentId));
    }
}
