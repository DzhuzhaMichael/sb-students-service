package com.example.sbstudentsservice.repository;

import com.example.sbstudentsservice.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    Page<Student> findByGroupId(Long groupId, Pageable pageable);

}
