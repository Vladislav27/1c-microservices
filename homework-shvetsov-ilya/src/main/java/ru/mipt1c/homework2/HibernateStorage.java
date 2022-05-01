package ru.mipt1c.homework2;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mipt1c.homework.task1.KeyValueStorage;
import ru.mipt1c.homework2.model.Relation;
import ru.mipt1c.homework2.model.AbstractValue;
import ru.mipt1c.homework2.repository.RelationRepository;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

public class HibernateStorage<K, V extends AbstractValue> implements KeyValueStorage<K, V> {
    boolean isClosed;
    private final JpaRepository<K, K> keyRepository;
    private final JpaRepository<V, Integer> valueRepository;
    private final RelationRepository relationRepository;

    public HibernateStorage(JpaRepository<K, K> keyRepository, JpaRepository<V, Integer> valueRepository,
                            RelationRepository relationRepository) {
        this.keyRepository = keyRepository;
        this.valueRepository = valueRepository;
        this.relationRepository = relationRepository;
        this.isClosed = false;
    }

    @Override
    public V read(K key) {
        if (isClosed) throw new RuntimeException("Closed!");
        if (!exists(key))
            return null;
        Optional<Relation> relationOptional = relationRepository.findById(key.toString());
        if (relationOptional.isEmpty())
            return null;
        Integer valueId = relationOptional.get().getValueId();
        Optional<V> valueOptional = valueRepository.findById(valueId);
        if (valueOptional.isEmpty()) {
            return null;
        } else {
            return valueOptional.get();
        }
    }

    @Override
    public boolean exists(K key) {
        if (isClosed) throw new RuntimeException("Closed!");
        return keyRepository.existsById(key);
    }

    @Override
    public void write(K key, V value) {
        if (isClosed) throw new RuntimeException("Closed!");
        keyRepository.save(key);
        V newValue = valueRepository.save(value);
        Relation relation = new Relation(key.toString(), newValue.getId());
        relationRepository.save(relation);
    }

    @Override
    public void delete(K key) {
        if (isClosed) throw new RuntimeException("Closed!");
        if (!exists(key))
            return;
        keyRepository.delete(key);
        Optional<Relation> relationOptional = relationRepository.findById(key.toString());
        if (relationOptional.isEmpty())
            return;
        Relation relation = relationOptional.get();
        relationRepository.delete(relation);
        valueRepository.deleteById(relation.getValueId());
    }

    @Override
    public Iterator<K> readKeys() {
        if (isClosed) throw new RuntimeException("Closed!");
        return keyRepository.findAll().iterator();
    }

    @Override
    public int size() {
        if (isClosed) throw new RuntimeException("Closed!");
        return (int) keyRepository.count();
    }

    @Override
    public void flush() {
        keyRepository.flush();
        valueRepository.flush();
        relationRepository.flush();
    }

    public void clear() {
        if (isClosed) throw new RuntimeException("Closed!");
        keyRepository.deleteAll();
        valueRepository.deleteAll();
        relationRepository.flush();
    }

    @Override
    public void close() {
        isClosed = true;
        flush();
    }
}
