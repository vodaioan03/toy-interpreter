package model.adt;

import exceptions.KeyNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<K,V> implements MyIDictionary<K,V>{

    Map<K,V> map;

    public MyDictionary() {
        map = new HashMap<>();
    }
    
    @Override
    public void insert(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void remove(K key) throws KeyNotFoundException {
        if(map.containsKey(key))
            map.remove(key);
        else
            throw new KeyNotFoundException("Key not found");
    }

    @Override
    public Boolean contains(K key) {
        return map.containsKey(key);
    }
    public Boolean myContains(K key) {
        return map.containsKey(key);
    }

    @Override
    public V get(K key) throws KeyNotFoundException {
        if(map.containsKey(key))
            return map.get(key);
        else
            throw new KeyNotFoundException("Key not found");
    }

    @Override
    public Set<K> getKeys() {
        return map.keySet();
    }
    @Override
    public Collection<V> getValues() {return map.values();}

    @Override
    public Map<K, V> toMap() {
        return this.map;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for(K key : map.keySet())
            str.append(key).append(" -> ").append(map.get(key)).append("\n");
        return str.toString();
    }
}
