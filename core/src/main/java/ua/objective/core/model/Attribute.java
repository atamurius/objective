package ua.objective.core.model;

/**
 * Data field (single typed value)
 */
public class Attribute {
    private String name;
    private AttrType type;

    public Attribute(String name, AttrType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttrType getType() {
        return type;
    }

    public void setType(AttrType type) {
        this.type = type;
    }
}
