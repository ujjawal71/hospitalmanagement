package com.hospital.service;

import com.hospital.dto.DepartmentDTO;
import com.hospital.entity.Department;
import com.hospital.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentDTO> findAll() {
        return departmentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepartmentDTO findById(Long id) {
        return departmentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
    }

    @Transactional
    public DepartmentDTO create(DepartmentDTO dto) {
        if (departmentRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Department already exists: " + dto.getName());
        }
        Department d = new Department();
        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        return toDTO(departmentRepository.save(d));
    }

    @Transactional
    public DepartmentDTO update(Long id, DepartmentDTO dto) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        d.setName(dto.getName());
        d.setDescription(dto.getDescription());
        return toDTO(departmentRepository.save(d));
    }

    @Transactional
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentDTO toDTO(Department d) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(d.getId());
        dto.setName(d.getName());
        dto.setDescription(d.getDescription());
        return dto;
    }
}
