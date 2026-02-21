package com.hospital.controller;

import com.hospital.dto.AppointmentDTO;
import com.hospital.dto.TimeSlotDTO;
import com.hospital.entity.Appointment.AppointmentStatus;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import com.hospital.service.TimeSlotService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final TimeSlotService timeSlotService;

    public DoctorController(AppointmentService appointmentService, PatientService patientService,
                            TimeSlotService timeSlotService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.timeSlotService = timeSlotService;
    }

    private Long getDoctorId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(HttpServletRequest request) {
        Long doctorId = getDoctorId(request);
        return ResponseEntity.ok(appointmentService.findByDoctor(doctorId));
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpServletRequest request) {
        Long doctorId = getDoctorId(request);
        AppointmentDTO app = appointmentService.findById(id);
        if (!app.getDoctorId().equals(doctorId)) {
            return ResponseEntity.status(403).build();
        }
        AppointmentStatus s = AppointmentStatus.valueOf(status.toUpperCase());
        if (s != AppointmentStatus.APPROVED && s != AppointmentStatus.COMPLETED && s != AppointmentStatus.CANCELLED) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(appointmentService.updateStatus(id, s));
    }

    @GetMapping("/appointments/{id}/patient")
    public ResponseEntity<?> getPatientDetails(@PathVariable Long id, HttpServletRequest request) {
        Long doctorId = getDoctorId(request);
        AppointmentDTO app = appointmentService.findById(id);
        if (!app.getDoctorId().equals(doctorId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(patientService.findById(app.getPatientId()));
    }

    @GetMapping("/time-slots")
    public ResponseEntity<List<TimeSlotDTO>> getMyTimeSlots(HttpServletRequest request) {
        Long doctorId = getDoctorId(request);
        return ResponseEntity.ok(timeSlotService.findByDoctor(doctorId));
    }

    @PostMapping("/time-slots")
    public ResponseEntity<TimeSlotDTO> addTimeSlot(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        Long doctorId = getDoctorId(request);
        return ResponseEntity.ok(timeSlotService.create(doctorId, slotDate, startTime, endTime));
    }

    @DeleteMapping("/time-slots/{id}")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long id, HttpServletRequest request) {
        Long doctorId = getDoctorId(request);
        TimeSlotDTO slot = timeSlotService.findById(id);
        if (!slot.getDoctorId().equals(doctorId)) {
            return ResponseEntity.status(403).build();
        }
        timeSlotService.delete(id);
        return ResponseEntity.ok().build();
    }
}
