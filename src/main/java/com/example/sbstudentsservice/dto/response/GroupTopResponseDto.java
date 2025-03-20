package com.example.sbstudentsservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupTopResponseDto {

    private Long id;

    private String name;

    private Long count;

}
