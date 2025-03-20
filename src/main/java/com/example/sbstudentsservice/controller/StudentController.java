package com.example.sbstudentsservice.controller;

import com.example.sbstudentsservice.dto.request.StudentListRequestDto;
import com.example.sbstudentsservice.dto.request.StudentRequestDto;
import com.example.sbstudentsservice.dto.response.StudentListResponseDto;
import com.example.sbstudentsservice.dto.response.StudentResponseDto;
import com.example.sbstudentsservice.model.Student;
import com.example.sbstudentsservice.service.GroupService;
import com.example.sbstudentsservice.service.StudentService;
import com.example.sbstudentsservice.service.mapper.StudentMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, GroupService groupService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.studentMapper = studentMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(studentMapper.mapToDto(student));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody StudentRequestDto studentRequestDto) {
        groupService.findById(studentRequestDto.getGroupId());
        Student student = studentService.save(studentMapper.mapToModel(studentRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(studentMapper.mapToDto(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id, @RequestBody StudentRequestDto studentRequestDto) {
        Student updatedStudent = studentService.update(id, studentMapper.mapToModel(studentRequestDto));
        return ResponseEntity.ok(studentMapper.mapToDto(updatedStudent));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadStudents(@RequestBody List<StudentRequestDto> studentRequestDtoList) {
        if (studentRequestDtoList == null || studentRequestDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "List of students is empty"));
        }

        Map<String, Object> response = studentService.uploadStudents(studentRequestDtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/_list")
    public ResponseEntity<Map<String, Object>> getStudentsList(@RequestBody StudentListRequestDto requestDto) {
        if (requestDto.getGroupId() == null) {
            throw new IllegalArgumentException("Group ID must not be null");
        }
        if (requestDto.getPage() <= 0) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }
        if (requestDto.getSize() <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }

        Page<Student> studentsPage = studentService.findAllByGroupIdWithPagination(
                requestDto.getGroupId(), requestDto.getPage(), requestDto.getSize()
        );

        List<StudentListResponseDto> list = studentsPage.getContent().stream()
                .map(studentMapper::mapToListDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalPages", studentsPage.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
