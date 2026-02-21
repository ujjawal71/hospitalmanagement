package com.hospital.repository;

import com.hospital.entity.Appointment;
import com.hospital.entity.Appointment.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientPatientId(Long patientId);
    List<Appointment> findByDoctorDoctorId(Long doctorId);
    List<Appointment> findByDoctorDoctorIdAndDate(Long doctorId, LocalDate date);
    List<Appointment> findByStatus(AppointmentStatus status);
}
