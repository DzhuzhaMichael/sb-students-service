package com.example.sbstudentsservice.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class StudentRequestDto {

    private String name;

    private String surname;

    private String email;

    private String phone;

    private Date birthDate;

    private Long groupId;
}
