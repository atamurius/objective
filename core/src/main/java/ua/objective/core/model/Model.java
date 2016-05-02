package ua.objective.core.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Types container.
 */
public class Model {

    private final Map<String,Type> types = new HashMap<>();

    public Collection<Type> getTypes() {
        return types.values();
    }

    public Type getTypeByQName(String name) {
        return types.get(name);
    }

    public void add(Type type) {
        types.put(type.getQualifiedName(), type);
    }

    @Override
    public String toString() {
        return getTypes().toString();
    }
}
