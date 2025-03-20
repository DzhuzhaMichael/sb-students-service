package com.example.sbstudentsservice.service.mapper;

import com.example.sbstudentsservice.dto.request.StudentRequestDto;
import com.example.sbstudentsservice.dto.response.StudentListResponseDto;
import com.example.sbstudentsservice.dto.response.StudentResponseDto;
import com.example.sbstudentsservice.model.Student;
import com.example.sbstudentsservice.service.GroupService;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper implements RequestDtoMapper<StudentRequestDto, Student>, ResponseDtoMapper<StudentResponseDto, Student> {

    private final GroupService groupService;

    public StudentMapper (GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Student mapToModel(StudentRequestDto dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setSurname(dto.getSurname());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setBirthDate(dto.getBirthDate());
        student.setGroup(groupService.findById(dto.getGroupId()));
        return student;
    }

    @Override
    public StudentResponseDto mapToDto(Student student) {
        StudentResponseDto studentResponseDto = new StudentResponseDto();
        studentResponseDto.setId(student.getId());
        studentResponseDto.setName(student.getName());
        studentResponseDto.setSurname(student.getSurname());
        studentResponseDto.setEmail(student.getEmail());
        studentResponseDto.setPhone(student.getPhone());
        studentResponseDto.setBirthDate(student.getBirthDate());
        studentResponseDto.setGroupId(student.getGroup().getId());
        return studentResponseDto;
    }

    public StudentListResponseDto mapToListDto(Student student) {
        StudentListResponseDto dto = new StudentListResponseDto();
        dto.setName(student.getName());
        dto.setSurname(student.getSurname());
        return dto;
    }
}
