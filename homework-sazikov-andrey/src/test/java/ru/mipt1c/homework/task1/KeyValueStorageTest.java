package ru.mipt1c.homework.task1;

import ru.mipt1c.homework.tests.task1.AbstractSingleFileStorageTest;
import ru.mipt1c.homework.tests.task1.Student;
import ru.mipt1c.homework.tests.task1.StudentKey;
import ru.mitp1c.homework.task1.DumpableKeyValueStorage;

public class KeyValueStorageTest extends AbstractSingleFileStorageTest {

  @Override
  protected KeyValueStorage<String, String> buildStringsStorage(String path)
      throws MalformedDataException {
    return new DumpableKeyValueStorage<String, String>(path);
  }

  @Override
  protected KeyValueStorage<Integer, Double> buildNumbersStorage(String path)
      throws MalformedDataException {
    return new DumpableKeyValueStorage<Integer, Double>(path);
  }

  @Override
  protected KeyValueStorage<StudentKey, Student> buildPojoStorage(String path)
      throws MalformedDataException {
    return new DumpableKeyValueStorage<StudentKey, Student>(path);
  }
};