package ua.objective.core.model;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Types container.
 */
public interface Model {

    @Nonnull Collection<Type> getTypes();

    /**
     * @throws NoSuchElementException
     */
    @Nonnull Type getTypeByQName(String name);

    @Nonnull Set<AttrType> getAttrTypes();

    @Nonnull
    Type.Builder createType(String group, String name);
}
