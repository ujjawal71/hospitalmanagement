package com.hospital.service;

import com.hospital.dto.TimeSlotDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.TimeSlot;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional(readOnly = true)
    public List<TimeSlotDTO> findByDoctor(Long doctorId) {
        return timeSlotRepository.findByDoctorDoctorId(doctorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TimeSlotDTO findById(Long id) {
        return timeSlotRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Time slot not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<TimeSlotDTO> findAvailableByDoctorAndDate(Long doctorId, LocalDate date) {
        return timeSlotRepository.findByDoctorDoctorIdAndSlotDateAndAvailable(doctorId, date, true).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TimeSlotDTO create(Long doctorId, LocalDate slotDate, LocalTime startTime, LocalTime endTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        TimeSlot slot = new TimeSlot();
        slot.setDoctor(doctor);
        slot.setSlotDate(slotDate);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setAvailable(true);
        return toDTO(timeSlotRepository.save(slot));
    }

    @Transactional
    public void delete(Long slotId) {
        timeSlotRepository.deleteById(slotId);
    }

    private TimeSlotDTO toDTO(TimeSlot s) {
        TimeSlotDTO dto = new TimeSlotDTO();
        dto.setId(s.getId());
        dto.setDoctorId(s.getDoctor().getDoctorId());
        dto.setSlotDate(s.getSlotDate());
        dto.setStartTime(s.getStartTime());
        dto.setEndTime(s.getEndTime());
        dto.setAvailable(s.getAvailable());
        return dto;
    }
}
