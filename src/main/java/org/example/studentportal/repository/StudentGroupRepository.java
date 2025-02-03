package org.example.studentportal.repository;


import org.example.studentportal.modul.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    boolean existsByName(String name);

}
