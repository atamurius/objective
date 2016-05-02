package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Package extends PkgContainer {

    private String name;

    private List<Type> types;

    public Package() { }

    public Package(String name) {
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
    public List<Type> getTypes() {
        if (types == null) {
            types = new ArrayList<>();
        }
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }
}
