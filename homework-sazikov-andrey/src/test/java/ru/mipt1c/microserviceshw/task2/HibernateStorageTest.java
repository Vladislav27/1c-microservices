package ru.mipt1c.microserviceshw.task2;

import static org.junit.jupiter.api.Assertions.*;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mipt1c.homework.tests.task1.StorageTestUtils;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = DatabaseProvider.ZONKY)
public class HibernateStorageTest {

  @Autowired
  private StudentKeyRepository keyRepository;

  @Autowired
  private StudentRepository valueRepository;

  private HibernateStorage<StudentKey, Student> storage;

  @BeforeEach
  public void setUp() {
    storage = new HibernateStorage<>(keyRepository,
        valueRepository);
    storage.clear();
  }

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
  public void simpleTest() throws IllegalAccessException {

    storage.write(KEY_1, VALUE_1);

    assertEquals(VALUE_1, storage.read(KEY_1));
    assertEquals(1, storage.size());
    StorageTestUtils.assertFullyMatch(storage.readKeys(), KEY_1);
  }


  @Test
  public void testIteratorWithConcurrentKeysModification() throws IllegalAccessException {
    HibernateStorage<StudentKey, Student> storage = new HibernateStorage<>(keyRepository,
        valueRepository);
    storage.clear();

    storage.write(KEY_1, VALUE_1);
    storage.write(KEY_2, VALUE_2);
    storage.write(KEY_3, VALUE_3);
    Iterator<StudentKey> iterator = storage.readKeys();
    assertTrue(iterator.hasNext());
    assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
    assertTrue(iterator.hasNext());
    assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
    storage.delete(KEY_2);
    iterator.hasNext();
    iterator.next();
  }

  @Test
  public void testIteratorWithConcurrentValuesModification() throws IllegalAccessException {
    HibernateStorage<StudentKey, Student> storage = new HibernateStorage<>(keyRepository,
        valueRepository);
    storage.clear();
    storage.write(KEY_1, VALUE_1);
    storage.write(KEY_2, VALUE_2);
    storage.write(KEY_3, VALUE_3);
    Iterator<StudentKey> iterator = storage.readKeys();
    assertTrue(iterator.hasNext());
    assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
    assertTrue(iterator.hasNext());
    assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
    storage.write(KEY_3, VALUE_2);
    assertTrue(iterator.hasNext());
    assertTrue(Arrays.asList(KEY_1, KEY_2, KEY_3).contains(iterator.next()));
  }
}
