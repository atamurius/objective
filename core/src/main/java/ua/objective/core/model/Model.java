package ua.objective.core.model;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Types container.
 */
public interface Model {

    Collection<? extends Type> getTypes();

    /**
     * @throws NoSuchElementException
     */
    Type getTypeByQName(String name);

    Set<AttrType> getAttrTypes();
}
