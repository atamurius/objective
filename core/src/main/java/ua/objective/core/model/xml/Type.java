package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class Type extends ChildOf<Package> {

    private boolean isAbstract;
    private String name;

    private List<String> baseTypeNames;

    private Set<Type> baseTypes;

    public Type() { }

    public Type(Package pkg, String name) {
        parent = pkg;
        setName(name);
        pkg.getTypes().add(this);
    }

    @XmlTransient
    public String getQualifiedName() {
        return parent.getFullName() +":"+ getName();
    }

    @XmlAttribute
    public boolean isAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getName() {
        return name;
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

    @XmlTransient
    public Set<Type> getBaseTypes() {
        if (baseTypes == null) {
            baseTypes = new HashSet<>();
        }
        return baseTypes;
    }

    public void extendFrom(Type type) {
        getBaseTypes().add(type);
        getBaseTypeNames().add(type.getQualifiedName());
    }
}
