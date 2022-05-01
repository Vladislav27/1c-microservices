package ru.mipt1c.homework2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mipt1c.homework2.model.StudentKey;

@Repository
public interface StudentKeyRepository extends JpaRepository<StudentKey, StudentKey> {
}
