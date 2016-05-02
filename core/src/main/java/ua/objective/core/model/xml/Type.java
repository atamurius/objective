package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Type {

    private boolean isAbstract;
    private String name;

    private List<String> baseTypeNames;
    private List<Attribute> attributes;

    public Type() { }

    public Type(Package pkg, String name) {
        setName(name);
        pkg.getTypes().add(this);
    }

    @XmlAttribute
    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlElement(name = "attribute")
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    @XmlElement(name = "extends")
    public List<String> getBaseTypeNames() {
        if (baseTypeNames == null) {
            baseTypeNames = new ArrayList<>();
        }
        return baseTypeNames;
    }

    public void setBaseTypeNames(List<String> baseTypeNames) {
        this.baseTypeNames = baseTypeNames;
    }

    public void extendFrom(String type) {
        getBaseTypeNames().add(type);
    }
}
