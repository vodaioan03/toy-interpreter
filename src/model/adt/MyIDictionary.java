package model.adt;

import exceptions.KeyNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyIDictionary<K, V> {

    void insert(K key, V value);
    void remove(K key) throws KeyNotFoundException;
    Boolean contains(K key);
    Boolean myContains(K key);
    V get(K key) throws KeyNotFoundException;
    Collection<V> getValues();
    Set<K> getKeys();
    Map<K, V> toMap();
}
