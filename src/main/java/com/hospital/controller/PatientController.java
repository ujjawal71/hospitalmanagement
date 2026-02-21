package com.hospital.controller;

import com.hospital.dto.AppointmentDTO;
import com.hospital.dto.DepartmentDTO;
import com.hospital.dto.DoctorDTO;
import com.hospital.dto.PatientDTO;
import com.hospital.entity.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.DepartmentService;
import com.hospital.service.DoctorService;
import com.hospital.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PatientService patientService;

    public PatientController(DepartmentService departmentService, DoctorService doctorService,
                             AppointmentService appointmentService, PatientService patientService) {
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
    }

    private Long getPatientId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/doctors/by-department/{departmentId}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(doctorService.findByDepartment(departmentId));
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentDTO> bookAppointment(
            HttpServletRequest request,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {
        Long patientId = getPatientId(request);
        return ResponseEntity.ok(appointmentService.book(patientId, doctorId, date, time));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(HttpServletRequest request) {
        Long patientId = getPatientId(request);
        return ResponseEntity.ok(appointmentService.findByPatient(patientId));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, HttpServletRequest request) {
        Long patientId = getPatientId(request);
        appointmentService.cancel(id, patientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<PatientDTO> getProfile(HttpServletRequest request) {
        Long patientId = getPatientId(request);
        return ResponseEntity.ok(patientService.findById(patientId));
    }

    @PutMapping("/profile")
    public ResponseEntity<PatientDTO> updateProfile(HttpServletRequest request, @RequestBody Patient updates) {
        Long patientId = getPatientId(request);
        return ResponseEntity.ok(patientService.update(patientId, updates));
    }
}
