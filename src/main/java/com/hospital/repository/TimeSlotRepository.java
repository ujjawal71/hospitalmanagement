package com.hospital.repository;

import com.hospital.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByDoctorDoctorId(Long doctorId);
    List<TimeSlot> findByDoctorDoctorIdAndSlotDateAndAvailable(Long doctorId, LocalDate date, Boolean available);
}
