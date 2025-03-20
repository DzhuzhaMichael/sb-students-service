package com.example.sbstudentsservice.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreatedEvent {
    private Long id;
    private String name;
    private String curator;
    private LocalDateTime createdAt;
}
