package com.example.sbstudentsservice.service;

import com.example.sbstudentsservice.dto.request.StudentRequestDto;
import com.example.sbstudentsservice.model.Student;
import com.example.sbstudentsservice.repository.StudentRepository;
import com.example.sbstudentsservice.service.mapper.StudentMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final GroupService groupService;

    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, GroupService groupService, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.studentMapper = studentMapper;
    }

    @Override
    public Student save(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new EntityExistsException("Student with email '" + student.getEmail() + "' already exists");
        }
        return studentRepository.save(student);
    }


    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Student with id: " + id + " not found"));
    }

    @Override
    public Student update(Long id, Student student) {
        Student studentFromDb = getStudentById(id);
        if (studentRepository.existsByEmail(student.getEmail()) && !studentFromDb.getEmail().equals(student.getEmail())) {
            throw new EntityExistsException("Student with email '" + student.getEmail() + "' already exists");
        }
        studentFromDb.setName(student.getName());
        studentFromDb.setSurname(student.getSurname());
        studentFromDb.setEmail(student.getEmail());
        studentFromDb.setPhone(student.getPhone());
        studentFromDb.setBirthDate(student.getBirthDate());
        studentFromDb.setGroup(student.getGroup());
        return studentRepository.save(studentFromDb);
    }

    @Override
    public Map<String, Object> uploadStudents(List<StudentRequestDto> studentRequestDtoList) {
        int uploadedCount = 0;
        List<String> errors = new ArrayList<>();
        for (StudentRequestDto dto : studentRequestDtoList) {
            try {
                groupService.findById(dto.getGroupId());
                save(studentMapper.mapToModel(dto));
                uploadedCount++;
            } catch (EntityNotFoundException | EntityExistsException e) {
                errors.add("Error for student " + dto.getEmail() + ": " + e.getMessage());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("uploaded", uploadedCount);
        if (!errors.isEmpty()) {
            result.put("errors", errors);
        }
        return result;
    }

    @Override
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student with id " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public Page<Student> findAllByGroupIdWithPagination(Long groupId, int page, int size) {
        if (groupId == null) {
            throw new IllegalArgumentException("Group ID must not be null");
        }
        groupService.findById(groupId);
        if (page <= 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return studentRepository.findByGroupId(groupId, pageable);
    }
}
