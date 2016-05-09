package ua.objective.core.model.xml;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Model and Package both contains packages
 */
public abstract class PkgContainer {

    private List<PackageNode> packages;

    @XmlElement(name = "package")
    public List<PackageNode> getPackages() {
        if (packages == null) {
            packages = new ArrayList<>();
        }
        return packages;
    }

    public void setPackages(List<PackageNode> packages) {
        this.packages = packages;
    }
}
