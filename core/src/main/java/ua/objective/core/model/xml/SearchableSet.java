package ua.objective.core.model.xml;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Set with search by key operation
 */
public class SearchableSet<K,T> implements Set<T> {

    private final Map<K,T> objects = new HashMap<>();
    private final Function<T,K> key;

    public SearchableSet(Function<T, K> key) {
        this.key = key;
    }

    public T get(K key) {
        return objects.get(key);
    }

    @SuppressWarnings("unchecked")
    private K key(Object o) {
        try {
            return key.apply((T) o);
        }
        catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public int size() {
        return objects.size();
    }

    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return objects.containsKey(key(o));
    }

    @Override
    public Iterator<T> iterator() {
        return objects.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return objects.values().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return objects.values().toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return objects.put(key(t), t) == null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        return objects.remove(key(o)) != null;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.stream().allMatch(o -> objects.containsKey(key(o)));
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return collection.stream().map(this::add).reduce(false, (was,v) -> was || v);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return objects.keySet().retainAll(collection.stream().map(this::key).collect(toList()));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return objects.keySet().removeAll(collection.stream().map(this::key).collect(toList()));
    }

    @Override
    public void clear() {
        objects.clear();
    }
}