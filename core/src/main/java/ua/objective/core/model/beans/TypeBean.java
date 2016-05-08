package ua.objective.core.model.beans;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.Attribute;
import ua.objective.core.model.Type;

import javax.annotation.Nonnull;
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
    private @Nonnull String group;
    private @Nonnull String name;
    private final @Nonnull Set<Type> superTypes;
    private final @Nonnull Set<Type> subTypes;
    private final @Nonnull Set<Attribute> ownAttributes;
    private final @Nonnull Map<String,Attribute> attributes;

    private final @Nonnull Set<Type> superTypesP;
    private final @Nonnull Set<Type> subTypesP;
    private final @Nonnull Set<Attribute> ownAttributesP;
    private final @Nonnull Map<String,Attribute> attributesP;

    TypeBean(@Nonnull String group, @Nonnull String name) {
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

    class Editor implements Type.Builder {

        private ModelBean model;

        Editor(ModelBean model) {
            this.model = model;
        }

        public TypeBean updateDerivedAttributes() {
            attributes.clear();
            eachType(false, t -> attributes.putAll(t.getAttributes()));
            ownAttributes.forEach(a -> attributes.put(a.getQualifiedName(), a));
            return TypeBean.this;
        }

        public Set<Type> superTypes() {
            return superTypes;
        }

        @Override
        public Editor extend(Type type) {
            superTypes.add(type);
            ((TypeBean) type).subTypes.add(TypeBean.this);
            return this;
        }

        @Override
        public Editor addAttribute(String name, AttrType type) {
            ownAttributes.add(new AttributeBean(TypeBean.this, name, type));
            return this;
        }

        public Set<Type> subTypes() {
            return subTypes;
        }

        public Set<Attribute> ownAttributes() {
            return ownAttributes;
        }

        @Override
        public Editor setGroup(@Nonnull String group) {
            TypeBean.this.group = group;
            return this;
        }

        @Override
        public Editor setName(@Nonnull String name) {
            TypeBean.this.name = name;
            return this;
        }

        @Override
        public Editor setAbstract(boolean isAbstract) {
            TypeBean.this.isAbstract = isAbstract;
            return this;
        }

        @Override
        public Type build() {
            if (model != null) model.add(TypeBean.this);
            return updateDerivedAttributes();
        }
    }

    @Override
    public void eachType(boolean withSelf, Consumer<Type> consumer) {
        superTypes.forEach(t -> t.eachType(true, consumer));
        if (withSelf) {
            consumer.accept(this);
        }
    }

    @Override
    public void eachSubtype(boolean withSelf, Consumer<Type> consumer) {
        if (withSelf) {
            consumer.accept(this);
        }
        subTypes.forEach(t -> t.eachSubtype(false, consumer));
    }

    @Override
    @Nonnull
    public String getGroup() {
        return group;
    }

    @Override
    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    @Nonnull
    public Set<Type> getSuperTypes() {
        return superTypesP;
    }

    @Override
    @Nonnull
    public Set<Type> getSubTypes() {
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
    @Nonnull
    public Set<Attribute> getOwnAttributes() {
        return ownAttributesP;
    }

    @Override
    @Nonnull
    public Map<String, Attribute> getAttributes() {
        return attributesP;
    }

    @Override
    public Attribute getAttribute(String qName) {
        return attributes.get(qName);
    }

    @Override
    @Nonnull
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
    @Nonnull
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
