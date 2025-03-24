package com.example.sbstudentsservice.service;

import com.example.sbstudentsservice.dto.response.GroupTopResponseDto;
import com.example.sbstudentsservice.messaging.GroupEventPublisher;
import com.example.sbstudentsservice.model.Group;
import com.example.sbstudentsservice.repository.GroupRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupEventPublisher eventPublisher;

    @InjectMocks
    protected GroupServiceImpl groupServiceImpl;

    @Test
    void testSaveNewGroup() {
        Group group = new Group();
        group.setName("Test Group");
        group.setCurator("Test Curator");

        when(groupRepository.existsByNameAndCurator(group.getName(), group.getCurator())).thenReturn(false);
        when(groupRepository.save(group)).thenReturn(group);

        Group savedGroup = groupServiceImpl.save(group);

        assertThat(savedGroup).isNotNull();
        verify(groupRepository).save(group);
        verify(eventPublisher).publishGroupCreatedEvent(group);
    }

    @Test
    void testSaveExistingGroupThrowsException() {
        Group group = new Group();
        group.setName("Existing Group");
        group.setCurator("Existing Curator");

        when(groupRepository.existsByNameAndCurator(group.getName(), group.getCurator())).thenReturn(true);

        assertThatThrownBy(() -> groupServiceImpl.save(group))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void testFindByIdNotFound() {
        when(groupRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> groupServiceImpl.findById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void testFindByIdFound() {
        Group group = new Group();
        group.setId(1L);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Group result = groupServiceImpl.findById(1L);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void testFindAllGroups() {
        List<Group> groups = List.of(new Group(), new Group());
        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> result = groupServiceImpl.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void testDeleteGroup() {
        when(groupRepository.existsById(1L)).thenReturn(true);
        groupServiceImpl.delete(1L);
        verify(groupRepository).deleteById(1L);
    }

    @Test
    void testDeleteGroupNotFound() {
        when(groupRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> groupServiceImpl.delete(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testFindTopGroups() {
        List<GroupTopResponseDto> topGroups = List.of(
                new GroupTopResponseDto(1L, "Group1", 10L),
                new GroupTopResponseDto(2L, "Group2", 8L)
        );
        when(groupRepository.findGroupsWithStudentCount()).thenReturn(topGroups);

        List<GroupTopResponseDto> result = groupServiceImpl.findTopGroups(2);
        assertThat(result).hasSize(2);
    }

    @Test
    void testFindTopGroupsInvalidArgument() {
        assertThatThrownBy(() -> groupServiceImpl.findTopGroups(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testFindTopGroupsExceedsAvailable() {
        List<GroupTopResponseDto> topGroups = List.of(
                new GroupTopResponseDto(1L, "Group1", 10L)
        );
        when(groupRepository.findGroupsWithStudentCount()).thenReturn(topGroups);

        assertThatThrownBy(() -> groupServiceImpl.findTopGroups(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("There are only 1 groups");
    }
}
