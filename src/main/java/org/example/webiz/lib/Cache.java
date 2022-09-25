package org.example.webiz.lib;


import java.util.Optional;

public interface Cache<K, V> {

    Optional<V> get(K key);

    void put(K key, V value);

    boolean containsKey(K key);

    int size();

}
