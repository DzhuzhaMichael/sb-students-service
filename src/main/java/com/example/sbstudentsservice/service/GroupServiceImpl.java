package com.example.sbstudentsservice.service;

import com.example.sbstudentsservice.dto.response.GroupTopResponseDto;
import com.example.sbstudentsservice.messaging.GroupEventPublisher;
import com.example.sbstudentsservice.model.Group;
import com.example.sbstudentsservice.repository.GroupRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final GroupEventPublisher eventPublisher;

    public GroupServiceImpl(GroupRepository groupRepository, GroupEventPublisher eventPublisher) {
        this.groupRepository = groupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group with id: " + id + " not found"));
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group save(Group group) {
        if (groupRepository.existsByNameAndCurator(group.getName(), group.getCurator())) {
            throw new EntityExistsException("Group with name '" + group.getName() +
                    "' and curator '" + group.getCurator() + "' already exists");
        }
        Group savedGroup = groupRepository.save(group);
        log.info("✓ Group saved: id={}, name={}, curator={}", savedGroup.getId(), savedGroup.getName(), savedGroup.getCurator());
        eventPublisher.publishGroupCreatedEvent(savedGroup);
        return savedGroup;
    }

    @Override
    public Group update(Long id, Group group) {
        Group groupFromDb = findById(id);
        if (groupRepository.existsByNameAndCurator(group.getName(), group.getCurator())) {
            throw new EntityExistsException("Group with name '" + group.getName() +
                    "' and curator '" + group.getCurator() + "' already exists");
        }
        groupFromDb.setName(group.getName());
        groupFromDb.setCurator(group.getCurator());
        return groupRepository.save(groupFromDb);
    }

    @Override
    public void delete(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new EntityNotFoundException("Group with id " + id + " not found");
        }
        groupRepository.deleteById(id);
    }

    @Override
    public List<GroupTopResponseDto> findTopGroups(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Argument n should be more then 0");
        }
        List<GroupTopResponseDto> groups = groupRepository.findGroupsWithStudentCount();
        if (n > groups.size()) {
            throw new IllegalArgumentException("There are only " + groups.size() + " groups, but your argument was: " + n);
        }
        return groups.stream()
                .limit(n)
                .collect(Collectors.toList());
    }
}
