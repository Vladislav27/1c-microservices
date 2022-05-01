package ru.mipt1c.microserviceshw.task2;

import java.util.Iterator;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mipt1c.homework.task1.KeyValueStorage;

public class HibernateStorage<K extends StorageKey, V extends StorageValue> implements
    KeyValueStorage<K, V> {

  private boolean closed;
  private JpaRepository<K, String> keyRepository;
  private JpaRepository<V, String> valueRepository;

  HibernateStorage(JpaRepository<K, String> keyRepo, JpaRepository<V, String> valueRepo) {
    this.closed = false;
    this.keyRepository = keyRepo;
    this.valueRepository = valueRepo;
  }

  public void clear() {
    keyRepository.deleteAll();
    valueRepository.deleteAll();
  }

  @Override
  public V read(K key) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }

    // we rely on getUID to return unique id of a key
    return valueRepository.findById(key.getUUID()).orElse(null);
  }

  @Override
  public boolean exists(K key) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }

    return keyRepository.existsById(key.getUUID());
  }

  @Override
  public void write(K key, V value) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    K k = keyRepository.save(key);
    value.setUUID(k.getUUID());
    valueRepository.save(value);
  }

  @Override
  public void delete(K key) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }

    keyRepository.deleteById(key.getUUID());
    valueRepository.deleteById(key.getUUID());
  }

  @Override
  public Iterator<K> readKeys() throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    return keyRepository.findAll().iterator();
  }

  @Override
  public int size() throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    return (int) keyRepository.count();
  }

  @Override
  public void close() {
    closed = true;
    flush();
  }

  @Override
  public void flush() {
    keyRepository.flush();
    valueRepository.flush();
  }
}
