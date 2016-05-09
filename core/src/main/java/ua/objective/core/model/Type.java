package ua.objective.core.model;

import java.util.*;

/**
 * Data type, contains attributes, derives attributes from super types.
 */
public interface Type {

    String TYPE_SEPARATOR = ":";
    String GROUP_SEPARATOR = ".";
    String ATTRIBUTE_SEPARATOR = "#";

    String getGroup();

    String getName();

    /**
     * Direct type supertypes.
     */
    Set<? extends Type> getSuperTypes();

    Set<? extends Type> getSubTypes();

    boolean hasDirectSuperType(Type type);

    boolean hasSuperType(Type type);

    /**
     * Attributes, defined in this type
     */
    Set<Attribute> getOwnAttributes();

    /**
     * All type attributes by qualified name.
     */
    Map<String,Attribute> getAttributes();

    /**
     * Gets attribute by qualified name, returns null if no such attribute exists.
     */
    Attribute getAttribute(String qName);

    /**
     * Gets set of attributes by simple name.
     */
    Set<Attribute> getAttributeUnqualified(String name);

    boolean isAbstract();

    String getQualifiedName();
}
