package ua.objective.core.model.beans;

import ua.objective.core.model.AttrType;
import ua.objective.core.model.Attribute;
import ua.objective.core.model.Type;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class AttributeBean implements Attribute {
    private @Nonnull String name;
    private @Nonnull AttrType type;
    private @Nonnull final Type owner;

    public AttributeBean(Type owner, String name, AttrType type) {
        this.name = requireNonNull(name, "name");
        this.type = requireNonNull(type, "type");
        this.owner = requireNonNull(owner, "owner");
    }

    @Override
    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    @Nonnull
    public AttrType getType() {
        return type;
    }

    public void setName(@Nonnull String name) {
        this.name = requireNonNull(name);
    }

    public void setType(@Nonnull AttrType type) {
        this.type = requireNonNull(type);
    }

    @Override
    @Nonnull
    public Type getOwner() {
        return owner;
    }

    @Override
    @Nonnull
    public String getQualifiedName() {
        return owner.getQualifiedName() + "#" + name;
    }
}
