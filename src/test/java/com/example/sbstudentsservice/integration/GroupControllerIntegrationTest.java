package com.example.sbstudentsservice.integration;

import com.example.sbstudentsservice.dto.request.GroupRequestDto;
import com.example.sbstudentsservice.model.Group;
import com.example.sbstudentsservice.repository.GroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateGroup() throws Exception {
        GroupRequestDto requestDto = new GroupRequestDto();
        requestDto.setName("Test Group");
        requestDto.setCurator("Test Curator");

        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Group"));
    }

    @Test
    void testListGroups() throws Exception {
        groupRepository.save(new Group(null, "Group 1", "Curator 1"));
        groupRepository.save(new Group(null, "Group 2", "Curator 2"));

        mockMvc.perform(get("/api/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Group 1"));
    }

    @Test
    void testUpdateGroup() throws Exception {
        Group existingGroup = groupRepository.save(new Group(null, "Old Name", "Old Curator"));

        GroupRequestDto updateDto = new GroupRequestDto();
        updateDto.setName("Updated Name");
        updateDto.setCurator("Updated Curator");

        mockMvc.perform(put("/api/group/{id}", existingGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.curator").value("Updated Curator"));
    }

    @Test
    void testDeleteGroup() throws Exception {
        Group group = groupRepository.save(new Group(null, "ToDelete", "Curator"));

        mockMvc.perform(delete("/api/group/{id}", group.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListGroupsWhenEmpty() throws Exception {
        mockMvc.perform(get("/api/group"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetTopGroups() throws Exception {
        groupRepository.save(new Group(null, "Group1", "Curator1"));
        groupRepository.save(new Group(null, "Group2", "Curator2"));

        mockMvc.perform(get("/api/group/top")
                        .param("n", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetTopGroupsInvalidN() throws Exception {
        mockMvc.perform(get("/api/group/top")
                        .param("n", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Argument n should be more then 0"));
    }
}
