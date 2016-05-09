package ua.objective.core.model.types;

import ua.objective.core.model.AttrType;

public abstract class AbstractType implements AttrType {

    private final String name;

    public AbstractType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
