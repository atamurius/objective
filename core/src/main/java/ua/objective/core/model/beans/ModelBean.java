package ua.objective.core.model.beans;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.Attribute;
import ua.objective.core.model.Model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Host for all change operations.
 */
public class ModelBean implements Model {

    private final Map<String,TypeBean> types = new ConcurrentHashMap<>();
    private final Set<AttrType> attrTypes = new ConcurrentSkipListSet<>();

    private TypeBean register(TypeBean bean) {
        String name = bean.getQualifiedName();
        if (types.containsKey(name)) {
            throw new IllegalArgumentException("Type " + name + " already exists");
        }
        else {
            types.put(name, bean);
            return bean;
        }
    }

    /**
     * Create and register new abstract type
     */
    public TypeBean createType(String group, String name) {
        return register(new TypeBean(group, name));
    }

    @Override
    public Collection<TypeBean> getTypes() {
        return types.values();
    }

    /**
     * @throws NoSuchElementException
     */
    @Override
    public TypeBean getTypeByQName(String name) {
        TypeBean type = types.get(name);
        if (type != null)
            return type;
        else
            throw new NoSuchElementException(name);
    }

    @Override
    public Set<AttrType> getAttrTypes() {
        return attrTypes;
    }

    /**
     * Adds `supertype` to list of super types of `type`
     * @throws IllegalArgumentException in case if `supertype` is subtype of `type` (cyclic extension)
     */
    public void extend(TypeBean type, TypeBean supertype) {
        if (! type.hasDirectSuperType(supertype)) {
            if (supertype.hasSuperType(type)) {
                throw new IllegalArgumentException("Cyclic extension is not allowed, trying extend " +
                        type.getQualifiedName() + " from " + supertype.getQualifiedName());
            }
            type.superTypes().add(supertype);
            supertype.subTypes().add(type);
            updateTreeAttributes(type);
        }
    }

    /**
     * Removes `supertype` from super types list of `type`
     */
    public void unextend(TypeBean type, TypeBean supertype) {
        if (type.hasDirectSuperType(supertype)) {
            type.superTypes().remove(supertype);
            supertype.subTypes().remove(type);
            updateTreeAttributes(type);
        }
    }

    /**
     * Changes abstract status of type
     */
    public void changeAbstract(TypeBean type, boolean isAbstract) {
        type.setAbstract(isAbstract);
    }

    /**
     * Renames type
     */
    public TypeBean rename(TypeBean type, String group, String name) {
        if (! type.getGroup().equals(group) || ! type.getName().equals(name)) {
            types.remove(type.getQualifiedName());
            type.setGroup(group);
            type.setName(group);
            return register(type);
        }
        else {
            return type;
        }
    }

    /**
     * Adds new attribute to type
     * @throws IllegalArgumentException if name is not unique within type
     */
    public AttributeBean addAttribute(TypeBean type, String name, AttrType attrType) {
        AttributeBean attr = new AttributeBean(type, name, attrType);
        if (type.getAttributes().containsKey(attr.getQualifiedName())) {
            throw new IllegalArgumentException("Attribute with name "+ name +
                    " already exists in type "+ type.getQualifiedName());
        }
        type.ownAttributes().add(attr);
        updateTreeAttributes(type);
        return attr;
    }

    public void removeAttribute(TypeBean type, String name) {
        type.ownAttributes().remove(
                type.getAttribute(type.getAttributeQualifiedName(name)));
        updateTreeAttributes(type);
    }

    public AttributeBean rename(AttributeBean attr, String name) {
        TypeBean owner = attr.getOwner();
        Attribute existing = owner.getAttribute(owner.getAttributeQualifiedName(name));
        if (existing != null) {
            throw new IllegalArgumentException("Attribute with name "+ name +
                    " already exists in type "+ owner.getQualifiedName());
        }
        else {
            attr.setName(name);
            updateTreeAttributes(owner);
            return attr;
        }
    }

    public AttributeBean changeType(AttributeBean attr, AttrType type) {
        attr.setType(type);
        return attr;
    }

    private void updateTreeAttributes(TypeBean owner) {
        owner.eachSubtype(true, TypeBean::updateDerivedAttributes);
    }

    @Override
    public String toString() {
        return getTypes().toString();
    }
}
