package ru.mipt1c.homework2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mipt1c.homework.tests.task1.StorageTestUtils;
import ru.mipt1c.homework2.model.Student;
import ru.mipt1c.homework2.model.StudentKey;
import ru.mipt1c.homework2.repository.RelationRepository;
import ru.mipt1c.homework2.repository.StudentKeyRepository;
import ru.mipt1c.homework2.repository.StudentRepository;

import java.util.*;

@SpringBootTest
class Homework2ApplicationTests {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    StudentKeyRepository studentKeyRepository;
    @Autowired
    RelationRepository relationRepository;

    public static final StudentKey KEY_1 = new StudentKey(591, "Vasya Pukin");
    public static final Student VALUE_1 = new Student(
            591,
            "Vasya Pukin",
            "Vasyuki",
            StorageTestUtils.date(1996, 4, 14),
            true,
            7.8);

    public static final StudentKey KEY_2 = new StudentKey(591, "Ahmad Ben Hafiz");
    public static final Student VALUE_2 = new Student(
            591,
            "Ahmad Ben Hafiz",
            "Cairo",
            StorageTestUtils.date(1432, 9, 2),
            false,
            3.3);

    public static final StudentKey KEY_3 = new StudentKey(599, "John Smith");
    public static final Student VALUE_3 = new Student(
            599,
            "John Smith",
            "Glasgow",
            StorageTestUtils.date(1874, 3, 8),
            true,
            9.1);

    @Test
    public void simpleTest() {
        HibernateStorage<StudentKey, Student> storage = new HibernateStorage<>(studentKeyRepository, studentRepository, relationRepository);
        storage.clear();

        storage.write(KEY_1, VALUE_1);

        Assertions.assertEquals(VALUE_1, storage.read(KEY_1));
        Assertions.assertEquals(1, storage.size());
        StorageTestUtils.assertFullyMatch(storage.readKeys(), KEY_1);
    }

    @Test
    public void testIteratorWithConcurrentKeysModification() {
        HibernateStorage<StudentKey, Student> storage = new HibernateStorage<>(studentKeyRepository, studentRepository, relationRepository);
        storage.clear();
        storage.write(KEY_1, VALUE_1);
        storage.write(KEY_2, VALUE_2);
        storage.write(KEY_3, VALUE_3);
        Iterator<StudentKey> iterator = storage.readKeys();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
        storage.delete(KEY_2);
        iterator.hasNext();
        iterator.next();
    }

    @Test
    public void testIteratorWithConcurrentValuesModification() {
        HibernateStorage<StudentKey, Student> storage = new HibernateStorage<>(studentKeyRepository, studentRepository, relationRepository);
        storage.clear();
        storage.write(KEY_1, VALUE_1);
        storage.write(KEY_2, VALUE_2);
        storage.write(KEY_3, VALUE_3);
        Iterator<StudentKey> iterator = storage.readKeys();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
        storage.write(KEY_3, VALUE_2);
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
    }
}
