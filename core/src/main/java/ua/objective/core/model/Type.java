package ua.objective.core.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Data type, contains attributes.
 */
public class Type {

    private boolean isAbstract;
    private String group;
    private String name;
    private List<Type> superTypes = new ArrayList<>();
    private List<Attribute> attributes = new ArrayList<>();

    private Map<String,Attribute> allAttributes = new LinkedHashMap<>();

    public Type(String group, String name) {
        this.group = group;
        this.name = name;
    }

    public void eachType(Consumer<Type> consumer) {
        superTypes.forEach(t -> t.eachType(consumer));
        consumer.accept(this);
    }

    private void updateAttrCache() {
        allAttributes.clear();
        eachType(t -> {
            String base = t.group + ":" + t.name + ":";
            t.attributes.forEach(a ->
                    allAttributes.put(base + a.getName(), a));
        });
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Type> getSuperTypes() {
        return superTypes;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public String getQualifiedName() {
        return getGroup() +":"+ getName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(isAbstract ? "abstract " : "");
        sb.append(getQualifiedName());
        if (! getSuperTypes().isEmpty()) {
            int i = 0;
            for (Type type : getSuperTypes()) {
                sb.append(i++ > 0 ? ", " : " extends ").append(type.getQualifiedName());
            }
        }
        sb.append("{");
        for (Attribute a : getAttributes()) {
            sb.append("\n\t").append(a.getName()).append(": ").append(a.getType().getClass().getCanonicalName());
        }
        sb.append("\n}");
        return sb.toString();
    }
}
