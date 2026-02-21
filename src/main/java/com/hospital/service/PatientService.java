package com.hospital.service;

import com.hospital.dto.PatientDTO;
import com.hospital.entity.Patient;
import com.hospital.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientDTO findById(Long id) {
        return patientRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
    }

    @Transactional
    public PatientDTO register(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new RuntimeException("Email already registered: " + patient.getEmail());
        }
        return toDTO(patientRepository.save(patient));
    }

    @Transactional
    public PatientDTO update(Long id, Patient updates) {
        Patient p = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        if (updates.getName() != null) p.setName(updates.getName());
        if (updates.getEmail() != null) {
            if (patientRepository.existsByEmail(updates.getEmail()) && !updates.getEmail().equals(p.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            p.setEmail(updates.getEmail());
        }
        if (updates.getPassword() != null && !updates.getPassword().isBlank()) p.setPassword(updates.getPassword());
        if (updates.getPhone() != null) p.setPhone(updates.getPhone());
        return toDTO(patientRepository.save(p));
    }

    @Transactional
    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found: " + id);
        }
        patientRepository.deleteById(id);
    }

    private PatientDTO toDTO(Patient p) {
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(p.getPatientId());
        dto.setName(p.getName());
        dto.setEmail(p.getEmail());
        dto.setPhone(p.getPhone());
        return dto;
    }
}
