package com.hospital.controller;

import com.hospital.dto.*;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public AdminController(DepartmentService departmentService, DoctorService doctorService,
                           PatientService patientService, AppointmentService appointmentService) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    private Long getAdminId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    // --- Departments ---
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @PostMapping("/departments")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO dto) {
        return ResponseEntity.ok(departmentService.create(dto));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
        return ResponseEntity.ok(departmentService.update(id, dto));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok().build();
    }

    // --- Doctors ---
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @PostMapping("/doctors")
    public ResponseEntity<DoctorDTO> addDoctor(@RequestBody com.hospital.dto.DoctorCreateRequest req) {
        Doctor d = new Doctor();
        d.setName(req.getName());
        d.setEmail(req.getEmail());
        d.setPassword(req.getPassword());
        d.setSpecialization(req.getSpecialization());
        d.setAvailability(req.getAvailability());
        return ResponseEntity.ok(doctorService.create(d, req.getDepartmentId()));
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody com.hospital.dto.DoctorUpdateRequest req) {
        Doctor d = new Doctor();
        d.setName(req.getName());
        d.setEmail(req.getEmail());
        d.setPassword(req.getPassword());
        d.setSpecialization(req.getSpecialization());
        d.setAvailability(req.getAvailability());
        return ResponseEntity.ok(doctorService.update(id, d, req.getDepartmentId()));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok().build();
    }

    // --- Patients (manage users) ---
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody Patient updates) {
        return ResponseEntity.ok(patientService.update(id, updates));
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.ok().build();
    }

    // --- Appointments ---
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

}
