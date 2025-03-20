package com.example.sbstudentsservice.repository;

import com.example.sbstudentsservice.dto.response.GroupTopResponseDto;
import com.example.sbstudentsservice.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    boolean existsByNameAndCurator(String groupName, String curator);

    @Query("""
        SELECT new com.example.sbstudentsservice.dto.response.GroupTopResponseDto(g.id, g.name, COUNT(s.id))
        FROM Group g
        LEFT JOIN Student s ON s.group.id = g.id
        GROUP BY g.id, g.name
        ORDER BY COUNT(s.id) DESC
    """)
    List<GroupTopResponseDto> findGroupsWithStudentCount();
}
