package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class TypeNode {

    private boolean isAbstract;
    private String name;

    private List<String> baseTypeNames;
    private List<AttributeNode> attributes;

    public TypeNode() { }

    public TypeNode(PackageNode pkg, String name) {
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
    public List<AttributeNode> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    public void setAttributes(List<AttributeNode> attributes) {
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
