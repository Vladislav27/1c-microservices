package ru.mipt1c.microserviceshw.task2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentKeyRepository extends JpaRepository<StudentKey, String> {

}
