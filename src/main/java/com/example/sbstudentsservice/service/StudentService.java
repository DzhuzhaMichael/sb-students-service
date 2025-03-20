package com.example.sbstudentsservice.service;

import com.example.sbstudentsservice.dto.request.StudentRequestDto;
import com.example.sbstudentsservice.model.Student;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Student save(Student student);

    Student getStudentById(Long id);

    Student update(Long id, Student student);

    Map<String, Object> uploadStudents(List<StudentRequestDto> studentRequestDtoList);

    void delete(Long id);

    Page<Student> findAllByGroupIdWithPagination(Long groupId, int page, int size);
}
