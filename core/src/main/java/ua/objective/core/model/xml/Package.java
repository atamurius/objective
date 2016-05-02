package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@XmlRootElement
public class Package extends ChildOf<PackageContainer> implements PackageContainer {

    private String name;

    private List<Type> types;
    private SearchableSet<String,Package> packages;

    public Package() { }

    public Package(PackageContainer parent, String name) {
        this.parent = parent;
        setName(name);
        parent.getPackages().add(this);
    }

    @XmlTransient
    public String getFullName() {
        Package parentPackage = getParentPackage();
        if (parentPackage == null) {
            return getName();
        }
        else {
            return parentPackage.getFullName() +"."+ getName();
        }
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

    @Override
    public Set<Package> getPackages() {
        if (packages == null) {
            packages = new SearchableSet<>(Package::getName);
        }
        return packages;
    }

    @Override
    public void setPackages(Set<Package> packages) {
        this.packages = null;
        getPackages().addAll(packages);
    }

    @XmlTransient
    public Package getParentPackage() {
        return parent instanceof Package ? (Package) parent : null;
    }

    @XmlTransient
    public Model getModel() {
        Package pkg = getParentPackage();
        return pkg == null ? (Model) parent : pkg.getModel();
    }
}
