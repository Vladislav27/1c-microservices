package ru.mipt1c.homework2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mipt1c.homework2.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
