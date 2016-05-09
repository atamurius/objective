package ua.objective.core.model.beans;

import ua.objective.core.model.Attribute;
import ua.objective.core.model.Type;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

public class TypeBean implements Type {

    private boolean isAbstract;
    private String group;
    private String name;
    private final Set<TypeBean> superTypes;
    private final Set<TypeBean> subTypes;
    private final Set<Attribute> ownAttributes;
    private final Map<String,Attribute> attributes;

    private final Set<TypeBean> superTypesP;
    private final Set<TypeBean> subTypesP;
    private final Set<Attribute> ownAttributesP;
    private final Map<String,Attribute> attributesP;

    TypeBean(String group, String name) {
        this.isAbstract = true;
        this.group = requireNonNull(group, "group");
        this.name = requireNonNull(name, "name");
        this.superTypes = new CopyOnWriteArraySet<>();
        this.subTypes = new CopyOnWriteArraySet<>();
        this.ownAttributes = new CopyOnWriteArraySet<>();
        this.attributes = new ConcurrentHashMap<>();

        this.superTypesP = unmodifiableSet(superTypes);
        this.subTypesP = unmodifiableSet(subTypes);
        this.ownAttributesP = unmodifiableSet(ownAttributes);
        this.attributesP = unmodifiableMap(attributes);
    }

    /* --- mutators --- */

    void updateDerivedAttributes() {
        attributes.clear();
        eachType(false, t -> attributes.putAll(t.getAttributes()));
        ownAttributes.forEach(a -> attributes.put(a.getQualifiedName(), a));
    }

    Set<TypeBean> superTypes() {
        return superTypes;
    }

    Set<TypeBean> subTypes() {
        return subTypes;
    }

    Set<Attribute> ownAttributes() {
        return ownAttributes;
    }

    void setGroup(String group) {
        this.group = group;
    }

    void setName(String name) {
        this.name = name;
    }

    void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    String getAttributeQualifiedName(String name) {
        return getQualifiedName() + "#" + name;
    }

    /* --- end of mutators --- */

    public void eachType(boolean withSelf, Consumer<TypeBean> consumer) {
        superTypes.forEach(t -> t.eachType(true, consumer));
        if (withSelf) {
            consumer.accept(this);
        }
    }

    public void eachSubtype(boolean withSelf, Consumer<TypeBean> consumer) {
        if (withSelf) {
            consumer.accept(this);
        }
        subTypes.forEach(t -> t.eachSubtype(false, consumer));
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<TypeBean> getSuperTypes() {
        return superTypesP;
    }

    @Override
    public Set<TypeBean> getSubTypes() {
        return subTypesP;
    }

    @Override
    public boolean hasDirectSuperType(Type type) {
        return superTypes.contains(type)
            || superTypes.stream().anyMatch(t -> t.equals(type));
    }

    @Override
    public boolean hasSuperType(Type type) {
        return hasDirectSuperType(type)
            || superTypes.stream()
                .anyMatch(t -> t.hasSuperType(type));
    }

    @Override
    public Set<Attribute> getOwnAttributes() {
        return ownAttributesP;
    }

    @Override
    public Map<String, Attribute> getAttributes() {
        return attributesP;
    }

    @Override
    public Attribute getAttribute(String qName) {
        return attributes.get(qName);
    }

    @Override
    public Set<Attribute> getAttributeUnqualified(String name) {
        return attributes.values().stream()
                .filter(a -> a.getName().equals(name))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public String getQualifiedName() {
        return getGroup() +":"+ getName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(isAbstract ? "abstract " : "");
        sb.append(getQualifiedName());
        if (! getSuperTypes().isEmpty()) {
            int i = 0;
            for (Type type : getSuperTypes()) {
                sb.append(i++ > 0 ? ", " : " extends ").append(type.getQualifiedName());
            }
        }
        sb.append("{");
        for (Attribute a : attributes.values()) {
            sb.append("\n\t").append(a.getName()).append(": ").append(a.getType().getClass().getCanonicalName());
        }
        sb.append("\n}");
        return sb.toString();
    }
}
