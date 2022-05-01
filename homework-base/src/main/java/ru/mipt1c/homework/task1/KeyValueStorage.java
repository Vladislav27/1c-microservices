package ru.mipt1c.homework.task1;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Перзистентное хранилище ключ-значение.
 *
 * Хранилище не обязано сразу же после выполнения запроса изменять состояние на диске, т.е. в процессе работы допустимо
 * расхождение консистентности. Но после выполнения операций {@link #close()} и {@link #flush()} хранилище должно
 * перейти в консистентное состояние, то есть, на диске должны остаться актуальные данные.
 *
 * В случае, если в директории, где предлагается работать хранилищу, нарушена целостность данных, то при создании
 * экземпляра хранилища нужно бросить {@link MalformedDataException}.
 *
 */
public interface KeyValueStorage<K, V> extends Closeable {
    /**
     * Возвращает значение для данного ключа, если оно есть в хранилище.
     * Иначе возвращает null.
     */
    V read(K key) throws IllegalAccessException;

    /**
     * Возвращает true, если данный ключ есть в хранилище
     */
    boolean exists(K key) throws IllegalAccessException;

    /**
     * Записывает в хранилище пару ключ-значение.
     */
    void write(K key, V value) throws IllegalAccessException;

    /**
     * Удаляет пару ключ-значение из хранилища.
     */
    void delete(K key) throws IllegalAccessException;

    /**
     * Читает все ключи в хранилище.
     * <p>
     * Итератор должен бросать {@link java.util.ConcurrentModificationException},
     * если данные в хранилище были изменены в процессе итерирования.
     */
    Iterator<K> readKeys() throws IllegalAccessException;

    /**
     * Возвращает число ключей, которые сейчас в хранилище.
     */
    int size() throws IllegalAccessException;

    /**
     * Приводит хранилище на диске в консистентное состояние на момент вызова. Может блокировать другие операции записи.
     */
    default void flush() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
