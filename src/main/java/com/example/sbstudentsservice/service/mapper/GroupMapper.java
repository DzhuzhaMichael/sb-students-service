package com.example.sbstudentsservice.service.mapper;

import com.example.sbstudentsservice.dto.request.GroupRequestDto;
import com.example.sbstudentsservice.dto.response.GroupResponseDto;
import com.example.sbstudentsservice.model.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper implements ResponseDtoMapper<GroupResponseDto, Group>, RequestDtoMapper<GroupRequestDto, Group> {

    @Override
    public Group mapToModel(GroupRequestDto dto) {
        Group group = new Group();
        group.setName(dto.getName());
        group.setCurator(dto.getCurator());
        return group;
    }

    @Override
    public GroupResponseDto mapToDto(Group group) {
        GroupResponseDto groupResponseDto = new GroupResponseDto();
        groupResponseDto.setId(group.getId());
        groupResponseDto.setName(group.getName());
        groupResponseDto.setCurator(group.getCurator());
        return groupResponseDto;
    }

}
