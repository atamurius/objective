package ua.objective.core.model;

/**
 * Data field (single typed value)
 */
public interface Attribute {

    String getName();

    AttrType getType();

    Type getOwner();

    String getQualifiedName();
}
