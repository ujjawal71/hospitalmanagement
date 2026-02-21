package com.hospital.service;

import com.hospital.dto.AppointmentDTO;
import com.hospital.entity.Appointment;
import com.hospital.entity.Appointment.AppointmentStatus;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findAll() {
        return appointmentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByPatient(Long patientId) {
        return appointmentRepository.findByPatientPatientId(patientId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> findByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorDoctorId(doctorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AppointmentDTO findById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + id));
    }

    @Transactional
    public AppointmentDTO book(Long patientId, Long doctorId, LocalDate date, LocalTime time) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        Appointment a = new Appointment();
        a.setPatient(patient);
        a.setDoctor(doctor);
        a.setDate(date);
        a.setTime(time);
        a.setStatus(AppointmentStatus.PENDING);
        return toDTO(appointmentRepository.save(a));
    }

    @Transactional
    public AppointmentDTO updateStatus(Long appointmentId, AppointmentStatus status) {
        Appointment a = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
        a.setStatus(status);
        return toDTO(appointmentRepository.save(a));
    }

    @Transactional
    public void cancel(Long appointmentId, Long patientId) {
        Appointment a = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
        if (!a.getPatient().getPatientId().equals(patientId)) {
            throw new RuntimeException("Not authorized to cancel this appointment");
        }
        a.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(a);
    }

    private AppointmentDTO toDTO(Appointment a) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAppointmentId(a.getAppointmentId());
        dto.setPatientId(a.getPatient().getPatientId());
        dto.setDoctorId(a.getDoctor().getDoctorId());
        dto.setPatientName(a.getPatient().getName());
        dto.setDoctorName(a.getDoctor().getName());
        dto.setDepartmentName(a.getDoctor().getDepartment().getName());
        dto.setDate(a.getDate());
        dto.setTime(a.getTime());
        dto.setStatus(a.getStatus());
        return dto;
    }
}
