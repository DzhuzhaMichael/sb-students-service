package com.example.sbstudentsservice.service;

import com.example.sbstudentsservice.dto.response.GroupTopResponseDto;
import com.example.sbstudentsservice.model.Group;

import java.util.List;

public interface GroupService {

    List<Group> findAll();

    Group findById(Long id);

    Group save(Group group);

    Group update(Long id, Group group);

    void delete(Long id);

    List<GroupTopResponseDto> findTopGroups(int n);

}
