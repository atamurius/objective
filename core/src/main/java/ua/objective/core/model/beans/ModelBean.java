package ua.objective.core.model.beans;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.Model;
import ua.objective.core.model.Type;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ModelBean implements Model {

    private final Map<String,Type> types = new ConcurrentHashMap<>();
    private final Set<AttrType> attrTypes = new ConcurrentSkipListSet<>();

    @Nonnull
    @Override
    public Type.Builder createType(String group, String name) {
        return new TypeBean(group, name).new Editor(this);
    }

    @Override
    @Nonnull
    public Collection<Type> getTypes() {
        return types.values();
    }

    @Override
    @Nonnull
    public Type getTypeByQName(String name) {
        Type type = types.get(name);
        if (type != null)
            return type;
        else
            throw new NoSuchElementException(name);
    }

    public void add(@Nonnull Type type) {
        String name = type.getQualifiedName();
        if (types.containsKey(name)) {
            throw new IllegalArgumentException("Type " + name + " already exists");
        }
        else {
            types.put(name, type);
        }
    }

    @Override
    @Nonnull
    public Set<AttrType> getAttrTypes() {
        return attrTypes;
    }

    public void extend(@Nonnull Type type, @Nonnull Type supertype) {
        if (! type.hasDirectSuperType(supertype)) {
            if (supertype.hasSuperType(type)) {
                throw new IllegalArgumentException("Cyclic extension is not allowed, trying extend " +
                        type.getQualifiedName() + " from " + supertype.getQualifiedName());
            }
            editor(type).superTypes().add(supertype);
            editor(supertype).subTypes().add(type);
            type.eachSubtype(true, t -> editor(t).updateDerivedAttributes());
        }
    }

    public void unextend(@Nonnull Type type, @Nonnull Type supertype) {
        if (type.hasDirectSuperType(supertype)) {
            editor(type).superTypes().remove(supertype);
            editor(supertype).subTypes().remove(type);
            type.eachSubtype(true, t -> editor(t).updateDerivedAttributes());
        }
    }

    public void changeAbstract(@Nonnull Type type, boolean isAbstract) {
        editor(type).setAbstract(isAbstract);
    }

    public void changeName(@Nonnull Type type, @Nonnull String group, @Nonnull String name) {
        if (! type.getGroup().equals(group) || ! type.getName().equals(name)) {
            types.remove(type.getQualifiedName());
            editor(type).setGroup(group);
            editor(type).setName(group);
            types.put(type.getQualifiedName(), type);
        }
    }

    // TODO attributes

    private static TypeBean.Editor editor(Type type) {
        if (type instanceof TypeBean) {
            return ((TypeBean) type).new Editor(null);
        }
        else {
            throw new IllegalArgumentException("Only TypeBean implementation allowed");
        }
    }

    @Override
    public String toString() {
        return getTypes().toString();
    }
}
