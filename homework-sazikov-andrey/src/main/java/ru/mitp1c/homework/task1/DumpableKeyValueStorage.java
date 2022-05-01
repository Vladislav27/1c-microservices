package ru.mitp1c.homework.task1;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import ru.mipt1c.homework.task1.KeyValueStorage;
import ru.mipt1c.homework.task1.MalformedDataException;

public class DumpableKeyValueStorage<K, V> implements KeyValueStorage<K, V> {

  private final String storageDumpDir;
  private Path storageDumpFile;
  private HashMap<K, V> storage;
  private boolean closed;

  public DumpableKeyValueStorage(String directory) throws MalformedDataException {
    this.storageDumpDir = directory;

    validate();
    loadIfNeeded();

    closed = false;
  }

  private void validate() throws MalformedDataException {
    if (!Files.exists(Paths.get(storageDumpDir))) {
      throw new MalformedDataException("directory does not exists");
    }

    storageDumpFile = Paths.get(storageDumpDir, "key_value_storage.dump");
  }

  private void loadIfNeeded() {
    if (!Files.exists(storageDumpFile)) {
      storage = new HashMap<>();
      return;
    }

    try (ObjectInputStream inp = new ObjectInputStream(Files.newInputStream(storageDumpFile))) {
      storage = (HashMap<K, V>) inp.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new MalformedDataException(e);
    }
  }

  @Override
  public V read(K key) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    return storage.get(key);
  }

  @Override
  public boolean exists(K key) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    return storage.containsKey(key);
  }

  @Override
  public void write(K key, V value) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    storage.put(key, value);
  }

  @Override
  public void delete(K key) throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    storage.remove(key);
  }

  @Override
  public Iterator<K> readKeys() throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    return storage.keySet().iterator();
  }

  @Override
  public int size() throws IllegalAccessException {
    if (closed) {
      throw new IllegalAccessException("storage is closed");
    }
    return storage.size();
  }

  @Override
  public void flush() throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(storageDumpFile))) {
      out.writeObject(storage);
    }
  }

  @Override
  public void close() throws IOException {
    closed = true;
    flush();
  }
}
