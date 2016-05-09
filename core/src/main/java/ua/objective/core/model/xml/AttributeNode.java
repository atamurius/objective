package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class AttributeNode {

    private String name;
    private String type;

    public AttributeNode() { }

    public AttributeNode(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
