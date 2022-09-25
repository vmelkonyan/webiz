package org.example.webiz.lib.cache;


import org.example.webiz.lib.Cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class WebizCache<K, V> implements Cache<K, V> {

    private final int capacity;
    protected Map<K, Element<K, V>> elementsMap;
    protected LinkedList<Element<K, V>> elementsList;

    public WebizCache(int capacity) {
        this.capacity = capacity;
        this.elementsMap = new HashMap<>();
        this.elementsList = new LinkedList<>();
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> result = Optional.empty();
        if (containsKey(key)) {
            final Element<K, V> element = elementsMap.get(key);
            result = Optional.of(element.value);
            elementsList.remove(element);
            elementsList.addFirst(element);
        }
        return result;
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)) {
            elementsList.remove(elementsMap.get(key));
        } else {
            ensureCapacity();
        }
        final Element<K, V> newElement = new Element<>(key, value);
        elementsMap.put(key, newElement);
        elementsList.addFirst(newElement);
    }

    @Override
    public boolean containsKey(K key) {
        return elementsMap.containsKey(key);
    }

    @Override
    public int size() {
        return elementsList.size();
    }

    Optional<K> getLastRecentKey() {
        return elementsList.size() > 0 ? Optional.of(elementsList.getFirst().key) : Optional.empty();
    }

    private boolean isSizeExceeded() {
        return size() == capacity;
    }

    private void ensureCapacity() {
        if (isSizeExceeded()) {
            final Element<K, V> removedElement = elementsList.removeLast();
            elementsMap.remove(removedElement.key);
        }
    }

    @Override
    public String toString() {
        return "WebizCache{" +
                "elementsMap=" + elementsMap.toString() +
                "elementsList=" + elementsList.toString() +
                '}';
    }

    protected static class Element<K, V> {

        protected final K key;
        protected final V value;

        public Element(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
