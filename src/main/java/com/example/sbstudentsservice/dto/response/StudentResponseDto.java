package com.example.sbstudentsservice.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class StudentResponseDto {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private Date birthDate;

    private Long groupId;
}
