package com.example.sbstudentsservice.controller;

import com.example.sbstudentsservice.dto.request.GroupRequestDto;
import com.example.sbstudentsservice.dto.response.GroupResponseDto;
import com.example.sbstudentsservice.dto.response.GroupTopResponseDto;
import com.example.sbstudentsservice.model.Group;
import com.example.sbstudentsservice.service.GroupService;
import com.example.sbstudentsservice.service.mapper.GroupMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    public ResponseEntity<List<GroupResponseDto>> listGroups() {
        List<Group> groups = groupService.findAll();
        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<GroupResponseDto> groupsDto = groups.stream()
                .map(groupMapper::mapToDto)
                .toList();
        return ResponseEntity.ok(groupsDto);
    }

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody GroupRequestDto groupRequestDto) {
        Group group = groupService.save(groupMapper.mapToModel(groupRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(groupMapper.mapToDto(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDto> updateGroup(@PathVariable Long id, @RequestBody GroupRequestDto groupRequestDto) {
        Group updatedGroup = groupService.update(id, groupMapper.mapToModel(groupRequestDto));
        return ResponseEntity.ok(groupMapper.mapToDto(updatedGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top")
    public ResponseEntity<List<GroupTopResponseDto>> getTopGroups(@RequestParam int n) {
        List<GroupTopResponseDto> topGroups = groupService.findTopGroups(n);
        return ResponseEntity.ok(topGroups);
    }
}
