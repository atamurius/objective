package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

public interface PackageContainer {

    @XmlElement(name = "package")
    Set<Package> getPackages();

    void setPackages(Set<Package> packages);
}
