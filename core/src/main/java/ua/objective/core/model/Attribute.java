package ua.objective.core.model;

import javax.annotation.Nonnull;

/**
 * Data field (single typed value)
 */
public interface Attribute {

    @Nonnull String getName();

    @Nonnull AttrType getType();

    @Nonnull Type getOwner();

    @Nonnull String getQualifiedName();
}
