package ru.mipt1c.microserviceshw.task2;

import org.junit.jupiter.api.Test;
import ru.mipt1c.homework.tests.task1.StorageTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

  @Test
  public void testKeyCompare() {
    assertEquals(new StudentKey(591, "Vasya Pukin"), new StudentKey(591, "Vasya Pukin"));
    assertNotEquals(new StudentKey(592, "Vasya Pukin"), new StudentKey(591, "Vasya Pukin"));

  }

  @Test
  public void testValueCompare() {
    Student s = new Student(
        591,
        "Ahmad Ben Hafiz",
        "Cairo",
        StorageTestUtils.date(1432, 9, 2),
        false,
        3.3);

    assertEquals(new Student(
        591,
        "Ahmad Ben Hafiz",
        "Cairo",
        StorageTestUtils.date(1432, 9, 2),
        false,
        3.3), new Student(
        591,
        "Ahmad Ben Hafiz",
        "Cairo",
        StorageTestUtils.date(1432, 9, 2),
        false,
        3.3));

    assertEquals(s, new Student(
        591,
        "Ahmad Ben Hafiz",
        "Cairo",
        StorageTestUtils.date(1432, 9, 2),
        false,
        3.3));

    s.setUUID("some-uuid");
    assertEquals(s, new Student(
        591,
        "Ahmad Ben Hafiz",
        "Cairo",
        StorageTestUtils.date(1432, 9, 2),
        false,
        3.3));
  }

}
