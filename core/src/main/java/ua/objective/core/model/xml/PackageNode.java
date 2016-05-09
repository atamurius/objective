package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class PackageNode extends PkgContainer {

    private String name;

    private List<TypeNode> types;

    public PackageNode() { }

    public PackageNode(String name) {
        setName(name);
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    public List<TypeNode> getTypes() {
        if (types == null) {
            types = new ArrayList<>();
        }
        return types;
    }

    public void setTypes(List<TypeNode> types) {
        this.types = types;
    }
}
