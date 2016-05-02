package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
public class Model implements PackageContainer {

    private SearchableSet<String,Package> packages;

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
}
