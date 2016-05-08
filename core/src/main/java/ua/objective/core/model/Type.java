package ua.objective.core.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Data type, contains attributes, derives attributes from super types.
 */
public interface Type {

    interface Builder {

        Builder extend(Type type);

        Builder addAttribute(String name, AttrType type);

        Builder setGroup(@Nonnull String group);

        Builder setName(@Nonnull String name);

        Builder setAbstract(boolean isAbstract);

        Type build();
    }

    /**
     * Iterates over all super types and (maybe) self.
     */
    void eachType(boolean withSelf, Consumer<Type> consumer);

    /**
     * Iterates over all subtypes and (maybe) self.
     */
    void eachSubtype(boolean withSelf, Consumer<Type> consumer);

    @Nonnull String getGroup();

    @Nonnull String getName();

    /**
     * Direct type supertypes.
     */
    @Nonnull Set<Type> getSuperTypes();

    @Nonnull Set<Type> getSubTypes();

    boolean hasDirectSuperType(Type type);

    boolean hasSuperType(Type type);

    /**
     * Attributes, defined in this type
     */
    @Nonnull Set<Attribute> getOwnAttributes();

    /**
     * All type attributes by qualified name.
     */
    @Nonnull Map<String,Attribute> getAttributes();

    /**
     * Gets attribute by qualified name, returns null if no such attribute exists.
     */
    @Nullable Attribute getAttribute(String qName);

    /**
     * Gets set of attributes by simple name.
     */
    @Nonnull Set<Attribute> getAttributeUnqualified(String name);

    boolean isAbstract();

    @Nonnull String getQualifiedName();
}
