package ua.objective.core.model.beans;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.Attribute;

import static java.util.Objects.requireNonNull;

public class AttributeBean implements Attribute {
    private String name;
    private AttrType type;
    private final TypeBean owner;

    public AttributeBean(TypeBean owner, String name, AttrType type) {
        this.name = requireNonNull(name, "name");
        this.type = requireNonNull(type, "type");
        this.owner = requireNonNull(owner, "owner");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AttrType getType() {
        return type;
    }

    void setName(String name) {
        this.name = requireNonNull(name);
    }

    void setType(AttrType type) {
        this.type = requireNonNull(type);
    }

    @Override
    public TypeBean getOwner() {
        return owner;
    }

    @Override
    public String getQualifiedName() {
        return owner.getAttributeQualifiedName(name);
    }
}
