package com.example.sbstudentsservice.dto.request;

import lombok.Data;

@Data
public class StudentListRequestDto {

    private Long groupId;

    private int page;

    private int size;
}
