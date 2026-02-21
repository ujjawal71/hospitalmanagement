package com.hospital.service;

import com.hospital.dto.DoctorDTO;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public DoctorService(DoctorRepository doctorRepository, DepartmentRepository departmentRepository) {
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DoctorDTO> findAll() {
        return doctorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorDTO> findByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorDTO findById(Long id) {
        return doctorRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
    }

    @Transactional
    public DoctorDTO create(Doctor doctor, Long departmentId) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new RuntimeException("Email already registered: " + doctor.getEmail());
        }
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));
        doctor.setDepartment(dept);
        return toDTO(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorDTO update(Long id, Doctor updates, Long departmentId) {
        Doctor d = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
        if (updates.getName() != null) d.setName(updates.getName());
        if (updates.getEmail() != null) {
            if (doctorRepository.existsByEmail(updates.getEmail()) && !updates.getEmail().equals(d.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            d.setEmail(updates.getEmail());
        }
        if (updates.getPassword() != null && !updates.getPassword().isBlank()) d.setPassword(updates.getPassword());
        if (updates.getSpecialization() != null) d.setSpecialization(updates.getSpecialization());
        if (updates.getAvailability() != null) d.setAvailability(updates.getAvailability());
        if (departmentId != null) {
            Department dept = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found: " + departmentId));
            d.setDepartment(dept);
        }
        return toDTO(doctorRepository.save(d));
    }

    @Transactional
    public void delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found: " + id);
        }
        doctorRepository.deleteById(id);
    }

    private DoctorDTO toDTO(Doctor d) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDoctorId(d.getDoctorId());
        dto.setName(d.getName());
        dto.setEmail(d.getEmail());
        dto.setDepartmentId(d.getDepartment().getId());
        dto.setDepartmentName(d.getDepartment().getName());
        dto.setSpecialization(d.getSpecialization());
        dto.setAvailability(d.getAvailability());
        return dto;
    }
}
